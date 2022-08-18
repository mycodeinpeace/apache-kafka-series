package com.codeinpeace.kafkabasics;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallBack {
    private static final String BOOTSTRAP_SERVERS = "127.0.0.1:9092";

    public static void main( String[] args ) {
        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallBack.class);

        // create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the producer
        KafkaProducer kafkaProducer = new KafkaProducer<String, String>(properties);

        for (int i = 0; i < 10; i++) {
            // create a producer record
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic", "some message " + Integer.toString(i));

            // send data - async
            kafkaProducer.send(record, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) { // callback
                    // executes every time a record is successfully sent or an exception is thrown
                    if (e == null) { // the record was successfully sent

                        logger.info("Received new metadata. \n" +
                                "Topic:" + recordMetadata.topic() + "\n" +
                                "Partition:" + recordMetadata.partition() + "\n" +
                                "Offset: " + recordMetadata.offset() + "\n" +
                                "Timestamp: " + recordMetadata.timestamp());
                    } else {
                        logger.error("Error while producing", e);
                    }
                }
            });
        }

        kafkaProducer.flush();
        kafkaProducer.close();
    }
}

