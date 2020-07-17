package com.andysierra.culebrita.modelos;

import android.util.Log;

import com.andysierra.culebrita.consts.Consts;
import java.util.ArrayList;
import java.util.Collections;

public class Serpiente
{
    private static final String TAG="Serpiente";
    public Consts.direccion direccionCabeza;
    public Consts.direccion direccionCola;
    public Consts.direccion direccionCabezaColision;
    public ArrayList<int[]> cuerpo;
    public int longitud;
    public int cabezaI;
    public int cabezaJ;
    public int colaI;
    public int colaJ;


    // CONSTRUCTOR: Inicializar los objetos
    public Serpiente(int cabezaI, int cabezaJ, int longitud, Consts.direccion direccionCabeza) {
        this.cabezaI = cabezaI;
        this.cabezaJ = cabezaJ;
        this.colaI = cabezaI;
        this.colaJ = cabezaJ;
        this.longitud = longitud;
        Juego.matriz[cabezaI][cabezaJ] = Consts.SERPIENTE_CABEZA;
        this.direccionCabeza = direccionCabeza;
        this.direccionCola = direccionCabeza;
        this.direccionCabezaColision = null;
        cuerpo= new ArrayList<>();
        agregarSegmentos(longitud);
    }


    private void agregarSegmentos(int longitud) {
        // Según la dirección, agrega un segmento
        for(int k=1; k<longitud; k++) {
            if(cuerpo.size()==0) {
                cuerpo.add(new int[]{cabezaI, cabezaJ+k, Consts.direccion.IZQUIERDA.toInt()});
                colaJ++;
            }
            else {
                if(cuerpo.get(cuerpo.size()-1)[2] == Consts.direccion.ARRIBA.toInt()) {
                    if(colaI+1 < Consts.FILAS) colaI++;     // QUE PASA SI NO SE PUEDE AGREGAR?
                    cuerpo.add(new int[]{colaI, colaJ, Consts.direccion.ARRIBA.toInt()});
                }
                else if(cuerpo.get(cuerpo.size()-1)[2] == Consts.direccion.ABAJO.toInt()) {
                    if(colaI-1 >= 0) colaI--;               // QUE PASA SI NO SE PUEDE AGREGAR?
                    cuerpo.add(new int[]{colaI, colaJ, Consts.direccion.ABAJO.toInt()});
                }
                else if(cuerpo.get(cuerpo.size()-1)[2] == Consts.direccion.DERECHA.toInt()) {
                    if(colaJ-1 >= 0) colaJ--;               // QUE PASA SI NO SE PUEDE AGREGAR?
                    cuerpo.add(new int[]{colaI, colaJ, Consts.direccion.DERECHA.toInt()});
                }
                else if(cuerpo.get(cuerpo.size()-1)[2] == Consts.direccion.IZQUIERDA.toInt()) {
                    if(colaJ+1 < Consts.COLS) colaJ++;     // QUE PASA SI NO SE PUEDE AGREGAR?
                    cuerpo.add(new int[]{colaI, colaJ, Consts.direccion.IZQUIERDA.toInt()});
                }
            }
        }

        // Acomodar segmentos (Ahora cuerpo.get(0) es la cola de la serpiente)
        Collections.reverse(cuerpo);

        // Pintar los demás segmentos
        for(int i=1; i<cuerpo.size(); i++)
            Juego.matriz[cuerpo.get(i)[0]][cuerpo.get(i)[1]] = Consts.SERPIENTE;

        this.direccionCola =
                Consts.direccion.toDireccion(cuerpo.get(0)[2]);              // Dirección de la cola
        Juego.matriz[colaI][colaJ] = Consts.SERPIENTE_COLA;                        // Pintar la cola
    }

    public boolean mover(Consts.direccion direccion) {
        // No permitir reversa de serpiente
        if(direccion.toInt() == (this.direccionCabeza.toInt()*(-1)))
            direccion = Consts.direccion.toDireccion(this.direccionCabeza.toInt());

        // Actualizar la nueva posición y evitar una colisión
        int[] anterior = new int[]{cabezaI, cabezaJ,direccion.toInt()};
        if(       direccion == Consts.direccion.ARRIBA    && cabezaI > 0)
            if(!esColision(direccion)) cabezaI--; else return false;
        else if ( direccion == Consts.direccion.ABAJO     && cabezaI < Consts.FILAS-1)
            if(!esColision(direccion)) cabezaI++; else return false;
        else if ( direccion == Consts.direccion.DERECHA   && cabezaJ < Consts.COLS-1)
            if(!esColision(direccion)) cabezaJ++; else return false;
        else if ( direccion == Consts.direccion.IZQUIERDA && cabezaJ > 0)
            if(!esColision(direccion)) cabezaJ--; else return false;
        else return false;  // No se puede mover
        cuerpo.add(anterior); // Agregar la anterior posición al cuerpo

        // Pintar cabeza
        this.direccionCabeza= direccion;                                   // Dirección de la cabeza
        Juego.matriz[cabezaI][cabezaJ]  = Consts.SERPIENTE_CABEZA;         // Pintar la nueva cabeza

        // Limpiar
        Juego.matriz[cuerpo.get(0)[0]][cuerpo.get(0)[1]]=
                ((cuerpo.get(0)[0] %2==0&& cuerpo.get(0)[1] %2==0) ||
                 (cuerpo.get(0)[0] %2!=0&& cuerpo.get(0)[1] %2!=0))?
                        Consts.VACIO1 : Consts.VACIO2;
        cuerpo.remove(0); // Elimina la anterior posición de la serpiente

        // Nueva cola
        colaI = cuerpo.get(0)[0];
        colaJ = cuerpo.get(0)[1];

        // Pintar los demás segmentos
        for(int i=1; i<cuerpo.size(); i++)
            Juego.matriz[cuerpo.get(i)[0]][cuerpo.get(i)[1]] = Consts.SERPIENTE;

        // Pintar cola
        this.direccionCola =
               Consts.direccion.toDireccion(cuerpo.get(0)[2]);               // Dirección de la cola
        Juego.matriz[colaI][colaJ] = Consts.SERPIENTE_COLA;                        // Pintar la cola

        return true;
    }





    private boolean esColision(Consts.direccion direccion) {
        for(int[] p : cuerpo)
            if( (direccion == Consts.direccion.ARRIBA    && p[0] == cabezaI-1 && p[1] == cabezaJ)    ||
                (direccion == Consts.direccion.ABAJO     && p[0] == cabezaI+1 && p[1] == cabezaJ)    ||
                (direccion == Consts.direccion.DERECHA   && p[1] == cabezaJ+1 && p[0] == cabezaI)    ||
                (direccion == Consts.direccion.IZQUIERDA && p[1] == cabezaJ-1 && p[0] == cabezaI)) {
                this.direccionCabezaColision = direccion;
                return true;
            }
        return false;
    }
}