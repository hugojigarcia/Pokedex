package com.example.pokedex.modelo_juego;

import com.example.pokedex.BDConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JuegoModel implements IJuegoModel{
    private Connection connection;

    public JuegoModel() throws SQLException, ClassNotFoundException {
        connection = BDConnector.getInstance().getConnection();
    }

    @Override
    public Juego getJuegoPorId(int id) {
        return null;
    }

    @Override
    public Juego getJuegoPorNombres(String nombres) {
        return null;
    }

    @Override
    public List<Juego> getJuegos() {
        return null;
    }

    @Override
    public List<String> getNombresJuegos() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT nombres FROM Juego;");

            ArrayList<String> resultado = new ArrayList();
            while(resultSet.next()) {
                resultado.add(resultSet.getString(1));
            }

            statement.close();
            return resultado;
        } catch (SQLException throwables) {
            return new ArrayList<>();
        }
    }

    @Override
    public int insertar(Juego juego) {
        return 0;
    }

    @Override
    public int modificar(Juego juego) {
        return 0;
    }

    @Override
    public int eliminar(Juego juego) {
        return 0;
    }

    @Override
    public int eliminar(List<Juego> juegos) {
        return 0;
    }
}
