CREATE TABLE `tasker_dw_db`.`task` (
  `taskId` VARCHAR(50) NOT NULL,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(255) NULL,
  `taskDate` VARCHAR(45) NULL,
  PRIMARY KEY (`taskId`));
