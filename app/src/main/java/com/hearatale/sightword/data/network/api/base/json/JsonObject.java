package com.hearatale.sightword.data.network.api.base.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JsonObject {

    private static Gson gson;
    private com.google.gson.JsonObject json;

    public JsonObject() {
        json = new com.google.gson.JsonObject();
    }

    public JsonObject(com.google.gson.JsonObject jsonobject) {
        json = jsonobject;
    }

    public JsonObject(String jsonString) {
        try {
            json = (new JsonParser()).parse(jsonString).getAsJsonObject();
        } catch (JsonParseException e) {
            json = new com.google.gson.JsonObject();
            e.printStackTrace();
        }
    }

    public JsonObject(Map map) {
        json = (com.google.gson.JsonObject) gson.toJsonTree(map);
    }

    public static Object make(Object obj) {
        if (!(obj instanceof JsonElement)) {
            return null;
        }
        JsonElement jsonelement = (JsonElement) obj;
        if (jsonelement.isJsonArray()) {
            obj = new JsonArray(jsonelement.getAsJsonArray());
        } else if (jsonelement.isJsonObject()) {
            obj = new JsonObject(jsonelement.getAsJsonObject());
        } else if (obj instanceof JsonPrimitive) {
            obj = jsonelement.getAsString();
        } else {
            obj = null;
        }
        return obj;
    }

    public final double getDouble(String key, double def) {
        if (json == null || json.get(key) == null || !json.get(key).isJsonPrimitive()) {
            return def;
        }

        double d;
        try {
            d = json.get(key).getAsDouble();
        }
        // Misplaced declaration of an exception variable
        catch (UnsupportedOperationException ex) {
            return def;
        }
        return d;
    }

    public final byte getByte(String key, byte def) {
        if (json == null || json.get(key) == null || !json.get(key).isJsonPrimitive()) {
            return def;
        }
        byte i;

        try {
            i = json.get(key).getAsByte();
        }
        // Misplaced declaration of an exception variable
        catch (UnsupportedOperationException ex) {
            return def;
        }
        return i;
    }

    public final Byte getByte(String key) {
        if (json == null || json.get(key) == null || !json.get(key).isJsonPrimitive()) {
            return null;
        }
        byte i;

        try {
            i = json.get(key).getAsByte();
        }
        // Misplaced declaration of an exception variable
        catch (UnsupportedOperationException ex) {
            return null;
        }
        return i;
    }

    public final int getInt(String key, int def) {
        if (json == null || json.get(key) == null || !json.get(key).isJsonPrimitive()) {
            return def;
        }
        int i;

        try {
            i = json.get(key).getAsInt();
        }
        // Misplaced declaration of an exception variable
        catch (UnsupportedOperationException ex) {
            return def;
        }
        return i;
    }

    public final Integer getInt(String key) {
        if (json == null || json.get(key) == null || !json.get(key).isJsonPrimitive()) {
            return null;
        }
        int i;

        try {
            i = json.get(key).getAsInt();
        }
        // Misplaced declaration of an exception variable
        catch (UnsupportedOperationException ex) {
            return null;
        }
        return i;
    }

    public final long getLong(String key, long def) {
        if (json == null || json.get(key) == null || !json.get(key).isJsonPrimitive()) {
            return def;
        }
        long l;
        try {
            l = json.get(key).getAsLong();
        }
        // Misplaced declaration of an exception variable
        catch (UnsupportedOperationException ex) {
            return def;
        }
        return l;
    }

    public final Boolean getBoolean(String key) {
        return getBoolean(key, Boolean.valueOf(false));
    }

    public final Boolean getBoolean(String key, Boolean def) {
        if (json == null || json.get(key) == null || !json.get(key).isJsonPrimitive()) {
            return def;
        }
        boolean flag;
        try {
            flag = json.get(key).getAsBoolean();
        }
        // Misplaced declaration of an exception variable
        catch (UnsupportedOperationException ex) {
            return def;
        }
        return Boolean.valueOf(flag);
    }

    public final Object getObject(Class _class) {
        return gson.fromJson(json, _class);
    }

    public final String getString(String key, String def) {
        if (json == null || json.get(key) == null) {
            return def;
        }

        String s;
        try {
            s = json.get(key).getAsString();
        }
        // Misplaced declaration of an exception variable
        catch (UnsupportedOperationException ex) {
            return def;
        }
        return s;
    }

    public final String[] getStringArray(String key) {
        String[] retArray;
        if (json == null || json.get(key) == null) {
            return new String[0];
        }

        JsonArray jsonArray;
        try {
            jsonArray = getJsonArray(key);
            retArray = new String[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                retArray[i] = jsonArray.getString(i);
            }

        } catch (UnsupportedOperationException ex) {
            return new String[0];
        }

        return retArray;

    }

    public final double[] getDoubleArray(String key) {
        double[] ret;
        if (json == null || json.get(key) == null) {
            return new double[0];
        }

        JsonArray jsonArray;
        try {
            jsonArray = getJsonArray(key);
            ret = new double[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                ret[i] = jsonArray.getDouble(i);
            }
        } catch (UnsupportedOperationException ex) {
            return new double[0];
        }

        return ret;
    }

    public final JsonObject get(String key) {
        if (json != null) {
            JsonElement el = json.get(key);
            if (el != null && el.isJsonObject()) {
                return new JsonObject((com.google.gson.JsonObject) el);
            }
        }
        return new JsonObject();
    }

    public final JsonObject getCritical(String key) {
        if (json != null) {
            JsonElement el = json.get(key);
            if (el != null && el.isJsonObject()) {
                return new JsonObject((com.google.gson.JsonObject) el);
            }
        }
        return null;
    }

    public final int size() {
        return json.entrySet().size();
    }

    public final JsonArray getJsonArray(String key) {
        if (json == null || json.get(key) == null || !json.get(key).isJsonArray()) {
            return new JsonArray();
        } else {
            return new JsonArray(json.getAsJsonArray(key));
        }
    }

    public final boolean has(String key) {
        return json.has(key);
    }

    public boolean equals(Object obj) {
        return obj == this || (obj instanceof JsonObject) && ((JsonObject) obj).json.equals(json);
    }

    public int hashCode() {
        return json.hashCode();
    }

    public String toString() {
        return json.toString();
    }

    static {
        gson = new Gson();
    }

    public final float getFloat(String key, float def) {
        if (json == null || json.get(key) == null) {
            return def;
        }

        float l;
        try {
            l = json.get(key).getAsFloat();
        }
        // Misplaced declaration of an exception variable
        catch (UnsupportedOperationException ex) {
            return def;
        }
        return l;
    }

    public Set<String> getKeys() {

        if (json == null) {
            return new HashSet<>();
        }

        return json.keySet();
    }

}
