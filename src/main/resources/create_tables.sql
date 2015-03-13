DROP TABLE weatherstations_WMO;
CREATE TABLE weatherstations_WMO (id INT NOT NULL AUTO_INCREMENT,
    -- wmo_station_id
    station_id VARCHAR(6),
    station_name VARCHAR(255),
    latitude VARCHAR(15),
    longitude VARCHAR(15),
    elevation INTEGER,
    country VARCHAR(255),

    extra1 VARCHAR(255),
    PRIMARY KEY (id));
-- SELECT * FROM weatherstations_WMO;
-- DESCRIBE weatherstations_WMO;

DROP TABLE weatherstations_DWD;
CREATE TABLE weatherstations_DWD (id INT NOT NULL AUTO_INCREMENT,
    station_id VARCHAR(6),
    station_name VARCHAR(255),
    latitude VARCHAR(15),
    longitude VARCHAR(15),
    elevation INTEGER,
    country VARCHAR(255),

    from_date VARCHAR(8),
    to_date VARCHAR(8),
    state VARCHAR(255),
    PRIMARY KEY (id));

-- SELECT * FROM weatherstations_DWD;
-- DESCRIBE weatherstations_DWD;
