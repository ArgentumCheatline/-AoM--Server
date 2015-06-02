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

import com.github.aom.core.protocol.InvalidMessageException;
import com.github.aom.core.protocol.Message;
import com.github.aom.core.protocol.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.util.List;

/**
 * Define a {@link MessageToMessageEncoder} for {@link Message}s
 */
public final class MessageEncoder extends MessageToMessageEncoder {
    private volatile Protocol mProtocol;

    /**
     * Sets the event of the decoder.
     *
     * @param protocol The new event of the decoder.
     */
    public void setProtocol(Protocol protocol) {
        mProtocol = protocol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void encode(ChannelHandlerContext context, Object message, List output) throws IOException {
        try {
            output.add(mProtocol.encode((Message) message));
        } catch (InvalidMessageException e) {
            throw new IOException(e);
        }
    }
}
