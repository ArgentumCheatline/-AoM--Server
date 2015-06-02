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
import com.github.aom.core.preference.Preferences;
import com.github.aom.core.scheduler.Scheduler;

/**
 * Define a singleton instance of {@link Engine} to access across the entire application.
 */
public final class EngineAPI {
    //!
    //! Reference to the engine instance.
    //!
    private static Engine sEngine;

    /**
     * Prevent this class to be initialised.
     */
    private EngineAPI() {
        throw new IllegalStateException("This class cannot be constructed.");
    }

    /**
     * Retrieves the {@link Engine} instance.
     *
     * @return A reference to the instance of the engine.
     */
    static public Engine getEngine() {
        if (sEngine == null) {
            throw new IllegalStateException("Engine singleton is not set.");
        }
        return sEngine;
    }

    /**
     * Sets the {@link Engine} instance.
     * <p>
     * NOTE: The instance cannot be set twice.
     *
     * @param engine The instance of the engine.
     *
     * @return A reference to the instance of the engine.
     */
    static public Engine setEngine(Engine engine) {
        if (sEngine != null) {
            throw new IllegalStateException("Engine singleton is already set.");
        }
        return sEngine = engine;
    }

    /**
     * {@see Engine#getPreferences}
     */
    static public Preferences getPreferences() {
        return sEngine.getPreferences();
    }

    /**
     * {@see Engine#getScheduler}
     */
    static public Scheduler getScheduler() {
        return sEngine.getScheduler();
    }

    /**
     * {@see Engine#getEventManager}
     */
    static public EventManager getEventManager() {
        return sEngine.getEventManager();
    }

    /**
     * {@see Engine#getPluginManager}
     */
    static public PluginManager getPluginManager() {
        return sEngine.getPluginManager();
    }
}
