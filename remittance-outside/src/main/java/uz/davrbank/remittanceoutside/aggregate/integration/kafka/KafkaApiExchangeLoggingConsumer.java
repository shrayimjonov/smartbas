package uz.davrbank.remittanceoutside.aggregate.integration.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import uz.davrbank.remittanceoutside.aggregate.repository.ClickhouseRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaApiExchangeLoggingConsumer {

    private final ClickhouseRepository repository;

    @Value("${app.clickhouse.log.table}")
    private String targetTable;

    @KafkaListener(topics = "${app.kafka.topic.name}", containerFactory = "kafkaListenerContainerFactory")
    public void onBatch(List<String> messages) {
        if (messages == null || messages.isEmpty()) return;
        try {
            repository.saveAll(targetTable, messages);
            log.info("clickhouse.batch.insert.ok table={} size={}", targetTable, messages.size());
        } catch (Exception e) {
            log.error("clickhouse.batch.insert.failed table={} err={}", targetTable, e.toString());
        }
    }
}
