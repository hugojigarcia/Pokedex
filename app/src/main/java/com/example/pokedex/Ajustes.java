package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.example.pokedex.R;

public class Ajustes extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private Switch switch_modoOscuro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        initToolBar();
        initComponents();
    }

    private void initToolBar(){
        setSupportActionBar(findViewById(R.id.barraHerramientas));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initComponents(){
        editor = getSharedPreferences("ajustes", Context.MODE_PRIVATE).edit();
        switch_modoOscuro = (Switch) findViewById(R.id.switch_modoOscuro);
        switch_modoOscuro.setChecked(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES);
    }

    public void onclick_switchModoOscuro(View view){
        if(switch_modoOscuro.isChecked()) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        editor.putInt("modoOscuro", AppCompatDelegate.getDefaultNightMode()).commit();
    }

    public void onclick_irAInicio(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onclick_back(View view){
        startActivity(new Intent(this, MainActivity.class));
    }
}