package com.example.pokedex;

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDConnector {
    private static final BDConnector INSTANCE = new BDConnector();
    private static Connection connection;
    private static String urlAnterior, userAnterior, passwordAnterior;

    private BDConnector(){
        connection = null;
        urlAnterior = null;
        userAnterior = null;
        passwordAnterior = null;
    }

    public static BDConnector getInstance() throws SQLException {
        if(connection==null) throw new SQLException("Sin conexión con la base de datos.");
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if(connection.isClosed()) reconectar();
        return connection;
    }

    public static void connect(String url, String user, String password) throws SQLException, ClassNotFoundException {
        urlAnterior = url;
        userAnterior = user;
        passwordAnterior = password;
        reconectar();
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

    private static void reconectar() throws SQLException, ClassNotFoundException{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(urlAnterior, userAnterior, passwordAnterior);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}