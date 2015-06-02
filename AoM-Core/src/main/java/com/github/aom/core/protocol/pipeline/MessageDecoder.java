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
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * Define the {@link ReplayingDecoder} for handling {@link Message}s.
 * <br/>
 * It converts raw frames to {@link Message} frames.
 */
public final class MessageDecoder extends ReplayingDecoder<MessageDecoder.DecoderState> {
    private volatile Protocol mProtocol;
    private int mMessageId;
    private int mMessageLength;

    /**
     * Default constructor for {@link MessageDecoder}.
     */
    public MessageDecoder() {
        super(DecoderState.READ_ID);
    }

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
    protected void decode(ChannelHandlerContext context, ByteBuf input, List<Object> output) throws Exception {
        switch (state()) {
            case READ_ID:
                mMessageId = input.readUnsignedByte();
                checkpoint(DecoderState.READ_LENGTH);
                break;
            case READ_LENGTH:
                mMessageLength = input.readUnsignedShort();
                checkpoint(DecoderState.READ_CONTENT);
                break;
            case READ_CONTENT:
                output.add(mProtocol.decode(mMessageId, input.readBytes(mMessageLength).nioBuffer()));
                checkpoint(DecoderState.READ_ID);
        }
    }

    /**
     * All possible states of {@link MessageDecoder}.
     */
    protected static enum DecoderState {
        READ_ID,
        READ_LENGTH,
        READ_CONTENT
    }
}
