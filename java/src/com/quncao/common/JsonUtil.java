package com.quncao.common;

import com.google.gson.Gson;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by shk on 2015/11/6.
 */
public class JsonUtil {
    public static String map2Json(Map<String,String> map){
        String jsonData = null;
        try {
            //使用XContentBuilder创建json数据
            XContentBuilder jsonBuild = XContentFactory.jsonBuilder();

            jsonBuild.startObject();
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                jsonBuild.field(String.valueOf(entry.getKey()),String.valueOf(entry.getValue()));
            }
            jsonBuild.endObject();

            jsonData = jsonBuild.string();
            //System.out.println(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData;
    }

    public static Map<?, ?> json2Map(String jsonStr) {
        Map<?, ?> ObjectMap = null;
        Gson gson = new Gson();
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<?,?>>() {}.getType();
        ObjectMap = gson.fromJson(jsonStr, type);
        return ObjectMap;
    }
}
