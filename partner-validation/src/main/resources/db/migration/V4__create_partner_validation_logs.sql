CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE partner_validation_logs (
    id UUID PRIMARY KEY,
    correlation_id UUID NOT NULL,
    monitoring_step VARCHAR(64) NOT NULL,
    request TEXT NOT NULL,
    response TEXT NOT NULL,
    method_type VARCHAR(16) NOT NULL,
    request_url VARCHAR(512) NOT NULL,
    request_date TIMESTAMP(3) NOT NULL,
    response_date TIMESTAMP(3) NOT NULL,
    ip_address VARCHAR(64) NOT NULL,
    success BOOLEAN NOT NULL
);

CREATE INDEX idx_partner_validation_logs_request_date_desc
    ON partner_validation_logs (request_date DESC);

CREATE INDEX idx_partner_validation_logs_correlation_request_date
    ON partner_validation_logs (correlation_id, request_date DESC);

CREATE INDEX idx_partner_validation_logs_monitoring_step_request_date
    ON partner_validation_logs (monitoring_step, request_date DESC);

CREATE INDEX idx_partner_validation_logs_success_request_date
    ON partner_validation_logs (success, request_date DESC);

CREATE INDEX idx_partner_validation_logs_method_type_request_date
    ON partner_validation_logs (lower(method_type), request_date DESC);

CREATE INDEX idx_partner_validation_logs_request_url_trgm
    ON partner_validation_logs USING gin (lower(request_url) gin_trgm_ops);

CREATE INDEX idx_partner_validation_logs_ip_address_trgm
    ON partner_validation_logs USING gin (lower(ip_address) gin_trgm_ops);

CREATE INDEX idx_partner_validation_logs_request_trgm
    ON partner_validation_logs USING gin (lower(request) gin_trgm_ops);

CREATE INDEX idx_partner_validation_logs_response_trgm
    ON partner_validation_logs USING gin (lower(response) gin_trgm_ops);
