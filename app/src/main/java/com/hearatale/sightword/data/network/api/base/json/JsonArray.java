package com.hearatale.sightword.data.network.api.base.json;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class JsonArray {

    public com.google.gson.JsonArray jsonArray;

    public JsonArray() {
        jsonArray = new com.google.gson.JsonArray();
    }

    public JsonArray(com.google.gson.JsonArray jsonarray) {
        jsonArray = jsonarray;
    }

    public JsonArray(String s) {
        try {
            jsonArray = (new JsonParser()).parse(s).getAsJsonArray();
        } catch (JsonParseException e) {
            jsonArray = new com.google.gson.JsonArray();
            e.printStackTrace();
        }

    }

    public final int size() {
        if (jsonArray == null) {
            return 0;
        } else {
            return jsonArray.size();
        }
    }

    public final String getString(int i) {
        return jsonArray.get(i).getAsString();
    }

    public final String getJsonElementString(int i) {
        return jsonArray.get(i).toString();
    }

    public final JsonObject get(int i) {
        if (jsonArray == null || jsonArray.size() <= i || jsonArray.get(i) == null || !jsonArray.get(i).isJsonObject()) {
            return null;
        } else {
            return new JsonObject((com.google.gson.JsonObject) jsonArray.get(i));
        }
    }

    public boolean equals(Object obj) {
        return obj == this || (obj instanceof JsonArray) && ((JsonArray) obj).jsonArray.equals(jsonArray);
    }

    public int hashCode() {
        return jsonArray.hashCode();
    }

    public String toString() {
        return jsonArray.toString();
    }

    public double getDouble(int i) {
        return jsonArray.get(i).getAsDouble();
    }

    public int getInt(int i) {
        return jsonArray.get(i).getAsInt();
    }

    public long getLong(int i) {
        return jsonArray.get(i).getAsLong();
    }

    public float getFloat(int i) {
        return jsonArray.get(i).getAsFloat();
    }

}
