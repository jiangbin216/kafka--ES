package com.quncao.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Prop;
import com.quncao.common.JsonUtil;
import com.quncao.common.PropKit;
import scala.util.parsing.combinator.testing.Str;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by shk on 2015/11/4.
 */
public class PropController extends Controller {
    public void getProps(){
        Map map = new HashMap<>();
        map.put("zookeeper", PropKit.get("zookeeper"));
        map.put("groupId", PropKit.get("groupId"));
        map.put("topic", PropKit.get("topic"));
        map.put("ElasticSearchURL", PropKit.get("ElasticSearchURL"));
        map.put("index", PropKit.get("index"));
        renderText(JsonUtil.map2Json(map));
    }

    @Before(POST.class)
    public void updateProps(){
        String props = getPara("props");
        Map map = JsonUtil.json2Map(props);
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = String.valueOf(entry.getKey());
            String val = String.valueOf(entry.getValue());
            PropKit.update("res/public.properties",key,val);
        }
        PropKit.clear();
        PropKit.use("res/public.properties");
        renderText("success");
    }
}
