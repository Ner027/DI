CREATE TABLE IF NOT EXISTS factory_data
(
    factory_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    factory_name TEXT,
    coord_long TEXT,
    coord_lat TEXT
);

CREATE TABLE IF NOT EXISTS local_manager_data
(
    lm_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    parent_factory_id BIGINT,
    CONSTRAINT fk_pid FOREIGN KEY (parent_factory_id) REFERENCES factory_data(factory_id) ON DELETE CASCADE,
    lm_name TEXT,
    hw_id TEXT
);

CREATE TABLE IF NOT EXISTS sensor_data
(
    sens_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    parent_lm_id BIGINT,
    CONSTRAINT fk_pid FOREIGN KEY (parent_lm_id) REFERENCES local_manager_data(lm_id) ON DELETE CASCADE,
    sens_name TEXT,
    sens_type SMALLINT,
    sens_status SMALLINT
);

CREATE TABLE IF NOT EXISTS sensor_logs
(
    log_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    parent_sens_id BIGINT,
    CONSTRAINT fk_pid FOREIGN KEY (parent_sens_id) REFERENCES sensor_data(sens_id) ON DELETE CASCADE,
    log_timestamp BIGINT,
    log_value FLOAT
);