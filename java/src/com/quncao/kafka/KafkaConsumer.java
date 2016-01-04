package com.quncao.kafka;

import java.util.*;
import java.util.concurrent.SynchronousQueue;

import com.quncao.common.MessageQueue;
import com.quncao.common.PropKit;
import com.quncao.elastic.ElasticSearchSender;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.utils.Time;

/**
 * Created by shk on 2015/10/30.
 */


public class KafkaConsumer extends Thread{
    private ConsumerConnector consumer;
    private Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = null;
    private MessageQueue messageQueue = MessageQueue.getMessageQueue();
    private final String topic;


    public KafkaConsumer(String zookeeper, String groupId, String topic) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        props.put("auto.commit.interval.ms", "1000");
        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        this.topic = topic;

        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        topicMap.put(topic, new Integer(1));
        consumerMap = consumer.createMessageStreams(topicMap);
    }

    public KafkaConsumer() {

        String zookeeper = PropKit.get("zookeeper");
        String groupId = PropKit.get("groupId");
        String topic = PropKit.get("topic");

        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        props.put("auto.commit.interval.ms", "1000");
        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        this.topic = topic;

        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        topicMap.put(topic, new Integer(1));
        consumerMap = consumer.createMessageStreams(topicMap);
    }

    public void getMessage() {
        KafkaStream stream = consumerMap.get(topic).get(0);
        ConsumerIterator<byte[], byte[]> consumerIte = stream.iterator();
        String message;
        System.out.println("********** The consumer start **********");
        while ( consumerIte.hasNext() ) {
            message = new String(consumerIte.next().message()).replaceAll("\\r\\n", "").replaceAll(".u003c.*?003e", "");
            int i = message.indexOf("{");
            messageQueue.offerQueue(message.substring(i));
        }
        System.out.println("********** The consumer stop **********");
    }

    public void closeConsumer(){
        if (consumer != null)
            consumer.shutdown();
    }

    @Override
    public void run(){
        getMessage();
    }
}
