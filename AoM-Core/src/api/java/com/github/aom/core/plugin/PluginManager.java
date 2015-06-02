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

import java.nio.file.Path;
import java.util.Collection;

/**
 * Encapsulate a service for loading and managing {@link Plugin}s.
 */
public interface PluginManager {
    /**
     * Loads a plug-in.
     * <br/>
     * If the plug-in is already loaded then has no effect.
     *
     * @throws InvalidPluginException If the plug-in is invalid.
     */
    public void loadPlugin(Path file) throws InvalidPluginException;

    /**
     * Enables a plug-in.
     * <br/>
     * If the plug-in is already enabled then has no effect.
     *
     * @param plugin The plug-in to enable.
     */
    public void enablePlugin(Plugin plugin);

    /**
     * Enables all plug-in(s).
     */
    public void enableAllPlugins();

    /**
     * Disable a plug-in.
     * <br/>
     * If the plug-in is already disabled then has no effect.
     *
     * @param plugin The plug-in to disable.
     */
    public void disablePlugin(Plugin plugin);

    /**
     * Disable all plug-in(s).
     */
    public void disableAllPlugins();

    /**
     * Retrieve a plug-in from the context.
     *
     * @param name The name of the plug-in to retrieve.
     */
    public Plugin getPlugin(String name);

    /**
     * Retrieve all plug-in that has been loaded by the context.
     */
    public Collection<Plugin> getAllPlugins();
}
