package com.example.pokedex.modelo_juego;

import java.io.Serializable;

public class Juego implements Serializable {
    private int id, minPersonas, maxPersonas, numEquipos, edadRecomendada;
    private String descripcion, nombres;

    public Juego(int id, int minPersonas, int maxPersonas, int numEquipos, int edadRecomendada, String descripcion, String nombres) {
        this.id = id;
        this.minPersonas = minPersonas;
        this.maxPersonas = maxPersonas;
        this.numEquipos = numEquipos;
        this.edadRecomendada = edadRecomendada;
        this.descripcion = descripcion;
        this.nombres = nombres;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMinPersonas() { return minPersonas; }
    public void setMinPersonas(int minPersonas) { this.minPersonas = minPersonas; }

    public int getMaxPersonas() { return maxPersonas; }
    public void setMaxPersonas(int maxPersonas) {  this.maxPersonas = maxPersonas; }

    public int getNumEquipos() {  return numEquipos; }
    public void setNumEquipos(int numEquipos) { this.numEquipos = numEquipos; }

    public int getEdadRecomendada() { return edadRecomendada; }
    public void setEdadRecomendada(int edadRecomendada) { this.edadRecomendada = edadRecomendada; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
}
