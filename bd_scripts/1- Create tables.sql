CREATE DATABASE IF NOT EXISTS pokedex; 
USE pokedex;

CREATE TABLE Juego
(
  nombreJuego VARCHAR(100) NOT NULL,
  PRIMARY KEY (nombreJuego)
);

CREATE TABLE Ruta
(
  nombreRuta VARCHAR(100) NOT NULL,
  totalEntrenadores INT NOT NULL,
  entrenadoresDerrotados INT NOT NULL,
  nombreJuego VARCHAR(100) NOT NULL,
  PRIMARY KEY (nombreRuta, nombreJuego),
  FOREIGN KEY (nombreJuego) REFERENCES Juego(nombreJuego)
);

CREATE TABLE Zona
(
  nombreZona VARCHAR(100) NOT NULL,
  nombreRuta VARCHAR(100) NOT NULL,
  nombreJuego VARCHAR(100) NOT NULL,
  PRIMARY KEY (nombreZona, nombreRuta, nombreJuego),
  FOREIGN KEY (nombreRuta, nombreJuego) REFERENCES Ruta(nombreRuta, nombreJuego)
);

CREATE TABLE Pokemon
(
  nombrePokemon VARCHAR(100) NOT NULL,
  numero INT,
  PRIMARY KEY (nombrePokemon)
);

CREATE TABLE Ubicacion
(
  nombreUbicacion VARCHAR(100) NOT NULL,
  nombreZona VARCHAR(100) NOT NULL,
  nombreRuta VARCHAR(100) NOT NULL,
  nombreJuego VARCHAR(100) NOT NULL,
  PRIMARY KEY (nombreUbicacion, nombreZona, nombreRuta, nombreJuego),
  FOREIGN KEY (nombreZona, nombreRuta, nombreJuego) REFERENCES Zona(nombreZona, nombreRuta, nombreJuego)
);

CREATE TABLE ubicacion_pokemon
(
  probabilidad INT NOT NULL,
  nombreUbicacion VARCHAR(100) NOT NULL,
  nombreZona VARCHAR(100) NOT NULL,
  nombreRuta VARCHAR(100) NOT NULL,
  nombreJuego VARCHAR(100) NOT NULL,
  nombrePokemon VARCHAR(100) NOT NULL,
  PRIMARY KEY (nombreUbicacion, nombreZona, nombreRuta, nombreJuego, nombrePokemon),
  FOREIGN KEY (nombreUbicacion, nombreZona, nombreRuta, nombreJuego) REFERENCES Ubicacion(nombreUbicacion, nombreZona, nombreRuta, nombreJuego),
  FOREIGN KEY (nombrePokemon) REFERENCES Pokemon(nombrePokemon)
);

CREATE TABLE juego_pokemon
(
  capturado BOOLEAN NOT NULL,
  nombreJuego VARCHAR(100) NOT NULL,
  nombrePokemon VARCHAR(100) NOT NULL,
  PRIMARY KEY (nombreJuego, nombrePokemon),
  FOREIGN KEY (nombreJuego) REFERENCES Juego(nombreJuego),
  FOREIGN KEY (nombrePokemon) REFERENCES Pokemon(nombrePokemon)
);