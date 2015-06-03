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

import com.gs.collections.api.map.ImmutableMap;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.primitive.ImmutableIntObjectMap;
import com.gs.collections.api.map.primitive.MutableIntObjectMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.mutable.primitive.IntObjectHashMap;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

/**
 * Represent an interface for a protocol.
 * </p>
 * A protocol is being constructed using a <b>BUILDER</b> pattern.
 */
public final class Protocol {
    private final ImmutableIntObjectMap<MessageCodec<?>> mInbound;
    private final ImmutableMap<Class<? extends Message>, MessageCodec<?>> mOutbound;

    /**
     * Default constructor for {@link Protocol}.
     *
     * @param inbound  A collection of all inbound messages.
     * @param outbound A collection of all outbound messages.
     */
    protected Protocol(ImmutableIntObjectMap<MessageCodec<?>> inbound,
                       ImmutableMap<Class<? extends Message>, MessageCodec<?>> outbound) {
        this.mInbound = inbound;
        this.mOutbound = outbound;
    }

    /**
     * Decodes a {@link Message}.
     *
     * @param id    The unique identifier of the message.
     * @param input The buffer that contains the message.
     *
     * @throws InvalidMessageException If the message isn't registered or the buffer is invalid.
     */
    public <T extends Message> T decode(int id, ByteBuffer input) throws InvalidMessageException {
        if (id < 0 || id > mInbound.size()) {
            throw new IllegalArgumentException("Opcode " + id + " is out of bounds.");
        }
        final MessageCodec<T> codec = (MessageCodec<T>) mInbound.get(id);
        if (codec == null) {
            throw new InvalidMessageException("Unknown operation code: " + id);
        }
        return codec.decode(input);
    }

    /**
     * Encodes a {@link Message}.
     *
     * @param message The message to be encoded into a buffer.
     *
     * @throws InvalidMessageException If the message isn't registered or the buffer is invalid.
     */
    public <T extends Message> ByteBuffer encode(T message) throws InvalidMessageException {
        final MessageCodec<T> codec = (MessageCodec<T>) mOutbound.get(message.getClass());
        if (codec == null) {
            throw new InvalidMessageException("Unknown operation class: " + message.getClass());
        }
        final ByteBuffer body = codec.encode(message);
        final ByteBuffer header = ByteBuffer.allocate(3 + body.capacity())
                .put((byte) codec.getOpcode())
                .putShort((short) body.capacity());
        return header.put(body);
    }

    /**
     * Define a builder pattern for {@link com.github.aom.core.protocol.Protocol}.
     */
    public final static class Builder {
        private final MutableIntObjectMap<MessageCodec<?>> mInbound = new IntObjectHashMap<>();
        private final MutableMap<Class<? extends Message>, MessageCodec<?>> mOutbound = new UnifiedMap<>();

        /**
         * Register a new inbound message into the protocol.
         *
         * @param codec The reference of the codec type.
         */
        public <J extends Message, T extends MessageCodec<J>> Builder inbound(Class<T> codec) {
            final T instance = asCodecInstance(codec);
            if (mInbound.containsKey(instance.getOpcode())) {
                throw new IllegalStateException("The given codec is already registered.");
            }
            mInbound.put(instance.getOpcode(), instance);
            return this;
        }

        /**
         * Register a new outbound message into the protocol.
         *
         * @param codec The reference of the codec type.
         */
        public <J extends Message, T extends MessageCodec<J>> Builder outbound(Class<T> codec) {
            final T instance = asCodecInstance(codec);
            if (mInbound.containsKey(instance.getOpcode())) {
                throw new IllegalStateException("The given codec is already registered.");
            }
            mOutbound.put(instance.getType(), instance);
            return this;
        }

        /**
         * Builds a {@link com.github.aom.core.protocol.Protocol}.
         */
        public Protocol build() {
            return new Protocol(mInbound.toImmutable(), mOutbound.toImmutable());
        }

        /**
         * Creates a new {@link MessageCodec}.
         */
        private <J extends Message, T extends MessageCodec<J>> T asCodecInstance(Class<T> clazz) {
            Constructor<T> constructor;
            try {
                constructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                throw (IllegalStateException) new IllegalStateException().initCause(e);
            }
            try {
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw (IllegalStateException) new IllegalStateException().initCause(e);
            }
        }
    }
}
