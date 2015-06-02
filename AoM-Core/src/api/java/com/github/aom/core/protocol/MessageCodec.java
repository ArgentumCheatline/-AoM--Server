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

import java.nio.ByteBuffer;

/**
 * Represent the codec of a {@link Message} implementation.
 */
public abstract class MessageCodec<T extends Message> {
    protected final Class<T> mType;
    protected final int mOpcode;

    /**
     * Default constructor for {@link MessageCodec}.
     */
    protected MessageCodec(int opcode, Class<T> clazz) {
        this.mOpcode = opcode;
        this.mType = clazz;
    }

    /**
     * Retrieve the identifier of the packet.
     */
    public final Class<T> getType() {
        return mType;
    }

    /**
     * Retrieve the opcode of the packet.
     */
    public final int getOpcode() {
        return mOpcode;
    }

    /**
     * Encodes the given {@link Message} into a NIO buffer.
     *
     * @param packet The message to be encoded.
     *
     * @throws InvalidMessageException If the message has invalid codification.
     */
    public abstract ByteBuffer encode(T packet) throws InvalidMessageException;

    /**
     * Decodes a {@link Message} from the given NIO buffer.
     *
     * @param buffer The buffer that contains the message data.
     *
     * @throws InvalidMessageException If the buffer has invalid codification.
     */
    public abstract T decode(ByteBuffer buffer) throws InvalidMessageException;
}