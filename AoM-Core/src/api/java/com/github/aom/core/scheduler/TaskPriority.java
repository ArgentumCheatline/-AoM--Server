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
package com.github.aom.core.scheduler;

/**
 * Enumerates the possible priorities of a {@link Task}.
 */
public enum TaskPriority {
    /**
     * Lowest priority task deferred up to 10 seconds.
     */
    LOWEST(10000),
    /**
     * Low priority task deferred up to 1.5 seconds.
     */
    LOW(1500),
    /**
     * Normal priority task deferred up to 0.5 seconds.
     */
    NORMAL(500),
    /**
     * High priority task deferred up to 0.15 seconds.
     */
    HIGH(150),
    /**
     * Highest priority task deferred up to 0.05 seconds.
     */
    HIGHEST(50),
    /**
     * Critical priority task are never deferred.
     */
    CRITICAL(0);

    private final int mDeferredTime;

    /**
     * Enumeration constructor for {@link TaskPriority}.
     */
    private TaskPriority(int deferredTime) {
        mDeferredTime = deferredTime;
    }

    /**
     * Retrieves the timing of the priority in-case the executor is overloaded.
     *
     * @return A number that indicates how many millisecond the task is deferred.
     */
    public int getDeferredTime() {
        return mDeferredTime;
    }
}
