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
package com.github.aom.core.event.plugin;

import com.github.aom.core.event.Event;
import com.github.aom.core.plugin.Plugin;

/**
 * An {@link Event} used to signify a {@link Plugin} based event.
 */
public abstract class PluginEvent extends Event {
    private final Plugin mPlugin;

    /**
     * Default constructor for {@link PluginEvent}.
     *
     * @param plugin        The plug-in of the event.
     * @param isCancellable True if the event is cancellable.
     */
    public PluginEvent(Plugin plugin, boolean isCancellable) {
        super(isCancellable);
        mPlugin = plugin;
    }

    /**
     * Retrieves the {@link Plugin} of the event.
     *
     * @return The plug-in of the event.
     */
    public final Plugin getPlugin() {
        return mPlugin;
    }
}