package uz.davrbank.remittanceoutside.aggregate.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import uz.davrbank.remittanceoutside.aggregate.repository.ClickhouseRepository;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ClickhouseRepositoryImpl implements ClickhouseRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClickhouseRepositoryImpl(@Qualifier("clickHouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveAll(String table, Collection<String> jsonRows) {
        if (jsonRows == null || jsonRows.isEmpty()) {
            return;
        }
        try {
            String body = jsonRows.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining("\n", "", "\n"));
            if (body.isEmpty()) {
                return;
            }
            jdbcTemplate.update("INSERT INTO " + table + " FORMAT JSONEachRow " + body);
        } catch (Exception e) {
            log.error("clickhouse.insert.batch.failed table={} err={}", table, e.toString());
        }
    }
}
