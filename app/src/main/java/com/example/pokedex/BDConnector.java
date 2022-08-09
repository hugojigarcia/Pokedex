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

    public ArrayList<String> leerListaJuegos() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT nombres FROM Juego;");

        ArrayList<String> resultado = new ArrayList();

        while(resultSet.next()) {
            resultado.add(resultSet.getString(1));
        }

        statement.close();
        return resultado;
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