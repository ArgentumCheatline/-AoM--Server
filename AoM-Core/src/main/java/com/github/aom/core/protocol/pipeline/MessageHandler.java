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
package com.github.aom.core.protocol.pipeline;

import com.github.aom.core.protocol.Message;
import com.github.aom.core.protocol.SimpleSession;
import com.github.aom.core.protocol.SimpleSessionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * Define the {@link SimpleChannelInboundHandler} for {@link SimpleSessionManager}.
 */
public final class MessageHandler extends SimpleChannelInboundHandler<Message> {
    private final SimpleSessionManager mManager;
    private SimpleSession mSession;

    /**
     * Default constructor for {@link MessageHandler}.
     *
     * @param manager The session manager.
     */
    public MessageHandler(SimpleSessionManager manager) {
        this.mManager = manager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void channelActive(ChannelHandlerContext context) {
        mSession = new SimpleSession(UUID.randomUUID(), context.channel());
        mManager.add(mSession);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void channelInactive(ChannelHandlerContext context) {
        mManager.remove(mSession);
        mSession.disconnect("<Inactive>");
        mSession = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Message msg) throws Exception {
        mSession.addMessageToQueue(msg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        if (context.channel().isActive()) {
            context.close();
        }
        mSession.getUncaughtExceptionHandler().uncaughtException(null, cause);
        mSession = null;
    }
}
