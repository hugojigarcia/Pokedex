package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class Ruta extends AppCompatActivity {
    private Intent pantallaZona;
    private ListView listaRutas;
    private String nombreJuego;
    private EditText et_nombreRuta, et_totalEntrenadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);
        recoverData();
        initToolBar();
        initComponents();
        pantallaZona = new Intent(this, Zona.class);
    }

    private void recoverData(){
        nombreJuego = getIntent().getStringExtra("Juego");
    }

    private void initToolBar(){
        setSupportActionBar(findViewById(R.id.barraHerramientas));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initComponents(){
        et_nombreRuta = (EditText) findViewById(R.id.et_nombreRuta);
        et_totalEntrenadores = (EditText) findViewById(R.id.et_totalEntrenadores);
        initLista();
    }

    private void initLista(){
        listaRutas = (ListView)findViewById(R.id.lista_rutas);

        listaRutas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pantallaZona.putExtra("Juego", nombreJuego);
                pantallaZona.putExtra("Ruta", (String) listaRutas.getItemAtPosition(i)); //Modify
                startActivity(pantallaZona);
            }
        });
        consultarRutas();
    }

    private void consultarRutas(){
        try {
            ArrayList<String> opciones = BDConnector.getInstance().leerListaRutas(nombreJuego); //Modify
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, opciones);
            listaRutas.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onclick_addRuta(View view){
        String nombreRuta = et_nombreRuta.getText().toString();
        et_nombreRuta.setText("");
        int totalEntrenadores = Integer.parseInt(et_totalEntrenadores.getText().toString());
        et_totalEntrenadores.setText("");

        try {
            BDConnector.getInstance().addRuta(nombreJuego, nombreRuta, totalEntrenadores);
        } catch (SQLException throwables) {
            //TODO lanzar error por pantalla
            Toast.makeText(this, throwables.getMessage(), Toast.LENGTH_LONG).show();
        }

        consultarRutas();
    }

    public void onclick_irAInicio(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onclick_back(View view){
        startActivity(new Intent(this, MainActivity.class));
    } //Modify
}