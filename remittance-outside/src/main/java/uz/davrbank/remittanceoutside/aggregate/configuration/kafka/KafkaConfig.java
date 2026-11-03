package uz.davrbank.remittanceoutside.aggregate.configuration.kafka;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@Slf4j
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapServers;

    @Value("${spring.application.name}")
    String appName;

    @Value("${app.kafka.topic.name}")
    String topicName;

    @Value("${app.kafka.group.name}")
    String groupName;

    @Value("${app.kafka.topic.partitions}")
    int topicPartitions;

    @Value("${app.kafka.topic.replication-factor}")
    short topicReplicationFactor;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.ACKS_CONFIG, "all");
        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        configs.put(ProducerConfig.RETRIES_CONFIG, 5);
        configs.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        configs.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 10000);
        configs.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 30000);
        configs.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 10000);
        configs.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, 30000);
        configs.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        configs.put(ProducerConfig.BATCH_SIZE_CONFIG, 32 * 1024);
        configs.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        configs.put(ProducerConfig.CLIENT_ID_CONFIG, appName + "-producer");
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        KafkaTemplate<String, String> template = new KafkaTemplate<>(producerFactory);
        template.setDefaultTopic(topicName);
        return template;
    }

    @Bean
    public NewTopic remittanceApiExchangeLogTopic() {
        return new NewTopic(topicName, topicPartitions, topicReplicationFactor);
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupName);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 60000);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, appName + "-consumer");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        return factory;
    }

    @PostConstruct
    void logKafkaSettings() {
        log.info("kafka.config bootstrapServers={} topic={} group={}", bootstrapServers, topicName, groupName);
    }
}

