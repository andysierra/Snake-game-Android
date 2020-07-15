package com.andysierra.culebrita.modelos;

import android.util.Log;
import com.andysierra.culebrita.consts.Consts;

public class Serpiente
{
    private static final String TAG="Serpiente";
    public Serpiente next;
    public Consts.direccion direccion;
    public int i;
    public int j;

    public Serpiente(int i, int j, Consts.direccion direccion) {
        this.next = null;
        this.i = i;
        this.j = j;
        Log.println(Log.ASSERT, TAG, "Serpiente: CREANDO SERPIENTE: "+i+","+j);
        Juego.matriz[i][j] = Consts.SERPIENTE_CABEZA;
        this.direccion = direccion;
    }

    public boolean mover(Serpiente segmento, Consts.direccion direccion) {
        boolean sePuedeMover = true;

        // MOVER HACIA ARRIBA
        if(direccion == Consts.direccion.ARRIBA) {
            if(segmento.i > 0) {                                    // Si está dentro de los límites
                if(segmento.next==null && segmento.i+1 < Consts.FILAS)                    // Limpiar
                    limpiar(segmento.i+1, segmento.j);
                segmento.i--;                                                               // Mover
            }
            else sePuedeMover = false;                                              // No pudo mover
        }

        // MOVER HACIA ABAJO
        else if(direccion == Consts.direccion.ABAJO) {
            if(segmento.i < Consts.FILAS-1) {                       // Si está dentro de los límites
                if(segmento.next==null && segmento.i-1 > 0)                               // Limpiar
                    limpiar(segmento.i-1, segmento.j);
                segmento.i++;                                                               // Mover
            }
            else sePuedeMover = false;                                              // No pudo mover
        }

        // MOVER HACIA LA DERECHA
        else if(direccion == Consts.direccion.DERECHA) {
            if(segmento.j < Consts.COLS-1) {                        // Si está dentro de los límites
                if(segmento.next==null && segmento.j-1 > 0)                               // Limpiar
                    limpiar(segmento.i, segmento.j-1);
                segmento.j++;                                                               // Mover
            }
            else sePuedeMover = false;                                              // No pudo mover
        }

        // MOVER HACIA LA IZQUIERDA
        else if(direccion == Consts.direccion.IZQUIERDA) {
            if(segmento.j > 0) {                                    // Si está dentro de los límites
                if(segmento.next==null && segmento.j+1 < Consts.COLS)                     // Limpiar
                    limpiar(segmento.i, segmento.j+1);
                segmento.j--;                                                               // Mover
            }
            else sePuedeMover = false;                                              // No pudo mover
        }

        // Si se puede mover, mover los demás segmentos de forma recursiva
        if(sePuedeMover == true) {
            Juego.matriz[segmento.i][segmento.j] =  Consts.SERPIENTE_CABEZA;
            if(segmento.next != null) mover(segmento.next, direccion);
        }
        else Juego.matriz[segmento.i][segmento.j] = Consts.SERPIENTE_CABEZA_CRASH;

        return sePuedeMover;
    }




    // Limpia la anterior posición
    private void limpiar(int i, int j) {
        Juego.matriz[i][j] = ((i%2==0&&j%2==0)||(i%2!=0&&j%2!=0))? Consts.VACIO1 : Consts.VACIO2;
    }



    // Crecer agrega un segmento al final de la serpiente
    public void crecer(Serpiente segmento, int veces) {
        Serpiente cabeza = segmento;
        for(int k=0; k<veces; k++) {
            while(segmento.next != null) segmento = segmento.next;
            if(this.direccion == Consts.direccion.ARRIBA)
                segmento.next = new Serpiente(++segmento.i, segmento.j, segmento.direccion);
            else if(this.direccion == Consts.direccion.ABAJO)
                segmento.next = new Serpiente(--segmento.i, segmento.j, segmento.direccion);
            else if(this.direccion == Consts.direccion.DERECHA)
                segmento.next = new Serpiente(segmento.i, --segmento.j, segmento.direccion);
            else if(this.direccion == Consts.direccion.IZQUIERDA)
                segmento.next = new Serpiente(segmento.i, ++segmento.j, segmento.direccion);
            segmento = cabeza;
        }
    }
}
