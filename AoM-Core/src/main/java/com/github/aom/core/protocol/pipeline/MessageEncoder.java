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
import com.github.aom.core.protocol.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Define a {@link MessageToMessageEncoder} for {@link Message}s
 */
public final class MessageEncoder extends MessageToMessageEncoder {
    private final Protocol mProtocol;

    /**
     * Default constructor for {@link MessageEncoder}.
     */
    public MessageEncoder(Protocol protocol) {
        this.mProtocol = protocol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void encode(ChannelHandlerContext context, Object message, List output) throws IOException {
        try {
            final ByteBuffer buf1 = mProtocol.encode((Message) message);
            final ByteBuf buf2 = Unpooled.wrappedBuffer(buf1.array());
            output.add(buf2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }
}
