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

import java.net.InetAddress;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Define a service for managing multiple {@link Session}s.
 */
public interface SessionManager {
    /**
     * Define a predicate for sending a message to ALL sessions.
     */
    public final static Predicate<Session> ALL = (session -> true);

    /**
     * Binds the service to an address.
     *
     * @param address The internet address to bind to.
     * @param port    The port on which the session will bind to.
     */
    public boolean bind(InetAddress address, int port);

    /**
     * Stops the service and disconnects all sessions attached.
     *
     * @param reason The reason for disconnection.
     */
    public void stop(String reason);

    /**
     * Retrieve a {@link Session}.
     *
     * @param identifier An identifier for the session.
     */
    public Session getSession(UUID identifier);

    /**
     * Retrieve all {@link Session}s attached.
     *
     * @return A collection that contains all sessions.
     */
    public Collection<Session> getAllSessions();

    /**
     * Disconnect any number of {@link Session}s.
     *
     * @param predicate The predicate of the action.
     * @param reason    The reason of the disconnection.
     */
    public void disconnect(Predicate<Session> predicate, String reason);

    /**
     * Sends the given {@link Message} without any priority.
     *
     * @param predicate The predicate of the action.
     * @param message   The message to send.
     */
    public void send(Predicate<Session> predicate, Message message);

    /**
     * Sends the given {@link Message} with the given priority.
     *
     * @param predicate The predicate of the action.
     * @param message   The message to send.
     * @param urgent    Whether the message should be sent immediately.
     */
    public void send(Predicate<Session> predicate, Message message, boolean urgent);

    /**
     * Sends an arbitrary number of {@link Message}s.
     *
     * @param predicate The predicate of the action.
     * @param messages  A collection of messages.
     */
    public void sendAll(Predicate<Session> predicate, Message... messages);

    /**
     * Retrieve the uncaught exception handler for this service.
     *
     * @return A reference to the uncaught exception handler.
     */
    public Session.UncaughtExceptionHandler getUncaughtExceptionHandler();

    /**
     * Sets the exception handler for any uncaught exceptions for this service.
     *
     * @param handler The new handler for this service.
     */
    public void setUncaughtExceptionHandler(Session.UncaughtExceptionHandler handler);

    /**
     * Default exception handler for the service.
     */
    public static final class DefaultUncaughtExceptionHandler implements Session.UncaughtExceptionHandler {
        private final SessionManager mSessionManager;

        /**
         * Default constructor for {@link DefaultUncaughtExceptionHandler}.
         */
        public DefaultUncaughtExceptionHandler(SessionManager manager) {
            this.mSessionManager = manager;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void uncaughtException(Message message, Throwable throwable) {
            throwable.printStackTrace();
            mSessionManager.disconnect(ALL, throwable.getMessage());
        }
    }
}
