package com.example.johnathan.vigilate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class Settings extends AppCompatActivity {
    Spinner idioma;
    Spinner sonido;
    String idiomas[] ={"Ingles","Español"};
    String sonidos[] ={"Ocean","Morning","Mystic"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        idioma =(Spinner) findViewById(R.id.idiomas);
        sonido =(Spinner) findViewById(R.id.tiposSonido);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,idiomas);
        ArrayAdapter<String> adaptador1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sonidos);

        idioma.setAdapter(adaptador);
        sonido.setAdapter(adaptador1);
    }
}
