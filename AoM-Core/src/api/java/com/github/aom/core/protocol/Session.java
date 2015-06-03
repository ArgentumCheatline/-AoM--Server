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

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * A single session, which may or may not be associated with a controller.
 */
public interface Session {
    /**
     * Check if the session is still active.
     *
     * @return True if the session is active, false otherwise.
     */
    public boolean isActive();

    /**
     * Retrieve a unique identifier for this session.
     *
     * @return A reference to the unique identifier of the session.
     */
    public UUID getUUID();

    /**
     * Retrieve the address of the session.
     *
     * @return A reference to the address of the session.
     */
    public InetSocketAddress getAddress();

    /**
     * Sends the given {@link Message} without any priority.
     *
     * @param message The message to send.
     */
    public void send(Message message);

    /**
     * Sends the given {@link Message} with the given priority.
     *
     * @param message The message to send.
     * @param urgent  Whether the message should be sent immediately.
     */
    public void send(Message message, boolean urgent);

    /**
     * Sends an arbitrary number of {@link Message}s.
     *
     * @param messages A collection of messages.
     */
    public void sendAll(Message... messages);

    /**
     * Disconnects the session with the given reason.
     *
     * @param reason The reason why the session was disconnected.
     */
    public void disconnect(String reason);

    /**
     * Sets the event of the session.
     *
     * @param protocol The new event of the session.
     */
    public void setProtocol(Protocol protocol);

    /**
     * Retrieve the current protocol of the session.
     *
     * @return A reference to the current protocol.
     */
    public Protocol getProtocol();

    /**
     * Retrieve the uncaught exception handler for this session.
     *
     * @return A reference to the uncaught exception handler.
     */
    public UncaughtExceptionHandler getUncaughtExceptionHandler();

    /**
     * Sets the exception handler for any uncaught exceptions for this session.
     *
     * @param handler The new handler for this session.
     */
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler handler);

    /**
     * Defines an exception handler for any exception which occurs whilst handling a message.
     */
    public static interface UncaughtExceptionHandler {
        /**
         * Called when a throwable is thrown during message handling and it is not handled.
         *
         * @param message   The message which threw the exception.
         * @param throwable The throwable thrown.
         */
        public void uncaughtException(Message message, Throwable throwable);
    }

    /**
     * Default exception handler for a {@link Session}.
     */
    public static final class DefaultUncaughtExceptionHandler implements UncaughtExceptionHandler {
        private final Session mSession;

        /**
         * Default constructor for {@link DefaultUncaughtExceptionHandler}.
         */
        public DefaultUncaughtExceptionHandler(Session session) {
            this.mSession = session;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void uncaughtException(Message message, Throwable throwable) {
            mSession.disconnect("An exception was raised: " + throwable.getMessage());
        }
    }
}
