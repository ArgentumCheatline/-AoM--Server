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
package com.github.aom.core.preference;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Define a preference structure on which to read or write any element.
 */
public interface Preference extends PreferenceSection {
    /**
     * Parse the document that can be read from the given yaml.
     *
     * @param filename The name of the yaml on which to read the document.
     *
     * @throws IOException indicates a failure in the document.
     */
    default public void parse(String filename) throws IOException {
        try {
            parse(new FileInputStream(new File(filename)));
        } catch (FileNotFoundException exception) {
            throw new IOException(exception);
        }
    }

    /**
     * Parse the document that can be read from the given yaml.
     * </p>
     * If the yaml does not exist, provide a default yaml that will be copy to the destination.
     *
     * @param filename The name of the yaml on which to read the document.
     * @param input    The input stream from which the document can be read.
     *
     * @throws IOException indicates a failure in the document.
     */
    default public void parseIfAbsentCreate(String filename, InputStream input) throws IOException {
        parseIfAbsentCreate(Paths.get(filename), input);
    }

    /**
     * Parse the document that can be read from the given yaml.
     * </p>
     * If the yaml does not exist, provide a default yaml that will be copy to the destination.
     *
     * @param path  The path of the yaml on which to read the document.
     * @param input The input stream from which the document can be read.
     *
     * @throws IOException indicates a failure in the document.
     */
    default public void parseIfAbsentCreate(Path path, InputStream input) throws IOException {
        if (!Files.exists(path)) {
            Files.copy(input, path);
        }
        parse(Files.newInputStream(path));
    }

    /**
     * Parse the document that can be read from the given input stream.
     *
     * @param input The input stream from which the document can be read.
     *
     * @throws IOException indicates a failure in the document.
     */
    public void parse(InputStream input) throws IOException;

    /**
     * Store the document into the given path.
     *
     * @param path The path to store this document.
     *
     * @throws IOException indicates a failure in the document.
     */
    default public void save(Path path) throws IOException {
        save(Files.newOutputStream(path));
    }

    /**
     * Store the document into the given stream.
     *
     * @param output The stream to store this document.
     *
     * @throws IOException indicates a failure in the document.
     */
    public void save(OutputStream output) throws IOException;
}
