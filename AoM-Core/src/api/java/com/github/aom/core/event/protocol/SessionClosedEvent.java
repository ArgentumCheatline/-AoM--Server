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
package com.github.aom.core.event.protocol;

import com.github.aom.core.protocol.Session;

/**
 * Define an {@link SessionEvent} to handle when a close a connection.
 */
public final class SessionClosedEvent extends SessionEvent {
    /**
     * Default constructor for {@link SessionClosedEvent}.
     *
     * @param session The session of the event.
     */
    public SessionClosedEvent(Session session) {
        super(session, false);
    }
}
