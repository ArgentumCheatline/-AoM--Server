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

import com.github.aom.core.protocol.SimpleSession;
import com.github.aom.core.protocol.SimpleSessionManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.io.IOException;

/**
 * Define a {@link ChannelInitializer} for {@link SimpleSessionManager}.
 */
public class MessageInitializer extends ChannelInitializer<SocketChannel> {
    private final SimpleSessionManager mParent;

    /**
     * Default constructor for {@link MessageInitializer}.
     *
     * @param parent The session manager.
     */
    public MessageInitializer(SimpleSessionManager parent) {
        this.mParent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initChannel(SocketChannel ch) throws IOException {
        ch.pipeline().addLast(SimpleSession.HANDLER_DECODER, new MessageDecoder());
        ch.pipeline().addLast(SimpleSession.HANDLER_ENCODER, new MessageEncoder());
        ch.pipeline().addLast(SimpleSession.HANDLER, new MessageHandler(mParent));
    }
}