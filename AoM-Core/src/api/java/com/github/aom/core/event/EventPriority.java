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

/**
 * Enumerates all possible priorities of an {@link Event}.
 */
public enum EventPriority {
    /**
     * Highest priority is executed before all others.
     */
    HIGHEST(0, true),
    /**
     * High priority which ignores cancelled events.
     */
    HIGH_IGNORE_CANCELLED(1, true),
    /**
     * High priority executed after highest priority.
     */
    HIGH(1, false),
    /**
     * Normal priority which ignores cancelled events.
     */
    NORMAL_IGNORE_CANCELLED(2, true),
    /**
     * Normal priority executed after high priority.
     */
    NORMAL(2, false),
    /**
     * Low priority which ignores cancelled events.
     */
    LOW_IGNORE_CANCELLED(3, true),
    /**
     * Low is executed after lowest priorities.
     */
    LOW(3, false),
    /**
     * Lowest priority which ignores cancelled events.
     */
    LOWEST_IGNORE_CANCELLED(4, true),
    /**
     * Lowest is executes as the first priority.
     */
    LOWEST(4, false);

    private final boolean mIgnoresCancelled;
    private final int mPriority;

    /**
     * Enumeration constructor for {@link EventPriority}.
     */
    private EventPriority(int priority, boolean ignoresCancelled) {
        this.mPriority = priority;
        this.mIgnoresCancelled = ignoresCancelled;
    }

    /**
     * Check if the priority allows to ignore cancelled status.
     *
     * @return True if the priority ignores cancelled, false otherwise.
     */
    public boolean ignoresCancelled() {
        return mIgnoresCancelled;
    }

    /**
     * Retrieve the priority of the enumeration.
     *
     * @return The priority of the enumeration.
     */
    public int getPriority() {
        return mPriority;
    }
}
