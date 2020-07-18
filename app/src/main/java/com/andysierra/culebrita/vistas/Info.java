package com.andysierra.culebrita.vistas;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.andysierra.culebrita.R;
import com.andysierra.culebrita.actividades.MainActivity;
import com.andysierra.culebrita.consts.Consts;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class Info implements Observer
{
    private static final String TAG="Info";
    private MainActivity mainActivity;

    public Info(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void actualizaTiempo(String tiempo) {
        ((TextView)mainActivity.findViewById(R.id.txftiempo)).setText(tiempo);
    }

    public void actualizaPuntaje(int puntaje) {
        ((TextView)mainActivity.findViewById(R.id.txfManzanas)).setText(String.valueOf(puntaje));
    }

    @Override
    public void update(Observable o, Object arg) {
        int operacion = ((int)((Object[])arg)[0]);
        final ImageView imgHint = (ImageView) this.mainActivity.findViewById(R.id.imghint);
        final TextView  txfHint = (TextView) this.mainActivity.findViewById(R.id.txfHint);

        if(operacion == Consts.EJECUTE_ACTUALIZAR_PUNTAJE)
            this.actualizaPuntaje(((int)((Object[])arg)[1]));

        else if(operacion == Consts.EJECUTE_ACTUALIZAR_TXFTIEMPO)
            this.actualizaTiempo(((String)((Object[])arg)[1]));

        else if(operacion == Consts.EJECUTE_ACTUALIZAR_HINT) {
            if(((String)((Object[])arg)[1]) == Consts.BOMBA_BUENA) {
                txfHint.setText(Consts.BOMBA_BUENA);
                imgHint.setImageDrawable(this.mainActivity.getDrawable(R.drawable.only_bomba));
            }
            if(((String)((Object[])arg)[1]) == Consts.BOMBA_MALA) {
                txfHint.setText(Consts.BOMBA_MALA);
                imgHint.setImageDrawable(this.mainActivity.getDrawable(R.drawable.only_bomba));
            }
            if(((String)((Object[])arg)[1]) == Consts.GAME_OVER) {
                txfHint.setText(Consts.GAME_OVER);
                imgHint.setImageDrawable(null);
            }


            new Timer().schedule(new TimerTask()
            {
                @Override
                public void run() {
                    mainActivity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            txfHint.setText("(ﾉ◕‿◕)ﾉ*:･ﾟ✧･ﾟ✧");
                            imgHint.setImageDrawable(null);
                        }
                    });
                }
            }, 5000);
        }
    }
}
