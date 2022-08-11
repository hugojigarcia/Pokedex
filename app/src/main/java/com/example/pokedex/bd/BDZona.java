package com.example.pokedex.bd;

import com.example.pokedex.BDConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BDZona {
    private static final BDZona INSTANCE = new BDZona();
    private BDZona() {

    }
    public static BDZona getInstance() {
        return INSTANCE;
    }

    public ArrayList<String> leerListaZonas(String nombreJuego, String nombreRuta) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("SELECT nombreZona FROM Zona WHERE nombreJuego=? AND nombreRuta=?");

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

    public void addZona (String nombreJuego, String nombreRuta, String nombreZona) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("INSERT INTO Zona (nombreJuego, nombreRuta, nombreZona) VALUES (?, ?, ?)");
        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        statement.setString(3, nombreZona);
        statement.execute();
        statement.close();
    }


}
