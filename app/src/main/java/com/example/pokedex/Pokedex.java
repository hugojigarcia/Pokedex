package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class Pokedex extends AppCompatActivity {
    private Intent pantallaRuta;
    private ListView listaPokemons;
    private String nombreJuego;
    private EditText et_nombrePokemon, et_numero;
    private CheckBox cb_capturado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);
        recoverData();
        initToolBar();
        initComponents();
        pantallaRuta = new Intent(this, Ruta.class);
    }

    private void recoverData(){
        nombreJuego = getIntent().getStringExtra("Juego");
    }

    private void initToolBar(){
        setSupportActionBar(findViewById(R.id.barraHerramientas));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initComponents(){
        et_nombrePokemon = (EditText) findViewById(R.id.et_nombrePokemon);
        et_numero = (EditText) findViewById(R.id.et_numero);
        cb_capturado = (CheckBox) findViewById(R.id.cb_capturado);
        initLista();
    }

    private void initLista(){
        listaPokemons = (ListView)findViewById(R.id.lista_pokemons);
        consultarPokemons();
    }

    private void consultarPokemons(){
        try {
            ArrayList<String> opciones = BDConnector.getInstance().leerListaPokemons(nombreJuego); //Modify
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, opciones);
            listaPokemons.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onclick_addPokemon(View view){
        String nombrePokemon = et_nombrePokemon.getText().toString();
        et_nombrePokemon.setText("");
        String textoNumero = et_numero.getText().toString().trim();
        int numero = "".equals(textoNumero)?null:Integer.parseInt(textoNumero);
        et_numero.setText("");
        boolean capturado = cb_capturado.isChecked();
        cb_capturado.setText("");

        try {
            BDConnector.getInstance().addPokemon(nombreJuego, nombrePokemon, numero, capturado);
        } catch (SQLException throwables) {
            //TODO lanzar error por pantalla
            Toast.makeText(this, throwables.getMessage(), Toast.LENGTH_LONG).show();
        }

        consultarPokemons();
    }

    public void onclick_irAInicio(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onclick_back(View view){
        pantallaRuta.putExtra("Juego", nombreJuego);
        startActivity(pantallaRuta);
    } //Modify
}