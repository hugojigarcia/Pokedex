package com.example.pokedex.bd;

import com.example.pokedex.BDConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BDRuta {
    private static final BDRuta INSTANCE = new BDRuta();
    private BDRuta() {

    }
    public static BDRuta getInstance() {
        return INSTANCE;
    }

    public ArrayList<String> leerListaRutas(String nombreJuego) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("SELECT nombreRuta FROM Ruta WHERE nombreJuego= (?) ");

        statement.setString(1, nombreJuego);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<String> resultado = new ArrayList();
        while(resultSet.next()) {
            resultado.add(resultSet.getString(1));
        }

        statement.close();
        return resultado;
    }

    public void addRuta (String nombreJuego, String nombreRuta, int totalEntrenadores) throws SQLException, ClassNotFoundException{
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("INSERT INTO Ruta (nombreJuego, nombreRuta, totalEntrenadores, entrenadoresDerrotados) VALUES (?, ?, ?, 0)");
        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        statement.setInt(3, totalEntrenadores);
        statement.execute();
        statement.close();
    }

    public int[] leerEntrenadoresDeRuta(String nombreJuego, String nombreRuta) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("SELECT entrenadoresDerrotados, totalEntrenadores FROM Ruta WHERE nombreJuego=? AND nombreRuta=?");

        statement.setString(1, nombreJuego);
        statement.setString(2, nombreRuta);
        ResultSet resultSet = statement.executeQuery();

        resultSet.next();
        int[] resultado = {resultSet.getInt("entrenadoresDerrotados"), resultSet.getInt("totalEntrenadores")};
        statement.close();
        return resultado;
    }

    public void setEntrenadoresDeRuta(String nombreJuego, String nombreRuta, int entrenadoresDerrotados) throws SQLException, ClassNotFoundException {
        PreparedStatement statement = BDConnector.getInstance().getConnection().prepareStatement("UPDATE Ruta SET entrenadoresDerrotados = ? WHERE nombreJuego=? AND nombreRuta=?");
        statement.setInt(1, entrenadoresDerrotados);
        statement.setString(2, nombreJuego);
        statement.setString(3, nombreRuta);
        statement.execute();

        statement.close();
    }
}
