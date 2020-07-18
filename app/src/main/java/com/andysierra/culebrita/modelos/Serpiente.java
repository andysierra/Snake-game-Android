package com.andysierra.culebrita.modelos;

import android.util.Log;

import com.andysierra.culebrita.consts.Consts;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

public class Serpiente extends Observable
{
    private static final String TAG="Serpiente";
    public Consts.direccion direccionCabeza;
    public Consts.direccion direccionCola;
    public Consts.direccion direccionCabezaColision;
    public ArrayList<int[]> cuerpo;
    public ArrayList<Observer> observadores;
    public boolean siguiente_nivel;
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
        //Juego.matriz[cabezaI][cabezaJ] = Consts.SERPIENTE_CABEZA;
        this.direccionCabeza = direccionCabeza;
        this.direccionCola = direccionCabeza;
        this.direccionCabezaColision = null;
        cuerpo= new ArrayList<>();
        observadores= new ArrayList<>();
        siguiente_nivel = false;
    }


    public void agregarSegmentos() {
        // Según la dirección, agrega un segmento
        for(int k=1; k<this.longitud; k++) {
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


    public void reiniciarSerpiente() {
        this.cabezaI = Consts.POSICION_INICIAL_I;
        this.cabezaJ = Consts.POSICION_INICIAL_J;
        this.colaI = cabezaI;
        this.colaJ = cabezaJ;
        this.longitud = Consts.LONGITUD_INICIAL;
        this.direccionCabeza = Consts.ORIENTACION_INICIAL;
        this.direccionCola = this.direccionCabeza;
        for(int[] segmento : this.cuerpo)
            Juego.matriz[segmento[0]][segmento[1]] =
                    ((segmento[0]%2==0 && segmento[1]%2==0) || (segmento[0]%2!=0 && segmento[1]%2!=0))?
                            Consts.VACIO1 : Consts.VACIO2;
        this.cuerpo.clear();
        this.cuerpo = new ArrayList<>();
        this.colaI = this.cabezaI;
        this.colaJ = this.cabezaJ;
        this.siguiente_nivel = false;
    }



    public boolean mover(Consts.direccion direccion) {
        // No permitir reversa de serpiente
        if(direccion.toInt() == (this.direccionCabeza.toInt()*(-1)))
            direccion = Consts.direccion.toDireccion(this.direccionCabeza.toInt());

        // Dirección de la cabeza
        this.direccionCabeza= direccion;

        // Crecer?
        boolean crecer = true;

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

// SI EL MOVIMIENTO ES VÁLIDO:

        // Si come un objeto, altere el puntaje y el tamaño de la serpiente
        int crecimiento = comioManzana();
        if(crecimiento > 0) Juego.puntaje += crecimiento;
        else if(crecimiento < 0) {
            // Moche a la serpiente mientras su longitud esté entre 3 y la mitad del tablero
            if(cuerpo.size()>2) {
                for(int i=0; i<(crecimiento*(-1)); i++) {

                    // Limpiar
                    Juego.matriz[cuerpo.get(0)[0]][cuerpo.get(0)[1]]=
                            ((cuerpo.get(0)[0] %2==0&& cuerpo.get(0)[1] %2==0) ||
                                    (cuerpo.get(0)[0] %2!=0&& cuerpo.get(0)[1] %2!=0))?
                                    Consts.VACIO1 : Consts.VACIO2;

                    // Mochar
                    cuerpo.remove(0);
                }
            }
            crecer = false;
        }
        else if(crecimiento == 0) crecer = false;


        // Agregar la anterior posición al cuerpo
        cuerpo.add(anterior);

        // Pintar cabeza
        Juego.matriz[cabezaI][cabezaJ]  = Consts.SERPIENTE_CABEZA;         // Pintar la nueva cabeza

        // Limpiar
        Juego.matriz[cuerpo.get(0)[0]][cuerpo.get(0)[1]]=
                ((cuerpo.get(0)[0] %2==0&& cuerpo.get(0)[1] %2==0) ||
                 (cuerpo.get(0)[0] %2!=0&& cuerpo.get(0)[1] %2!=0))?
                        Consts.VACIO1 : Consts.VACIO2;

        // Elimina la anterior posición de la serpiente
        if(!crecer) cuerpo.remove(0);

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



    // Detecta si la serpiente se colisionará consigo misma
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




    // APARECER
    //      manzana roja    +1 pto
    //      manzana verde   +10 ptos
    //      manzana dorada  (siguiente nivel)
    //      veneno          23 segundos sin manzanas
    //      bomba           -10 ptos
    private int comioManzana() {
        if(Juego.matriz[this.cabezaI][this.cabezaJ] == Consts.MANZANA) {
            Juego.manzanasEnJuego--;
            Juego.manzanaCounter++;
            return 1;
        }

        else if(Juego.matriz[this.cabezaI][this.cabezaJ] == Consts.MANZANA_VERDE) {
            Juego.manzanasEnJuego--;
            Juego.manzanaCounter++;
            return 10;
        }

        else if(Juego.matriz[this.cabezaI][this.cabezaJ] == Consts.MANZANA_DORADA) {
            this.siguiente_nivel = true;
        }

        else if(Juego.matriz[this.cabezaI][this.cabezaJ] == Consts.BOMBA) {

            if(cuerpo.size()<(Consts.FILAS*Consts.COLS)/2) {
                // Si tiene puntaje, disminuya como castigo
                if(Juego.puntaje > 10) {
                    Juego.puntaje-=10;
                    notifyObservers(new Object[]{
                            Consts.EJECUTE_ACTUALIZAR_HINT,
                            Consts.BOMBA_MALA
                    });
                }
                else notifyObservers(new Object[]{
                            Consts.EJECUTE_ACTUALIZAR_HINT,
                            Consts.BOMBA_BUENA
                     });
            }
            else if(cuerpo.size()>(Consts.FILAS*Consts.COLS)/2) {
                // Si es muy largo o su puntaje es muy pequeño, no castigará
                notifyObservers(new Object[]{
                    Consts.EJECUTE_ACTUALIZAR_HINT,
                    Consts.BOMBA_BUENA
                });
            }


            return -2;
        }
        else if(Juego.matriz[this.cabezaI][this.cabezaJ] == Consts.VENENO) {
            Juego.venenoEfecto = 23;  // Número primo para demora en envenenamiento
        }
        return 0;
    }

    @Override
    public synchronized void addObserver(Observer o) { observadores.add(o); }

    @Override
    public void notifyObservers(Object arg) { for(Observer o : observadores) o.update(this, arg); }
}