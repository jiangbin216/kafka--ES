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
        // Post�����url����get��ͬ���ǲ���Ҫ������
        URL postUrl = new URL(POST_URL);

        // ������
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

        //�򿪶�д���ԣ�Ĭ�Ͼ�Ϊfalse
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // ��������ʽ��Ĭ��ΪGET
        connection.setRequestMethod("POST");

        // Post ������ʹ�û���
        connection.setUseCaches(false);

        // URLConnection.setFollowRedirects��static ���������������е�URLConnection����
        // connection.setFollowRedirects(true);

        //URLConnection.setInstanceFollowRedirects �ǳ�Ա�������������ڵ�ǰ����
        connection.setInstanceFollowRedirects(true);

        // �������ӵ�Content-type������Ϊapplication/x- www-form-urlencoded����˼��������urlencoded�������form�������������ǿ��Կ������Ƕ���������ʹ��URLEncoder.encode���б���
        connection.setRequestProperty("Content-Type", "application/json");


        // ���ӣ���postUrl.openConnection()���˵����ñ���Ҫ�� connect֮ǰ��ɣ�
        // Ҫע�����connection.getOutputStream()�������Ľ��е��� connect()�������������ʡ��
        //connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());

        //����������ʵ��get��URL��'?'��Ĳ����ַ���һ��
        ESSenderRequest esSenderRequest = new ESSenderRequest();
        esSenderRequest.setData(messageDataList);
        esSenderRequest.setIndex(index);

        // DataOutputStream.writeBytes���ַ����е�16λ�� unicode�ַ���8λ���ַ���ʽд��������
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
