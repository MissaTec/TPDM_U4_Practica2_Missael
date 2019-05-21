package com.example.tpdm_u4_practica2_missael;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Reloj {
    int segundos=60;

    public Reloj(){

    }

    public Canvas dibujarReloj(Canvas canvas){

        Paint pTexto=new Paint();

        double tamanoTexto=canvas.getWidth()*.058;
        pTexto.setTextSize((int)tamanoTexto);

        if(segundos > 10){
            pTexto.setColor(Color.rgb(254,192,6));
            canvas.drawText(String.valueOf(segundos),500,150,pTexto);
        }
        else{
            pTexto.setColor(Color.rgb(255,0,0));
            canvas.drawText("0"+String.valueOf(segundos),500,150,pTexto);
        }


        return canvas;
    }
}
