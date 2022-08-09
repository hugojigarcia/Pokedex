package com.example.pokedex.vista_juego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pokedex.BDConnector;
import com.example.pokedex.MainActivity;
import com.example.pokedex.R;

import java.sql.SQLException;
import java.util.ArrayList;

public class ListaJuegos extends AppCompatActivity {
    private Intent pantallaVisualizarJuego;
    private ListView listaJuegos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_juegos);
        initToolBar();
        initComponents();
        pantallaVisualizarJuego = new Intent(this, VisualizarJuego.class);
    }

    private void initToolBar(){
        setSupportActionBar(findViewById(R.id.barraHerramientas));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initComponents(){
        listaJuegos = (ListView)findViewById(R.id.lista_rutas);

        listaJuegos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    pantallaVisualizarJuego.putExtra("Juego", BDConnector.getInstance().leerJuego((String) listaJuegos.getItemAtPosition(i)));
                    startActivity(pantallaVisualizarJuego);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });
        consultarJuegos();
    }

    //Método para el botón consultar juegos
    public void onclick_consultarJuegos(View view){
        consultarJuegos();
    }

    private void consultarJuegos(){
        try {
            ArrayList<String> opciones = BDConnector.getInstance().leerListaJuegos();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, opciones);
            listaJuegos.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onclick_irAInicio(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onclick_back(View view){
        startActivity(new Intent(this, MainActivity.class));
    }
}