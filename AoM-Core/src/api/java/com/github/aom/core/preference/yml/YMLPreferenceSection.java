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
package com.github.aom.core.preference.yml;

import com.github.aom.core.preference.PreferenceSection;
import com.gs.collections.api.map.MutableMap;
import com.gs.collections.impl.map.mutable.UnifiedMap;

import java.util.Map;
import java.util.function.Consumer;

/**
 * YML implementation for {@link PreferenceSection}.
 */
public class YMLPreferenceSection implements PreferenceSection {
    /**
     * Define the path separator for the sections.
     */
    public final static String PATH_SEPARATOR = ":";

    protected final MutableMap<String, Object> mNodes = new UnifiedMap<>();
    protected final PreferenceSection mParent;
    protected final String mPath;
    protected final String mCompletePath;

    /**
     * Empty constructor for {@link YMLPreferenceSection}.
     */
    public YMLPreferenceSection() {
        this(null, "", "");
    }

    /**
     * Default constructor for {@link YMLPreferenceSection}.
     *
     * @param parent   The parent section of the section.
     * @param path     The path of the section.
     * @param fullPath The full path of the section.
     */
    public YMLPreferenceSection(PreferenceSection parent, String path, String fullPath) {
        this.mParent = parent;
        this.mPath = path;
        this.mCompletePath = fullPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return mPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return mCompletePath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String name, Object value) {
        final int delimiter = name.indexOf(PATH_SEPARATOR);

        if (delimiter != -1) {
            final PreferenceSection section = getSection(name.substring(0, delimiter));
            final String dstPath = name.substring(delimiter + 1);

            if (section == null) {
                createSection(name).set(dstPath, value);
            } else {
                section.set(dstPath, value);
            }
        } else {
            if (value == null) {
                mNodes.remove(name);
            } else {
                mNodes.put(name, value);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferenceSection createSection(String name) {
        final int delimiter = name.indexOf(PATH_SEPARATOR);
        if (name.contains(PATH_SEPARATOR)) {
            final PreferenceSection section = getSection(name.substring(0, delimiter));
            final String dstPath = name.substring(delimiter + 1);
            if (section == null) {
                return createSection(name).createSection(dstPath);
            } else {
                return section.createSection(dstPath);
            }
        } else {
            final PreferenceSection result
                    = new YMLPreferenceSection(this, mPath, mCompletePath + PATH_SEPARATOR + mPath);
            mNodes.put(name, result);
            return result;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferenceSection createSection(String name, Map<?, ?> map) {
        final PreferenceSection section = createSection(name);

        map.entrySet().forEach(new Consumer<Map.Entry<?, ?>>() {
            @Override
            public void accept(Map.Entry<?, ?> e) {
                if (e.getValue() instanceof Map) {
                    section.createSection(e.getKey().toString(), (Map<?, ?>) e.getValue());
                } else {
                    section.set(e.getKey().toString(), e.getValue());
                }
            }
        });
        return section;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name, Object def) {
        final int delimiter = name.indexOf(PATH_SEPARATOR);
        if (delimiter != -1) {
            final PreferenceSection section = getSection(name.substring(0, delimiter));
            if (section == null) {
                return def;
            }
            return section.get(name.substring(delimiter + 1), def);
        } else {
            final Object result = mNodes.get(name);
            return (result == null ? def : result);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferenceSection getSection(String path) {
        final Object value = get(path);
        return (value instanceof PreferenceSection ? (PreferenceSection) value : null);
    }
}
