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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

/**
 * Default implementation of {@link Plugin}.
 */
public final class SimplePlugin implements Plugin {
    private final PluginListener mListener;
    private final Descriptor mDescriptor;
    private final Logger mLogger;
    private boolean mEnabled;

    /**
     * Default constructor for {@link SimplePlugin}.
     *
     * @param listener   The listener of the plug-in.
     * @param descriptor The descriptor of the plug-in.
     */
    protected SimplePlugin(PluginListener listener, Descriptor descriptor) {
        this.mListener = listener;
        this.mDescriptor = descriptor;
        this.mLogger = LogManager.getLogger(descriptor.getName());
        this.mEnabled = false;
    }

    /**
     * Change the state of this plug-in.
     *
     * @param isEnabled True if the plug-in is enabled, false otherwise.
     */
    public void setEnabled(boolean isEnabled) {
        mEnabled = isEnabled;
    }

    /**
     * Retrieves the {@link PluginListener} of the plug-in.
     *
     * @return The listener of the plug-in.
     */
    public PluginListener getListener() {
        return mListener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return mDescriptor.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger() {
        return mLogger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Descriptor getDescriptor() {
        return mDescriptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getFolder() {
        return mDescriptor.getPath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDisabled() {
        return !mEnabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return mEnabled;
    }
}