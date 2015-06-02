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
package com.github.aom.core.plugin;

/**
 * Define the listener for the {@link Plugin}s.
 */
public interface PluginListener {
    /**
     * Called when the plug-in has been enabled.
     *
     * @param plugin The plug-in that owns the listener.
     */
    public void onPluginEnabled(Plugin plugin);

    /**
     * Called when the plug-in has been disabled.
     *
     * @param plugin The plug-in that owns the listener.
     */
    public void onPluginDisabled(Plugin plugin);

    /**
     * Called when the plug-in has been loaded.
     *
     * @param plugin The plug-in that owns the listener.
     */
    public void onPluginLoaded(Plugin plugin);

    /**
     * Called when the plug-in has been unloaded.
     *
     * @param plugin The plug-in that owns the listener.
     */
    public void onPluginUnloaded(Plugin plugin);
}
