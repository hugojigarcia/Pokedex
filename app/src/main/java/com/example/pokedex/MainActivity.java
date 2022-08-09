package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.pokedex.R;
import com.example.pokedex.vista_juego.ListaJuegos;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    private Intent pantallaListaJuegos, pantallaAjustes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        conectarBD();
        setUserID();
        initToolBar();
        initComponents();
        setAjustesGuardados();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            BDConnector.closeConnection();
        } catch (SQLException ignored) { }
    }

    private void setUserID(){
        try {
            Usuario.getInstance().setUserID( Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID) );
        } catch (Exception ignored) {}
    }

    private void initToolBar(){
        setSupportActionBar(findViewById(R.id.barraHerramientas));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initComponents(){
        pantallaListaJuegos = new Intent(this, ListaJuegos.class);
        pantallaAjustes = new Intent(this, Ajustes.class);
    }

    private void conectarBD(){
        try {
            BDConnector.connect(this.getResources().openRawResource(R.raw.config));
        } catch (Exception e) {
            //TODO lanzar error por pantalla
        }
    }

    private void setAjustesGuardados(){
        SharedPreferences archivoAjustes = getSharedPreferences("ajustes", Context.MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode( archivoAjustes.getInt("modoOscuro", AppCompatDelegate.getDefaultNightMode()) );
    }

    public void onclick_irAVentanaListaJuegos(View view){
        try {
            pantallaListaJuegos.putStringArrayListExtra("ListaJuegos",BDConnector.getInstance().leerListaJuegos());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        startActivity(pantallaListaJuegos);
    }

    public void onclick_irAVentanaAjustes(View view){
        startActivity(pantallaAjustes);
    }


}