package com.andysierra.culebrita.consts;

public class Consts
{
    // Tablero y Lógica
    public static final int FILAS               = 15;
    public static final int COLS                = 10;
    public static final int TIEMPO1             = 300;          // Velocidad de serpiente
    public static final int TIEMPO2             = 1000;         // Tic para animación
    public static final int TIEMPO3             = 300;          // Tic para aparición de objetos

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
    public static final int AREA_SEGURA                 = 44;
    public static final int MAPACHE                     = 50;   // Primer   jefe
    public static final int ZORRO                       = 51;   // Segundo  Jefe
    public static final int COYOTE                      = 52;   // Tercer   Jefe
    public static final int MANGOSTA                    = 53;   // Último   Jefe
}