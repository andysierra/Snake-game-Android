package com.andysierra.culebrita.actividades;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.andysierra.culebrita.R;
import com.andysierra.culebrita.control.Control;
import com.andysierra.culebrita.modelos.Juego;
import com.andysierra.culebrita.vistas.Tablero;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity
{
    Tablero tablero;
    Juego   juego;
    Control control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tablero = new Tablero((LinearLayout)findViewById(R.id.linearLayout), this);
        juego   = new Juego(new WeakReference<MainActivity>(this));
        control = new Control(juego, tablero);
    }
}