package com.example.pokedex.modelo_juego;

import java.util.List;

public interface IJuegoModel {
    Juego getJuegoPorId(int id);

    Juego getJuegoPorNombres(String nombres);

    List<Juego> getJuegos();

    List<String> getNombresJuegos();

    int insertar(Juego juego);

    int modificar(Juego juego);

    int eliminar(Juego juego);

    int eliminar(List<Juego> juegos);

}
