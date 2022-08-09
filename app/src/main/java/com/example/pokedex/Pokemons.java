package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class Pokemons extends AppCompatActivity {
    private Intent pantallaUbicacion;
    private ListView listaPokemons;
    private String nombreJuego, nombreRuta, nombreZona, nombreUbicacion;
    private EditText et_nombrePokemon, et_probabilidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemons);
        recoverData();
        initToolBar();
        initComponents();
        pantallaUbicacion = new Intent(this, Ubicacion.class);
    }

    private void recoverData(){
        nombreJuego = getIntent().getStringExtra("Juego");
        nombreRuta = getIntent().getStringExtra("Ruta");
        nombreZona = getIntent().getStringExtra("Zona");
        nombreUbicacion = getIntent().getStringExtra("Ubicacion");
    }

    private void initToolBar(){
        setSupportActionBar(findViewById(R.id.barraHerramientas));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initComponents(){
        et_nombrePokemon = (EditText) findViewById(R.id.et_nombrePokemon);
        et_probabilidad = (EditText) findViewById(R.id.et_probabilidad);
        initLista();
    }

    private void initLista(){
        listaPokemons = (ListView)findViewById(R.id.lista_pokemons);
        listaPokemons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nombrePokemon = listaPokemons.getItemAtPosition(i).toString().split(" ")[1];
                actualizarPokemon(nombrePokemon, listaPokemons.isItemChecked(i));
            }
        });
        consultarPokemons();
    }

    private void consultarPokemons(){
        try {
            ArrayList<Pokemon> pokemons = BDConnector.getInstance().leerListaPokemons(nombreJuego, nombreRuta, nombreZona, nombreUbicacion); //TODO Modify
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
            resultado.add(String.format("%04d", pokemon.getNumero())+"- "+pokemon.getNombre()+" "+pokemon.getProbabilidad()+"%");
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

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*int id = item.getItemId();
        if(id==R.id.item_done){
            String itemSelected = "Selected items: \n";
            for(int i=0; i<listaPokemons.getCount();i++) {
                if (listaPokemons.isItemChecked(i)) {
                    itemSelected += listaPokemons.getItemAtPosition(i) + "\n";
                }
            }
        }
        Toast.makeText(this, "Elegido " +item.getItemId(), Toast.LENGTH_SHORT).show();

        //return super.onOptionsItemSelected(item);
        return true;
    }*/

    public void onclick_addPokemon(View view){
        String nombrePokemon = et_nombrePokemon.getText().toString();
        int probabilidad = Integer.parseInt(et_probabilidad.getText().toString());

        try {
            BDConnector.getInstance().addPokemon(nombreJuego, nombreRuta, nombreZona, nombreUbicacion, nombrePokemon, probabilidad);
            et_nombrePokemon.setText("");
            et_probabilidad.setText("");
        } catch (SQLException throwables) {
            //TODO lanzar error por pantalla
            Toast.makeText(this, throwables.getMessage(), Toast.LENGTH_LONG).show();
        }

        consultarPokemons();
    }

    private void actualizarPokemon(String nombrePokemon, boolean capturado){
        try {
            if(BDConnector.getInstance().consultarPokemon(nombrePokemon)!=null){
                BDConnector.getInstance().addPokemonCapturado(nombreJuego, nombrePokemon, capturado);
            } else {
                Toast.makeText(this, "El nombre del Pokemon no es correcto", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException throwables) {
            //TODO lanzar error por pantalla
            Toast.makeText(this, throwables.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onclick_irAInicio(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onclick_back(View view){
        pantallaUbicacion.putExtra("Juego", nombreJuego);
        pantallaUbicacion.putExtra("Ruta", nombreRuta);
        pantallaUbicacion.putExtra("Zona", nombreZona);
        startActivity(pantallaUbicacion);
    } //Modify
}