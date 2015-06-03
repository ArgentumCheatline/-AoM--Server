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

import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

/**
 * Encapsulate the base class for any type of plug-in.
 */
public interface Plugin {
    /**
     * Retrieve the name of the plug-in.
     *
     * @return A reference to the name of the plug-in.
     */
    public String getName();

    /**
     * Retrieve the {@link Logger} of the plug-in.
     *
     * @return A reference to the logger of the plug-in.
     */
    public Logger getLogger();

    /**
     * Retrieve the {@link Descriptor} of the plug-in.
     *
     * @return A reference to the descriptor of the plug-in.
     */
    public Descriptor getDescriptor();

    /**
     * Retrieve the folder of the plug-in's assets.
     *
     * @return A reference to the folder of the plug-in.
     */
    public Path getFolder();

    /**
     * Check if the plug-in is disabled by the parent context.
     *
     * @return True if the plug-in is disabled, false otherwise.
     */
    public boolean isDisabled();

    /**
     * Check if the plug-in is enabled by the parent context.
     *
     * @return True if the plug-in is enabled, false otherwise.
     */
    public boolean isEnabled();
}
