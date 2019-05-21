package com.example.tpdm_u4_practica2_missael;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class Lienzo extends View {
    Reloj reloj;
    int contadorReloj;
    boolean relojModificado = false;
    String resultado = "";

    //VARIABLES PARA DETERMINAR LOS LIMITES DE LA PANTALLA TANTO EN X COMO EN Y
    private int xMin = 0;
    private int xMax;
    private int yMin = 0;
    private int yMax;
    //ARREGLO DE MOSCAS PEQUEÑAS
    Mosca[] arregloMoscas;
    Mosca moscardon;

    private int moscasActuales = 4;
    private int contadorMoscasPequenas = 0;
    private int contadorGolpesMoscardon = 0;
    private int topeMoscas = 30;
    private int topeMoscardon = 5;

    CountDownTimer timer;
    Context context;
    float leftBoton=250;
    float rightBoton=600;
    float topBoton=280;
    float bottomBoton=420;

    boolean juegoTerminado = false;

    public Lienzo(final Context context) {
        super(context);
        this.context = context;
        //Reloj del juego
        reloj = new Reloj();

        //Contador para salir del juego
        final int[] contadorSalir = {0};

        arregloMoscas = new Mosca[4];

        cargarArregloMoscas();
        final int[] tiempoContador = {0};
        timer=new CountDownTimer(1500,60) {
            @Override
            public void onTick(long l) {
                tiempoContador[0]++;
                if(reloj.segundos>0){
                    if(tiempoContador[0] == 10){
                        reloj.segundos--;
                        tiempoContador[0] = 0;
                    }
                }

                if(juegoTerminado == true){
                    contadorSalir[0]++;
                    if(contadorSalir[0] > 20){
                        Intent pantallaMain = new Intent(context,MainActivity.class);
                        context.startActivity(pantallaMain);
                    }
                }

                invalidate();
            }

            @Override
            public void onFinish() {
                timer.start();
            }
        }.start();
    }

    public void cargarArregloMoscas(){
        Random r = new Random();
        for(int i=0;i<arregloMoscas.length;i++){
            int posicionInicialX = 0;
            int posicionInicialY = 0;
            //derecha
            if(i == 0){
                posicionInicialX = 0;
                posicionInicialY = 800;
                posicionInicialY = posicionInicialY = r.nextInt(600);
            }
            //izquierda
            if(i == 1){
                posicionInicialX = 900;
                posicionInicialY = 800;
                posicionInicialY = posicionInicialY = r.nextInt(600);
            }
            //arriba
            if(i == 2){
                posicionInicialX = 500;
                posicionInicialX = posicionInicialX = r.nextInt(500);
                posicionInicialY = 0;
            }

            //abajo
            if(i == 3){
                posicionInicialX = 500;
                posicionInicialX = posicionInicialX = r.nextInt(500);
                posicionInicialY = 1700;
            }


            int moscaVelocidadX = 30;
            int moscaVelocidadY = moscaVelocidadX;

            Bitmap img = BitmapFactory.decodeResource(context.getResources(),R.drawable.mosca);

            Mosca mosca=new Mosca(posicionInicialX,posicionInicialY,moscaVelocidadX,moscaVelocidadY,img);
            mosca.moscaX = posicionInicialX;
            mosca.moscaY = posicionInicialY;
            arregloMoscas[i]=mosca;
        }
        moscasActuales = 4;
        //MOSCARDON
        int posicionInicialX = 600;
        int posicionInicialY = 850;

        int moscaVelocidadX = 60;
        int moscaVelocidadY = moscaVelocidadX;

        Bitmap img = BitmapFactory.decodeResource(context.getResources(),R.drawable.moscardon);

        moscardon =new Mosca(posicionInicialX,posicionInicialY,moscaVelocidadX,moscaVelocidadY,img);
        moscardon.moscaX = posicionInicialX;
        moscardon.moscaY = posicionInicialY;
    }

    @Override
    public void onDraw(Canvas canvas){
        if(juegoTerminado){
            finJuego(canvas);
            return;
        }
        if(moscasActuales == 0){
            cargarArregloMoscas();
        }

        //si aun no se matan a todas las moscas
        if(contadorMoscasPequenas < topeMoscas){
            String marca = "Moscas: "+contadorMoscasPequenas;
            marcador(canvas,marca);
        }
        //si ya se muestra el moscardon
        if(contadorMoscasPequenas == topeMoscas){
            String marca = "Golpes: "+contadorGolpesMoscardon;
            marcador(canvas,marca);
        }

        //Si matas a todas las moscas el reloj debe ser 10 segundos
        if(contadorMoscasPequenas == topeMoscas){
            if(relojModificado == false){
                reloj.segundos = 10;
                relojModificado = true;
            }
        }

        if(contadorMoscasPequenas < topeMoscas){
            mostrarMoscas(canvas);
        }
        else{
            mostrarMoscardon(canvas);
        }

        //dibujando el reloj
        canvas=reloj.dibujarReloj(canvas);

        //FIN DEL JUEGO
        //no matar las moscas pequeñas
        if(contadorMoscasPequenas < topeMoscas && reloj.segundos == 0){
            resultado = "perdio";
            finJuego(canvas);
        }
        //no matar a la mosca grande
        if(contadorMoscasPequenas == topeMoscas && reloj.segundos == 0 && contadorGolpesMoscardon < topeMoscardon){
            resultado = "perdio";
            finJuego(canvas);
        }

        //matar a todas las moscas y moscardon antes de que termine el tiempo
        if(contadorMoscasPequenas == topeMoscas && reloj.segundos > 0 && contadorGolpesMoscardon == topeMoscardon){
            resultado = "gano";
            finJuego(canvas);
        }
    }

    public void mostrarMoscas(Canvas canvas){
        Random r = new Random();

        for(int i=0; i<arregloMoscas.length; i++){
            if(arregloMoscas[i] != null){
                Mosca mosca = arregloMoscas[i];
                Paint p=new Paint();
                canvas.drawBitmap(mosca.img, mosca.moscaX, mosca.moscaY, p);

                //ACTUALIZAR LA POSICION DE LA BURBUJA
                mosca.moscaX+=mosca.moscaVelocidadX;
                mosca.moscaY+=mosca.moscaVelocidadY;

                //Cuando toque pared derecha
                if ( mosca.moscaX +mosca.img.getWidth()/2 > xMax) {
                    mosca.moscaVelocidadX = -mosca.moscaVelocidadX;
                    mosca.moscaX = xMax-mosca.img.getWidth()/2 ;
                }
                //cuando toque pared izquierda
                else if (mosca.moscaX - mosca.img.getWidth()/2 < xMin) {
                    mosca.moscaVelocidadX = - mosca.moscaVelocidadX;
                    mosca.moscaX = xMin + mosca.img.getWidth()/2 + r.nextInt(40);;
                }

                //cuando toque pared superior
                if (mosca.moscaY +mosca.img.getHeight()/2 > yMax) {
                    mosca.moscaVelocidadY = -mosca.moscaVelocidadY;
                    mosca.moscaY = yMax - mosca.img.getHeight()/2;
                }

                //cuando toque pared inferior
                else if (mosca.moscaY - mosca.img.getHeight()/2 < yMin) {
                    mosca.moscaVelocidadY = -  mosca.moscaVelocidadY;
                    mosca.moscaY = yMin + mosca.img.getHeight()/2 + r.nextInt(40);
                }
            }
        }
    }

    public void mostrarMoscardon(Canvas canvas){
        Random r = new Random();
        if(moscardon != null){

            Paint p=new Paint();
            canvas.drawBitmap(moscardon.img, moscardon.moscaX, moscardon.moscaY, p);

            //ACTUALIZAR LA POSICION DE LA BURBUJA
            moscardon.moscaX+=moscardon.moscaVelocidadX;
            moscardon.moscaY+=moscardon.moscaVelocidadY;

            //Cuando toque pared derecha
            if ( moscardon.moscaX +moscardon.img.getWidth()/2 > xMax) {
                moscardon.moscaVelocidadX = -moscardon.moscaVelocidadX;
                moscardon.moscaX = xMax-moscardon.img.getWidth()/2 ;
            }
            //cuando toque pared izquierda
            else if (moscardon.moscaX - moscardon.img.getWidth()/2 < xMin) {
                moscardon.moscaVelocidadX = - moscardon.moscaVelocidadX;
                moscardon.moscaX = xMin + moscardon.img.getWidth()/2 + r.nextInt(700 + 1 + 500) - 500;
            }

            //cuando toque pared superior
            if (moscardon.moscaY +moscardon.img.getHeight()/2 > yMax) {
                moscardon.moscaVelocidadY = -moscardon.moscaVelocidadY;
                moscardon.moscaY = yMax - moscardon.img.getHeight()/2;
            }

            //cuando toque pared inferior
            else if (moscardon.moscaY - moscardon.img.getHeight()/2 < yMin) {
                moscardon.moscaVelocidadY = -  moscardon.moscaVelocidadY;
                moscardon.moscaY = yMin + moscardon.img.getHeight()/2 + r.nextInt(700 + 1 + 500) - 500;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent e){
//
        switch (e.getAction()){

            case MotionEvent.ACTION_DOWN:
                float toqueX=e.getX();
                float toqueY=e.getY();
                if(contadorMoscasPequenas < topeMoscas){
                    for(int i=0;i<arregloMoscas.length;i++){
                        if(arregloMoscas[i]!=null){
                            Mosca mosca = arregloMoscas[i];
                            if(mosca.moscaTocada(toqueX,toqueY)){
                                //mp = MediaPlayer.create(context, R.raw.plop);
                                //mp.start();
                                moscasActuales --;
                                contadorMoscasPequenas ++;
                                arregloMoscas[i]=null;
                            }
                        }
                    }
                }
                else {
                    if(moscardon != null){
                        if(moscardon.moscaTocada(toqueX,toqueY)){
                            //mp = MediaPlayer.create(context, R.raw.plop);
                            //mp.start();
                            contadorGolpesMoscardon ++;
                            if(contadorGolpesMoscardon == topeMoscardon){
                                moscardon = null;
                            }
                        }
                    }
                }
                break;

            default:
                break;
        }
        return true;
    }

    public void finJuego(Canvas canvas){
        Paint pTexto=new Paint();
        pTexto.setTextSize(150);
        if(resultado.equals("perdio")){
            pTexto.setColor(Color.rgb(255,0,0));
            canvas.drawText("Perdiste",250,850,pTexto);
        }
        else{
            pTexto.setColor(Color.rgb(254,192,6));
            canvas.drawText("Ganaste",250,800,pTexto);
        }
        juegoTerminado = true;
    }

    public void marcador(Canvas canvas, String marca){
        Paint pTexto=new Paint();
        pTexto.setTextSize(40);
        pTexto.setColor(Color.rgb(0,0,0));
        canvas.drawText(marca,250,220,pTexto);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        xMax = w-1;
        yMax = h-1;
    }
}
