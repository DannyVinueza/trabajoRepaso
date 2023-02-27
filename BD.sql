USE pruebare;
CREATE TABLE `pruebare`.`categorias` (
  `idcategory` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `categorias` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idcategory`));

INSERT INTO `pruebare`.`categorias` (`categorias`) VALUES ('Legumbres');
INSERT INTO `pruebare`.`categorias` (`categorias`) VALUES ('Frutas');
INSERT INTO `pruebare`.`categorias` (`categorias`) VALUES ('Verduras');

CREATE TABLE `pruebare`.`stock` (
  `idstock` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(30) NOT NULL,
  `cantidad` INT NOT NULL,
  `precio` DECIMAL(4,2) NOT NULL,
  `categoria` VARCHAR(45) NOT NULL,
  `fechaSis` DATE NOT NULL,
  `fechaIng` DATE NOT NULL,
  PRIMARY KEY (`idstock`));

INSERT INTO `pruebare`.`stock` (`descripcion`, `cantidad`, `precio`, `categoria`, `fechaSis`, `fechaIng`) VALUES ('Manzanas', '3', '0.24', 'Frutas', '2020-02-26', '2023-02-06');
