package buildengine.configuration;

import buildengine.docs.CanReturnNull;

import java.util.*;

/**
 * The {@code Configuration} class represents a collection of keys containing values.
 * <p>
 *     Formally the values are stored in a {@code HashMap<String, String>} object. This map is accessed using the
 *     getter methods in this class. The getter methods convert the stored {@code String} values to the corresponding
 *     getter values, but all {@code Object}'s can be stored and converted back from a {@code String}.
 * </p>
 * <p>
 *     This class contains one central method for putting values into the map. For example, to add a boolean object:
 *     <blockquote><pre>
 *         Configuration config = new Configuration();
 *         config.set("visible", true);
 *     </pre></blockquote>
 *     All objects are stored this way. Formally, that means {@code Object} values converted as {@code String} values
 *     using their {@code toString()} method and are stored with a {@code String} key.<br><br>
 *     The stored data can be accessed again, in this case because we stored a {@code Boolean} object, by calling
 *     the {@link Configuration#getBoolean(String key)} method using the previously defined key:
 *     <blockquote><pre>
 *         config.getBoolean("visible");
 *     </pre></blockquote>
 *     This will return {@code true}.
 * </p>
 *
 * @see FileConfiguration
 * @author Kai van Maurik
 * @since 1.0
 */
public class Configuration {

    /**
     * The value is used for storing keys and values.
     * @see #getData()
     */
    protected final Map<String, String> data;

    /**
     * Initializes a newly created {@code Configuration} object, containing no stored values.
     * This will create a {@code HashMap} object and use it to store values.
     */
    public Configuration() {
        this(new HashMap<>());
    }

    /**
     * Initializes a newly created {@code Configuration} object, using a previously created {@code Map} object.
     * This map will be used to store values. All values already in the map will be kept.
     * @param data The map used to store values.
     */
    public Configuration(Map<String, String> data) {
        this.data = data;
    }

    /**
     * Stores a value in the configuration. Formally, that means {@code Object} values converted as {@code String} values
     * using their {@code toString()} method and are {@code put()} into the data map using a {@code String} key.
     * @param key   The key used to access the value.
     * @param value The value to be stored.
     */
    public void set(String key, Object value) {
        // If the value is an array, convert to list to use the abstract collection toString() method
        if(value instanceof String[])
            value = List.of(value);
        data.put(key, value.toString());
    }

    /**
     * Returns the {@code String} value of the given key.
     * @param key   The key used to access the value.
     * @return      the {@code String} value of the given key, or {@code null} if there is no such key stored.
     */
    @CanReturnNull
    public String getString(String key) {
        return data.get(key);
    }

    /**
     * Returns the {@code int} value of the given key. Formally, it tries to convert the value stored into an {@code int}
     * using the {@link Integer#parseInt(String) parseInt} method. If that method fails it will return {@code 0}.
     * @param key   The key used to access the value.
     * @return      the {@code int} value of the given key, or {@code -1} if no such key is sored, or {@code 0} if
     *              the stored value couldn't be converted to an {@code int} value.
     */
    public int getInt(String key) {
        if(data.get(key) == null)
            return -1;
        try {
            return Integer.parseInt(data.get(key));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Returns the {@code long} value of the given key. Formally, it tries to convert the value stored into a {@code long}
     * using the {@link Long#parseLong(String) parseLong} method. If that method fails it will return {@code 0L}.
     * @param key   The key used to access the value.
     * @return      the {@code long} value of the given key, or {@code -1} if no such key is sored, or {@code 0L} if
     *              the stored value couldn't be converted to a {@code long} value.
     */
    public long getLong(String key) {
        if(data.get(key) == null)
            return -1;
        try {
            return Long.parseLong(data.get(key));
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    /**
     * Returns the {@code float} value of the given key. Formally, it tries to convert the value stored into a {@code float}
     * using the {@link Float#parseFloat(String) parseFloat} method. If that method fails it will return {@code 0f}.
     * @param key   The key used to access the value.
     * @return      the {@code float} value of the given key, or {@code -1} if no such key is sored, or {@code 0f} if
     *              the stored value couldn't be converted to a {@code float} value.
     */
    public float getFloat(String key) {
        if(data.get(key) == null)
            return -1;
        try {
            return Float.parseFloat(data.get(key));
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    /**
     * Returns the {@code double} value of the given key. Formally, it tries to convert the value stored into a {@code double}
     * using the {@link Double#parseDouble(String) parseDouble} method. If that method fails it will return {@code 0d}.
     * @param key   The key used to access the value.
     * @return      the {@code double} value of the given key, or {@code -1} if no such key is sored, or {@code 0d} if
     *              the stored value couldn't be converted to a {@code double} value.
     */
    public double getDouble(String key) {
        if(data.get(key) == null)
            return -1;
        try {
            return Double.parseDouble(data.get(key));
        } catch (NumberFormatException e) {
            return 0d;
        }
    }

    /**
     * Returns the {@code boolean} value of the given key. Formally, it tries to convert the value stored into a {@code boolean}
     * using the {@link Boolean#parseBoolean(String) parseBoolean} method. If that method fails it will return {@code false}.
     * @param key   The key used to access the value.
     * @return      the {@code boolean} value of the given key, or {@code false} if no such key is sored. If the stored value, is
     *              anything other than {@code "true"}, it will also return {@code false}.
     */
    public boolean getBoolean(String key) {
        try {
            return Boolean.parseBoolean(data.get(key));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the {@code String[]} value of the given key. Formally, it checks for the validity of a String list, and creates one by
     * decoding the string. Expects codex used by the {@link AbstractCollection#toString() AbstractCollection.toString()} method.
     * @param key   The key used to access the value.
     * @return      the {@code String[]} value of the given key. Returns {@code null} if: <ul>
     *     <li>no such key is stored</li>
     *     <li>the string value of the field is {@code "null"}</li>
     *     <li>the string value of the field doesn't start with {@code "["} or ends with {@code "]"}</li>
     * </ul>
     */
    @CanReturnNull
    public String[] getStringList(String key) {
        // Get raw string data, returns an empty list
        String raw = data.get(key);
        if(raw == null || raw.equals("null"))
            return null;
        // Clear spaces and check if the string matches the array start and end
        raw = raw.replaceAll("\\s+","");
        if(!raw.startsWith("[") || !raw.endsWith("]"))
            return null;
        raw = raw.substring(1, raw.length() - 1);
        // Building list
        return raw.split(",");
    }

    /**
     * Returns the {@code Map} variable storing the configuration keys and values.
     * @return the {@code Map} variable storing the configuration keys and values.
     */
    public Map<String, String> getData() {
        return data;
    }

}
