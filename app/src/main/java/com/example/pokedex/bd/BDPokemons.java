package com.example.pokedex.bd;

import com.example.pokedex.BDConnector;
import com.example.pokedex.Pokemon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BDPokemons {
    private static final BDPokemons INSTANCE = new BDPokemons();
    private BDPokemons() {

    }
    public static BDPokemons getInstance() {
        return INSTANCE;
    }

    public ArrayList<Pokemon> leerListaPokemons(String nombreJuego, String nombreRuta, String nombreZona, String nombreUbicacion) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("SELECT ubicacion_pokemon.nombrePokemon, numero, probabilidad, capturado " +
                "FROM ubicacion_pokemon INNER JOIN Pokemon ON ubicacion_pokemon.nombrePokemon=Pokemon.nombrePokemon " +
                "INNER JOIN juego_pokemon ON Pokemon.nombrePokemon=juego_pokemon.nombrePokemon " +
                "WHERE ubicacion_pokemon.nombreJuego=juego_pokemon.nombreJuego AND ubicacion_pokemon.nombreJuego=? " +
                "AND nombreRuta=? AND nombreZona=? AND nombreUbicacion=? ORDER BY probabilidad DESC, numero ASC");

        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        statement.setString(3, nombreZona);
        statement.setString(4, nombreUbicacion);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Pokemon> resultado = new ArrayList();
        while(resultSet.next()) {
            resultado.add(new Pokemon(resultSet.getString("nombrePokemon"), resultSet.getInt("numero"),
                    resultSet.getBoolean("capturado"), resultSet.getInt("probabilidad")));
        }

        statement.close();
        return resultado;
    }

    public void addPokemonAUbicacion(String nombreJuego, String nombreRuta, String nombreZona, String nombreUbicacion, String nombrePokemon, int probablidad)
            throws SQLException, ClassNotFoundException {
        ponerPokemonEnPokedex(nombreJuego, nombrePokemon);
        if(comprobarYaAddAUbicacion(nombreJuego, nombreRuta, nombreZona, nombreUbicacion, nombrePokemon)){
            actualizarPokemonAUbicacion(nombreJuego, nombreRuta, nombreZona, nombreUbicacion, nombrePokemon, probablidad);
        } else {
            insertarPokemonAUbicacion(nombreJuego, nombreRuta, nombreZona, nombreUbicacion, nombrePokemon, probablidad);
        }
    }

    private void ponerPokemonEnPokedex(String nombreJuego, String nombrePokemon) throws SQLException, ClassNotFoundException {
        if(!BDPokedex.getInstance().comprobarYaAddPokedex(nombreJuego, nombrePokemon)){
            BDPokedex.getInstance().addPokemonAPokedex(nombreJuego, nombrePokemon, false);
        }
    }

    private boolean comprobarYaAddAUbicacion(String nombreJuego, String nombreRuta, String nombreZona, String nombreUbicacion, String nombrePokemon)
            throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("SELECT nombrePokemon FROM ubicacion_pokemon " +
                "WHERE nombreJuego=? AND nombreRuta=? AND nombreZona=? AND nombreUbicacion=? AND nombrePokemon=?");
        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        statement.setString(3, nombreZona);
        statement.setString(4, nombreUbicacion);
        statement.setString(5, nombrePokemon);
        boolean resultado = statement.executeQuery().next();
        statement.close();
        return resultado;
    }

    private void actualizarPokemonAUbicacion(String nombreJuego, String nombreRuta, String nombreZona, String nombreUbicacion, String nombrePokemon, int probablidad)
            throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("UPDATE ubicacion_pokemon SET probabilidad=? " +
                "WHERE nombreJuego=? AND nombreRuta=? AND nombreZona=? AND nombreUbicacion=? AND nombrePokemon=?");
        statement.setInt(1, probablidad);
        statement.setString(2, nombreJuego);
        statement.setString(3, nombreRuta);
        statement.setString(4, nombreZona);
        statement.setString(5, nombreUbicacion);
        statement.setString(6, nombrePokemon);
        statement.execute();
        statement.close();
    }

    private void insertarPokemonAUbicacion(String nombreJuego, String nombreRuta, String nombreZona, String nombreUbicacion, String nombrePokemon, int probablidad)
            throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("INSERT INTO ubicacion_pokemon (nombreJuego, nombreRuta, nombreZona, nombreUbicacion, " +
                "nombrePokemon, probabilidad) VALUES (?, ?, ?, ?, ?, ?)");
        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        statement.setString(3, nombreZona);
        statement.setString(4, nombreUbicacion);
        statement.setString(5, nombrePokemon);
        statement.setInt(6, probablidad);
        statement.execute();
        statement.close();
    }


}
