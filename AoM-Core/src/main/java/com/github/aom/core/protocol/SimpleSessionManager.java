/**
 * This file is part of -AoM--Server, licensed under the APACHE License.
 *
 * Copyright (c) 2015 AoW Powered <https://github.com/AO-Modding>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.aom.core.protocol;

import com.github.aom.core.event.protocol.SessionClosedEvent;
import com.github.aom.core.event.protocol.SessionConnectedEvent;
import com.github.aom.core.protocol.pipeline.MessageInitializer;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static com.github.aom.core.EngineAPI.getEventManager;

/**
 * Default implementation for {@link SessionManager}.
 */
public final class SimpleSessionManager implements SessionManager {
    private final MutableMap<UUID, SimpleSession> mRegistry;
    private final ChannelGroup mGroup;
    private final EventLoopGroup mBossGroup;
    private final EventLoopGroup mWorkerGroup;
    private final ServerBootstrap mBootstrap;
    private final AtomicReference<Session.UncaughtExceptionHandler> mUncaughtExceptionHandler;

    /**
     * Default constructor for {@link SimpleSessionManager}.
     */
    public SimpleSessionManager() {
        this.mRegistry = new UnifiedMap<>();
        this.mGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        this.mBossGroup = new NioEventLoopGroup();
        this.mWorkerGroup = new NioEventLoopGroup();
        this.mBootstrap = new ServerBootstrap();
        this.mBootstrap.group(mBossGroup, mWorkerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new MessageInitializer(this))
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        this.mUncaughtExceptionHandler = new AtomicReference<>(new DefaultUncaughtExceptionHandler(this));
    }

    /**
     * Pulse every {@link Session} registered into this manager.
     */
    public void pulse() {
        mRegistry.values().forEach(SimpleSession::pulse);
    }

    /**
     * Adds a new {@link Session} into the pool.
     *
     * @param session The session to add to the pool.
     */
    public void add(SimpleSession session) {
        getEventManager().invokeAsyncEvent(new SessionConnectedEvent(session), this::onEvent);
    }

    /**
     * Removes a {@link Session} from the pool.
     *
     * @param session The session to remove from the pool.
     */
    public void remove(SimpleSession session) {
        mRegistry.remove(session.getUUID());
        mGroup.remove(session.mChannel);
        getEventManager().invokeAsyncEvent(new SessionClosedEvent(session));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean bind(InetAddress address, int port) {
        boolean isValid = false;
        try {
            mGroup.add(mBootstrap.bind(address, port).sync().channel());
            isValid = true;
        } catch (InterruptedException exception) {
            getUncaughtExceptionHandler().uncaughtException(null, exception);
        }
        return isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(String reason) {
        mRegistry.values().forEach(session -> session.disconnect(reason));

        final ChannelGroupFuture future = mGroup.close();
        try {
            future.await();
        } catch (InterruptedException ex) {
            getUncaughtExceptionHandler().uncaughtException(null, ex);
        }
        mBossGroup.shutdownGracefully();
        mWorkerGroup.shutdownGracefully();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session getSession(UUID identifier) {
        return mRegistry.get(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Session> getAllSessions() {
        final List<Session> sessions = new ArrayList<>(mRegistry.size());
        mRegistry.values().forEach(sessions::add);
        return sessions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect(Predicate<Session> predicate, String reason) {
        mRegistry.values().stream().filter(predicate).forEach(e -> e.disconnect(reason));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Predicate<Session> predicate, Message message) {
        mRegistry.values().stream().filter(predicate).forEach(e -> e.send(message));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Predicate<Session> predicate, Message message, boolean urgent) {
        mRegistry.values().stream().filter(predicate).forEach(e -> e.send(message, urgent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAll(Predicate<Session> predicate, Message... messages) {
        mRegistry.values().stream().filter(predicate).forEach(e -> e.sendAll(messages));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Session.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return mUncaughtExceptionHandler.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUncaughtExceptionHandler(Session.UncaughtExceptionHandler handler) {
        mUncaughtExceptionHandler.set(handler);
    }

    /**
     * Handle {@link SessionConnectedEvent}.
     *
     * @param event The reference of the event to handle.
     */
    private void onEvent(SessionConnectedEvent event) {
        final SimpleSession session = (SimpleSession) event.getSession();
        if (event.isCancelled()) {
            session.disconnect("<Cancelled>");
        } else {
            mGroup.add(session.mChannel);
            mRegistry.put(session.getUUID(), session);
        }
    }
}
