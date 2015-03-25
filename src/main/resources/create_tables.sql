DROP TABLE weatherstations;
CREATE TABLE weatherstations (id INT NOT NULL AUTO_INCREMENT,
  -- wmo_station_id
  station_id VARCHAR(15),
  station_name VARCHAR(255),
  latitude VARCHAR(15),
  longitude VARCHAR(15),
-- to_date VARCHAR(8),
-- state VARCHAR(255),
-- itude VARCHAR(15),
  elevation VARCHAR(6),
  country_name VARCHAR(255),
  PRIMARY KEY (id));
  -- extra1 VARCHAR(255),

-- SELECT * FROM weatherstations_WMO;
-- DESCRIBE weatherstations_WMO;

-- DROP TABLE weatherstations_DWD;
/*
CREATE TABLE weatherstations_DWD (id INT NOT NULL AUTO_INCREMENT,
  station_id VARCHAR(6),
  station_name VARCHAR(255),
  latitude VARCHAR(15),
  longitude VARCHAR(15),
  elevation VARCHAR(6),
  country_name VARCHAR(255),
  PRIMARY KEY (id));
*/
  -- from_date VARCHAR(8),
  -- to_date VARCHAR(8),
  -- state VARCHAR(255),
  -- PRIMARY KEY (id));

-- SELECT * FROM weatherstations_DWD;
-- DESCRIBE weatherstations_DWD;


CREATE table climatedata (
  id INT NOT NULL AUTO_INCREMENT,
  year INT,
  month INT,
  station_id VARCHAR(16),
  mean_monthly_station_level_pressure INT,
  mean_monthly_air_temp INT,
 total_monthly_precipitation INT,
 number_of_days_with_precipitation INT,
 total_monthly_sunshine INT,
 days_with_max_temp_gt_25 INT,
 days_with_max_temp_gt_30 INT,
 days_with_max_temp_gt_35 INT,
 days_with_max_temp_gt_40 INT,
 days_with_min_temp_lt_0 INT,
 days_with_max_temp_lt_0 INT,
 max_temp_per_month INT,
 min_temp_per_month INT,
 max_gust_wind_speed_per_month INT,
 number_of_days_with_thunderstorms INT,
 number_of_days_with_hail INT,
  PRIMARY KEY (id));