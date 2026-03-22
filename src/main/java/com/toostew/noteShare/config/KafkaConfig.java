package com.toostew.noteShare.config;

import com.toostew.noteShare.entity.processRequest;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;


import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {


    @Value("${kafka.topic}")
    private String kafkaTopic;

    @Value("${kafka.filescan.topic}")
    private String kafkaFileScanTopic;

    @Value("${kafka.port}")
    private String kafkaPort;


    @Bean
    public ProducerFactory<String, processRequest> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map producerConfigs() {
        Map props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPort);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 0);
        return props;
    }

    @Bean
    public KafkaTemplate<String, processRequest> kafkaTemplate() {
        return new KafkaTemplate(producerFactory());
    }


    //create topic
    @Bean
    public NewTopic thumbnailTopic() {
        return TopicBuilder.name(kafkaTopic)
                .partitions(3) // a listener can take up one partition
                .replicas(1)   //replicas replicate the messages in other brokers for safety
                .build();
    }

    @Bean
    public NewTopic fileScanTopic(){
        return TopicBuilder.name(kafkaFileScanTopic)
                .partitions(3) // a listener can take up one partition
                .replicas(1)   //replicas replicate the messages in other brokers for safety
                .build();
    }
}
