Where to get the data?
-> http://www.dwd.de/bvbw/appmanager/bvbw/dwdwwwDesktop?_nfpb=true&_pageLabel=_dwdwww_klima_umwelt_klimadaten_deutschland&T82002gsbDocumentPath=Navigation%2FOeffentlichkeit%2FKlima__Umwelt%2FKlimadaten%2Fkldaten__kostenfrei%2Fabrufsysteme__ftp__home__node.html%3F__nnn%3Dtrue

DWD
stations -> ftp://ftp-cdc.dwd.de/pub/CDC/help/KL_Tageswerte_Beschreibung_Stationen.txt
data -> ftp://ftp-cdc.dwd.de/pub/CDC/observations_germany/climate/daily/kl/

CLIMAT
stations -> ftp://ftp-cdc.dwd.de/pub/CDC/help/stations_list_CLIMAT_data.txt
data -> ftp://ftp-cdc.dwd.de/pub/CDC/observations_global/CLIMAT/monthly/raw/

TODOs
install mysql
sudo apt-get install mysql-server-5.6

create user sqluser identified by 'sqluserpw';
create database climatedata;
use climatedata;

grant usage on *.* to sqluser@localhost identified by 'sqluserpw';
grant all privileges on climatedata.* to sqluser@localhost;

CREATE TABLE weatherstations_CLIMATE (id INT NOT NULL AUTO_INCREMENT,
    -- wmo_station_id
    station_id VARCHAR(6),
    station_name VARCHAR(255),
    latitude VARCHAR(15),
    longitude VARCHAR(15),
    elevation INTEGER,
    extra1 VARCHAR(255),
    country VARCHAR(255),
    PRIMARY KEY (id));

CREATE TABLE weatherstations_DWD (id INT NOT NULL AUTO_INCREMENT,
    station_id VARCHAR(6),
    from_date VARCHAR(8),
    to_date VARCHAR(8),
    elevation INTEGER,
    latitude VARCHAR(15),
    longitude VARCHAR(15),
    station_name VARCHAR(255),
    state VARCHAR(255)
    country VARCHAR(255),
    PRIMARY KEY (id));

/*
CREATE TABLE weatherstations (id INT NOT NULL AUTO_INCREMENT,
    wban VARCHAR(5) NOT NULL,
    name VARCHAR(100),
    country_code VARCHAR(5),
    latitude VARCHAR(15),
    longitude VARCHAR(15),
    elevation INTEGER,
    PRIMARY KEY (id));
*/
--INSERT INTO weatherstations values (default, '33913', 'AIN EL','AL', '3623N', '00637E', 611);
/*
CREATE TABLE countries (id INT NOT NULL AUTO_INCREMENT,
    country_code VARCHAR(5),
    country_name VARCHAR(255),
    PRIMARY KEY (id));
*/
--INSERT INTO countries values (default, 'AB', 'Albania');

-- select w.name, w.countryCode, c.countryName, w.latitude, w.longitude from weatherstations w inner join countries c where c.countryCode=w.countryCode order by c.countryName;


-- Create an Index and name it climate

-- Get the mapping for the index with name awi
GET http://localhost:9200/climatedata/_mapping
