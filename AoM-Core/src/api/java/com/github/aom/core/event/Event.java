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
 * Encapsulate the base class for any event implementation.
 */
public abstract class Event {
    private boolean mCancelled;
    private boolean mCancellable;

    /**
     * Default constructor for {@link Event}.
     *
     * @param cancellable True if the event is allowed to be cancelled.
     */
    public Event(boolean cancellable) {
        this.mCancelled = false;
        this.mCancellable = cancellable;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     */
    final public boolean isCancelled() {
        return mCancellable && mCancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel True if you wish to cancel this event.
     */
    final public void setCancelled(boolean cancel) {
        mCancelled = cancel;
    }
}
