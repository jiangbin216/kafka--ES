package com.quncao.elastic;

import java.util.List;

/**
 * Created by shaohk on 2015/10/13.
 */
public class ESSenderRequest {
    private List<String> data;
    String index = "message";

    /**
     * 根据ES批量导入数据规则，将数据处理成符合规则的json串
     * @return
     */
    public String toJsonString(){
        String messageString = new StringBuilder("{\"create\":{\"_index\":\"")
                .append(index)
                .append("\",\"_type\":\"")
                .append("APPMessage")
                .append("\"}} \n")
                .toString();
        StringBuilder tmpStringBuilder = new StringBuilder();
        for (String str : data){
            tmpStringBuilder.append(messageString).append(str).append("\n");
        }
        //System.out.println(tmpStringBuilder.toString());
        return tmpStringBuilder.toString();
    }

    public String getIndex(){
        return index;
    }
    public void setIndex(String index){
        this.index = index;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
