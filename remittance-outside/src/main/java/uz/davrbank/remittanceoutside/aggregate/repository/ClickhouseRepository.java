package uz.davrbank.remittanceoutside.aggregate.repository;

import java.util.Collection;

public interface ClickhouseRepository {
    void saveAll(String table, Collection<String> jsonRows);
}
