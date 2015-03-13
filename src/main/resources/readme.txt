TODOs
install mysql
sudo apt-get install mysql-server-5.6

create user sqluser identified by 'sqluserpw';
create database climatedata;
use climatedata;

grant usage on *.* to sqluser@localhost identified by 'sqluserpw';
grant all privileges on climatedata.* to sqluser@localhost;

CREATE TABLE weatherstations (id INT NOT NULL AUTO_INCREMENT,
    wban VARCHAR(5) NOT NULL,
    name VARCHAR(100),
    countryCode VARCHAR(5) NOT NULL,
    latitude VARCHAR(10),
    longitude VARCHAR(10),
    elevation INTEGER,
    PRIMARY KEY (id));

INSERT INTO weatherstations values (default, '33913', 'AIN EL','AL', '3623N', '00637E', 611);

CREATE TABLE countries (id INT NOT NULL AUTO_INCREMENT,
    countryCode VARCHAR(2) NOT NULL,
    countryName VARCHAR(100),
    PRIMARY KEY (id));

INSERT INTO countries values (default, 'AB', 'Albania');
-- Create an Index and name it climate

-- Get the mapping for the index with name awi
GET http://localhost:9200/awi/_mapping
