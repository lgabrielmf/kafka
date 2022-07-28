package com.gabrielfernandes.estudo.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithKeys {
    public static final Logger log = LoggerFactory.getLogger(ProducerDemoWithKeys.class.getSimpleName());
    public static void main(String[] args) {
        log.info("I am a Kafka Producer");

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.0.1.11:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int i = 0; i < 10; i++) {
            String topic = "demo_java";
            String value = "hello world " + i;
            String key = "id_" + i;

            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);

            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        log.info("Received new metadata/ \n" +
                                "Topic: " + recordMetadata.topic() + "\n" +
                                "Key: " + producerRecord.key() + "\n" +
                                "Partition: " + recordMetadata.partition() + "\n" +
                                "Offset: " + recordMetadata.offset() + "\n" +
                                "Timestamp: " + recordMetadata.timestamp());
                    } else {
                        log.error("Error: " + e);
                    }
                }
            });

        }

        producer.flush();
        producer.close();

    }
}
