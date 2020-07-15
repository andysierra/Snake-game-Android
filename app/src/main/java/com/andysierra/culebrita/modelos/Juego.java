package com.andysierra.culebrita.modelos;

import android.util.Log;

import com.andysierra.culebrita.actividades.MainActivity;
import com.andysierra.culebrita.consts.Consts;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Juego extends Observable
{
    private static final String TAG="Juego";
    private ArrayList<Observer> observadores;
    WeakReference<MainActivity> weakActivity;
    public static int[][] matriz;
    public int puntaje;
    public boolean enPausa, esActivo;
    private Thread loop;
    public Serpiente serpiente;
    public Consts.direccion direccion;


    // CONSTRUCTOR: Inicializar los objetos
    public Juego(WeakReference<MainActivity> weakActivity) {
        this.weakActivity = weakActivity;   // Referencia débil de MainActivity
        observadores = new ArrayList<>();   // Mis observadores
    }




    // CONSTRUCTOR: Inicializar los objetos
    public void iniciarJuego() {
        Juego.matriz = new int[Consts.FILAS][Consts.COLS];

        // iniciar backgrund ajedrezao
        for(int i=0; i<Consts.FILAS; i++)
            for(int j=0; j<Consts.COLS; j++)
                Juego.matriz[i][j] = ((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0))?
                        Consts.VACIO1 : Consts.VACIO2;

        // crear serpiente
        serpiente = new Serpiente(Consts.FILAS/2, Consts.COLS-3, Consts.direccion.IZQUIERDA);
        serpiente.crecer(serpiente, 2);

        // reiniciar puntaje
        this.puntaje = 0;

        // reiniciar dirección
        this.direccion = Consts.direccion.IZQUIERDA;

        // ejecutar juego
        if( ((MainActivity)weakActivity.get()) != null &&
            !((MainActivity)weakActivity.get()).isFinishing() &&
            (loop==null || !loop.isAlive()) ) { this.loop(); }
    }





    // Loop principal del juego
    private void loop() {
        esActivo = true;
        enPausa  = false;
        loop = new Thread(new Runnable()    // Hilo del loop
        {
            long actual;
            @Override
            public void run() {
                actual = System.currentTimeMillis();    // Iniciar el tiempo actual
                while (esActivo) {                      // Mientras el juego esté activo
                    if(!enPausa) {                      // Si el juego no está en Pausa

                        // Ejecute a una velocidad/dificultad FACIL, MEDIO, DIFICIL, CRACK
                        if(System.currentTimeMillis()-actual > Consts.DIFICULTAD_FACIL) {

                            // Notificar en el Thread principal
                            ((MainActivity)weakActivity.get()).runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run() {

                                    Log.println(Log.ASSERT, TAG, "run: "+
                                            (serpiente.mover(serpiente, direccion) ?"Moviendo":"No moviendo")+
                                            " | "+serpiente.i+","+serpiente.j+" hacia "+direccion);
                                    Log.println(Log.ASSERT, TAG, "run: *");


                                    notifyObservers(matriz);    // if serpiente mover -> notify (cambiar a esto)
                                }
                            });

                            actual = System.currentTimeMillis();    // actualizar el tiempo actual
                        }
                    }
                }
            }
        },"loop_del_juego");
        loop.start();   // Iniciar el loop
    }





    @Override
    public synchronized void addObserver(Observer o) { this.observadores.add(o); }

    @Override
    public void notifyObservers(Object arg) {
        ArrayList<Object> args = new ArrayList<>();
        args.add(arg);

        for(Observer o : this.observadores) o.update(this, args);
    }
}
