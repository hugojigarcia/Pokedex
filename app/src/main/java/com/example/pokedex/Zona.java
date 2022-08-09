package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class Zona extends AppCompatActivity {
    private Intent pantallaUbicacion, pantallaRuta; //Modify
    private ListView listaZonas;
    private String nombreJuego, nombreRuta;
    private int entrenadoresDerrotados, totalEntrenadores;
    private EditText et_nombreZona;
    private TextView tv_entrenadoresRestantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zona);
        recoverData();
        initToolBar();
        initComponents();
        pantallaUbicacion = new Intent(this, Ubicacion.class); //Modify
        pantallaRuta = new Intent(this, Ruta.class); //Modify
    }

    private void recoverData(){ //Modify
        nombreJuego = getIntent().getStringExtra("Juego");
        nombreRuta = getIntent().getStringExtra("Ruta");
    }

    private void initToolBar(){
        setSupportActionBar(findViewById(R.id.barraHerramientas));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initComponents(){
        et_nombreZona = (EditText) findViewById(R.id.et_nombrePokemon);
        tv_entrenadoresRestantes = (TextView) findViewById(R.id.tv_entrenadoresRestantes);
        initLista();
        consultarNumEntrenadores();
    }

    private void initLista(){
        listaZonas = (ListView)findViewById(R.id.lista_rutas);

        listaZonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pantallaUbicacion.putExtra("Juego", nombreJuego);
                pantallaUbicacion.putExtra("Ruta", nombreRuta);
                pantallaUbicacion.putExtra("Zona", (String) listaZonas.getItemAtPosition(i)); //Modify
                startActivity(pantallaUbicacion);
            }
        });
        consultarZonas();
    }

    private void consultarZonas(){
        try {
            ArrayList<String> opciones = BDConnector.getInstance().leerListaZonas(nombreJuego, nombreRuta); //TODO Modify
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, opciones);
            listaZonas.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void consultarNumEntrenadores(){
        try {
            int[] entrenadores = BDConnector.getInstance().leerEntrenadoresDeRuta(nombreJuego, nombreRuta);
            this.entrenadoresDerrotados = entrenadores[0];
            this.totalEntrenadores = entrenadores[1];
            actualizarTextoEntrenadores();
        } catch (SQLException throwables) {
            //TODO lanzar error por pantalla
            Toast.makeText(this, throwables.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void actualizarTextoEntrenadores(){
        tv_entrenadoresRestantes.setText("Entrenadores derrotados: "+entrenadoresDerrotados+"/"+totalEntrenadores);
    }

    public void onclick_addZona(View view){
        String nombreZona = et_nombreZona.getText().toString();
        et_nombreZona.setText("");

        try {
            BDConnector.getInstance().addZona(nombreJuego, nombreRuta, nombreZona);
        } catch (SQLException throwables) {
            //TODO lanzar error por pantalla
            Toast.makeText(this, throwables.getMessage(), Toast.LENGTH_LONG).show();
        }

        consultarZonas();
    }

    public void onclick_disminuirEntrenadores(View view){
        if(entrenadoresDerrotados>0) {
            this.entrenadoresDerrotados--;
            try {
                BDConnector.getInstance().setEntrenadoresDeRuta(nombreJuego, nombreRuta, entrenadoresDerrotados);
            } catch (SQLException throwables) {
                //TODO lanzar error por pantalla
                Toast.makeText(this, throwables.getMessage(), Toast.LENGTH_LONG).show();
            }
            actualizarTextoEntrenadores();
        }
    }

    public void onclick_aumentarEntrenadores(View view){
        if(entrenadoresDerrotados<totalEntrenadores) {
            this.entrenadoresDerrotados++;
            try {
                BDConnector.getInstance().setEntrenadoresDeRuta(nombreJuego, nombreRuta, entrenadoresDerrotados);
            } catch (SQLException throwables) {
                //TODO lanzar error por pantalla
                Toast.makeText(this, throwables.getMessage(), Toast.LENGTH_LONG).show();
            }
            actualizarTextoEntrenadores();
        }
    }

    public void onclick_irAInicio(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onclick_back(View view){
        pantallaRuta.putExtra("Juego", nombreJuego);
        startActivity(pantallaRuta);
    } //Modify
}