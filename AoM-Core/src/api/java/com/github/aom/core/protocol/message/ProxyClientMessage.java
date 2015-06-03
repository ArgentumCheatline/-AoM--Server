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

import com.github.aom.core.protocol.Message;

/**
 * Encapsulate a {@link Message} that represent a packet for the client.
 */
public final class ProxyClientMessage extends Message {
    private final byte[] mBytes;

    /**
     * Default constructor for {@link ProxyClientMessage}.
     *
     * @param bytes A collection of bytes.
     */
    public ProxyClientMessage(byte[] bytes) {
        this.mBytes = bytes;
    }

    /**
     * Retrieves the bytes of the packet.
     *
     * @return A reference to the collection of bytes.
     */
    public byte[] getBytes() {
        return mBytes;
    }
}
