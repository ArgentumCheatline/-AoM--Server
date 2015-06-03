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

import com.github.aom.core.preference.PreferenceSection;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulate the primary information about a {@link Plugin}.
 */
public final class Descriptor {
    private final Path mPath;
    private final List<String> mAuthors;
    private final List<String> mDependencies;
    private final String mName;
    private final String mWebsite;
    private final String mEntry;
    private final int mVersion;

    /**
     * Default constructor for {@link Descriptor}.
     *
     * @param document The document where all the data is.
     */
    public Descriptor(PreferenceSection document) {
        this.mPath = Paths.get(document.getString("path"));
        this.mName = document.getString("name", "Unknown");
        this.mVersion = document.getInt("version", 0);
        this.mWebsite = document.getString("website", "Unknown");
        this.mAuthors = document.getStringList("authors");
        this.mDependencies = document.getStringList("dependencies");
        this.mEntry = document.getString("entry", "Unknown");
    }

    /**
     * Retrieve the plug-in path.
     *
     * @return The path of the plug-in.
     */
    public Path getPath() {
        return mPath;
    }

    /**
     * Retrieve the plug-in entry.
     *
     * @return The entry of the plug-in.
     */
    public String getEntry() {
        return mEntry;
    }

    /**
     * Retrieve all plug-in's authors.
     *
     * @return The authors of the plug-in.
     */
    public List<String> getAuthors() {
        return Collections.unmodifiableList(mAuthors);
    }

    /**
     * Retrieve all plug-in's dependencies.
     *
     * @return The dependencies of the plug-in.
     */
    public List<String> getDependencies() {
        return new ArrayList<>(mDependencies);
    }

    /**
     * Retrieve the plug-in's name.
     *
     * @return The name of the plug-in.
     */
    public String getName() {
        return mName;
    }

    /**
     * Retrieve the plug-in's version.
     *
     * @return The version of the plug-in.
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * Retrieve the plug-in's author website.
     *
     * @return The website of the plug-in's author.
     */
    public String getWebsite() {
        return mWebsite;
    }
}
