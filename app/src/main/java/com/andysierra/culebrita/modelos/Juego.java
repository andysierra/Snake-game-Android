package com.andysierra.culebrita.modelos;

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
    public static boolean up;
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
        Juego.up = true;

        // iniciar backgrund ajedrezao
        for(int i=0; i<Consts.FILAS; i++)
            for(int j=0; j<Consts.COLS; j++)
                Juego.matriz[i][j] = ((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0))?
                        Consts.VACIO1 : Consts.VACIO2;

        // crear serpiente
        serpiente =
        new Serpiente(Consts.FILAS/2, Consts.COLS-7, 7, Consts.direccion.IZQUIERDA);

        // reiniciar puntaje
        this.puntaje = 0;

        // reiniciar dirección
        this.direccion = Consts.direccion.IZQUIERDA;

        // ejecutar juego
        if( ((MainActivity)weakActivity.get()) != null &&
            !((MainActivity)weakActivity.get()).isFinishing() &&
            (loop==null || !loop.isAlive()) ) { this.loop(); }
    }




    private int[] getCeldaVacia() {
        int i = (int)(Math.random()*Consts.FILAS);
        int j = (int)(Math.random()*Consts.COLS);

        while(Juego.matriz[i][j] != Consts.VACIO1 && Juego.matriz[i][j] != Consts.VACIO2) {
            i = (int)(Math.random()*Consts.FILAS);
            j = (int)(Math.random()*Consts.COLS);
        }
        return new int[]{i, j};
    }






    // Loop principal del juego
    private void loop() {
        esActivo = true;
        enPausa  = false;
        loop = new Thread(new Runnable()    // Hilo del loop
        {
            long    tiempo1,    // Velocidad de serpiente
                    tiempo2,    // Tic para animación
                    tiempo3;    // Tic para aparición de manzanas
            @Override
            public void run() {
                tiempo1= System.currentTimeMillis();    // Iniciar el tiempo1
                tiempo2= System.currentTimeMillis();    // Iniciar el tiempo2
                tiempo3= System.currentTimeMillis();    // Iniciar el tiempo2

                while (esActivo) {                      // Mientras el juego esté activo
                    if(!enPausa) {                      // Si el juego no está en Pausa


                        // Ejecute a una velocidad tiempo 1 (Movimiento serpiente)
                        if(System.currentTimeMillis()- tiempo1 > Consts.TIEMPO1) {
                            ejecutar(Consts.TIEMPO1);
                            tiempo1= System.currentTimeMillis();    // actualizar el tiempo1
                        }



                        // Ejecute a una velocidad tiempo2 (tic/tac de up)
                        if(System.currentTimeMillis()- tiempo2 > Consts.TIEMPO2) {
                            ejecutar(Consts.TIEMPO2);
                            tiempo2= System.currentTimeMillis();    // actualizar el tiempo2
                        }



                        // Ejecute a una velocidad tiempo3 (cada vez que se coloca una manzana)
                        if(System.currentTimeMillis()- tiempo3 > Consts.TIEMPO3) {
                            ejecutar(Consts.TIEMPO3);
                            tiempo3= System.currentTimeMillis();    // actualizar el tiempo2
                        }
                    }
                }
            }
        },"loop_del_juego");
        loop.start();   // Iniciar el loop
    }




    private synchronized void ejecutar(int operacion) {

        // EJECUTAR MOVIMIENTO DE SERPIENTE
        if (operacion == Consts.TIEMPO1) {
            ((MainActivity)weakActivity.get()).runOnUiThread(new Runnable()
            {
                @Override
                public void run() {             // Notificar en el Thread principal

                    // Si la cabeza no se mueve, muestre que se estrelló,
                    // o sino siga actualizando el tablero

                    if(!serpiente.mover(direccion)) {
                        Juego.matriz[serpiente.cabezaI][serpiente.cabezaJ] =
                                Consts.SERPIENTE_CABEZA_CRASH;
                        enPausa = true;
                    }

                    notifyObservers(new Object[]{
                            Consts.TIEMPO1,
                            matriz,
                            serpiente.cuerpo,
                            serpiente.direccionCabeza,
                            serpiente.direccionCola,
                            serpiente.direccionCabezaColision
                    });
                }
            });
        }


        // TIC DE ANIMACIÓN
        else if(operacion == Consts.TIEMPO2) {
            Juego.up = !Juego.up;
        }


        // EJECUTAR APARICIÓN DE PREMIOS (Manzanas)
        else if(operacion == Consts.TIEMPO3) {
            notifyObservers(new Object[]{
                    Consts.TIEMPO3,
                    getCeldaVacia()
            });
        }
    }


    @Override
    public synchronized void addObserver(Observer o) { observadores.add(o); }

    @Override
    public void notifyObservers(Object arg) { for(Observer o : observadores) o.update(this, arg); }
}