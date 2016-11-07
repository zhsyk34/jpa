CREATE TABLE IF NOT EXISTS project (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name       VARCHAR(60)     NOT NULL,
  createTime TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS build (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  projectId  BIGINT UNSIGNED NOT NULL,
  name       VARCHAR(30)     NOT NULL,
  no         VARCHAR(6)      NOT NULL,
  createTime TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS unit (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  buildId    BIGINT UNSIGNED NOT NULL,
  name       VARCHAR(30)     NOT NULL,
  createTime TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS house (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  unitId     BIGINT UNSIGNED NOT NULL,
  name       VARCHAR(30)     NOT NULL,
  createTime TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

/*设备*/
CREATE TABLE IF NOT EXISTS gateway (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  unitId     BIGINT UNSIGNED NOT NULL,
  sn         VARCHAR(30)     NOT NULL,
  udid       VARCHAR(60)     NOT NULL,
  name       VARCHAR(30)     NOT NULL,
  ip         VARCHAR(40)              DEFAULT '127.0.0.1',
  port       INT UNSIGNED             DEFAULT 50000,
  remote     VARCHAR(40)              DEFAULT '114.55.219.171',
  version    VARCHAR(30),
  qrCode     VARCHAR(50),
  createTime TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS locks (
  id         BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT,
  gatewayId  BIGINT UNSIGNED  NOT NULL,
  houseId    BIGINT UNSIGNED  NOT NULL, /*未安装时为0*/
  uuid       VARCHAR(36)      NOT NULL,
  name       VARCHAR(30)      NOT NULL,
  password   VARCHAR(30)      NOT NULL,
  tempword   VARCHAR(30)      NOT NULL,
  permission BOOLEAN          NOT NULL DEFAULT TRUE,
  device     TINYINT UNSIGNED NOT NULL,
  createTime TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

/*用户:管理员+租客*/
CREATE TABLE IF NOT EXISTS user (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name       VARCHAR(30)     NOT NULL,
  password   VARCHAR(255)    NOT NULL,
  createTime TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;


CREATE TABLE IF NOT EXISTS tenant (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  houseId    BIGINT UNSIGNED NOT NULL,
  name       VARCHAR(30)     NOT NULL,
  idCard     VARCHAR(18),
  phone      VARCHAR(11),
  gender     VARCHAR(6),
  email      VARCHAR(60),
  used       BOOLEAN         NOT NULL DEFAULT TRUE,
  createTime TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS credential (
  id         BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT,
  tenantId   BIGINT UNSIGNED  NOT NULL, /*未登记租客时为0*/
  lockId     BIGINT UNSIGNED  NOT NULL,
  type       VARCHAR(20)      NOT NULL,
  value      VARCHAR(6)       NOT NULL,
  sequence   TINYINT UNSIGNED NOT NULL,
  createTime TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS record (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  uuid        VARCHAR(36)     NOT NULL,
  action      VARCHAR(30)     NOT NULL,
  type        VARCHAR(20)     NOT NULL,
  pass        TINYINT UNSIGNED,
  description VARCHAR(50)     NOT NULL,
  createTime  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  eventTime   TIMESTAMP       NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS localrecord (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  uuid        VARCHAR(36)     NOT NULL,
  action      VARCHAR(30)     NOT NULL,
  type        VARCHAR(20)     NOT NULL,
  pass        TINYINT UNSIGNED,
  description VARCHAR(50)     NOT NULL,
  createTime  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  eventTime   TIMESTAMP       NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS errorrecord (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  lockId      BIGINT UNSIGNED NOT NULL,
  original    VARCHAR(20)     NOT NULL,
  current     VARCHAR(20)     NOT NULL,
  description VARCHAR(100)    NOT NULL,
  createTime  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  eventTime   TIMESTAMP       NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS status (
  id       BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT,
  uuid     VARCHAR(36)      NOT NULL,
  locked   TINYINT UNSIGNED NOT NULL,
  upLock   TINYINT UNSIGNED NOT NULL,
  backLock TINYINT UNSIGNED NOT NULL,
  voltage  INT UNSIGNED     NOT NULL,
  online   TINYINT UNSIGNED NOT NULL,
  time     TIMESTAMP        NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS sessioninfo (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  sn         VARCHAR(36),
  udid       VARCHAR(36)     NOT NULL,
  address    VARCHAR(36)     NOT NULL,
  tcpport    INT UNSIGNED,
  udpport    INT UNSIGNED,
  curudpport INT UNSIGNED,
  createTime TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS config (
  id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name        VARCHAR(30)     NOT NULL,
  value       VARCHAR(255)    NOT NULL,
  description VARCHAR(50)     NOT NULL,
  createTime  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime  TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS gatewayversion (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  major      INT(11)         NOT NULL,
  minor      INT(11)         NOT NULL,
  version    VARCHAR(255)    NOT NULL,
  versionNo  VARCHAR(255)    NOT NULL,
  url        VARCHAR(255)    NOT NULL,
  createTime TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS userbuild (
  id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  userId  BIGINT UNSIGNED NOT NULL,
  buildId BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS doorcard (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  lockId     BIGINT UNSIGNED NOT NULL,
  type       VARCHAR(30)     NOT NULL,
  no         VARCHAR(60)     NOT NULL,
  enabled    BOOLEAN                  DEFAULT TRUE,
  createTime TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

/*外键*/
ALTER TABLE build
  ADD CONSTRAINT fk_build_project FOREIGN KEY (projectId) REFERENCES project (id);

ALTER TABLE unit
  ADD CONSTRAINT fk_unit_build FOREIGN KEY (buildId) REFERENCES build (id);

ALTER TABLE house
  ADD CONSTRAINT fk_house_unit FOREIGN KEY (unitId) REFERENCES unit (id);

ALTER TABLE gateway
  ADD CONSTRAINT fk_gateway_unit FOREIGN KEY (unitId) REFERENCES unit (id);


ALTER TABLE locks
  ADD CONSTRAINT fk_locks_gateway FOREIGN KEY (gatewayId) REFERENCES gateway (id);
-- ADD CONSTRAINT fk_locks_house FOREIGN KEY (houseId) REFERENCES house (id);

ALTER TABLE tenant
  ADD CONSTRAINT fk_tenant_house FOREIGN KEY (houseId) REFERENCES house (id);

ALTER TABLE credential
  ADD CONSTRAINT fk_credential_locks FOREIGN KEY (lockId) REFERENCES locks (id);

ALTER TABLE doorcard
  ADD CONSTRAINT fk_doorcard_locks FOREIGN KEY (lockId) REFERENCES locks (id);

ALTER TABLE userbuild
  ADD CONSTRAINT fk_userbuild_user FOREIGN KEY (userId) REFERENCES user (id);
ALTER TABLE userbuild
  ADD CONSTRAINT fk_userbuild_build FOREIGN KEY (buildId) REFERENCES build (id);

/*索引*/
ALTER TABLE record
  ADD INDEX index_record_uuid (uuid);

ALTER TABLE localrecord
  ADD INDEX index_localrecord_uuid (uuid);

/**/
ALTER TABLE user
  ADD CONSTRAINT unique_name UNIQUE (name);

/*-------------------------------2016-11-03 修改-------------------------------*/
CREATE TABLE IF NOT EXISTS userbuild (
  id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  userId  BIGINT UNSIGNED NOT NULL,
  buildId BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS doorcard (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  lockId     BIGINT UNSIGNED NOT NULL,
  type       VARCHAR(30)     NOT NULL,
  no         VARCHAR(60)     NOT NULL,
  enabled    BOOLEAN                  DEFAULT TRUE,
  createTime TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS project (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name       VARCHAR(60)     NOT NULL,
  createTime TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updateTime TIMESTAMP,
  PRIMARY KEY (id)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = UTF8;

ALTER TABLE doorcard
  ADD CONSTRAINT fk_doorcard_locks FOREIGN KEY (lockId) REFERENCES locks (id);
ALTER TABLE userbuild
  ADD CONSTRAINT fk_userbuild_user FOREIGN KEY (userId) REFERENCES user (id);
ALTER TABLE userbuild
  ADD CONSTRAINT fk_userbuild_build FOREIGN KEY (buildId) REFERENCES build (id);

/*取消关联,删除上传数据时先置为0*/
ALTER TABLE locks
  DROP FOREIGN KEY fk_locks_house;

INSERT INTO project (name, updateTime) VALUES ('万科项目', CURRENT_TIMESTAMP);

ALTER TABLE build
  ADD COLUMN projectId BIGINT UNSIGNED;

UPDATE build
SET projectId = 1;

ALTER TABLE build
  ADD CONSTRAINT fk_build_project FOREIGN KEY (projectId) REFERENCES project (id);

/*-----------------------2016-11-03-------------------------------*/


INSERT INTO `user` (`name`, `password`, `createTime`, `updateTime`) VALUES ('root', 'DC:76:E9:F0:C0:00:6E:8F:91:9E:0C:51:5C:66:DB:BA:39:82:F7:85', NOW(), NOW());
INSERT INTO config (`name`, `value`, `description`, `createTime`, `updateTime`) VALUES ('tmpPwd', '123456', '临时密码', NOW(), NOW());
INSERT INTO config (`name`, `value`, `description`, `createTime`, `updateTime`) VALUES ('serverIP', '114.55.219.171', '服务器IP', NOW(), NOW());
INSERT INTO config (`name`, `value`, `description`, `createTime`, `updateTime`) VALUES ('serverPort', '15999', '服务器端口', NOW(), NOW());
INSERT INTO config (`name`, `value`, `description`, `createTime`, `updateTime`) VALUES ('ntpIP0', '202.108.6.95', 'NTP服务器地址', NOW(), NOW());
INSERT INTO config (`name`, `value`, `description`, `createTime`, `updateTime`) VALUES ('ntpIP1', '132.163.4.101', '备用NTP服务器地址', NOW(), NOW());
INSERT INTO config (`name`, `value`, `description`, `createTime`, `updateTime`) VALUES ('ntpPort', '123', 'NTP服务器端口', NOW(), NOW());
INSERT INTO config (`name`, `value`, `description`, `createTime`, `updateTime`) VALUES ('weatherURL', 'api.thinkpage.cn/v3/weather/now.json?key=AL87IW41QK&location=ip&language=z h-Hans&unit=c', '天气服务URL', NOW(), NOW());

