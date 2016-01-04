package com.quncao.service;

import com.quncao.common.MessageQueue;
import com.quncao.common.PropKit;
import com.quncao.elastic.ElasticSearchSender;
import com.quncao.kafka.KafkaConsumer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by shk on 2015/11/5.
 */
public class ConsumeService extends Thread {
    private MessageQueue messageQueue = MessageQueue.getMessageQueue();
    private KafkaConsumer kafkaConsumer = new KafkaConsumer();
    private ElasticSearchSender elasticSearchSender = new ElasticSearchSender(PropKit.get("ElasticSearchURL"),getIndexName());
    List<String> messageList = new ArrayList<>();
    private boolean isWorking = false;
    private boolean sendFlag = false;
    private int messageSize = 0;
    private TimeThread timeThread = new TimeThread();
    @Override
    public void run() {
        kafkaConsumer.start();
        isWorking = true;
        timeThread.start();
        while (isWorking) {
            try {
                sendFlag = false;
                while ( !sendFlag ){
                    if (!messageQueue.isQueueEmpty())
                        messageList.add(messageQueue.pollQueue());
                }
                messageSize = messageList.size();
                if (!messageList.isEmpty()) {
                    elasticSearchSender.setIndex(getIndexName());
                    elasticSearchSender.send(messageList);
                }
                messageList.clear();
            } catch (Exception e) {
                try {
                    elasticSearchSender.send(messageList);
                } catch (Exception e1) {
                    e.printStackTrace();
                    for (String str : messageList){
                        System.out.println(str);
                    }
                }
                messageList.clear();
                //return;
            }
        }
    }

    public void closeConsumerService(){
        isWorking = false;
        kafkaConsumer.closeConsumer();
    }

    public int getRate(){
        return messageSize;
    }

    private String getIndexName(){
        String esIndex = PropKit.get("index");
        Calendar cal = Calendar.getInstance();
        String str = new StringBuilder(esIndex)
                .append("-")
                .append(cal.get(Calendar.YEAR))
                .append(".")
                .append(cal.get(Calendar.MONTH)+1)
                .append(".")
                .append(cal.get(Calendar.DATE))
                .toString();
        return str;
    }

    private class TimeThread extends Thread{
        @Override
        public void run() {
            while (isWorking) {
                try {
                    if (sendFlag == false) {
                        Thread.sleep(1000);
                        sendFlag = true;
                    }
                } catch (Exception e) {}
            }
        }
    }
}
