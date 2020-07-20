package com.andysierra.culebrita.control;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import com.andysierra.culebrita.actividades.MainActivity;
import com.andysierra.culebrita.consts.Consts;
import com.andysierra.culebrita.modelos.Juego;
import com.andysierra.culebrita.modelos.Serpiente;
import com.andysierra.culebrita.vistas.Info;
import com.andysierra.culebrita.vistas.Tablero;

public class Control implements ViewTreeObserver.OnGlobalLayoutListener,    // Controlar el layout
                                View.OnTouchListener,                       // Controlar el touch
                                View.OnClickListener
{
    private static final String TAG="Control";
    private Tablero     tablero;
    private Info        info;
    private Juego       juego;
    private Serpiente[] serpientes;
    private float   x1;
    private float   x2;
    private float   y1;
    private float   y2;



    // CONSTRUCTOR: Inicializar los objetos
    public Control(MainActivity mainActivity, Tablero tablero, Info info, Juego juego, Serpiente... serpientes) {
        this.tablero    = tablero;
        this.info       = info;
        this.serpientes = serpientes;
        this.juego      = juego;

        try {
            if(serpientes.length<1)
                throw new InstantiationException("Al instanciar un objeto control, debes pasar como " +
                        "parámetro al menos una serpiente");
        }
        catch (InstantiationException i) {
            Log.e(TAG, i.getMessage(), i);
            mainActivity.salir();
        }

        juego.addObserver(tablero);
        juego.addObserver(info);
        for(Serpiente s : serpientes) s.addObserver(info);
        tablero.contenedor.getViewTreeObserver().addOnGlobalLayoutListener(this);
        tablero.contenedor.setOnTouchListener(this);
        tablero.setOnClickListener(this);
    }


    public void iniciarJuego() {
        this.juego.prepararJuego();

        // Conteo
        new Thread(new Runnable()
        {
            @Override
            public void run() {
                long tiempo = System.currentTimeMillis();
                int segundos = 3;
                while(true) {
                    if((System.currentTimeMillis()-tiempo) > 1000) {
                        tablero.mensaje.setText(String.valueOf(segundos));
                        tiempo = System.currentTimeMillis();
                        if(segundos>0) segundos--; else break;
                    }
                }
                juego.enPausa = false;
                tablero.mensaje.setText("");
            }
        }).start();
    }


    // LISTENER NECESARIO PARA SABER LAS DIMENSIONES DE MIS VISTAS Y PODER DIBUJAR EL JUEGO
    @Override
    public void onGlobalLayout() {
        tablero.contenedor.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        tablero.cellSize = tablero.contenedor.getWidth() / Consts.COLS;
        this.iniciarJuego();
    }


    // LISTENER NECESARIO PARA EL TOUCH-SWIPE
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


    // LISTENER NECESARIO PARA LOS BOTONES DE JUGAR DE NUEVO Y SALIR
    @Override
    public void onClick(View v) {
        if(v.getTag() == this.tablero.btnReiniciar.getTag()) {
            this.iniciarJuego();
            this.tablero.mensaje.setText("Preparados");
            this.tablero.btnReiniciar.setVisibility(View.INVISIBLE);
            this.tablero.btnSalir.setVisibility(View.INVISIBLE);
        }
        else if(v.getTag() == this.tablero.btnSalir.getTag()) {
            Log.println(Log.ASSERT, TAG, "onClick: SALIENDO DEL JUEGO");
        }
    }
}