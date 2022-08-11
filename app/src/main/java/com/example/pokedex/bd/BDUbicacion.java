package com.example.pokedex.bd;

import com.example.pokedex.BDConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BDUbicacion {
    private static final BDUbicacion INSTANCE = new BDUbicacion();
    private BDUbicacion() {

    }
    public static BDUbicacion getInstance() {
        return INSTANCE;
    }

    public ArrayList<String> leerListaUbicaciones(String nombreJuego, String nombreRuta, String nombreZona) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("SELECT nombreUbicacion FROM Ubicacion WHERE nombreJuego=? AND nombreRuta=? AND nombreZona=?");

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

    public void addUbicacion(String nombreJuego, String nombreRuta, String nombreZona, String nombreUbicacion) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("INSERT INTO Ubicacion (nombreJuego, nombreRuta, nombreZona, nombreUbicacion) VALUES (?, ?, ?, ?)");
        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        statement.setString(3, nombreZona);
        statement.setString(4, nombreUbicacion);
        statement.execute();
        statement.close();
    }
}
