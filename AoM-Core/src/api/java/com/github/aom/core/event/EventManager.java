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
package com.github.aom.core.event;

import java.util.function.Consumer;

/**
 * Define a service for subscribing to {@link Event}s.
 */
public interface EventManager {
    /**
     * Invokes a synchronised event.
     *
     * @param event The event to be invoked by the manager.
     */
    public <T extends Event> T invokeEvent(T event);

    /**
     * Invokes a synchronised event.
     *
     * @param event    The event to be invoked by the manager.
     * @param consumer The consumer to be called after being invoked.
     */
    public <T extends Event> T invokeEvent(T event, Consumer<T> consumer);

    /**
     * Invokes an asynchronous event and executes the completion consumer afterwards.
     *
     * @param event    The event to be invoked by the manager.
     */
    default public <T extends Event> void invokeAsyncEvent(T event) {
        invokeAsyncEvent(event, null);
    }

    /**
     * Invokes an asynchronous event and executes the completion consumer afterwards.
     *
     * @param event    The event to be invoked by the manager.
     * @param consumer The consumer to be called after being invoked.
     */
    public <T extends Event> void invokeAsyncEvent(T event, Consumer<T> consumer);

    /**
     * Subscribe for a particular {@link Event} with {@link EventPriority#NORMAL} priority.
     *
     * @param owner    The owner of the consumer.
     * @param consumer The consumer to register to the given event.
     */
    default public <T extends Event> int registerEvent(Object owner, Consumer<T> consumer) {
        return registerEvent(owner, consumer, EventPriority.NORMAL);
    }

    /**
     * Subscribe for a particular {@link Event} with the given {@link EventPriority}.
     *
     * @param owner    The owner of the consumer.
     * @param consumer The consumer to register to the given event.
     * @param priority The priority of the consumer.
     */
    public <T extends Event> int registerEvent(Object owner, Consumer<T> consumer, EventPriority priority);

    /**
     * Subscribe any number of consumers for any number of {@link Event}s.
     *
     * @param owner    The owner of the consumers.
     * @param listener The object that contains the consumers.
     */
    public void registerEvents(Object owner, Object listener);

    /**
     * Unsubscribe a {@link Event} previously registered.
     *
     * @param id The unique identifier for the event.
     */
    public void unregisterEvent(int id);

    /**
     * Unsubscribe any number of consumers for any number of {@link Event}s.
     *
     * @param listener The object that contains the consumers.
     */
    public void unregisterEvents(Object listener);

    /**
     * Unregister all {@link Event}s that belong to the given owner.
     *
     * @param owner The owner of the events.
     */
    public void unregisterAllEvents(Object owner);

    /**
     * Unregister all {@link Event}s.
     */
    public void unregisterAllEvents();
}
