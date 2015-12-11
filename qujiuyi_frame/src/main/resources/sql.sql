CREATE TABLE `t_sys_config` (
	`id` INT(10) NOT NULL AUTO_INCREMENT,
	`configName` VARCHAR(50) NOT NULL COLLATE 'utf8_bin',
	`configValue` VARCHAR(200) NULL DEFAULT NULL COLLATE 'utf8_bin',
	`configType` INT(1) NULL DEFAULT NULL COMMENT '1:整型\\r\\n            2:浮点型\\r\\n            3:字符串',
	`description` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8_bin',
	PRIMARY KEY (`id`)
)
COMMENT='系统配置信息表'
COLLATE='utf8_bin'
ENGINE=InnoDB
AUTO_INCREMENT=22
;

CREATE TABLE `t_doctor_counter_daily` (
	`target` VARCHAR(80) NOT NULL COLLATE 'utf8_bin',
	`userId` INT(11) NOT NULL,
	`count` INT(11) NULL DEFAULT NULL,
	PRIMARY KEY (`target`, `userId`)
)
COLLATE='utf8_bin'
ENGINE=InnoDB
;

CREATE TABLE `t_doctor_kv_forever` (
	`userId` INT(11) NOT NULL,
	`target` VARCHAR(50) NOT NULL COLLATE 'utf8_bin',
	`value` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8_bin',
	PRIMARY KEY (`userId`, `target`)
)
COLLATE='utf8_bin'
ENGINE=InnoDB
;
