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
package com.github.aom.core.protocol.message;

import com.github.aom.core.protocol.InvalidMessageException;
import com.github.aom.core.protocol.MessageCodec;

import java.nio.ByteBuffer;

/**
 * Encapsulate the {@link MessageCodec} for {@link ProxyClientMessage}.
 */
public final class ProxyClientMessageCodec extends MessageCodec<ProxyClientMessage> {
    /**
     * Default constructor for {@link ProxyClientMessageCodec}.
     */
    public ProxyClientMessageCodec() {
        super(0x01, ProxyClientMessage.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer encode(ProxyClientMessage packet) throws InvalidMessageException {
        return ByteBuffer.wrap(packet.getBytes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProxyClientMessage decode(ByteBuffer buffer) throws InvalidMessageException {
        return new ProxyClientMessage(buffer.array());
    }
}
