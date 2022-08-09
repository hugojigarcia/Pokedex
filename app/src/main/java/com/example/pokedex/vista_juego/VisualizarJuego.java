package com.example.pokedex.vista_juego;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.pokedex.MainActivity;
import com.example.pokedex.R;
import com.example.pokedex.modelo_juego.Juego;

public class VisualizarJuego extends AppCompatActivity {
    private Juego juego;
    private TextView tv_nombres;
    private TextView tv_minPersonas, tv_maxPersonas;
    private TextView tv_numEquipos, tv_edadRecomendada;
    private TextView tv_descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_juego);
        initToolBar();
        initComponents();

    }

    private void initToolBar(){
        setSupportActionBar(findViewById(R.id.barraHerramientas));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initComponents(){
        juego = (Juego) getIntent().getSerializableExtra("Juego");

        tv_nombres = (TextView) findViewById(R.id.tv_nombres);
        tv_nombres.setText(juego.getNombres());

        tv_minPersonas = (TextView) findViewById(R.id.tv_minPersonas);
        tv_minPersonas.setText(""+juego.getMinPersonas());
        tv_maxPersonas = (TextView) findViewById(R.id.tv_maxPersonas);
        tv_maxPersonas.setText(""+juego.getMaxPersonas());

        tv_numEquipos = (TextView) findViewById(R.id.tv_numEquipos);
        tv_numEquipos.setText(""+juego.getNumEquipos());
        tv_edadRecomendada = (TextView) findViewById(R.id.tv_edadRecomendada);
        tv_edadRecomendada.setText(""+juego.getEdadRecomendada());

        tv_descripcion = (TextView) findViewById(R.id.tv_descripcion);
        tv_descripcion.setText(juego.getDescripcion());
    }

    public void onclick_irAInicio(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onclick_back(View view){
        startActivity(new Intent(this, ListaJuegos.class));
    }
}