package com.andysierra.culebrita.control;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import com.andysierra.culebrita.consts.Consts;
import com.andysierra.culebrita.modelos.Juego;
import com.andysierra.culebrita.vistas.Tablero;

import java.util.ArrayList;

public class Control implements ViewTreeObserver.OnGlobalLayoutListener,    // Controlar el layout
                                View.OnTouchListener                        // Controlar el touch
{
    private static final String TAG="Control";
    private Juego   juego;
    private Tablero tablero;
    private float   x1;
    private float   x2;
    private float   y1;
    private float   y2;



    // CONSTRUCTOR: Inicializar los objetos
    public Control(Juego juego, Tablero tablero) {
        this.juego   = juego;
        this.tablero = tablero;

        juego.addObserver(tablero);
        tablero.contenedor.getViewTreeObserver().addOnGlobalLayoutListener(this);
        tablero.contenedor.setOnTouchListener(this);
    }



    @Override
    public void onGlobalLayout() {
        tablero.contenedor.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        tablero.cellSize = tablero.contenedor.getWidth() / Consts.COLS;
        juego.iniciarJuego();
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            x1 = event.getX();
            y1 = event.getY();
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            x2 = event.getX();
            y2 = event.getY();

            float difX = x2-x1;
            float difY = y2-y1;
            if(Math.abs(difX) > Math.abs(difY))
                juego.direccion = (difX > 0)? Consts.direccion.DERECHA : Consts.direccion.IZQUIERDA;
            else
                juego.direccion = (difY > 0)? Consts.direccion.ABAJO : Consts.direccion.ARRIBA;
        }
        return true;
    }
}
