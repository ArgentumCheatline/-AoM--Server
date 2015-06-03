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
import com.github.aom.core.event.SimpleEventManager;
import com.github.aom.core.plugin.PluginManager;
import com.github.aom.core.plugin.SimplePluginManager;
import com.github.aom.core.preference.Preference;
import com.github.aom.core.preference.yml.YMLPreference;
import com.github.aom.core.protocol.SessionManager;
import com.github.aom.core.protocol.SimpleSessionManager;
import com.github.aom.core.scheduler.Scheduler;
import com.github.aom.core.scheduler.SimpleScheduler;
import com.github.aom.core.scheduler.TaskPriority;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.util.function.Consumer;

/**
 * Default implementation for {@link Engine}.
 */
public final class SimpleEngine implements Engine {
    /**
     * The version of the engine.
     */
    private final static String VERSION = "AO-Modding (Server) v0.100";

    /**
     * Folder where all plug-ins are.
     */
    private final static String PLUGIN_FOLDER = "plugins";

    /**
     * Preferences files.
     */
    private final static String PREFERENCE_FILE = "preferences.yml";

    /**
     * The logger of {@link Engine}.
     */
    private final static Logger LOGGER = LogManager.getLogger("AoM");

    //!
    //! Define the {@link Scheduler}.
    //!
    private final SimpleScheduler mScheduler = new SimpleScheduler(60);

    //!
    //! Define the {@link EventManager}.
    //!
    private final SimpleEventManager mEventManager = new SimpleEventManager();

    //!
    //! Define the {@link PluginManager}.
    //!
    private final SimplePluginManager mPluginManager = new SimplePluginManager();

    //!
    //! Define the {@link SessionManager}.
    //!
    private final SimpleSessionManager mSessionManager = new SimpleSessionManager();

    //!
    //! Define the {@link Preference}.
    //!
    private final Preference mPreferences = new YMLPreference();

    //!
    //! The shutdown thread.
    //!
    private final Thread mShutdownThread = new Thread(this::destroy);

    //!
    //! The consumers.
    //!
    private Consumer<Engine> mDestroyMethod;

    /**
     * Default constructor for {@link SimpleEngine}.
     */
    public SimpleEngine() {
        EngineAPI.setEngine(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(Consumer<Engine> start, Consumer<Engine> stop) {
        mDestroyMethod = stop;

        // Initialise the shutdown hook.
        Runtime.getRuntime().addShutdownHook(mShutdownThread);

        // [INIT] Packages
        LOGGER.info("Loading packages...");
        initPackages();

        // [INIT] Network
        LOGGER.info("Loading network packages...");
        initNetworkPackages();

        if (start != null) {
            start.accept(this);
        }
        mScheduler.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        try {
            Runtime.getRuntime().removeShutdownHook(mShutdownThread);
        } catch (Exception ignored) {
            /* Sometimes the engine is pre mature destroyed */
        }
        if (mDestroyMethod != null) {
            mDestroyMethod.accept(this);
        }

        shutdownPackages();
        shutdownOtherPackages();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Preference getPreferences() {
        return mPreferences;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scheduler getScheduler() {
        return mScheduler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventManager getEventManager() {
        return mEventManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PluginManager getPluginManager() {
        return mPluginManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionManager getSessionManager() {
        return mSessionManager;
    }

    /**
     * Load all packages.
     */
    private void initPackages() {
        // Load preferences, and if does not exist create a new file.
        try {
            mPreferences.parseIfAbsentCreate(PREFERENCE_FILE,
                    SimpleEngine.class.getResourceAsStream("/default/preferences.yml"));
        } catch (IOException exception) {
            LOGGER.warn(exception);
        }

        // Load plug-ins and enable them all.
        mPluginManager.loadAllPlugins(FileSystems.getDefault().getPath(PLUGIN_FOLDER), true);
        mPluginManager.enableAllPlugins();
    }

    /**
     * Load all network packages.
     */
    private void initNetworkPackages() {
        InetAddress address;
        try {
            address = InetAddress.getByName(mPreferences.getString("net_wIP", "0.0.0.0"));
        } catch (UnknownHostException e) {
            getLogger().warn("Trying to bind to an invalid address", e);
            address = new InetSocketAddress(0).getAddress();
        }
        final int port = mPreferences.getInt("net_wPort", 10000);

        mSessionManager.bind(address, port);
        getLogger().info("Server listening to: " + address.getHostAddress() + ":" + port);

        // Pulse the SessionManager every tick.
        mScheduler.invokeRepeatingTask(null, (T) -> mSessionManager.pulse(), TaskPriority.CRITICAL, 0L, 1L);
    }

    /**
     * Shutdown all packages.
     */
    private void shutdownPackages() {
        // Shutdown Plug-ins.
        mPluginManager.disableAllPlugins();
        mPluginManager.unloadAllPlugins();

        // Shutdown Scheduler
        mScheduler.stop();
    }

    /**
     * Shutdown other packages.
     */
    private void shutdownOtherPackages() {
        // Shutdowns LOG4J
        Configurator.shutdown((LoggerContext) LogManager.getContext());
    }
}
