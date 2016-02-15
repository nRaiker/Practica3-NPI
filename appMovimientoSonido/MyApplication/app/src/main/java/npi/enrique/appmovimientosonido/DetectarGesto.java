package npi.enrique.appmovimientosonido;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

class Punto3D{
    public float x;
    public float y;
    public float z;

    // Esta clase servira para almacenar los datos de las posiciones que queremos detectar
    public Punto3D(float x1,float y1, float z1){

        x=x1;
        y=y1;
        z=z1;
    }

    public Punto3D(){}

}

//En esta activity se detecta una serie de posiciones del telefono, y al aacabar de pasar por todas
//ellas se reproduce un sonido

public class DetectarGesto extends AppCompatActivity implements SensorEventListener {

    //Variables para los sensores
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    //Array que contendra la lista de posiciones que tendra que realizar el usuario
    private ArrayList<Punto3D> movimientos;
    int etapa;
    float margen;

    private MediaPlayer mSonido;

    boolean xt,yt,zt;

    TextView info;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detectar_gesto);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        movimientos=new ArrayList<Punto3D>();

        info=(TextView)findViewById(R.id.tv_infomoPasos);


        info.setText(R.string.Paso1);

        etapa=0;

        margen=1.5f;

        //Referenciamos el sonido almacenado en la carpeta raw
        mSonido=MediaPlayer.create(DetectarGesto.this,R.raw.fin);

        // Rellenamos el vector de movimientos con las posiciones que vamos a tener
        movimientos.add(new Punto3D(0f,0f,10f));
        movimientos.add(new Punto3D(0f,10f,0f));
        movimientos.add(new Punto3D(10f,0f,0f));
        movimientos.add(new Punto3D(0f,0f,-10f));


    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }




    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() != mAccelerometer.getType()) {
            return;
        }


        // Comprobamos que estamos en la posición adecuada
        if(etapa<movimientos.size()) {
            if (event.values[0] < (movimientos.get(etapa).x + margen) && event.values[0] > (movimientos.get(etapa).x - margen))
                xt = true;
            else xt = false;

            if (event.values[1] < (movimientos.get(etapa).y + margen) && event.values[1] > (movimientos.get(etapa).y - margen))
                yt = true;
            else yt = false;

            if (event.values[2] < (movimientos.get(etapa).z + margen) && event.values[2] > (movimientos.get(etapa).z - margen))
                zt = true;
            else zt = false;
        }




        if(xt && yt && zt){
            etapa++;
        }

        //Segun por que posicion vayamos, se muestra un mensaje distinto en la pantalla
        switch (etapa){
            case 0:
                info.setText(R.string.Paso1);
                break;
            case 1:
                info.setText(R.string.Paso2);
                break;
            case 2:
                info.setText(R.string.Paso3);
                break;
            case 3:
                info.setText(R.string.Paso4);
                break;
            case 4:
                info.setText(R.string.Final);
                etapa++;
                mSonido.start();
                break;




        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
