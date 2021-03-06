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

/**
 * Encapsulate an exception that occurs when a plug-in action is invalid.
 */
public final class InvalidPluginException extends Exception {
    /**
     * Simple constructor for {@link InvalidPluginException}.
     */
    public InvalidPluginException(String message) {
        super(message);
    }

    /**
     * Exception constructor for {@link InvalidPluginException}.
     */
    public InvalidPluginException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Full constructor for {@link InvalidPluginException}.
     */
    public InvalidPluginException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
