package com.andysierra.culebrita.vistas;

import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.andysierra.culebrita.R;
import com.andysierra.culebrita.actividades.MainActivity;
import com.andysierra.culebrita.consts.Consts;
import com.andysierra.culebrita.modelos.Juego;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Tablero <T extends ViewGroup> implements Observer
{
    private static final String TAG="Tablero";
    private static int rotacion = 0;
    public GridLayout gridLayout;
    public static String hint;
    private int[][] matriz;
    private ArrayList<int[]> celdasRotables;
    private Consts.direccion direccionCabeza;
    private Consts.direccion direccionCola;
    private Consts.direccion direccionColision;
    public T contenedor;
    private MainActivity mainActivity;
    public int cellSize;


    // CONSTRUCTOR: Inicializar los objetos
    public Tablero(T contenedor, MainActivity mainActivity) {
        this.contenedor = contenedor;
        this.mainActivity = mainActivity;
        gridLayout = new GridLayout(this.mainActivity);
        gridLayout.setColumnCount(Consts.COLS);
        gridLayout.setRowCount(Consts.FILAS);
        matriz = null;
        celdasRotables = null;
        direccionCabeza = null;

        contenedor.addView(gridLayout);


    }



    // UPDATE: Dibuja el tablero de acuerdo a la matriz en Logica
    @Override
    public void update(Observable o, Object arg) {

        if(((int)((Object[])arg)[0]) == Consts.EJECUTE_ACTUALIZAR_TABLERO) {
            // Remover celdas viejas
            if(gridLayout.getChildCount()>0) gridLayout.removeAllViews();


            this.matriz             = ((int[][])((Object[])arg)[1]);
            this.celdasRotables     = ((ArrayList<int[]>)((Object[])arg)[2]);
            this.direccionCabeza    = ((Consts.direccion)((Object[])arg)[3]);
            this.direccionCola      = ((Consts.direccion)((Object[])arg)[4]);
            this.direccionColision  = ((Consts.direccion)((Object[])arg)[5]);

            ImageView celda;

            // Para cada celda:
            for(int i=0; i<Consts.FILAS; i++) {
                for(int j=0; j<Consts.COLS; j++) {

                    // Poner una Imageview en el gridLayout
                    celda = new ImageView(mainActivity);
                    gridLayout.addView(celda);

                    // Dibujar celda
                    switch (matriz[i][j]) {
                        case Consts.VACIO1:
                            celda.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_greenie));
                            break;

                        case Consts.VACIO2:
                            celda.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_greenie_alt));
                            break;

                        case Consts.SERPIENTE:
                            if((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0))
                                celda.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_serpiente));
                            else
                                celda.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_serpiente_alt));
                            this.rotarSegmento(i, j, celda);
                            break;

                        case Consts.SERPIENTE_CABEZA:
                            if((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0))
                                celda.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_serpiente_cabeza));
                            else
                                celda.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_serpiente_cabeza_alt));
                            this.rotarSegmento(celda, true);
                            break;

                        case Consts.SERPIENTE_COLA:
                            if((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0))
                                celda.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_serpiente_cola));
                            else
                                celda.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_serpiente_cola_alt));
                            this.rotarSegmento(celda, false);
                            break;

                        case Consts.SERPIENTE_CABEZA_CRASH:
                            if((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0))
                                celda.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_serpiente_cabeza_crash));
                            else
                                celda.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_serpiente_cabeza_crash_alt));
                            this.rotarSegmento(celda, true);
                            break;

                        case Consts.MANZANA:
                            if((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0))
                                celda.setImageDrawable(mainActivity.getDrawable((Juego.up)?
                                        R.drawable.manzana_up : R.drawable.manzana_down));
                            else
                                celda.setImageDrawable(mainActivity.getDrawable((Juego.up)?
                                        R.drawable.manzana_up_alt : R.drawable.manzana_down_alt));
                            break;

                        case Consts.MANZANA_VERDE:
                            if((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0))
                                celda.setImageDrawable(mainActivity.getDrawable((Juego.up)?
                                        R.drawable.manzana_verde_up : R.drawable.manzana_verde_down));
                            else
                                celda.setImageDrawable(mainActivity.getDrawable((Juego.up)?
                                        R.drawable.manzana_verde_up_alt : R.drawable.manzana_verde_down_alt));
                            break;

                        case Consts.MANZANA_DORADA:
                            if((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0))
                                celda.setImageDrawable(mainActivity.getDrawable((Juego.up)?
                                        R.drawable.manzana_dorada_up : R.drawable.manzana_dorada_down));
                            else
                                celda.setImageDrawable(mainActivity.getDrawable((Juego.up)?
                                        R.drawable.manzana_dorada_up_alt : R.drawable.manzana_dorada_down_alt));
                            break;

                        case Consts.BOMBA:
                            if((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0))
                                celda.setImageDrawable(mainActivity.getDrawable((Juego.up)?
                                        R.drawable.bomba_up : R.drawable.bomba_down));
                            else
                                celda.setImageDrawable(mainActivity.getDrawable((Juego.up)?
                                        R.drawable.bomba_up_alt : R.drawable.bomba_down_alt));
                            break;
                    }

                    // Tamaño de celda (Obtenido desde Control.java)
                    celda.getLayoutParams().width   = cellSize;
                    celda.getLayoutParams().height  = cellSize;
                }
            }
            Tablero.rotacion += 180;
            gridLayout.invalidate();
        }
    }



    private void rotarSegmento(int i, int j, ImageView celda) {
        for(int k=0; k<celdasRotables.size(); k++) {
            if(celdasRotables.get(k)[0]==i && celdasRotables.get(k)[1]==j){


                // ROTACIÓN DE LA CURVA
                if(k>0 && celdasRotables.get(k)[2] != celdasRotables.get(k-1)[2]) {
                    if((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0))
                        celda.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_serpiente_curva));
                    else
                        celda.setImageDrawable(mainActivity.getDrawable(R.drawable.ic_serpiente_curva_alt));

                    // Rotar los segmentos intermedios de la serpiente
                    if(celdasRotables.get(k)[2]==Consts.direccion.ARRIBA.toInt()) {
                        if(celdasRotables.get(k-1)[2]==Consts.direccion.IZQUIERDA.toInt()) celda.setRotation(180);
                        else celda.setRotationX(180);
                    }
                    if(celdasRotables.get(k)[2]==Consts.direccion.ABAJO.toInt()) {
                        if(celdasRotables.get(k-1)[2]==Consts.direccion.DERECHA.toInt()) celda.setRotation(0);
                        else celda.setRotationY(180);
                    }
                    if(celdasRotables.get(k)[2]==Consts.direccion.DERECHA.toInt()) {
                        if(celdasRotables.get(k-1)[2]==Consts.direccion.ARRIBA.toInt()) celda.setRotationY(180);
                        else {
                            celda.setRotation(270);
                            celda.setRotationX(180);
                        }
                    }
                    if(celdasRotables.get(k)[2]==Consts.direccion.IZQUIERDA.toInt()) {
                        if (celdasRotables.get(k-1)[2]!=Consts.direccion.ABAJO.toInt()) celda.setRotation(90);
                        celda.setRotationX(180);
                    }
                }
                else {
                    celda.setRotation(rotacion);

                    // Rotar los segmentos intermedios de la serpiente
                    if(celdasRotables.get(k)[2]==Consts.direccion.ARRIBA.toInt())
                        celda.setRotation(Tablero.rotacion+90);
                    if(celdasRotables.get(k)[2]==Consts.direccion.ABAJO.toInt())
                        celda.setRotation(Tablero.rotacion+270);
                    if(celdasRotables.get(k)[2]==Consts.direccion.DERECHA.toInt())
                        celda.setRotation(Tablero.rotacion);
                    if(celdasRotables.get(k)[2]==Consts.direccion.IZQUIERDA.toInt())
                        celda.setRotation(Tablero.rotacion);
                }
            }
        }
    }
    // Sobrecarga
    private void rotarSegmento(ImageView celda, boolean cabeza) {
        if(cabeza) {
            Consts.direccion aux = (direccionColision==null)? direccionCabeza : direccionColision;
            if(aux == Consts.direccion.ARRIBA) celda.setRotation(90);
            if(aux == Consts.direccion.ABAJO) celda.setRotation(270);
            if(aux == Consts.direccion.IZQUIERDA) celda.setRotation(0);
            if(aux == Consts.direccion.DERECHA) celda.setRotation(180);
        }
        else {
            if(direccionCola == Consts.direccion.ARRIBA) {
                celda.setRotation(90);
                celda.setRotationY(Tablero.rotacion);
            }
            if(direccionCola == Consts.direccion.ABAJO) {
                celda.setRotation(270);
                celda.setRotationY(Tablero.rotacion );
            }
            if(direccionCola == Consts.direccion.IZQUIERDA) celda.setRotationX(Tablero.rotacion);
            if(direccionCola == Consts.direccion.DERECHA) {
                celda.setRotation(180);
                celda.setRotationX(Tablero.rotacion+180);
            }
        }
    }
}
