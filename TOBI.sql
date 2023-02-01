/* ========================================================================== */
/* 실습 시작 (PART1) */
SHOW DATABASES;	
CREATE DATABASE TOBI DEFAULT CHARACTER SET utf8;
-- CREATE USER 'jsy' IDENTIFIED BY '1234';
CREATE USER 'jsy'@'localhost' IDENTIFIED BY '1234';

DELETE FROM tobi WHERE USER='jsy';
DROP TABLE users;
/* ========================================================================== */
USE tobi;

CREATE TABLE users(
	Id VARCHAR(10) PRIMARY KEY,
    Name VARCHAR(20) NOT NULL,
    Password VARCHAR(20) NOT NULL
);

SELECT * FROM users;

