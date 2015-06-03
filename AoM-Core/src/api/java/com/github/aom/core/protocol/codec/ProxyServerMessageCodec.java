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
package com.github.aom.core.protocol.codec;

import com.github.aom.core.protocol.InvalidMessageException;
import com.github.aom.core.protocol.MessageCodec;
import com.github.aom.core.protocol.message.ProxyServerMessage;

import java.nio.ByteBuffer;

/**
 * Encapsulate the {@link MessageCodec} for {@link ProxyServerMessage}.
 */
public final class ProxyServerMessageCodec extends MessageCodec<ProxyServerMessage> {
    /**
     * Default constructor for {@link ProxyServerMessageCodec}.
     */
    public ProxyServerMessageCodec() {
        super(0x02, ProxyServerMessage.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer encode(ProxyServerMessage packet) throws InvalidMessageException {
        return ByteBuffer.wrap(packet.getBytes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProxyServerMessage decode(ByteBuffer buffer) throws InvalidMessageException {
        return new ProxyServerMessage(buffer.array());
    }
}
