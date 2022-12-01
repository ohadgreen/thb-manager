package com.acme.tweetstermanager.configuration;

import com.acme.tweetstermanager.model.WorkerTask;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class KafkaProducerConfig {

    private final static String BOOTSTRAP_SERVER = "localhost:29092";

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        props.put(CommonClientConfigs.METRIC_REPORTER_CLASSES_CONFIG, MonitoringIntegration.class.getName());
//        setupBatchingAndCompression(props);
        //Linger before sending batch if size not met
        props.put(ProducerConfig.LINGER_MS_CONFIG, 500);
        //Batch buffer sizes.
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,  500);
        //Use compression for batch compression.
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");

        return props;
    }

    @Bean
    public KafkaTemplate<String, WorkerTask> kafkaTemplate() {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigs()));
    }

    @Bean
    public AdminClient getAdminClient() {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        return AdminClient.create(config);
    }
}
