package org.cneko.sudo.util.ctlib;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Json {
    private Map<String, Object> configData;
    private Gson gson;
    private Path filePath;

    public Json(Path filePath) throws IOException {
        this.filePath = filePath;
        gson = new Gson();
        configData = loadConfigData(filePath);
    }

    public Json(String jsonString) {
        gson = new Gson();
        if (jsonString == null) {
            configData = new HashMap<>();
        } else {
            configData = gson.fromJson(jsonString, new TypeToken<Map<String, Object>>() {}.getType());
        }
    }

    private Map<String, Object> loadConfigData(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            try (FileReader reader = new FileReader(filePath.toFile())) {
                return gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
            }
        } else {
            return new HashMap<>();
        }
    }

    public String getString(String key) {
        Object value = get(key);
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Map || value instanceof List) {
            return new Gson().toJson(value);
        } else {
            return null;
        }
    }

    public int getInt(String key) {
        try {
            Object value = get(key);
            return value instanceof Number ? ((Number) value).intValue() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public double getDouble(String key) {
        try {
            Object value = get(key);
            return value instanceof Number ? ((Number) value).doubleValue() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public float getFloat(String key) {
        try {
            Object value = get(key);
            return value instanceof Number ? ((Number) value).floatValue() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean getBoolean(String key) {
        try {
            Object value = get(key);
            return value instanceof Boolean ? (boolean) value : false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        try {
            Object value = get(key);
            return value instanceof Boolean ? (boolean) value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Object get(String key) {
        return configData.get(key);
    }

    public ArrayList<String> getStringList(String key) {
        Object value = get(key);
        if (value instanceof List) {
            ArrayList<String> list = new ArrayList<>();
            for (Object obj : (List<?>) value) {
                if (obj instanceof String) {
                    list.add((String) obj);
                }
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public ArrayList<Integer> getIntList(String key) {
        Object value = get(key);
        if (value instanceof List) {
            ArrayList<Integer> list = new ArrayList<>();
            for (Object obj : (List<?>) value) {
                if (obj instanceof Number) {
                    list.add(((Number) obj).intValue());
                }
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public ArrayList<Double> getDoubleList(String key) {
        Object value = get(key);
        if (value instanceof List) {
            ArrayList<Double> list = new ArrayList<>();
            for (Object obj : (List<?>) value) {
                if (obj instanceof Number) {
                    list.add(((Number) obj).doubleValue());
                }
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public ArrayList<Float> getFloatList(String key) {
        Object value = get(key);
        if (value instanceof List) {
            ArrayList<Float> list = new ArrayList<>();
            for (Object obj : (List<?>) value) {
                if (obj instanceof Number) {
                    list.add(((Number) obj).floatValue());
                }
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public ArrayList<Json> getJsonList(String key) {
        Object value = get(key);
        if (value instanceof List) {
            ArrayList<Json> list = new ArrayList<>();
            for (Object obj : (List<?>) value) {
                if (obj instanceof Map) {
                    list.add(new Json(new Gson().toJson(obj)));
                }
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public ArrayList<Object> getList(String key) {
        Object value = get(key);
        if (value instanceof List) {
            return new ArrayList<>((List<?>) value);
        } else {
            return new ArrayList<>();
        }
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public Json getJsonConfiguration(String key) {
        Object value = get(key);
        if (value instanceof Map) {
            return new Json(new Gson().toJson(value));
        } else {
            return null;
        }
    }

    public void set(String key, Object value) {
        configData.put(key, value);
    }

    public void save() throws IOException {
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(configData, writer);
        }
    }

    @Override
    public String toString() {
        return gson.toJson(configData);
    }
    public boolean equals(Object obj) {
        return obj.toString().equals(this.toString());
    }
    public boolean equalsCaseIgnoreCase(Object obj) {
        if(obj.toString() != null) {
            return obj.toString().equalsIgnoreCase(this.toString());
        }else {
            return false;
        }
    }

}