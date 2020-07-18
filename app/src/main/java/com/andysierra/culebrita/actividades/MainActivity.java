package com.andysierra.culebrita.actividades;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.andysierra.culebrita.R;
import com.andysierra.culebrita.consts.Consts;
import com.andysierra.culebrita.control.Control;
import com.andysierra.culebrita.modelos.*;
import com.andysierra.culebrita.vistas.*;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG="MainActivity";
    Tablero     tablero;
    Info        info;
    Serpiente   serpiente;
    Juego       juego;
    Control     control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tablero     = new Tablero((LinearLayout)findViewById(R.id.linearLayout), this);
        info        = new Info(this);
        serpiente   = new Serpiente(Consts.POSICION_INICIAL_I,
                                    Consts.POSICION_INICIAL_J,
                                    Consts.LONGITUD_INICIAL,
                                    Consts.ORIENTACION_INICIAL);
        juego       = new Juego(new WeakReference<MainActivity>(this), serpiente);
        control     = new Control(this, tablero, info, juego, serpiente);
    }

    public void salir() {
        //tablero.clean();
        //info.clean();
        //serpiente.clean();
        //juego.clean();
        //control.clean();
        finishAndRemoveTask();
    }
}