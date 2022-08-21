package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pokedex.bd.BDPokedex;

import java.sql.SQLException;
import java.util.ArrayList;

public class Pokedex extends AppCompatActivity {
    private Intent pantallaRuta;
    private ListView listaPokemons;
    private String nombreJuego;
    private EditText et_nombrePokemon, et_busqueda;
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

    private void initComponents() {
        et_busqueda = (EditText) findViewById(R.id.et_busqueda);
        et_nombrePokemon = (EditText) findViewById(R.id.et_nombrePokemon);
        cb_capturado = (CheckBox) findViewById(R.id.cb_capturado);
        initLista();
    }

    private void initLista(){
        listaPokemons = (ListView)findViewById(R.id.lista_pokemons);
        listaPokemons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nombrePokemon = listaPokemons.getItemAtPosition(i).toString().substring(6);
                actualizarPokemon(nombrePokemon, listaPokemons.isItemChecked(i));
            }
        });
        consultarPokemons();
    }

    private void consultarPokemons(){
        try {
            ArrayList<Pokemon> pokemons = BDPokedex.getInstance().leerPokedex(nombreJuego); //Modify
            ArrayList<String> opciones = convertirArrayNombres(pokemons);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, opciones);
            listaPokemons.setAdapter(adapter);
            checkACapturados(pokemons);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> convertirArrayNombres(ArrayList<Pokemon> pokemons){
        ArrayList<String> resultado = new ArrayList();
        for(int i=0; i<pokemons.size();i++){
            Pokemon pokemon = pokemons.get(i);
            resultado.add(String.format("%04d", pokemon.getNumero())+"- "+pokemon.getNombre());
        }
        return resultado;
    }

    private void checkACapturados(ArrayList<Pokemon> pokemons){
        for(int i=0; i<pokemons.size();i++) {
            if(pokemons.get(i).isCapturado()) listaPokemons.setItemChecked(i, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.checkbox_menu, menu);
        return true;
    }

    public void onclick_addPokemon(View view){
        String nombrePokemon = et_nombrePokemon.getText().toString();
        boolean capturado = cb_capturado.isChecked();
        actualizarPokemon(nombrePokemon, capturado);
        consultarPokemons();
    }

    private void actualizarPokemon(String nombrePokemon, boolean capturado){
        try {
            if(BDPokedex.getInstance().existePokemon(nombrePokemon)){
                BDPokedex.getInstance().addPokemonAPokedex(nombreJuego, nombrePokemon, capturado);
                et_nombrePokemon.setText("");
                cb_capturado.setChecked(false);
            } else {
                Toast.makeText(this, "El nombre del Pokemon no es correcto", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            //TODO lanzar error por pantalla
            Toast.makeText(this, throwables.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onclick_buscar(View view){
        String buscar = et_busqueda.getText().toString();
        try {
            ArrayList<Pokemon> pokemons = BDPokedex.getInstance().busquedaEnPokedex(nombreJuego, buscar);
            ArrayList<String> opciones = convertirArrayNombres(pokemons);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, opciones);
            listaPokemons.setAdapter(adapter);
            checkACapturados(pokemons);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onclick_irAInicio(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed()
    {
        cargar_pantalla_anterior();
    }

    public void onclick_back(View view){
        cargar_pantalla_anterior();
    }

    private void cargar_pantalla_anterior(){
        pantallaRuta.putExtra("Juego", nombreJuego);
        startActivity(pantallaRuta);
    }
}