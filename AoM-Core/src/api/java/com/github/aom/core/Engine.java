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
package com.github.aom.core;

import com.github.aom.core.event.EventManager;
import com.github.aom.core.plugin.PluginManager;
import com.github.aom.core.preference.Preference;
import com.github.aom.core.protocol.SessionManager;
import com.github.aom.core.scheduler.Scheduler;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

/**
 * Define the engine.
 * <br/>
 * Contains every service in a modular paradigm.
 */
public interface Engine {
    /**
     * Initialise the engine.
     *
     * @param start A consumer to notify the engine has been started.
     * @param stop  A consumer to notify the engine has been stopped.
     */
    public void initialise(Consumer<Engine> start, Consumer<Engine> stop);

    /**
     * Destroy the engine.
     */
    public void destroy();

    /**
     * Retrieves the version of the engine.
     *
     * @return A reference to the version of the engine.
     */
    public String getVersion();

    /**
     * Retrieve the {@link Logger} of the engine.
     *
     * @return A reference to the logger of the engine.
     */
    public Logger getLogger();

    /**
     * Retrieves the {@link com.github.aom.core.preference.PreferenceSection} of the engine.
     *
     * @return A reference to the preferences of the engine.
     */
    public Preference getPreferences();

    /**
     * Retrieve the {@link Scheduler} service of the engine.
     *
     * @return A reference to the scheduler of the engine.
     */
    public Scheduler getScheduler();

    /**
     * Retrieve the {@link EventManager} service of the engine.
     *
     * @return A reference to the event manager of the engine.
     */
    public EventManager getEventManager();

    /**
     * Retrieve the {@link PluginManager} service of the engine.
     *
     * @return A reference to the plugin manager of the engine.
     */
    public PluginManager getPluginManager();

    /**
     * Retrieve the {@link SessionManager} service of the engine.
     *
     * @return A reference to the session manager of the engine.
     */
    public SessionManager getSessionManager();
}
