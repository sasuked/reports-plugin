package io.github.sasuked.reportsplugin.data.cache;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.sasuked.reportsplugin.data.ReportData;

import java.util.List;

class ReportRedisInstrumentation {

    private static final TypeToken<List<String>> STRING_LIST_TOKEN = new TypeToken<List<String>>() {
    };

    private static final Gson GSON_INSTANCE = new GsonBuilder()
      .enableComplexMapKeySerialization()
      .create();

    public static String toJson(ReportData data) {
        return GSON_INSTANCE.toJson(data);
    }

    public static String toJsonList(ReportData data) {
        return GSON_INSTANCE.toJson(data, STRING_LIST_TOKEN.getType());
    }

    public static ReportData deserializeReport(String json) {
        return GSON_INSTANCE.fromJson(json, ReportData.class);
    }

    public static List<ReportData> deserializieReportList(String json) {
        return GSON_INSTANCE.fromJson(json, STRING_LIST_TOKEN.getType());
    }
}
