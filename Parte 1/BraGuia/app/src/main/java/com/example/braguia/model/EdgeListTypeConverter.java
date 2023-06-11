package com.example.braguia.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class EdgeListTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Trail.Edge> stringToEdgeList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Trail.Edge>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String edgeListToString(List<Trail.Edge> edges) {
        return gson.toJson(edges);
    }
}


