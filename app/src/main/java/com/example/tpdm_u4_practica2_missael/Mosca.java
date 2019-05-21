package com.example.tpdm_u4_practica2_missael;

import android.graphics.Bitmap;

public class Mosca {
    Bitmap img;

    //DONDE ESTARA POSICIONADA LA BURBUJA POR PRIMERA VEZ
    int posicionInicialX;
    int posicionInicialY;

    //POSICION DE LA BURBUJA MIENTRAS AVANZA
    float moscaX;
    float moscaY;

    //VELOCIDAD DE MOVIMIENTO DE LA BURBUJA
    float moscaVelocidadX;
    float moscaVelocidadY;

    public Mosca(int posicionInicialX, int posicionInicialY,float moscaVelocidadX,float moscaVelocidadY, Bitmap img){
        this.posicionInicialX = posicionInicialX;
        this.posicionInicialY = posicionInicialY;
        this.moscaVelocidadX = moscaVelocidadX;
        this.moscaVelocidadY = moscaVelocidadY;
        this.img = img;
    }

    public boolean moscaTocada(float toqueX,float toqueY){
        if( toqueX > (moscaX - img.getWidth()/2 ) && toqueX < (moscaX + img.getWidth()/2 )){

            if( toqueY > (moscaY - img.getWidth()/2 ) && toqueY < (moscaY + img.getWidth()/2 )){
                return true;
            }
        }
        return false;
    }
}
