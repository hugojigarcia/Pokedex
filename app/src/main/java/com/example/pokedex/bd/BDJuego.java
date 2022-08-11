package com.example.pokedex.bd;

import com.example.pokedex.BDConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BDJuego {
    private static final BDJuego INSTANCE = new BDJuego();
    private BDJuego() {

    }
    public static BDJuego getInstance() {
        return INSTANCE;
    }

    public ArrayList<String> leerListaJuegos () throws SQLException, ClassNotFoundException {
        Statement statement = BDConnector.getInstance().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT nombreJuego FROM Juego;");

        ArrayList<String> resultado = new ArrayList();

        while(resultSet.next()) {
            resultado.add(resultSet.getString(1));
        }

        statement.close();
        return resultado;
    }

    public void addJuego (String nombreJuego) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("INSERT INTO Juego (nombreJuego) VALUES (?)");
        statement.setString(1, nombreJuego);
        statement.execute();
        statement.close();
    }
}
