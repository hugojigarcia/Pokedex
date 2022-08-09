package com.example.pokedex;

import android.os.StrictMode;

import com.example.pokedex.modelo_juego.Juego;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BDConnector {
    private static final BDConnector INSTANCE = new BDConnector();
    private static Connection connection;

    private BDConnector(){
        connection = null;
    }

    public static BDConnector getInstance() throws SQLException {
        if(connection==null) throw new SQLException("Sin conexión con la base de datos.");
        return INSTANCE;
    }

    public static void connect(String url, String user, String password) throws SQLException, ClassNotFoundException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, password);
    }

    public static void connect(InputStream inputStream) throws ClassNotFoundException, IOException, SQLException {
        BufferedReader lector = new BufferedReader(new InputStreamReader(inputStream));
        BDConnector.connect(lector.readLine(), lector.readLine(), lector.readLine());
        lector.close();
    }

    public static void connect(String rutaFichero) throws ClassNotFoundException, IOException, SQLException {
        BufferedReader lector = abrirFicheroConfiguracion(rutaFichero);
        BDConnector.connect(lector.readLine(), lector.readLine(), lector.readLine());
        lector.close();
    }

    private static BufferedReader abrirFicheroConfiguracion(String fichero) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(fichero);
        InputStreamReader is = new InputStreamReader(fis);
        return new BufferedReader(is);
    }

    public static void closeConnection() throws SQLException {
       if(connection!=null) connection.close();
    }

    public Connection getConnection(){
        return connection;
    }

    public ArrayList<String> leerListaJuegos () throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT nombreJuego FROM Juego;");

        ArrayList<String> resultado = new ArrayList();

        while(resultSet.next()) {
            resultado.add(resultSet.getString(1));
        }

        statement.close();
        return resultado;
    }

    public void addJuego (String nombreJuego) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Juego (nombreJuego) VALUES (?)");
        statement.setString(1, nombreJuego);
        statement.execute();
        statement.close();
    }

    public ArrayList<String> leerListaRutas(String nombreJuego) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT nombreRuta FROM Ruta WHERE nombreJuego= (?) ");

        statement.setString(1, nombreJuego);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> resultado = new ArrayList();
        while(resultSet.next()) {
            resultado.add(resultSet.getString(1));
        }

        statement.close();
        return resultado;
    }

    public void addRuta (String nombreJuego, String nombreRuta, int totalEntrenadores) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Ruta (nombreJuego, nombreRuta, totalEntrenadores, entrenadoresDerrotados) VALUES (?, ?, ?, 0)");
        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        statement.setInt(3, totalEntrenadores);
        statement.execute();
        statement.close();
    }

    public ArrayList<String> leerListaZonas(String nombreJuego, String nombreRuta) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT nombreZona FROM Zona WHERE nombreJuego=? AND nombreRuta=?");

        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> resultado = new ArrayList();
        while(resultSet.next()) {
            resultado.add(resultSet.getString(1));
        }

        statement.close();
        return resultado;
    }

    public void addZona (String nombreJuego, String nombreRuta, String nombreZona) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Zona (nombreJuego, nombreRuta, nombreZona) VALUES (?, ?, ?)");
        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        statement.setString(3, nombreZona);
        statement.execute();
        statement.close();
    }

    public int[] leerEntrenadoresDeRuta(String nombreJuego, String nombreRuta) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT entrenadoresDerrotados, totalEntrenadores FROM Ruta WHERE nombreJuego=? AND nombreRuta=?");

        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        ResultSet resultSet = statement.executeQuery();

        resultSet.next();
        int[] resultado = {resultSet.getInt("entrenadoresDerrotados"), resultSet.getInt("totalEntrenadores")};
        statement.close();
        return resultado;
    }

    public void setEntrenadoresDeRuta(String nombreJuego, String nombreRuta, int entrenadoresDerrotados) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE Ruta SET entrenadoresDerrotados = ? WHERE nombreJuego=? AND nombreRuta=?");
        statement.setInt(1, entrenadoresDerrotados);
        statement.setString(2, nombreJuego);
        statement.setString(3, nombreRuta);
        statement.execute();

        statement.close();
    }

    public ArrayList<String> leerListaUbicaciones(String nombreJuego, String nombreRuta, String nombreZona) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT nombreUbicacion FROM Ubicacion WHERE nombreJuego=? AND nombreRuta=? AND nombreZona=?");

        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        statement.setString(3, nombreZona);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> resultado = new ArrayList();
        while(resultSet.next()) {
            resultado.add(resultSet.getString(1));
        }

        statement.close();
        return resultado;
    }

    public void addUbicacion(String nombreJuego, String nombreRuta, String nombreZona, String nombreUbicacion) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Ubicacion (nombreJuego, nombreRuta, nombreZona, nombreUbicacion) VALUES (?, ?, ?, ?)");
        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        statement.setString(3, nombreZona);
        statement.setString(4, nombreUbicacion);
        statement.execute();
        statement.close();
    }

    public ArrayList<Pokemon> leerListaPokemons(String nombreJuego, String nombreRuta, String nombreZona, String nombreUbicacion) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT ubicacion_pokemon.nombrePokemon, numero, probabilidad, capturado " +
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

    public void addPokemon(String nombreJuego, String nombreRuta, String nombreZona, String nombreUbicacion, String nombrePokemon, int probablidad) throws SQLException {
        if(!comprobarYaAdd(nombreJuego, nombrePokemon)){
            addPokemonCapturado(nombreJuego, nombrePokemon, false);
        }
        PreparedStatement statement = connection.prepareStatement("INSERT INTO ubicacion_pokemon (nombreJuego, nombreRuta, nombreZona, nombreUbicacion, " +
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

    public ArrayList<Pokemon> leerPokedex(String nombreJuego) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT Pokemon.nombrePokemon, numero, capturado " +
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

    public ArrayList<Pokemon> buscarPokedex(String nombreJuego, String buscar) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT Pokemon.nombrePokemon, numero, capturado " +
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

    public void addPokemonCapturado(String nombreJuego, String nombrePokemon, boolean capturado) throws SQLException {
        if(comprobarYaAdd(nombreJuego, nombrePokemon)) {
            PreparedStatement statement = connection.prepareStatement("UPDATE juego_pokemon SET capturado = ? WHERE nombreJuego=? AND nombrePokemon=?");
            statement.setBoolean(1, capturado);
            statement.setString(2, nombreJuego);
            statement.setString(3, nombrePokemon);
            statement.execute();
            statement.close();
        } else {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO juego_pokemon (nombreJuego, nombrePokemon, capturado) VALUES (?, ?, ?)");
            statement.setString(1, nombreJuego);
            statement.setString(2, nombrePokemon);
            statement.setBoolean(3, capturado);
            statement.execute();
            statement.close();
        }
    }

    public boolean comprobarYaAdd (String nombreJuego, String nombrePokemon) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("SELECT nombrePokemon FROM juego_pokemon WHERE nombreJuego=? AND nombrePokemon=?");
        statement.setString(1, nombreJuego);
        statement.setString(2, nombrePokemon);
        return statement.executeQuery().next();
    }

    public Pokemon consultarPokemon (String nombrePokemon) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT nombrePokemon, numero FROM Pokemon WHERE nombrePokemon=?");
        statement.setString(1, nombrePokemon);
        ResultSet resultSet = statement.executeQuery();

        Pokemon pokemon = null;
        if(resultSet.next()) {
            pokemon = new Pokemon(resultSet.getString("nombrePokemon"), resultSet.getInt("numero"), false);
        }
        statement.close();
        return pokemon;
    }

    public Juego leerJuego (String nombres) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT juegoID, minPersonas, maxPersonas, numEquipos, edadRecomendada, descripcion, nombres\n" +
                                                                     "FROM Juego WHERE nombres= (?) ");
        statement.setString(1, nombres);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();

        Juego juego = new Juego(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5),
                resultSet.getString(6), resultSet.getString(7));

        statement.close();
        return juego;

    }
}