/*
 * This file is part of Ghrum, licensed under the Apache 2.0 License.
 *
 * Copyright (c) 2014-2015 Agustin Alvarez <wolftein1@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.aom.core.event;

import com.github.aom.core.EngineAPI;
import com.gs.collections.api.map.primitive.MutableIntIntMap;
import com.gs.collections.api.map.primitive.MutableIntObjectMap;
import com.gs.collections.impl.factory.primitive.IntIntMaps;
import com.gs.collections.impl.factory.primitive.IntObjectMaps;
import net.jodah.typetools.TypeResolver;

import java.lang.reflect.Method;
import java.util.PriorityQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Define the implementation of {@link EventManager}.
 */
public final class SimpleEventManager implements EventManager {
    private final MutableIntIntMap mIds = IntIntMaps.mutable.empty();
    private final MutableIntObjectMap<PriorityQueue<EventExecutor>> mEvents = IntObjectMaps.mutable.empty();
    private int mIndex;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Event> T invokeEvent(T event) {
        final PriorityQueue<EventExecutor> executors = mEvents.get(event.getClass().hashCode());
        if (executors != null) {
            executors.forEach(executor -> executor.execute(event));
        }
        return event;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Event> T invokeEvent(T event, Consumer<T> consumer) {
        consumer.accept(invokeEvent(event));
        return event;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Event> void invokeAsyncEvent(T event, Consumer<T> consumer) {
        EngineAPI.getScheduler().invokeAsyncTask(null, (T) -> invokeEvent(event, consumer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Event> int registerEvent(Object owner, Consumer<T> consumer, EventPriority priority) {
        final Class<?> clazz = TypeResolver.resolveRawArgument(Consumer.class, consumer.getClass());

        final int id = mIndex++;
        mIds.put(id, clazz.hashCode());

        PriorityQueue<EventExecutor> executor = mEvents.get(clazz.hashCode());
        if (executor == null) {
            mEvents.put(clazz.hashCode(), executor = new PriorityQueue<>());
        }
        executor.add(new EventExecutor<>(id, owner, null, consumer, priority));
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerEvents(Object owner, Object listener) {
        final Method[] methods = listener.getClass().getDeclaredMethods();
        for (final Method method : methods) {
            // Only those methods with EventHandler annotation.
            if (!method.isAnnotationPresent(EventHandler.class)) {
                continue;
            }

            // Make the method accessible if it is not.
            // NOTE: Private and protected methods are not accessible.
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            final EventHandler annotation = method.getAnnotation(EventHandler.class);

            final Class<?> clazz = method.getParameterTypes()[0];
            PriorityQueue<EventExecutor> executor = mEvents.get(clazz.hashCode());
            if (executor == null) {
                mEvents.put(clazz.hashCode(), executor = new PriorityQueue<>());
            }
            executor.add(new EventExecutor<>(mIndex++, owner, listener, (e) -> {
                try {
                    method.invoke(listener, e);
                } catch (Exception exception) {
                    EngineAPI.getEngine().getLogger().warn(exception);
                }
            }, annotation.priority()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterEvent(int id) {
        final int type = mIds.get(id);
        final PriorityQueue<EventExecutor> executors = mEvents.get(type);

        if (executors == null) {
            throw new IllegalStateException("No events to unregistered of the given type");
        }
        final boolean isRemoved = executors.removeIf(
                (EventExecutor executor) -> executor.mId == id
        );
        if (!isRemoved) {
            throw new IllegalStateException("Failed to unregister event from given plug-in");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterEvents(Object listener) {
        final Predicate<EventExecutor> predicate = executor ->
                executor.mContainer != null && executor.mContainer.equals(listener);
        mEvents.forEachValue(collection -> collection.removeIf(predicate));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterAllEvents(Object owner) {
        final Predicate<EventExecutor> predicate = executor ->
                executor.mOwner != null ? executor.mOwner.equals(owner) : owner == null;
        mEvents.forEachValue(collection -> collection.removeIf(predicate));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterAllEvents() {
        mEvents.clear();
    }
}
