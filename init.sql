CREATE TABLE `PARTY` (
                         `ID` BIGINT NOT NULL AUTO_INCREMENT,
                         `created_date_time` TIMESTAMP NOT NULL,
                         `description` varchar(1000),
    `party_type_id` BIGINT NOT NULL,
    PRIMARY KEY (`ID`)
    );

CREATE TABLE `PARTY_TYPE` (
                              `ID` BIGINT NOT NULL AUTO_INCREMENT,
                              `name` varchar(255) NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`ID`)
    );

CREATE TABLE `ACCOUNT` (
    `number` varchar(255) NOT NULL UNIQUE,
    `party_id` BIGINT NOT NULL,
    `created_date_ime` TIMESTAMP NOT NULL,
    `description` varchar(1000),
    `balance` BIGINT NOT NULL,
    PRIMARY KEY (`number`)
    );

CREATE TABLE `TRANSFER` (
                            `ID` BIGINT NOT NULL AUTO_INCREMENT,
                            `from_account_id` varchar(255) NOT NULL,
    `to_account_id` varchar(255) NOT NULL,
    `amount` BIGINT NOT NULL,
    `state` varchar(50) NOT NULL,
    `timestamp` TIMESTAMP NOT NULL,
    PRIMARY KEY (`ID`)
    );

ALTER TABLE `PARTY` ADD CONSTRAINT `PARTY_fk0` FOREIGN KEY (`party_type_id`) REFERENCES `PARTY_TYPE`(`ID`);

ALTER TABLE `ACCOUNT` ADD CONSTRAINT `ACCOUNT_fk0` FOREIGN KEY (`party_id`) REFERENCES `PARTY`(`ID`);

ALTER TABLE `TRANSFER` ADD CONSTRAINT `TRANSFER_fk0` FOREIGN KEY (`from_account_id`) REFERENCES `ACCOUNT`(`number`);

ALTER TABLE `TRANSFER` ADD CONSTRAINT `TRANSFER_fk1` FOREIGN KEY (`to_account_id`) REFERENCES `ACCOUNT`(`number`);





CREATE TABLE `PERSON` (
                          `PARTY_ID` BIGINT NOT NULL AUTO_INCREMENT,
                          `first_name` varchar(1000) NOT NULL,
    `last_name` varchar(1000) NOT NULL,
    `middle_name` varchar(1000),
    `birth_date` DATE NOT NULL,
    `address` varchar(1000) NOT NULL,
    PRIMARY KEY (`PARTY_ID`)
    );

CREATE TABLE `ORGANIZATION` (
                                `PARTY_ID` BIGINT NOT NULL AUTO_INCREMENT,
                                `name` varchar(1000) NOT NULL,
    `created_date` DATE NOT NULL,
    PRIMARY KEY (`PARTY_ID`)
    );


ALTER TABLE `PERSON` ADD CONSTRAINT `PERSON_fk0` FOREIGN KEY (`PARTY_ID`) REFERENCES `PARTY`(`ID`);

ALTER TABLE `ORGANIZATION` ADD CONSTRAINT `ORGANIZATION_fk0` FOREIGN KEY (`PARTY_ID`) REFERENCES `PARTY`(`ID`);


select t.from_account_id, t.to_account_id, t.timestamp, t.amount from TRANSFER t join ACCOUNT a on t.from_account_id = a.NUMBER or t.to_account_id = a.number