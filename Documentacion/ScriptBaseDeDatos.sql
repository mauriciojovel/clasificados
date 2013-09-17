SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `guananunciosdb` ;
CREATE SCHEMA IF NOT EXISTS `guananunciosdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `guananunciosdb` ;

-- -----------------------------------------------------
-- Table `guananunciosdb`.`pais`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `guananunciosdb`.`pais` ;

CREATE TABLE IF NOT EXISTS `guananunciosdb`.`pais` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `guananunciosdb`.`usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `guananunciosdb`.`usuario` ;

CREATE TABLE IF NOT EXISTS `guananunciosdb`.`usuario` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `pais_id` BIGINT NOT NULL,
  `nombre` VARCHAR(16) NOT NULL,
  `correo_electronico` VARCHAR(255) NOT NULL,
  `clave` VARCHAR(64) NOT NULL,
  `latitud` INT NOT NULL,
  `altitud` INT NOT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_usuario_pais_idx` (`pais_id` ASC),
  UNIQUE INDEX `unq_nombre_usuario` (`nombre` ASC),
  UNIQUE INDEX `unq_correo_electronico` (`correo_electronico` ASC),
  CONSTRAINT `fk_usuario_pais`
    FOREIGN KEY (`pais_id`)
    REFERENCES `guananunciosdb`.`pais` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `guananunciosdb`.`categoria`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `guananunciosdb`.`categoria` ;

CREATE TABLE IF NOT EXISTS `guananunciosdb`.`categoria` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `guananunciosdb`.`anuncio`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `guananunciosdb`.`anuncio` ;

CREATE TABLE IF NOT EXISTS `guananunciosdb`.`anuncio` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `usuario_id` BIGINT NOT NULL,
  `categoria_id` BIGINT NOT NULL,
  `titulo` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(750) NOT NULL,
  `fecha_creacion` DATETIME NOT NULL,
  `es_activo` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_anuncio_usuario1_idx` (`usuario_id` ASC),
  INDEX `fk_anuncio_categoria1_idx` (`categoria_id` ASC),
  CONSTRAINT `fk_anuncio_usuario1`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `guananunciosdb`.`usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_anuncio_categoria1`
    FOREIGN KEY (`categoria_id`)
    REFERENCES `guananunciosdb`.`categoria` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `guananunciosdb`.`imagen`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `guananunciosdb`.`imagen` ;

CREATE TABLE IF NOT EXISTS `guananunciosdb`.`imagen` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `anuncio_id` BIGINT NOT NULL,
  `imagen` BLOB NOT NULL,
  `fecha_creacion` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_imagen_anuncio1_idx` (`anuncio_id` ASC),
  CONSTRAINT `fk_imagen_anuncio1`
    FOREIGN KEY (`anuncio_id`)
    REFERENCES `guananunciosdb`.`anuncio` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SET SQL_MODE = '';
GRANT USAGE ON *.* TO guananuncio;
 DROP USER guananuncio;
SET SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';
CREATE USER 'guananuncio' IDENTIFIED BY 'anuncio$2013';

GRANT ALL ON `guananunciosdb`.* TO 'guananuncio';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `guananunciosdb`.`pais`
-- -----------------------------------------------------
START TRANSACTION;
USE `guananunciosdb`;
INSERT INTO `guananunciosdb`.`pais` (`id`, `nombre`) VALUES (1, 'El Salvador');
INSERT INTO `guananunciosdb`.`pais` (`id`, `nombre`) VALUES (2, 'Guatemala');
INSERT INTO `guananunciosdb`.`pais` (`id`, `nombre`) VALUES (3, 'Costa Rica');
INSERT INTO `guananunciosdb`.`pais` (`id`, `nombre`) VALUES (4, 'Nicaragua');
INSERT INTO `guananunciosdb`.`pais` (`id`, `nombre`) VALUES (5, 'Honduras');
INSERT INTO `guananunciosdb`.`pais` (`id`, `nombre`) VALUES (6, 'Panama');

COMMIT;

