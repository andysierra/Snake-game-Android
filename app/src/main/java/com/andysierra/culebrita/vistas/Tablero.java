package com.andysierra.culebrita.vistas;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import com.andysierra.culebrita.R;
import com.andysierra.culebrita.consts.Consts;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Tablero <T extends ViewGroup> implements Observer
{
    private static final String TAG="Tablero";
    public GridLayout gridLayout;
    public T contenedor;
    private Context context;
    public int cellSize;


    // CONSTRUCTOR: Inicializar los objetos
    public Tablero(T contenedor, final Context context) {
        this.contenedor = contenedor;
        this.context = context;
        gridLayout = new GridLayout(context);
        gridLayout.setColumnCount(Consts.COLS);
        gridLayout.setRowCount(Consts.FILAS);

        contenedor.addView(gridLayout);
    }



    // UPDATE: Dibuja el tablero de acuerdo a la matriz en Logica
    @Override
    public void update(Observable o, Object arg) {
        ArrayList<Object> argumentos = (ArrayList<Object>)arg;           // Obtener argumentos
        if(gridLayout.getChildCount()>0) gridLayout.removeAllViews();    // Remover viejas celdas

        // Para cada celda:
        for(int i=0; i<Consts.FILAS; i++) {
            for(int j=0; j<Consts.COLS; j++) {

                // Poner una Imageview en el gridLayout
                ImageView celda = new ImageView(context);
                gridLayout.addView(celda);

                // Dibujar celda
                switch (((int[][])argumentos.get(0))[i][j]) {
                    case Consts.VACIO1:
                        celda.setImageDrawable(context.getDrawable(R.drawable.ic_greenie));
                        break;
                    case Consts.VACIO2:
                        celda.setImageDrawable(context.getDrawable(R.drawable.ic_greenie_alt));
                        break;
                    case Consts.SERPIENTE_CABEZA:
                        celda.setImageDrawable(context.getDrawable(R.drawable.ic_serpiente_cabeza));
                        break;
                    case Consts.SERPIENTE_CABEZA_CRASH:
                        celda.setImageDrawable(context.getDrawable(R.drawable.ic_serpiente_cabeza_crash));
                        break;
                }

                // Tamaño de celda (Obtenido desde Control.java)
                celda.getLayoutParams().width   = cellSize;
                celda.getLayoutParams().height  = cellSize;
            }
        }
        gridLayout.invalidate();
        //contenedor.invalidate();
    }
}
