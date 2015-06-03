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

import com.github.aom.core.EngineAPI;
import com.github.aom.core.preference.Preference;
import com.github.aom.core.preference.yml.YMLPreference;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.api.map.primitive.MutableIntObjectMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.map.mutable.primitive.IntObjectHashMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * Default implementation for {@link PluginManager}.
 */
public final class SimplePluginManager implements PluginManager {
    /**
     * The name of the preferences file for the plug-in.
     */
    public final static String PREFERENCE_FILE_NAME = "plugin.yml";

    //!
    //! A mapping that contains each {@link Plugin}.
    //!
    private final MutableIntObjectMap<SimplePlugin> mPlugins = new IntObjectHashMap<>();

    /**
     * Loads all {@link SimplePlugin}s.
     * <br/>
     * NOTE: This method should be run on the main-thread.
     *
     * @param folder         The folder where to look for the plug-in's descriptors.
     * @param createIfAbsent True if the folder should be created.
     */
    public void loadAllPlugins(Path folder, boolean createIfAbsent) {
        final TreeMap<String, List<String>> dependencies = new TreeMap<>();
        final MutableMap<String, Path> paths = new UnifiedMap<>();

        try {
            if (!Files.exists(folder) && createIfAbsent) {
                Files.createDirectories(folder);
            }
            Files.newDirectoryStream(folder, "*.{jar}").forEach((path) -> {
                try {
                    final Descriptor descriptor = getDescriptor(path);

                    final List<String> depends = descriptor.getDependencies();
                    dependencies.put(descriptor.getName(), depends);

                    paths.put(descriptor.getName(), path);
                } catch (IOException exception) {
                    EngineAPI.getEngine().getLogger().error("Could not load '" + path.getFileName() + "'", exception);
                }
            });

            final HashSet<String> P = new HashSet<>();
            P.addAll(dependencies.keySet());

            // Find leaf nodes.
            final Queue<String> S = new LinkedBlockingDeque<>();
            S.addAll(P.stream()
                    .filter(t -> dependencies.get(t) == null || dependencies.get(t).isEmpty())
                    .collect(Collectors.toList()));

            // Visit all leaf nodes. Build result from vertices, that are visited
            // for the first time. Add vertices to not visited leaf vertices S, if
            // it contains current element n an all of it's values are visited.
            final HashSet<String> V = new HashSet<>();
            final ArrayList<String> L = new ArrayList<>(dependencies.size());
            String n;
            while (!S.isEmpty()) {
                if (V.add(n = S.poll())) {
                    L.add(n);
                }
                S.addAll(dependencies.keySet().stream()
                        .filter(t -> !V.contains(t) && V.containsAll(dependencies.get(t)))
                        .collect(Collectors.toList()));
            }

            // Print an error for each
            P.stream()
                    .filter(t -> !L.contains(t))
                    .forEach(t -> EngineAPI.getEngine().getLogger().warn("Cyclic dependency detected for: " + t));

            L.forEach(name -> safeLoadPlugin(paths.get(name)));
        } catch (Exception exception) {
            EngineAPI.getEngine().getLogger().warn("Exception when trying to load plug-ins", exception);
        }
    }

    /**
     * Unload all {@link SimplePlugin}s.
     * <br/>
     * NOTE: This method must run on the main-thread.
     */
    public void unloadAllPlugins() {
        mPlugins.values().stream().forEach(this::safeUnloadPlugin);
        mPlugins.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadPlugin(Path file) {
        safeLoadPlugin(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enablePlugin(Plugin plugin) {
        if (!(plugin instanceof SimplePlugin)) {
            throw new IllegalStateException("Invalid parameter.");
        }
        safeEnablePlugin((SimplePlugin) plugin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableAllPlugins() {
        mPlugins.forEachValue(this::safeEnablePlugin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disablePlugin(Plugin plugin) {
        if (!(plugin instanceof SimplePlugin)) {
            throw new IllegalStateException("Invalid parameter.");
        }
        safeDisablePlugin((SimplePlugin) plugin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableAllPlugins() {
        mPlugins.forEachValue(this::safeDisablePlugin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Plugin getPlugin(String name) {
        return mPlugins.get(name.hashCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Plugin> getAllPlugins() {
        return Collections.unmodifiableCollection(mPlugins.values());
    }

    /**
     * Unloads a {@link SimplePlugin}.
     *
     * @param plugin The plug-in to unload from memory.
     */
    private void safeUnloadPlugin(SimplePlugin plugin) {
        EngineAPI.getEngine().getLogger().info(plugin.getName() + " is being unloaded.");
        try {
            plugin.getListener().onPluginUnloaded(plugin);
        } catch (Throwable ignored) {
        }
        EngineAPI.getScheduler().cancelAllTasks(plugin);
        EngineAPI.getEventManager().unregisterAllEvents(plugin);
    }

    /**
     * Loads the given {@link SimplePlugin}.
     *
     * @param file The name of the file to load the plug-in.
     */
    private void safeLoadPlugin(Path file) {
        try {
            final Descriptor descriptor = getDescriptor(file);
            final Path path = file.getParent().resolve(descriptor.getName());
            Files.createDirectories(path);

            EngineAPI.getEngine().getLogger().info(descriptor.getName() + " is being loaded.");

            final List<String> dependencies = descriptor.getDependencies();
            for (String dependency : dependencies) {
                if (!mPlugins.containsKey(dependency.hashCode())) {
                    throw new IOException("Dependency missing: " + dependency);
                }
            }
            final SimplePlugin plugin = loadPluginInMemory(file, descriptor);
            mPlugins.put(descriptor.getName().hashCode(), plugin);
        } catch (IOException exception) {
            EngineAPI.getEngine().getLogger().warn("Exception trying to load a plug-in", exception);
        }
    }

    /**
     * Enables the given {@link SimplePlugin}.
     *
     * @param plugin The plug-in to enable.
     */
    private void safeEnablePlugin(SimplePlugin plugin) {
        if (!plugin.isDisabled()) {
            return;
        }
        EngineAPI.getEngine().getLogger().info(plugin.getName() + " is being enabled.");
        try {
            plugin.getListener().onPluginEnabled(plugin);
        } catch (Throwable exception) {
            EngineAPI.getEngine().getLogger().warn("Exception when trying to enable a plug-in.", exception);
        }
        plugin.setEnabled(true);
    }

    /**
     * Disables the given {@link SimplePlugin}.
     *
     * @param plugin The plug-in to disable.
     */
    private void safeDisablePlugin(SimplePlugin plugin) {
        if (!plugin.isEnabled()) {
            return;
        }
        EngineAPI.getEngine().getLogger().info(plugin.getName() + " is being disabled.");
        try {
            plugin.getListener().onPluginDisabled(plugin);
        } catch (Throwable exception) {
            EngineAPI.getEngine().getLogger().warn("Exception when trying to disable a plug-in.", exception);
        }
        plugin.setEnabled(true);
    }

    /**
     * Loads a {@link Plugin} from disk to memory.
     *
     * @param file       The path of the plug-in file.
     * @param descriptor The descriptor that contains the information of the plug-in.
     *
     * @return The instance of the plug-in in memory.
     * @throws java.io.IOException If the plug-in is invalid.
     */
    private SimplePlugin loadPluginInMemory(Path file, Descriptor descriptor) throws IOException {
        final SimplePlugin plugin;
        try {
            final String entry = descriptor.getEntry();
            if (entry == null) {
                throw new IOException("<Entry> is missing from descriptor file.");
            }

            final URLClassLoader loader = new URLClassLoader(new URL[]{file.toUri().toURL()}, getClass().getClassLoader());
            final Class<?> clazz = Class.forName(entry, true, loader);
            final PluginListener listener = clazz.asSubclass(PluginListener.class).newInstance();

            if (listener == null) {
                throw new IOException("<PluginListener> not found in the JAR.");
            }

            plugin = new SimplePlugin(listener, descriptor);
            listener.onPluginLoaded(plugin);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
            throw new IOException(exception);
        }
        return plugin;
    }

    /**
     * Retrieve a {@link Descriptor} for the given plug-in.
     *
     * @param file The path of the file to obtain from.
     *
     * @return The descriptor of the plug-in in memory.
     * @throws IOException If the descriptor is invalid.
     */
    private Descriptor getDescriptor(Path file) throws IOException {
        final FileSystem pack = FileSystems.newFileSystem(file, null);

        final Preference preference = new YMLPreference();
        try (InputStream input
                     = Files.newInputStream(pack.getPath(PREFERENCE_FILE_NAME), StandardOpenOption.READ)) {
            preference.parse(input);
        }
        preference.set("path", file.getParent());
        return new Descriptor(preference);
    }
}
