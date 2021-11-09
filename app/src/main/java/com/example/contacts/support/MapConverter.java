package com.example.contacts.support;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MapConverter {
    @TypeConverter
    public String getStringFromMap(Map<Integer, String> map) {
        StringBuilder mapAsString = new StringBuilder();
        for (Integer key : map.keySet()) {
            mapAsString.append(key + "=" + map.get(key) + ",");
        }
        return mapAsString.toString();
    }

    @TypeConverter
    public Map<Integer, String> getMapFromString(String s) {
        Map<Integer, String> myMap = new HashMap<Integer, String>();
        String[] pairs = s.split(","); // 1 элемент -  0=Элемент 0
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split("=");
            myMap.put(Integer.parseInt(keyValue[0]), keyValue[1]);
        }
        return myMap;
    }
}
