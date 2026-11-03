CREATE TABLE IF NOT EXISTS remittance_outside.remittance_api_exchange_log
(
    event_id        String,
    event_type      String,
    client_ip       Nullable(String),
    event_date      DateTime,
    url             String,
    http_method     String,
    event_header    Nullable(String),
    event_json      Nullable(String),
    additionals     Nullable(String),
    response_status Nullable(String),
    response_time   Nullable(Int64),
    exchange_type   String
    )
    ENGINE = ReplacingMergeTree(event_date)
    PARTITION BY toDate(event_date)
    ORDER BY (event_id, event_type)
    SETTINGS index_granularity = 8192;