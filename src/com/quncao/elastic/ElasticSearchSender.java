package com.quncao.elastic;

import com.jfinal.kit.Prop;
import com.quncao.common.PropKit;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaohk on 2015/9/30.
 */
public class ElasticSearchSender {

    private String POST_URL="http://192.168.30.14:9200/_bulk";
    private String index = "app_api_monitor";

    public ElasticSearchSender(){
        POST_URL = PropKit.get("ElasticSearchURL");
        index = PropKit.get("index");
    }

    public ElasticSearchSender(String post_url){
        this.POST_URL = post_url;
    }

    public ElasticSearchSender(String post_url,String index){
        this.POST_URL = post_url;
        this.index = index;
    }

    public void send(String message) throws Exception {
        List messageList = new ArrayList();
        messageList.add(message);
        send(messageList);
    }

    public void send(List<String> messageDataList) throws Exception{
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = new URL(POST_URL);

        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

        //打开读写属性，默认均为false
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 设置请求方式，默认为GET
        connection.setRequestMethod("POST");

        // Post 请求不能使用缓存
        connection.setUseCaches(false);

        // URLConnection.setFollowRedirects是static 函数，作用于所有的URLConnection对象。
        // connection.setFollowRedirects(true);

        //URLConnection.setInstanceFollowRedirects 是成员函数，仅作用于当前函数
        connection.setInstanceFollowRedirects(true);

        // 配置连接的Content-type，配置为application/x- www-form-urlencoded的意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码
        connection.setRequestProperty("Content-Type", "application/json");


        // 连接，从postUrl.openConnection()至此的配置必须要在 connect之前完成，
        // 要注意的是connection.getOutputStream()会隐含的进行调用 connect()，所以这里可以省略
        //connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());

        //正文内容其实跟get的URL中'?'后的参数字符串一致
        ESSenderRequest esSenderRequest = new ESSenderRequest();
        esSenderRequest.setData(messageDataList);
        esSenderRequest.setIndex(index);

        // DataOutputStream.writeBytes将字符串中的16位的 unicode字符以8位的字符形式写道流里面
        System.out.println("----------------------------------");
        System.out.println(esSenderRequest.toJsonString());
        System.out.println("----------------------------------");
        out.writeBytes(esSenderRequest.toJsonString());
        out.flush();
        out.close(); // flush and close
        //connection.getInputStream();
        connection.getInputStream();
        connection.disconnect();
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
