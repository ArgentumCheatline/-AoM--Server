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

import com.github.aom.core.preference.Preference;
import com.github.aom.core.preference.PreferenceSection;
import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * YML implementation for {@link Preference}.
 */
public final class YMLPreference extends YMLPreferenceSection implements Preference {
    private final static Yaml PARSER = new Yaml();

    /**
     * {@inheritDoc}
     */
    @Override
    public void parse(InputStream input) throws IOException {
        try {
            parseFromString(IOUtils.toString(input));
        } catch (IOException exception) {
            throw new IOException(exception);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(OutputStream output) throws IOException {
        output.write(PARSER.dump(mNodes).getBytes());
    }

    /**
     * Loads this configuration from the specified string.
     *
     * @param contents The contents of the configuration
     *
     * @throws IOException indicates a failure in the document.
     */
    public void parseFromString(String contents) throws IOException {
        Map<?, ?> input;
        try {
            input = (Map<?, ?>) PARSER.load(contents);
        } catch (YAMLException e) {
            throw new IOException(e);
        } catch (ClassCastException e) {
            throw new IOException("Top level is not a Map.");
        }
        if (input != null) {
            convertMapsToSections(input, this);
        }
    }

    /**
     * Convert a simple plain {@link java.util.Map} into a {@link PreferenceSection}.
     *
     * @param input   The map input to be converted.
     * @param section The section parent of the new section.
     */
    protected void convertMapsToSections(Map<?, ?> input, PreferenceSection section) {
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            final String key = entry.getKey().toString();
            final Object value = entry.getValue();

            if (value instanceof Map) {
                convertMapsToSections((Map<?, ?>) value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
    }
}
