package com.example.pokedex.bd;

import com.example.pokedex.BDConnector;
import com.example.pokedex.Pokemon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BDPokedex {
    private static final BDPokedex INSTANCE = new BDPokedex();
    private BDPokedex() {

    }
    public static BDPokedex getInstance() {
        return INSTANCE;
    }

    public ArrayList<Pokemon> leerPokedex(String nombreJuego) throws SQLException, ClassNotFoundException{
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("SELECT Pokemon.nombrePokemon, numero, capturado " +
                "FROM Pokemon INNER JOIN juego_pokemon ON Pokemon.nombrePokemon=juego_pokemon.nombrePokemon " +
                "WHERE nombreJuego=? ORDER BY numero ASC");

        statement.setString(1, nombreJuego);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Pokemon> resultado = new ArrayList();
        while(resultSet.next()) {
            resultado.add(new Pokemon(resultSet.getString("nombrePokemon"), resultSet.getInt("numero"), resultSet.getBoolean("capturado")));
        }

        statement.close();
        return resultado;
    }

    public ArrayList<Pokemon> busquedaEnPokedex (String nombreJuego, String buscar) throws SQLException, ClassNotFoundException{
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("SELECT Pokemon.nombrePokemon, numero, capturado " +
                "FROM Pokemon INNER JOIN juego_pokemon ON Pokemon.nombrePokemon=juego_pokemon.nombrePokemon " +
                "WHERE nombreJuego=? AND Pokemon.nombrePokemon LIKE ? ORDER BY numero ASC");

        statement.setString(1, nombreJuego);
        statement.setString(2, "%"+buscar+"%");
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Pokemon> resultado = new ArrayList();
        while(resultSet.next()) {
            resultado.add(new Pokemon(resultSet.getString("nombrePokemon"), resultSet.getInt("numero"), resultSet.getBoolean("capturado")));
        }

        statement.close();
        return resultado;
    }

    public void addPokemonAPokedex (String nombreJuego, String nombrePokemon, boolean capturado) throws SQLException, ClassNotFoundException{
        if(comprobarYaAddPokedex(nombreJuego, nombrePokemon)) {
            actualizarPokemonAPokedex(nombreJuego, nombrePokemon, capturado);
        } else {
            insertarPokemonAPokedex(nombreJuego, nombrePokemon, capturado);
        }
    }

    private void actualizarPokemonAPokedex(String nombreJuego, String nombrePokemon, boolean capturado) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("UPDATE juego_pokemon SET capturado = ? WHERE nombreJuego=? AND nombrePokemon=?");
        statement.setBoolean(1, capturado);
        statement.setString(2, nombreJuego);
        statement.setString(3, nombrePokemon);
        statement.execute();
        statement.close();
    }

    private void insertarPokemonAPokedex(String nombreJuego, String nombrePokemon, boolean capturado) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("INSERT INTO juego_pokemon (nombreJuego, nombrePokemon, capturado) VALUES (?, ?, ?)");
        statement.setString(1, nombreJuego);
        statement.setString(2, nombrePokemon);
        statement.setBoolean(3, capturado);
        statement.execute();
        statement.close();
    }

    public boolean comprobarYaAddPokedex(String nombreJuego, String nombrePokemon) throws SQLException, ClassNotFoundException{
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("SELECT nombrePokemon FROM juego_pokemon WHERE nombreJuego=? AND nombrePokemon=?");
        statement.setString(1, nombreJuego);
        statement.setString(2, nombrePokemon);
        return statement.executeQuery().next();
    }

    public boolean existePokemon(String nombrePokemon) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("SELECT nombrePokemon, numero FROM Pokemon WHERE nombrePokemon=?");
        statement.setString(1, nombrePokemon);

        boolean resultado = statement.executeQuery().next();
        statement.close();
        return resultado;
    }
}
