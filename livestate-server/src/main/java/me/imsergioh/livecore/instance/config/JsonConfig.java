package me.imsergioh.livecore.instance.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JsonConfig {

    private final Map<String, Object> data = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File file;

    public JsonConfig(String path) {
        this.file = new File(path);
        load();
    }

    public void load() {
        if (!file.exists()) return;
        try (Reader reader = Files.newBufferedReader(file.toPath())) {
            Map<String, Object> map = gson.fromJson(reader, Map.class);
            if (map != null) data.putAll(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(file.toPath())) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String key, String def) {
        Object value = data.get(key);
        return value != null ? value.toString() : def;
    }

    public int getInt(String key, int def) {
        Object value = data.get(key);
        return value instanceof Number ? ((Number) value).intValue() : def;
    }

    public double getDouble(String key, double def) {
        Object value = data.get(key);
        return value instanceof Number ? ((Number) value).doubleValue() : def;
    }

    public boolean getBoolean(String key, boolean def) {
        Object value = data.get(key);
        return value instanceof Boolean ? (Boolean) value : def;
    }

    public void set(String key, Object value) {
        data.put(key, value);
    }

    public boolean register(String key, Object value) {
        if (contains(key)) return false;
        set(key, value);
        return true;
    }

    public boolean contains(String key) {
        return data.containsKey(key);
    }

    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(data);
    }
}
