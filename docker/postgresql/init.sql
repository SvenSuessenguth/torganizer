CREATE USER docker;
CREATE DATABASE torganizer;
GRANT ALL PRIVILEGES ON DATABASE torganizer TO docker;
-- https://www.postgresql.org/message-id/m3mxujgnsl.fsf@passepartout.tim-landscheidt.de
\c torganizer
--
CREATE TABLE _RESTRICTIONS (_ID BIGINT NOT NULL, TYPE VARCHAR(31), _LAST_UPDATE TIMESTAMP, PRIMARY KEY (_ID));
CREATE TABLE _OPPONENT_TYPE_RESTRICTIONS (_ID BIGINT NOT NULL, _OPPONENT_TYPE VARCHAR(255), PRIMARY KEY (_ID));
CREATE TABLE _OPPONENTS (_ID BIGINT NOT NULL, TYPE VARCHAR(31), _LAST_UPDATE TIMESTAMP, _STATUS VARCHAR(255) NOT NULL, PRIMARY KEY (_ID));
CREATE TABLE _SQUADS (_ID BIGINT NOT NULL, PRIMARY KEY (_ID));
CREATE TABLE _BYES (_ID BIGINT NOT NULL, PRIMARY KEY (_ID));
CREATE TABLE _PLAYERS (_ID BIGINT NOT NULL, _LAST_MATCH_TIME VARCHAR(255), _CLUB_ID BIGINT, _PERSON_ID BIGINT NOT NULL, PRIMARY KEY (_ID));
CREATE TABLE _PERSONS (_ID BIGINT NOT NULL, _DATE_OF_BIRTH VARCHAR(255), _FIRST_NAME VARCHAR(255), _GENDER VARCHAR(255) NOT NULL, _LAST_NAME VARCHAR(255), _LAST_UPDATE TIMESTAMP, PRIMARY KEY (_ID));
CREATE TABLE _AGE_RESTRICTIONS (_ID BIGINT NOT NULL, _MAX_DATE_OF_BIRTH VARCHAR(255), _MIN_DATE_OF_BIRTH VARCHAR(255), PRIMARY KEY (_ID));
CREATE TABLE _GYMNASIUMS (_ID BIGINT NOT NULL, _LAST_UPDATE TIMESTAMP, _NAME VARCHAR(255) NOT NULL, PRIMARY KEY (_ID));
CREATE TABLE _CLUBS (_ID BIGINT NOT NULL, _LAST_UPDATE TIMESTAMP, _NAME VARCHAR(255) NOT NULL, PRIMARY KEY (_ID));
CREATE TABLE _GROUPS (_ID BIGINT NOT NULL, _LAST_UPDATE TIMESTAMP, _POSITION INTEGER, PRIMARY KEY (_ID));
CREATE TABLE _POSITIONAL_OPPONENTS (_ID BIGINT NOT NULL, _LAST_UPDATE TIMESTAMP, _POSITION INTEGER NOT NULL, _OPPONENT_ID BIGINT NOT NULL, PRIMARY KEY (_ID));
CREATE TABLE _MATCHES (_ID BIGINT NOT NULL, _FINISHED_TIME VARCHAR(255), _LAST_UPDATE TIMESTAMP, _POSITION INTEGER NOT NULL, _RUNNING BOOLEAN NOT NULL, _GUEST_ID BIGINT, _HOME_ID BIGINT, PRIMARY KEY (_ID));
CREATE TABLE _GENDER_RESTRICTIONS (_ID BIGINT NOT NULL, _GENDER VARCHAR(255) NOT NULL, PRIMARY KEY (_ID));
CREATE TABLE _COURTS (_ID BIGINT NOT NULL, _LAST_UPDATE TIMESTAMP, _NR INTEGER NOT NULL, _MATCH_ID BIGINT, PRIMARY KEY (_ID));
CREATE TABLE _ROUNDS (_ID BIGINT NOT NULL, _LAST_UPDATE TIMESTAMP, _POSITION INTEGER, _QUALIFIED INTEGER, _SYSTEM VARCHAR(255) NOT NULL, PRIMARY KEY (_ID));
CREATE TABLE _DISCIPLINES (_ID BIGINT NOT NULL, _LAST_UPDATE TIMESTAMP, _NAME VARCHAR(255) NOT NULL, PRIMARY KEY (_ID));
CREATE TABLE _UNKNOWNS (_ID BIGINT NOT NULL, PRIMARY KEY (_ID));
CREATE TABLE _TOURNAMENTS (_ID BIGINT NOT NULL, _LAST_UPDATE TIMESTAMP, _NAME VARCHAR(255), PRIMARY KEY (_ID));
CREATE TABLE _RESULTS (_ID BIGINT NOT NULL, _GUEST_SCORE INTEGER NOT NULL, _HOME_SCORE INTEGER NOT NULL, _LAST_UPDATE TIMESTAMP, _POSITION INTEGER NOT NULL, PRIMARY KEY (_ID));
CREATE TABLE _SQUAD_PLAYERS (_SQUAD_ID BIGINT NOT NULL, _PLAYER_ID BIGINT NOT NULL UNIQUE, PRIMARY KEY (_SQUAD_ID, _PLAYER_ID));
CREATE TABLE _GYMNASIUM_COURTS (_GYMNASIUM_ID BIGINT NOT NULL, _COURT_ID BIGINT NOT NULL UNIQUE, PRIMARY KEY (_GYMNASIUM_ID, _COURT_ID));
CREATE TABLE _GROUPS_MATCHES (_GROUP_ID BIGINT NOT NULL, _MATCH_ID BIGINT NOT NULL UNIQUE, PRIMARY KEY (_GROUP_ID, _MATCH_ID));
CREATE TABLE _GROUPS_POSITIONAL_OPPONENTS (_GROUP_ID BIGINT NOT NULL, _POSITIONAL_OPPONENT_ID BIGINT NOT NULL UNIQUE, PRIMARY KEY (_GROUP_ID, _POSITIONAL_OPPONENT_ID));
CREATE TABLE _MATCH_RESULTS (_MATCH_ID BIGINT NOT NULL, _RESULT_ID BIGINT NOT NULL UNIQUE, PRIMARY KEY (_MATCH_ID, _RESULT_ID));
CREATE TABLE _ROUNDS_GROUPS (_ROUND_ID BIGINT NOT NULL, _GROUP_ID BIGINT NOT NULL UNIQUE, PRIMARY KEY (_ROUND_ID, _GROUP_ID));
CREATE TABLE _DISCIPLINES_OPPONENTS (_DISCIPLINE_ID BIGINT NOT NULL, _OPPONENT_ID BIGINT NOT NULL UNIQUE, PRIMARY KEY (_DISCIPLINE_ID, _OPPONENT_ID));
CREATE TABLE _DISCIPLINES_RESTRICTIONS (_DISCIPLINE_ID BIGINT NOT NULL, _RESTRICTION_ID BIGINT NOT NULL UNIQUE, PRIMARY KEY (_DISCIPLINE_ID, _RESTRICTION_ID));
CREATE TABLE _DISCIPLINES_ROUNDS (_DISCIPLINE_ID BIGINT NOT NULL, _ROUND_ID BIGINT NOT NULL UNIQUE, PRIMARY KEY (_DISCIPLINE_ID, _ROUND_ID));
CREATE TABLE _TOURNAMENT_DISCIPLINES (_TOURNAMENT_ID BIGINT NOT NULL, _DISCIPLINE_ID BIGINT NOT NULL UNIQUE, PRIMARY KEY (_TOURNAMENT_ID, _DISCIPLINE_ID));
CREATE TABLE _TOURNAMENT_GYMNASIUMS (_TOURNAMENT_ID BIGINT NOT NULL, _GYMNASIUM_ID BIGINT NOT NULL UNIQUE, PRIMARY KEY (_TOURNAMENT_ID, _GYMNASIUM_ID));
CREATE TABLE _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID BIGINT NOT NULL, _OPPONENT_ID BIGINT NOT NULL UNIQUE, PRIMARY KEY (_TOURNAMENT_ID, _OPPONENT_ID));
ALTER TABLE _OPPONENT_TYPE_RESTRICTIONS ADD CONSTRAINT FK__OPPONENT_TYPE_RESTRICTIONS__ID FOREIGN KEY (_ID) REFERENCES _RESTRICTIONS (_ID);
ALTER TABLE _SQUADS ADD CONSTRAINT FK__SQUADS__ID FOREIGN KEY (_ID) REFERENCES _OPPONENTS (_ID);
ALTER TABLE _BYES ADD CONSTRAINT FK__BYES__ID FOREIGN KEY (_ID) REFERENCES _OPPONENTS (_ID);
ALTER TABLE _PLAYERS ADD CONSTRAINT FK__PLAYERS__PERSON_ID FOREIGN KEY (_PERSON_ID) REFERENCES _PERSONS (_ID);
ALTER TABLE _PLAYERS ADD CONSTRAINT FK__PLAYERS__CLUB_ID FOREIGN KEY (_CLUB_ID) REFERENCES _CLUBS (_ID);
ALTER TABLE _PLAYERS ADD CONSTRAINT FK__PLAYERS__ID FOREIGN KEY (_ID) REFERENCES _OPPONENTS (_ID);
ALTER TABLE _AGE_RESTRICTIONS ADD CONSTRAINT FK__AGE_RESTRICTIONS__ID FOREIGN KEY (_ID) REFERENCES _RESTRICTIONS (_ID);
ALTER TABLE _POSITIONAL_OPPONENTS ADD CONSTRAINT FK__POSITIONAL_OPPONENTS__OPPONENT_ID FOREIGN KEY (_OPPONENT_ID) REFERENCES _OPPONENTS (_ID);
ALTER TABLE _MATCHES ADD CONSTRAINT FK__MATCHES__HOME_ID FOREIGN KEY (_HOME_ID) REFERENCES _OPPONENTS (_ID);
ALTER TABLE _MATCHES ADD CONSTRAINT FK__MATCHES__GUEST_ID FOREIGN KEY (_GUEST_ID) REFERENCES _OPPONENTS (_ID);
ALTER TABLE _GENDER_RESTRICTIONS ADD CONSTRAINT FK__GENDER_RESTRICTIONS__ID FOREIGN KEY (_ID) REFERENCES _RESTRICTIONS (_ID);
ALTER TABLE _COURTS ADD CONSTRAINT FK__COURTS__MATCH_ID FOREIGN KEY (_MATCH_ID) REFERENCES _MATCHES (_ID);
ALTER TABLE _UNKNOWNS ADD CONSTRAINT FK__UNKNOWNS__ID FOREIGN KEY (_ID) REFERENCES _OPPONENTS (_ID);
ALTER TABLE _SQUAD_PLAYERS ADD CONSTRAINT FK__SQUAD_PLAYERS__PLAYER_ID FOREIGN KEY (_PLAYER_ID) REFERENCES _OPPONENTS (_ID);
ALTER TABLE _SQUAD_PLAYERS ADD CONSTRAINT FK__SQUAD_PLAYERS__SQUAD_ID FOREIGN KEY (_SQUAD_ID) REFERENCES _OPPONENTS (_ID);
ALTER TABLE _GYMNASIUM_COURTS ADD CONSTRAINT FK__GYMNASIUM_COURTS__GYMNASIUM_ID FOREIGN KEY (_GYMNASIUM_ID) REFERENCES _GYMNASIUMS (_ID);
ALTER TABLE _GYMNASIUM_COURTS ADD CONSTRAINT FK__GYMNASIUM_COURTS__COURT_ID FOREIGN KEY (_COURT_ID) REFERENCES _COURTS (_ID);
ALTER TABLE _GROUPS_MATCHES ADD CONSTRAINT FK__GROUPS_MATCHES__MATCH_ID FOREIGN KEY (_MATCH_ID) REFERENCES _MATCHES (_ID);
ALTER TABLE _GROUPS_MATCHES ADD CONSTRAINT FK__GROUPS_MATCHES__GROUP_ID FOREIGN KEY (_GROUP_ID) REFERENCES _GROUPS (_ID);
ALTER TABLE _GROUPS_POSITIONAL_OPPONENTS ADD CONSTRAINT FK__GROUPS_POSITIONAL_OPPONENTS__GROUP_ID FOREIGN KEY (_GROUP_ID) REFERENCES _GROUPS (_ID);
ALTER TABLE _GROUPS_POSITIONAL_OPPONENTS ADD CONSTRAINT FK__GROUPS_POSITIONAL_OPPONENTS__POSITIONAL_OPPONENT_ID FOREIGN KEY (_POSITIONAL_OPPONENT_ID) REFERENCES _POSITIONAL_OPPONENTS (_ID);
ALTER TABLE _MATCH_RESULTS ADD CONSTRAINT FK__MATCH_RESULTS__MATCH_ID FOREIGN KEY (_MATCH_ID) REFERENCES _MATCHES (_ID);
ALTER TABLE _MATCH_RESULTS ADD CONSTRAINT FK__MATCH_RESULTS__RESULT_ID FOREIGN KEY (_RESULT_ID) REFERENCES _RESULTS (_ID);
ALTER TABLE _ROUNDS_GROUPS ADD CONSTRAINT FK__ROUNDS_GROUPS__ROUND_ID FOREIGN KEY (_ROUND_ID) REFERENCES _ROUNDS (_ID);
ALTER TABLE _ROUNDS_GROUPS ADD CONSTRAINT FK__ROUNDS_GROUPS__GROUP_ID FOREIGN KEY (_GROUP_ID) REFERENCES _GROUPS (_ID);
ALTER TABLE _DISCIPLINES_OPPONENTS ADD CONSTRAINT FK__DISCIPLINES_OPPONENTS__OPPONENT_ID FOREIGN KEY (_OPPONENT_ID) REFERENCES _OPPONENTS (_ID);
ALTER TABLE _DISCIPLINES_OPPONENTS ADD CONSTRAINT FK__DISCIPLINES_OPPONENTS__DISCIPLINE_ID FOREIGN KEY (_DISCIPLINE_ID) REFERENCES _DISCIPLINES (_ID);
ALTER TABLE _DISCIPLINES_RESTRICTIONS ADD CONSTRAINT FK__DISCIPLINES_RESTRICTIONS__DISCIPLINE_ID FOREIGN KEY (_DISCIPLINE_ID) REFERENCES _DISCIPLINES (_ID);
ALTER TABLE _DISCIPLINES_RESTRICTIONS ADD CONSTRAINT FK__DISCIPLINES_RESTRICTIONS__RESTRICTION_ID FOREIGN KEY (_RESTRICTION_ID) REFERENCES _RESTRICTIONS (_ID);
ALTER TABLE _DISCIPLINES_ROUNDS ADD CONSTRAINT FK__DISCIPLINES_ROUNDS__DISCIPLINE_ID FOREIGN KEY (_DISCIPLINE_ID) REFERENCES _DISCIPLINES (_ID);
ALTER TABLE _DISCIPLINES_ROUNDS ADD CONSTRAINT FK__DISCIPLINES_ROUNDS__ROUND_ID FOREIGN KEY (_ROUND_ID) REFERENCES _ROUNDS (_ID);
ALTER TABLE _TOURNAMENT_DISCIPLINES ADD CONSTRAINT FK__TOURNAMENT_DISCIPLINES__TOURNAMENT_ID FOREIGN KEY (_TOURNAMENT_ID) REFERENCES _TOURNAMENTS (_ID);
ALTER TABLE _TOURNAMENT_DISCIPLINES ADD CONSTRAINT FK__TOURNAMENT_DISCIPLINES__DISCIPLINE_ID FOREIGN KEY (_DISCIPLINE_ID) REFERENCES _DISCIPLINES (_ID);
ALTER TABLE _TOURNAMENT_GYMNASIUMS ADD CONSTRAINT FK__TOURNAMENT_GYMNASIUMS__TOURNAMENT_ID FOREIGN KEY (_TOURNAMENT_ID) REFERENCES _TOURNAMENTS (_ID);
ALTER TABLE _TOURNAMENT_GYMNASIUMS ADD CONSTRAINT FK__TOURNAMENT_GYMNASIUMS__GYMNASIUM_ID FOREIGN KEY (_GYMNASIUM_ID) REFERENCES _GYMNASIUMS (_ID);
ALTER TABLE _TOURNAMENT_OPPONENTS ADD CONSTRAINT FK__TOURNAMENT_OPPONENTS__OPPONENT_ID FOREIGN KEY (_OPPONENT_ID) REFERENCES _OPPONENTS (_ID);
ALTER TABLE _TOURNAMENT_OPPONENTS ADD CONSTRAINT FK__TOURNAMENT_OPPONENTS__TOURNAMENT_ID FOREIGN KEY (_TOURNAMENT_ID) REFERENCES _TOURNAMENTS (_ID);
CREATE SEQUENCE ENTITY_SEQUENCE INCREMENT BY 50 START WITH 149;

-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-- sample data
-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
INSERT INTO _TOURNAMENTS (_ID, _NAME, _LAST_UPDATE) VALUES (1, 'Beispiel', '2020-03-27 07:57:12.83358');

INSERT INTO _CLUBS (_ID, _NAME, _LAST_UPDATE) VALUES (1, 'TuS Ickern', '2020-03-27 07:57:12.83358');
INSERT INTO _CLUBS (_ID, _NAME, _LAST_UPDATE) VALUES (2, 'TB Rauxel', '2020-03-27 07:57:12.83358');
INSERT INTO _CLUBS (_ID, _NAME, _LAST_UPDATE) VALUES (3, 'CTV', '2020-03-27 07:57:12.83358');
INSERT INTO _CLUBS (_ID, _NAME, _LAST_UPDATE) VALUES (4, 'HKC', '2020-03-27 07:57:12.83358');

INSERT INTO _PERSONS (_ID, _DATE_OF_BIRTH, _FIRST_NAME, _LAST_NAME, _GENDER, _LAST_UPDATE) VALUES (1, '2011-11-30', 'A-Firstname', 'A-Lastname', 'MALE', '2020-03-27 07:57:12.83358');
INSERT INTO _OPPONENTS (_ID, _STATUS, TYPE, _LAST_UPDATE) VALUES (1, 'ACTIVE', 'PLAYER', '2020-03-27 07:57:12.83358');
INSERT INTO _PLAYERS (_ID, _PERSON_ID, _CLUB_ID) VALUES (1, 1, 1);
INSERT INTO _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID, _OPPONENT_ID) VALUES (1, 1);

INSERT INTO _PERSONS (_ID, _DATE_OF_BIRTH, _FIRST_NAME, _LAST_NAME, _GENDER, _LAST_UPDATE) VALUES (2, '2011-10-30', 'B-Firstname', 'B-Lastname', 'FEMALE', '2020-03-27 07:57:12.83358');
INSERT INTO _OPPONENTS (_ID, _STATUS, TYPE, _LAST_UPDATE) VALUES (2, 'ACTIVE', 'PLAYER', '2020-03-27 07:57:12.83358');
INSERT INTO _PLAYERS (_ID, _PERSON_ID, _CLUB_ID) VALUES (2, 2, 1);
INSERT INTO _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID, _OPPONENT_ID) VALUES (1, 2);

INSERT INTO _PERSONS (_ID, _DATE_OF_BIRTH, _FIRST_NAME, _LAST_NAME, _GENDER, _LAST_UPDATE) VALUES (3, '2011-09-30', 'C-Firstname', 'C-Lastname', 'MALE', '2020-03-27 07:57:12.83358');
INSERT INTO _OPPONENTS (_ID, _STATUS, TYPE, _LAST_UPDATE) VALUES (3, 'ACTIVE', 'PLAYER', '2020-03-27 07:57:12.83358');
INSERT INTO _PLAYERS (_ID, _PERSON_ID, _CLUB_ID) VALUES (3, 3, 2);
INSERT INTO _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID, _OPPONENT_ID) VALUES (1, 3);

INSERT INTO _PERSONS (_ID, _DATE_OF_BIRTH, _FIRST_NAME, _LAST_NAME, _GENDER, _LAST_UPDATE) VALUES (4, '2011-08-30', 'D-Firstname', 'D-Lastname', 'FEMALE', '2020-03-27 07:57:12.83358');
INSERT INTO _OPPONENTS (_ID, _STATUS, TYPE, _LAST_UPDATE) VALUES (4, 'ACTIVE', 'PLAYER', '2020-03-27 07:57:12.83358');
INSERT INTO _PLAYERS (_ID, _PERSON_ID, _CLUB_ID) VALUES (4, 4, 2);
INSERT INTO _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID, _OPPONENT_ID) VALUES (1, 4);

INSERT INTO _PERSONS (_ID, _DATE_OF_BIRTH, _FIRST_NAME, _LAST_NAME, _GENDER, _LAST_UPDATE) VALUES (5, '2011-07-30', 'E-Firstname', 'E-Lastname', 'MALE', '2020-03-27 07:57:12.83358');
INSERT INTO _OPPONENTS (_ID, _STATUS, TYPE, _LAST_UPDATE) VALUES (5, 'ACTIVE', 'PLAYER', '2020-03-27 07:57:12.83358');
INSERT INTO _PLAYERS (_ID, _PERSON_ID, _CLUB_ID) VALUES (5, 5, 2);
INSERT INTO _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID, _OPPONENT_ID) VALUES (1, 5);

INSERT INTO _PERSONS (_ID, _DATE_OF_BIRTH, _FIRST_NAME, _LAST_NAME, _GENDER, _LAST_UPDATE) VALUES (6, '2011-06-30', 'F-Firstname', 'F-Lastname', 'FEMALE', '2020-03-27 07:57:12.83358');
INSERT INTO _OPPONENTS (_ID, _STATUS, TYPE, _LAST_UPDATE) VALUES (6, 'ACTIVE', 'PLAYER', '2020-03-27 07:57:12.83358');
INSERT INTO _PLAYERS (_ID, _PERSON_ID, _CLUB_ID) VALUES (6, 6, 2);
INSERT INTO _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID, _OPPONENT_ID) VALUES (1, 6);

INSERT INTO _PERSONS (_ID, _DATE_OF_BIRTH, _FIRST_NAME, _LAST_NAME, _GENDER, _LAST_UPDATE) VALUES (7, '2011-05-30', 'G-Firstname', 'G-Lastname', 'MALE', '2020-03-27 07:57:12.83358');
INSERT INTO _OPPONENTS (_ID, _STATUS, TYPE, _LAST_UPDATE) VALUES (7, 'ACTIVE', 'PLAYER', '2020-03-27 07:57:12.83358');
INSERT INTO _PLAYERS (_ID, _PERSON_ID, _CLUB_ID) VALUES (7, 7, 2);
INSERT INTO _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID, _OPPONENT_ID) VALUES (1, 7);

INSERT INTO _PERSONS (_ID, _DATE_OF_BIRTH, _FIRST_NAME, _LAST_NAME, _GENDER, _LAST_UPDATE) VALUES (8, '2011-04-30', 'H-Firstname', 'H-Lastname', 'FEMALE', '2020-03-27 07:57:12.83358');
INSERT INTO _OPPONENTS (_ID, _STATUS, TYPE, _LAST_UPDATE) VALUES (8, 'ACTIVE', 'PLAYER', '2020-03-27 07:57:12.83358');
INSERT INTO _PLAYERS (_ID, _PERSON_ID, _CLUB_ID) VALUES (8, 8, 4);
INSERT INTO _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID, _OPPONENT_ID) VALUES (1, 8);

INSERT INTO _PERSONS (_ID, _DATE_OF_BIRTH, _FIRST_NAME, _LAST_NAME, _GENDER, _LAST_UPDATE) VALUES (9, '2011-09-30', 'I-Firstname', 'I-Lastname', 'MALE', '2020-03-27 07:57:12.83358');
INSERT INTO _OPPONENTS (_ID, _STATUS, TYPE, _LAST_UPDATE) VALUES (9, 'ACTIVE', 'PLAYER', '2020-03-27 07:57:12.83358');
INSERT INTO _PLAYERS (_ID, _PERSON_ID, _CLUB_ID) VALUES (9, 9, 3);
INSERT INTO _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID, _OPPONENT_ID) VALUES (1, 9);

INSERT INTO _PERSONS (_ID, _DATE_OF_BIRTH, _FIRST_NAME, _LAST_NAME, _GENDER, _LAST_UPDATE) VALUES (10, '2011-09-30', 'J-Firstname', 'J-Lastname', 'FEMALE', '2020-03-27 07:57:12.83358');
INSERT INTO _OPPONENTS (_ID, _STATUS, TYPE, _LAST_UPDATE) VALUES (10, 'ACTIVE', 'PLAYER', '2020-03-27 07:57:12.83358');
INSERT INTO _PLAYERS (_ID, _PERSON_ID, _CLUB_ID) VALUES (10, 10, 3);
INSERT INTO _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID, _OPPONENT_ID) VALUES (1, 10);

INSERT INTO _PERSONS (_ID, _DATE_OF_BIRTH, _FIRST_NAME, _LAST_NAME, _GENDER, _LAST_UPDATE) VALUES (11, '2011-09-30', 'K-Firstname', 'K-Lastname', 'MALE', '2020-03-27 07:57:12.83358');
INSERT INTO _OPPONENTS (_ID, _STATUS, TYPE, _LAST_UPDATE) VALUES (11, 'ACTIVE', 'PLAYER', '2020-03-27 07:57:12.83358');
INSERT INTO _PLAYERS (_ID, _PERSON_ID, _CLUB_ID) VALUES (11, 11, 3);
INSERT INTO _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID, _OPPONENT_ID) VALUES (1, 11);

INSERT INTO _PERSONS (_ID, _DATE_OF_BIRTH, _FIRST_NAME, _LAST_NAME, _GENDER, _LAST_UPDATE) VALUES (12, '2011-09-30', 'L-Firstname', 'L-Lastname', 'FEMALE', '2020-03-27 07:57:12.83358');
INSERT INTO _OPPONENTS (_ID, _STATUS, TYPE, _LAST_UPDATE) VALUES (12, 'ACTIVE', 'PLAYER', '2020-03-27 07:57:12.83358');
INSERT INTO _PLAYERS (_ID, _PERSON_ID, _CLUB_ID) VALUES (12, 12, 3);
INSERT INTO _TOURNAMENT_OPPONENTS (_TOURNAMENT_ID, _OPPONENT_ID) VALUES (1, 12);

INSERT INTO _RESTRICTIONS (_ID, TYPE, _LAST_UPDATE) VALUES (1, 'AGE', '2020-03-27 07:57:12.83358');
INSERT INTO _RESTRICTIONS (_ID, TYPE, _LAST_UPDATE) VALUES(2, 'GENDER', '2020-03-27 07:57:12.83358');
INSERT INTO _RESTRICTIONS (_ID, TYPE, _LAST_UPDATE) VALUES(3, 'OPPONENT_TYPE', '2020-03-27 07:57:12.83358');
INSERT INTO _AGE_RESTRICTIONS (_ID, _MIN_DATE_OF_BIRTH, _MAX_DATE_OF_BIRTH) VALUES (1, '2010-12-31', '2011-12-31');
INSERT INTO _GENDER_RESTRICTIONS (_ID, _GENDER) VALUES (2, 'MALE');
INSERT INTO _OPPONENT_TYPE_RESTRICTIONS (_ID, _OPPONENT_TYPE) VALUES (3, 'PLAYER');

INSERT INTO _DISCIPLINES (_ID, _NAME, _LAST_UPDATE) VALUES (1, 'HE-A', '2020-03-27 07:57:12.83358');
INSERT INTO _DISCIPLINES_RESTRICTIONS (_DISCIPLINE_ID, _RESTRICTION_ID) VALUES (1, 1);
INSERT INTO _DISCIPLINES_RESTRICTIONS (_DISCIPLINE_ID, _RESTRICTION_ID) VALUES (1, 2);
INSERT INTO _DISCIPLINES_RESTRICTIONS (_DISCIPLINE_ID, _RESTRICTION_ID) VALUES (1, 3);

INSERT INTO _TOURNAMENT_DISCIPLINES (_TOURNAMENT_ID, _DISCIPLINE_ID) VALUES (1, 1);

INSERT INTO _DISCIPLINES_OPPONENTS (_DISCIPLINE_ID, _OPPONENT_ID) VALUES (1, 1);
INSERT INTO _DISCIPLINES_OPPONENTS (_DISCIPLINE_ID, _OPPONENT_ID) VALUES (1, 3);
INSERT INTO _DISCIPLINES_OPPONENTS (_DISCIPLINE_ID, _OPPONENT_ID) VALUES (1, 5);

INSERT INTO _ROUNDS (_ID, _LAST_UPDATE, _POSITION, _QUALIFIED, _SYSTEM) values (1, '2020-03-27 07:57:12.83358', 0, 0, 'DOUBLE_ELIMINATION');
INSERT INTO _ROUNDS (_ID, _LAST_UPDATE, _POSITION, _QUALIFIED, _SYSTEM) values (2, '2020-03-27 07:57:12.83359', 1, 0, 'DOUBLE_ELIMINATION');
INSERT INTO _DISCIPLINES_ROUNDS (_DISCIPLINE_ID, _ROUND_ID) values (1, 1);
INSERT INTO _DISCIPLINES_ROUNDS (_DISCIPLINE_ID, _ROUND_ID) values (1, 2);