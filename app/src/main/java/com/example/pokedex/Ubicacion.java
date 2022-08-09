package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class Ubicacion extends AppCompatActivity {
    private Intent pantallaPokemon, pantallaZona; //Modify
    private ListView listaUbicaciones;
    private String nombreJuego, nombreRuta, nombreZona;
    private EditText et_nombreUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        recoverData();
        initToolBar();
        initComponents();
        pantallaPokemon = new Intent(this, Pokemons.class); //Modify
        pantallaZona = new Intent(this, Zona.class); //Modify
    }

    private void recoverData(){ //Modify
        nombreJuego = getIntent().getStringExtra("Juego");
        nombreRuta = getIntent().getStringExtra("Ruta");
        nombreZona = getIntent().getStringExtra("Zona");
    }

    private void initToolBar(){
        setSupportActionBar(findViewById(R.id.barraHerramientas));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initComponents(){
        et_nombreUbicacion = (EditText) findViewById(R.id.et_nombrePokemon);
        initLista();
    }

    private void initLista(){
        listaUbicaciones = (ListView)findViewById(R.id.lista_rutas);

        listaUbicaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pantallaPokemon.putExtra("Juego", nombreJuego);
                pantallaPokemon.putExtra("Ruta", nombreRuta);
                pantallaPokemon.putExtra("Zona", nombreZona);
                pantallaPokemon.putExtra("Ubicacion", (String) listaUbicaciones.getItemAtPosition(i)); //Modify
                startActivity(pantallaPokemon);
            }
        });
        consultarUbicaciones();
    }

    private void consultarUbicaciones(){
        try {
            ArrayList<String> opciones = BDConnector.getInstance().leerListaUbicaciones(nombreJuego, nombreRuta, nombreZona); //TODO Modify
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, opciones);
            listaUbicaciones.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onclick_addUbicacion(View view){
        String nombreUbicacion = et_nombreUbicacion.getText().toString();
        et_nombreUbicacion.setText("");

        try {
            BDConnector.getInstance().addUbicacion(nombreJuego, nombreRuta, nombreZona, nombreUbicacion);
        } catch (SQLException throwables) {
            //TODO lanzar error por pantalla
            Toast.makeText(this, throwables.getMessage(), Toast.LENGTH_LONG).show();
        }

        consultarUbicaciones();
    }

    public void onclick_irAInicio(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onclick_back(View view){
        pantallaZona.putExtra("Juego", nombreJuego);
        pantallaZona.putExtra("Ruta", nombreRuta); //Modify
        startActivity(pantallaZona);
    } //Modify
}