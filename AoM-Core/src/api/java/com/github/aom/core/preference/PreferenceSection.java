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

import com.gs.collections.api.list.primitive.*;
import com.gs.collections.impl.list.mutable.primitive.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Define the element structure of the preferences node.
 */
public interface PreferenceSection {
    /**
     * Retrieve the name of this section.
     *
     * @return A reference to the name of the section.
     */
    public String getName();

    /**
     * Gets the path of this section from its root.
     * <br/>
     * To retrieve the single name of this section, that is, the final part of
     * the path returned by this method, you may use {@link #getName()}.
     *
     * @return A reference to the path of the section.
     */
    public String getPath();

    /**
     * Sets the specified attribute to the given value.
     * <br/>
     * If value is null, the entry will be removed. Any existing entry will be
     * replaced, regardless of what the new value is.
     *
     * @param name  The name of the attribute whose value should be set.
     * @param value The new value of the attribute.
     */
    public void set(String name, Object value);

    /**
     * Creates an empty {@link PreferenceSection} at the specified path.
     * <br/>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was itself a {@link PreferenceSection}, it will
     * be orphaned.
     *
     * @param name The name of the section whose value should be set.
     *
     * @return A reference to the newly created section.
     */
    public PreferenceSection createSection(String name);

    /**
     * Creates a {@link PreferenceSection} at the specified path, with
     * specified values.
     * <br/>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was itself a {@link PreferenceSection}, it will
     * be orphaned.
     *
     * @param name The name of the section whose value should be set.
     * @param map  The values used by the section.
     *
     * @return A reference to the newly created section.
     */
    public PreferenceSection createSection(String name, Map<?, ?> map);

    /**
     * Get the value specified for a given attribute on this element as a section.
     *
     * @param path The path of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or null if does not exist.
     */
    public PreferenceSection getSection(String path);

    /**
     * Get the value specified for a given attribute on this element as an object.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or null if does not exist.
     */
    default public Object get(String name) {
        return get(name, null);
    }

    /**
     * Get the value specified for a given attribute on this element as an object.
     *
     * @param name The name of the attribute whose value should be retrieved.
     * @param def  The default value to return if the attribute is specified.
     *
     * @return A reference to the section or default value if does not exist.
     */
    public Object get(String name, Object def);

    /**
     * Get the value specified for a given attribute on this element as a string.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public String getString(String name) {
        return getString(name, "");
    }

    /**
     * Get the value specified for a given attribute on this element as a string.
     *
     * @param name The name of the attribute whose value should be retrieved.
     * @param def  The default value to return if the attribute is specified.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public String getString(String name, String def) {
        return (String) get(name, def);
    }

    /**
     * Get the value specified for a given attribute on this element as a byte.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public byte getByte(String name) {
        return getByte(name, 0);
    }

    /**
     * Get the value specified for a given attribute on this element as a byte.
     *
     * @param name The name of the attribute whose value should be retrieved.
     * @param def  The default value to return if the attribute is specified.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public byte getByte(String name, int def) {
        return (Byte) get(name, def);
    }

    /**
     * Get the value specified for a given attribute on this element as a short.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public short getShort(String name) {
        return getShort(name, 0);
    }

    /**
     * Get the value specified for a given attribute on this element as a short.
     *
     * @param name The name of the attribute whose value should be retrieved.
     * @param def  The default value to return if the attribute is specified.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public short getShort(String name, int def) {
        return (Short) get(name, def);
    }

    /**
     * Get the value specified for a given attribute on this element as an integer.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public int getInt(String name) {
        return getInt(name, 0);
    }

    /**
     * Get the value specified for a given attribute on this element as an integer.
     *
     * @param name The name of the attribute whose value should be retrieved.
     * @param def  The default value to return if the attribute is specified.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public int getInt(String name, int def) {
        return (Integer) get(name, def);
    }

    /**
     * Get the value specified for a given attribute on this element as a long.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public long getLong(String name) {
        return getLong(name, 0L);
    }

    /**
     * Get the value specified for a given attribute on this element as a long.
     *
     * @param name The name of the attribute whose value should be retrieved.
     * @param def  The default value to return if the attribute is specified.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public long getLong(String name, long def) {
        return (Long) get(name, def);
    }

    /**
     * Get the value specified for a given attribute on this element as a float.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public float getFloat(String name) {
        return getFloat(name, 0.0f);
    }

    /**
     * Get the value specified for a given attribute on this element as a float.
     *
     * @param name The name of the attribute whose value should be retrieved.
     * @param def  The default value to return if the attribute is specified.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public float getFloat(String name, float def) {
        return (Float) get(name, def);
    }

    /**
     * Get the value specified for a given attribute on this element as a double.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public double getDouble(String name) {
        return getDouble(name, 0.0D);
    }

    /**
     * Get the value specified for a given attribute on this element as a double.
     *
     * @param name The name of the attribute whose value should be retrieved.
     * @param def  The default value to return if the attribute is specified.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public double getDouble(String name, double def) {
        return (Double) get(name, def);
    }

    /**
     * Get the value specified for a given attribute on this element as a boolean.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    /**
     * Get the value specified for a given attribute on this element as a boolean.
     *
     * @param name The name of the attribute whose value should be retrieved.
     * @param def  The default value to return if the attribute is specified.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public boolean getBoolean(String name, boolean def) {
        return (Boolean) get(name, def);
    }

    /**
     * Get the value specified for a given attribute on this element as a list.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public List<?> getList(String name) {
        return getList(name, null);
    }

    /**
     * Get the value specified for a given attribute on this element as a list.
     *
     * @param name The name of the attribute whose value should be retrieved.
     * @param def  The default value to return if the attribute is specified.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public List<?> getList(String name, List<?> def) {
        return (List<?>) get(name, def);
    }

    /**
     * Get the value specified for a given attribute on this element as a list of strings.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public List<String> getStringList(String name) {
        final List<?> values = getList(name);
        final List<String> result = values != null
                ? new ArrayList<>(values.size())
                : new ArrayList<>(0);
        if (values != null) {
            values.stream().forEach(e -> result.add(String.valueOf(e)));
        }
        return result;
    }

    /**
     * Get the value specified for a given attribute on this element as a list of bytes.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public ByteList getByteList(String name) {
        final List<?> values = getList(name);
        final ByteArrayList result = values != null
                ? new ByteArrayList(values.size())
                : new ByteArrayList(0);
        if (values != null) {
            values.stream().forEach(e -> result.add(Byte.valueOf(String.valueOf(e))));
        }
        return result;
    }

    /**
     * Get the value specified for a given attribute on this element as a list of shorts.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public ShortList getShortList(String name) {
        final List<?> values = getList(name);
        final ShortArrayList result = values != null
                ? new ShortArrayList(values.size())
                : new ShortArrayList(0);
        if (values != null) {
            values.stream().forEach(e -> result.add(Short.valueOf(String.valueOf(e))));
        }
        return result;
    }

    /**
     * Get the value specified for a given attribute on this element as a list of integers.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public IntList getIntList(String name) {
        final List<?> values = getList(name);
        final IntArrayList result = values != null
                ? new IntArrayList(values.size())
                : new IntArrayList(0);
        if (values != null) {
            values.stream().forEach(e -> result.add(Integer.valueOf(String.valueOf(e))));
        }
        return result;
    }

    /**
     * Get the value specified for a given attribute on this element as a list of longs.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public LongList getLongList(String name) {
        final List<?> values = getList(name);
        final LongArrayList result = values != null
                ? new LongArrayList(values.size())
                : new LongArrayList(0);
        if (values != null) {
            values.stream().forEach(e -> result.add(Long.valueOf(String.valueOf(e))));
        }
        return result;
    }

    /**
     * Get the value specified for a given attribute on this element as a list of floats.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public FloatList getFloatList(String name) {
        final List<?> values = getList(name);
        final FloatArrayList result = values != null
                ? new FloatArrayList(values.size())
                : new FloatArrayList(0);
        if (values != null) {
            values.stream().forEach(e -> result.add(Float.valueOf(String.valueOf(e))));
        }
        return result;
    }

    /**
     * Get the value specified for a given attribute on this element as a list of doubles.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public DoubleList getDoubleList(String name) {
        final List<?> values = getList(name);
        final DoubleArrayList result = values != null
                ? new DoubleArrayList(values.size())
                : new DoubleArrayList(0);
        if (values != null) {
            values.stream().forEach(e -> result.add(Double.valueOf(String.valueOf(e))));
        }
        return result;
    }

    /**
     * Get the value specified for a given attribute on this element as a list of booleans.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public List<Boolean> getBoolList(String name) {
        final List<?> values = getList(name);
        final List<Boolean> result = values != null
                ? new ArrayList<>(values.size())
                : new ArrayList<>(0);
        if (values != null) {
            values.stream().forEach(e -> result.add(Boolean.valueOf(String.valueOf(e))));
        }
        return result;
    }

    /**
     * Get the value specified for a given attribute on this element as a list of maps.
     *
     * @param name The name of the attribute whose value should be retrieved.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public List<Map<?, ?>> getMapList(String name) {
        return getMapList(name, null);
    }

    /**
     * Get the value specified for a given attribute on this element as a list of maps.
     *
     * @param name The name of the attribute whose value should be retrieved.
     * @param def  The default value to return if the attribute is specified.
     *
     * @return A reference to the section or default value if does not exist.
     */
    default public List<Map<?, ?>> getMapList(String name, List<Map<?, ?>> def) {
        final List<?> values = getList(name);
        final List<Map<?, ?>> result = values != null ? new ArrayList<>(values.size()) : def;
        if (values != null) {
            values.stream().filter(e -> e instanceof Map).forEach(e -> result.add((Map<?, ?>) e));
        }
        return result;
    }
}
