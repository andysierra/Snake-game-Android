package com.andysierra.culebrita.modelos;

import android.util.Log;

import com.andysierra.culebrita.actividades.MainActivity;
import com.andysierra.culebrita.consts.Consts;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class Juego extends Observable
{
    private static final String TAG="Juego";
    private ArrayList<Observer> observadores;
    WeakReference<MainActivity> weakActivity;
    public static int[][] matriz;
    private int segundoRandom;
    public static boolean up;
    public static int   puntaje,
                        segundos,
                        minutos,
                        horas,
                        manzanasEnJuego,
                        bombasEnJuego,
                        manzanaCounter,
                        venenoEfecto;
    public boolean enPausa, esActivo;
    private Thread loop;
    public Serpiente[] serpientes;
    public Consts.direccion direccion;


    // CONSTRUCTOR: Inicializar los objetos
    public Juego(WeakReference<MainActivity> weakActivity, Serpiente... serpientes) {
        this.weakActivity = weakActivity;   // Referencia débil de MainActivity
        try {
            this.serpientes = serpientes;
            if(serpientes.length<1)
                throw new InstantiationException("Al instanciar un objeto juego, debes pasar como " +
                        "parámetro al menos una serpiente");
        }
        catch (InstantiationException i) {
            Log.e(TAG, i.getMessage(), i);
            ((MainActivity)weakActivity.get()).salir();
        }
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

        // reiniciar valores del juego
        Juego.puntaje = 0;
        Juego.segundos = 0;
        Juego.minutos = 0;
        Juego.horas = 0;
        Juego.manzanasEnJuego = 0;
        Juego.bombasEnJuego = 0;
        Juego.manzanaCounter = 0;
        Juego.venenoEfecto = 1;
        segundoRandom = ((int)(Math.random()*60));
        Log.println(Log.ASSERT, TAG, "iniciarJuego: segundo random inicial es: "+segundoRandom);

        // reiniciar dirección
        this.direccion = Consts.direccion.IZQUIERDA;

        // reiniciar serpiente
        serpientes[0].reiniciarSerpiente();

        // Colocar serpiente
        Juego.matriz[serpientes[0].cabezaI][serpientes[0].cabezaJ] = Consts.SERPIENTE_CABEZA;
        serpientes[0].agregarSegmentos();

        // ejecutar juego
        if( ((MainActivity)weakActivity.get()) != null &&
            !((MainActivity)weakActivity.get()).isFinishing() ) { this.looper(); }
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
    private void looper() {
        esActivo = true;
        enPausa  = false;
        if(loop == null) {
            loop = new Thread(new Runnable()    // Hilo del loop
            {
                long movimientoSerpiente;    // Velocidad de serpiente
                long tiempo;                 // Segundero

                @Override
                public void run() {
                    movimientoSerpiente = System.currentTimeMillis();    // Iniciar fps
                    tiempo              = System.currentTimeMillis();    // Iniciar el tiempo

                    while (esActivo) {                      // Mientras el juego esté activo
                        if(!enPausa) {                      // Si el juego no está en Pausa

                            // Ejecute a una velocidad tiempo  (Movimiento serpiente)
                            if(System.currentTimeMillis()- movimientoSerpiente > Consts.TIEMPO_SERPIENTE) {
                                ejecutar(Consts.EJECUTE_ACTUALIZAR_TABLERO);            // refrescar pantalla
                                ejecutar(Consts.EJECUTE_ACTUALIZAR_PUNTAJE);
                                movimientoSerpiente= System.currentTimeMillis(); // actualizar el tiempo
                            }

                            // Obtenga el tiempo transcurrido (requerimiento)
                            if(System.currentTimeMillis()- tiempo > Consts.SEGUNDO) {
                                ejecutar(Consts.EJECUTE_TEMPORIZADOR);              // Actualizar Tiempo
                                ejecutar(Consts.EJECUTE_ACTUALIZAR_TXFTIEMPO);
                                if(Juego.manzanasEnJuego<1)
                                    ejecutar(Consts.EJECUTE_SPAWN_MANZANA);
                                if(segundoRandom == segundos)
                                    ejecutar(Consts.EJECUTE_SPAWN_BOMBA);
                                tiempo= System.currentTimeMillis();
                            }
                        }
                    }
                }
            },"loop_del_juego");
            loop.start();   // Iniciar el loop
        }
    }




    private synchronized void ejecutar(int operacion) {

        // EJECUTAR MOVIMIENTO DE SERPIENTE
        if(operacion == Consts.EJECUTE_ACTUALIZAR_TABLERO) {
            Juego.up = !Juego.up;

            ((MainActivity)weakActivity.get()).runOnUiThread(new Runnable()
            {
                @Override
                public void run() {             // Notificar en el Thread principal

                    // Si la cabeza no se mueve, muestre que se estrelló y pause el juego,
                    // o sino siga actualizando el tablero.
                    if(!serpientes[0].mover(direccion)) {
                        Juego.matriz[serpientes[0].cabezaI][serpientes[0].cabezaJ] =
                                Consts.SERPIENTE_CABEZA_CRASH;
                        enPausa = true;
                        notifyObservers(new Object[]{
                            Consts.EJECUTE_ACTUALIZAR_HINT,
                            Consts.GAME_OVER
                        });
                    }

                    if(serpientes[0].siguiente_nivel) {
                        iniciarJuego();
                    }

                    notifyObservers(new Object[]{
                            Consts.EJECUTE_ACTUALIZAR_TABLERO,
                            matriz,
                            serpientes[0].cuerpo,
                            serpientes[0].direccionCabeza,
                            serpientes[0].direccionCola,
                            serpientes[0].direccionCabezaColision
                    });
                }
            });
        }

        else if(operacion == Consts.EJECUTE_ACTUALIZAR_PUNTAJE) {
            ((MainActivity)weakActivity.get()).runOnUiThread(new Runnable()
            {
                @Override
                public void run() {
                    notifyObservers(new Object[]{
                            Consts.EJECUTE_ACTUALIZAR_PUNTAJE,
                            Juego.puntaje
                    });
                }
            });
        }

        // TEMPORIZADOR
        else if(operacion == Consts.EJECUTE_TEMPORIZADOR) {
            segundos++;
            if(segundos>59) {
                minutos++;
                segundos=0;
                segundoRandom = ((int)(Math.random()*60));
                Log.println(Log.ASSERT, TAG, "ejecutar: segundo random es: "+segundoRandom);
            }
            if(minutos>59) {
                horas++;
                minutos=0;
            }
            if(horas>24) horas=0;
        }

        else if(operacion == Consts.EJECUTE_ACTUALIZAR_TXFTIEMPO) {
            ((MainActivity)weakActivity.get()).runOnUiThread(new Runnable()
            {
                @Override
                public void run() {
                    notifyObservers(new Object[]{
                            Consts.EJECUTE_ACTUALIZAR_TXFTIEMPO,
                            (horas>0)?
                                    ""+horas+":"+((minutos<10)?"0"+minutos:minutos)+":"+((segundos<10)?"0"+segundos:segundos):
                                    ((minutos<10)?"0"+minutos:minutos)+":"+((segundos<10)?"0"+segundos:segundos)
                    });
                }
            });
        }

        // SPAWN DE MANZANAS
        else if(operacion == Consts.EJECUTE_SPAWN_MANZANA) {

            if(Juego.puntaje < Consts.SIGUIENTE_NIVEL) {
                // Si la serpiente está envenenada, no puede comer manzanas
                if(venenoEfecto>1) venenoEfecto--;

                // Aparecer manzana roja o manzana verde
                if(segundos%venenoEfecto==0) {
                    int[] coords = this.getCeldaVacia();
                    Juego.matriz[coords[0]][coords[1]] =
                            (manzanaCounter>0 && manzanaCounter%7==0) ?
                                    Consts.MANZANA_VERDE : Consts.MANZANA;
                    Juego.manzanasEnJuego++;
                }
            }
            else {
                int[] coords = this.getCeldaVacia();
                Juego.matriz[coords[0]][coords[1]] = Consts.MANZANA_DORADA;
            }
        }

        // SPAWN DE BOMBAS
        else if(operacion == Consts.EJECUTE_SPAWN_BOMBA) {
            if(bombasEnJuego==0) {
                for(int i=0; i<(Consts.FILAS*Consts.COLS)/7; i++) {
                    int[] coords = this.getCeldaVacia();
                    Juego.matriz[coords[0]][coords[1]] = Consts.BOMBA;
                    bombasEnJuego++;
                }
            }

            Log.println(Log.ASSERT, TAG, "ejecutar: BOMBAS EN JUEGO "+bombasEnJuego);

            new Timer().schedule(new TimerTask()
            {
                @Override
                public void run() {
                    bombasEnJuego = 0;
                    for(int i=0; i<Consts.FILAS; i++)
                        for(int j=0; j<Consts.COLS; j++)
                            if(matriz[i][j] == Consts.BOMBA)
                                matriz[i][j] = ((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0))?
                                        Consts.VACIO1 : Consts.VACIO2;
                }
            }, 5000);
        }
    }


    @Override
    public synchronized void addObserver(Observer o) { observadores.add(o); }

    @Override
    public void notifyObservers(Object arg) { for(Observer o : observadores) o.update(this, arg); }
}