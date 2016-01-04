package com.quncao.kafka;

/**
 * Created by shk on 2015/10/30.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "101.200.210.189:9092");
        Producer<Integer, String> producer = new Producer<Integer, String>(new ProducerConfig(props));
        String topic = "INTERFACE_ACCESS_LOG_TOPIC_";

        int line = 1;
        while (line < 10) {
            producer.send(new KeyedMessage<Integer, String>(topic,"--------"+line+"--------"));
            System.out.println("Success send [" + line + "] message ..");
            line++;
        }
        System.out.println("Total send [" + line + "] messages ..");

        producer.close();

    }
}
