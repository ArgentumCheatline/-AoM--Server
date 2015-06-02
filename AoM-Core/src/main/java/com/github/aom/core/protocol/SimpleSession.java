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

import com.github.aom.core.protocol.pipeline.MessageDecoder;
import com.github.aom.core.protocol.pipeline.MessageEncoder;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Default the implementation for {@link Session}.
 */
public final class SimpleSession implements Session {
    public final static String HANDLER = "HANDLER";
    public final static String HANDLER_ENCODER = "ENCODER";
    public final static String HANDLER_DECODER = "DECODER";

    protected final UUID mUUID;
    protected final Channel mChannel;
    protected final Queue<Message> mIncomingQueue;
    protected final Queue<Message> mOutgoingQueue;
    protected final AtomicReference<Protocol> mProtocol;
    protected final AtomicReference<UncaughtExceptionHandler> mUncaughtExceptionHandler;

    /**
     * Default constructor for {@link SimpleSession}.
     *
     * @param identifier The identifier of the session.
     * @param channel    The channel of the session.
     */
    public SimpleSession(UUID identifier, Channel channel) {
        this.mUUID = identifier;
        this.mChannel = channel;
        this.mIncomingQueue = new ArrayDeque<>();
        this.mOutgoingQueue = new ArrayDeque<>();
        this.mProtocol = new AtomicReference<>();
        this.mUncaughtExceptionHandler = new AtomicReference<>(new DefaultUncaughtExceptionHandler(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive() {
        return mChannel.isActive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getUUID() {
        return mUUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) mChannel.remoteAddress();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Message message) {
        send(message, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Message message, boolean urgent) {
        if (urgent && isActive()) {
            mChannel.writeAndFlush(message);
        } else {
            mOutgoingQueue.add(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAll(Message... message) {
        Arrays.asList(message).forEach(this::send);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect(String reason) {
        try {
            mChannel.disconnect().sync();
        } catch (InterruptedException exception) {
            getUncaughtExceptionHandler().uncaughtException(null, exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProtocol(Protocol protocol) {
        mProtocol.set(protocol);
        ((MessageEncoder) mChannel.pipeline().get(HANDLER_ENCODER)).setProtocol(protocol);
        ((MessageDecoder) mChannel.pipeline().get(HANDLER_DECODER)).setProtocol(protocol);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Protocol getProtocol() {
        return mProtocol.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return mUncaughtExceptionHandler.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler handler) {
        mUncaughtExceptionHandler.set(handler);
    }

    /**
     * Pulse the connection to handle all received messages and send all.
     */
    public void pulse() {
        while (!mIncomingQueue.isEmpty()) {
            handleMessage(mIncomingQueue.poll());
        }
        while (!mOutgoingQueue.isEmpty()) {
            mChannel.write(mOutgoingQueue.poll());
        }
        mChannel.flush();
    }

    /**
     * Adds a message into the incoming queue for handling later.
     *
     * @param message The message to handle by the connection.
     */
    public <T extends Message> void addMessageToQueue(T message) {
        mIncomingQueue.add(message);
    }

    /**
     * Handle a message that has been received by the connection.
     *
     * @param message The message to handle by the connection.
     */
    private <T extends Message> void handleMessage(T message) {
        final Protocol protocol = mProtocol.get();
        if (protocol != null) {
            try {
                protocol.handle(this, message);
            } catch (Exception ex) {
                getUncaughtExceptionHandler().uncaughtException(message, ex);
            }
        }
    }
}
