package buildengine.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * All types of data can be stored in here, with a key to reference it.
 * All data will be converted and stored as a string.
 */
public class Configuration {

    private final HashMap<String, String> data;

    public Configuration() {
        this(new HashMap<>());
    }

    public Configuration(HashMap<String, String> data) {
        this.data = data;
    }

    public void set(String key, Object value) {
        data.put(key, value.toString());
    }

    public String getString(String key) {
        return data.get(key);
    }

    public int getInt(String key) {
        try {
            return Integer.parseInt(data.get(key));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public long getLong(String key) {
        try {
            return Long.parseLong(data.get(key));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public float getFloat(String key) {
        try {
            return Float.parseFloat(data.get(key));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getDouble(String key) {
        try {
            return Double.parseDouble(data.get(key));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public List<String> getList(String key) {
        String raw = data.get(key);
        raw = raw.replace("[", "").replace("]", "").replaceAll("\\s+","");
        List<String> list = new ArrayList<>();
        for(String str : raw.split(",")) {
            if(str.getBytes().length > 0)
                list.add(str);
        }
        return list;
    }

    public boolean getBoolean(String key) {
        try {
            return Boolean.parseBoolean(data.get(key));
        } catch (Exception e) {
            return false;
        }
    }

    public List<Integer> getIntList(String key) {
        String raw = data.get(key);
        if(raw == null)
            return new ArrayList<>();
        raw = raw.replace("[", "").replace("]", "").replaceAll("\\s+","");
        List<Integer> list = new ArrayList<>();
        for(String str : raw.split(",")) {
            try {
                Integer.parseInt(str);
            } catch (NumberFormatException e) {
                continue;
            }
            list.add(Integer.parseInt(str));
        }

        return list;
    }

    public HashMap<String, String> getData() {
        return data;
    }

}
