package com.example.pokedex;

public class Pokemon {
    private String nombre;
    private int numero;
    private boolean capturado;
    private int probabilidad;

    public Pokemon(String nombre, int numero, boolean capturado, int probabilidad) {
        this.nombre = nombre;
        this.numero = numero;
        this.capturado = capturado;
        this.probabilidad = probabilidad;
    }

    public Pokemon(String nombre, int numero, boolean capturado) {
        this(nombre, numero, capturado, -1);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public boolean isCapturado() {
        return capturado;
    }

    public void setCapturado(boolean capturado) {
        this.capturado = capturado;
    }

    public int getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(int probabilidad) {
        this.probabilidad = probabilidad;
    }
}
