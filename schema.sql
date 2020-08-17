drop database poller;
CREATE DATABASE if not exists poller;
use poller;
CREATE TABLE if not exists locations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40),
    endpoint TEXT,
    frequency INT
);

CREATE TABLE if not exists  pollers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name varchar(40),
    last_heartbeat datetime
);
CREATE TABLE if not exists locations_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    location_id INT,
    poller_id INT,
    status VARCHAR(3),
    response_time_milliseconds INT,
    timedate DATETIME,
    FOREIGN KEY (location_id)
        REFERENCES locations (id),
    FOREIGN KEY (poller_id)
        REFERENCES pollers (id)
);
CREATE TABLE if not exists locations_responsibility (
    id INT AUTO_INCREMENT PRIMARY KEY,
    location_id INT,
    poller_id INT,
    FOREIGN KEY (location_id)
        REFERENCES locations (id),
    FOREIGN KEY (poller_id)
        REFERENCES pollers (id)
);