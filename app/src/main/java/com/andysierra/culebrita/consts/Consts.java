package com.andysierra.culebrita.consts;

public class Consts
{
    // Tablero y Lógica
    public static final int FILAS               = 15;
    public static final int COLS                = 10;
    public static final int SIGUIENTE_NIVEL     = 200; // default 200
    public static final int DIFICULTAD_BOMBAS   = 3;  // default 3

    // Medidas de tiempo y FPS
    public static final int TIEMPO_SERPIENTE            = 350;          // Velocidad de serpiente, defaults 150, 250, 350
    public static final int SEGUNDO                     = 1000;         // segundero
    public static final int TIEMPO_MANZANA              = 1000;         // aparición de manzanas

    public static final int EJECUTE_ACTUALIZAR_TABLERO      = 1000;         // id serpiente
    public static final int EJECUTE_ACTUALIZAR_PUNTAJE      = 2000;         // id puntaje
    public static final int EJECUTE_ACTUALIZAR_TXFTIEMPO    = 3000;         // id reloj
    public static final int EJECUTE_ACTUALIZAR_HINT         = 4000;         // id hint
    public static final int EJECUTE_ACTUALIZAR_MENSAJE      = 5000;         // id mensaje
    public static final int EJECUTE_TEMPORIZADOR            = 6000;         // id temporizador
    public static final int EJECUTE_SPAWN_MANZANA           = 7000;         // id spawn manzanas
    public static final int EJECUTE_SPAWN_BOMBA             = 8000;         // id spawn bomba


    // Serpiente
    public static enum direccion{
        ARRIBA(1), ABAJO(-1), DERECHA(2), IZQUIERDA(-2);
        private int orden;
        private direccion(int orden) { this.orden = orden; }
        public int toInt() { return this.orden; }
        public static Consts.direccion toDireccion(int orden) {
            if(orden==1) return direccion.ARRIBA;
            else if(orden==-1) return direccion.ABAJO;
            else if(orden==2) return direccion.DERECHA;
            else if(orden==-2) return direccion.IZQUIERDA;
            else return null;
        }
    }
    public static final int POSICION_INICIAL_I                  = Consts.FILAS/2;
    public static final int POSICION_INICIAL_J                  = Consts.COLS-4;
    public static final int LONGITUD_INICIAL                    = 4;
    public static final Consts.direccion ORIENTACION_INICIAL    = direccion.IZQUIERDA;

    // Mensajes
    public static final String BOMBA_BUENA      = "Bomba: No te quitará puntos por ahora :)";
    public static final String BOMBA_MALA       = "Bomba: -10 puntos";
    public static final String GAME_OVER        = "Game Over ¯\\_(ツ)_/¯";

    // Objetos del juego
    public static final int VACIO1                      = 10;
    public static final int VACIO2                      = 20;
    public static final int SERPIENTE                   = 30;
    public static final int SERPIENTE_CABEZA            = 31;
    public static final int SERPIENTE_CABEZA_CRASH      = 32;
    public static final int SERPIENTE_COLA              = 33;
    public static final int MANZANA                     = 40;
    public static final int MANZANA_VERDE               = 41;
    public static final int MANZANA_DORADA              = 42;
    public static final int BOMBA                       = 43;
    public static final int VENENO                      = 44;
    public static final int AREA_SEGURA                 = 45;
    public static final int MAPACHE                     = 50;   // Primer   jefe
    public static final int ZORRO                       = 51;   // Segundo  Jefe
    public static final int COYOTE                      = 52;   // Tercer   Jefe
    public static final int MANGOSTA                    = 53;   // Último   Jefe
}