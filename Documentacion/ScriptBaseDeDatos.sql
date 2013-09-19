SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';


-- -----------------------------------------------------
-- Table `pais`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pais` ;

CREATE TABLE IF NOT EXISTS `pais` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `usuario` ;

CREATE TABLE IF NOT EXISTS `usuario` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `pais_id` BIGINT NOT NULL,
  `nombre` VARCHAR(16) NOT NULL,
  `correo_electronico` VARCHAR(255) NOT NULL,
  `clave` VARCHAR(64) NOT NULL,
  `latitud` INT NOT NULL,
  `altitud` INT NOT NULL,
  `create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `token` VARCHAR(64) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_usuario_pais_idx` (`pais_id` ASC),
  UNIQUE INDEX `unq_nombre_usuario` (`nombre` ASC),
  UNIQUE INDEX `unq_correo_electronico` (`correo_electronico` ASC),
  CONSTRAINT `fk_usuario_pais`
    FOREIGN KEY (`pais_id`)
    REFERENCES `pais` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `categoria`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `categoria` ;

CREATE TABLE IF NOT EXISTS `categoria` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `anuncio`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `anuncio` ;

CREATE TABLE IF NOT EXISTS `anuncio` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `usuario_id` BIGINT NOT NULL,
  `categoria_id` BIGINT NOT NULL,
  `titulo` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(750) NOT NULL,
  `fecha_creacion` DATETIME NOT NULL,
  `es_activo` TINYINT NOT NULL,
  `precio` DOUBLE NULL,
  `telefono` VARCHAR(15) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_anuncio_usuario1_idx` (`usuario_id` ASC),
  INDEX `fk_anuncio_categoria1_idx` (`categoria_id` ASC),
  CONSTRAINT `fk_anuncio_usuario1`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_anuncio_categoria1`
    FOREIGN KEY (`categoria_id`)
    REFERENCES `categoria` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `imagen`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `imagen` ;

CREATE TABLE IF NOT EXISTS `imagen` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `anuncio_id` BIGINT NOT NULL,
  `imagen` BLOB NOT NULL,
  `fecha_creacion` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_imagen_anuncio1_idx` (`anuncio_id` ASC),
  CONSTRAINT `fk_imagen_anuncio1`
    FOREIGN KEY (`anuncio_id`)
    REFERENCES `anuncio` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `pais`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `pais` (`id`, `nombre`) VALUES (1, 'El Salvador');
INSERT INTO `pais` (`id`, `nombre`) VALUES (2, 'Guatemala');
INSERT INTO `pais` (`id`, `nombre`) VALUES (3, 'Costa Rica');
INSERT INTO `pais` (`id`, `nombre`) VALUES (4, 'Nicaragua');
INSERT INTO `pais` (`id`, `nombre`) VALUES (5, 'Honduras');
INSERT INTO `pais` (`id`, `nombre`) VALUES (6, 'Panama');

COMMIT;


-- -----------------------------------------------------
-- Data for table `usuario`
-- -----------------------------------------------------
START TRANSACTION;
INSERT INTO `usuario` (`id`, `pais_id`, `nombre`, `correo_electronico`, `clave`, `latitud`, `altitud`, `create_time`, `token`) VALUES (1, 1, 'test', 'test@gmail.com', 'e10adc3949ba59abbe56e057f20f883e', 50, 50, NULL, 'e10adc3949ba59abbe56e057f20f883e');

COMMIT;

