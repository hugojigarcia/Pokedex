package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pokedex.bd.BDJuego;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Intent pantallaRuta, pantallaAjustes;
    private ListView listaJuegos;
    private EditText et_nombreJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conectarBD();
        initToolBar();
        initComponents();
        setAjustesGuardados();

        pantallaRuta = new Intent(this, Ruta.class);
        pantallaAjustes = new Intent(this, Ajustes.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            BDConnector.getInstance().closeConnection();
        } catch (SQLException ignored) { }
    }

    private void initToolBar(){
        setSupportActionBar(findViewById(R.id.barraHerramientas));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initComponents(){
        et_nombreJuego = (EditText) findViewById(R.id.et_nombrePokemon);
        initLista();
    }

    private void initLista(){
        listaJuegos = (ListView)findViewById(R.id.lista_rutas);

        listaJuegos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pantallaRuta.putExtra("Juego", (String) listaJuegos.getItemAtPosition(i));
                startActivity(pantallaRuta);
            }
        });
        consultarJuegos();
    }

    private void consultarJuegos(){
        try {
            ArrayList<String> opciones = BDJuego.getInstance().leerListaJuegos(); //Modify
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, opciones);
            listaJuegos.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void conectarBD(){
        try {
            BDConnector.connect(this.getResources().openRawResource(R.raw.config));
        } catch (Exception e) {
            //TODO lanzar error por pantalla
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setAjustesGuardados(){
        SharedPreferences archivoAjustes = getSharedPreferences("ajustes", Context.MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode( archivoAjustes.getInt("modoOscuro", AppCompatDelegate.getDefaultNightMode()) );
    }

    public void onclick_irAVentanaAjustes(View view){
        startActivity(pantallaAjustes);
    }

    public void onclick_addJuego(View view){
        String nombreJuego = et_nombreJuego.getText().toString();
        et_nombreJuego.setText("");

        try {
            BDJuego.getInstance().addJuego(nombreJuego);
        } catch (SQLException | ClassNotFoundException throwables) {
            //TODO lanzar error por pantalla
            Toast.makeText(this, throwables.getMessage(), Toast.LENGTH_LONG).show();
        }

        consultarJuegos();
    }
}