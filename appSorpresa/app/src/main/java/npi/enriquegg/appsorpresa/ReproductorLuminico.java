package npi.enriquegg.appsorpresa;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReproductorLuminico extends AppCompatActivity implements SensorEventListener {

    //Creacion de los sensores
    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private TextView info;

    MediaPlayer cancion;
    //Almacena el valor maximo del sensor
    private float max_l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_luminico);


        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        mLightSensor= mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        info=(TextView)findViewById(R.id.tv_volumen);

        max_l=80;

        cancion = MediaPlayer.create(this, R.raw.kditw);
        cancion.setLooping(true);



    }

    protected void onResume() {
        //El start se hace en el resumen para que empiece otra vez la musica  si se ha bloqueado
        //el dispositio o si se ha dejado en pausa y volvemos a abrirla
        cancion.start();
        super.onResume();
        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        //Pausamos la cancion si la aplicacion pasa al estado pause
        cancion.pause();
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != mLightSensor.getType()) {
           return;
        }

        //Obtenemos el valor del sensor y lo normalizamos para que este en elrango 0-1
        //Esta normalizado para el maximo valor que detecta con la bombilla de mi habitacion
        info.setText(getString(R.string.volumen) + Float.toString((100f / max_l) * event.values[0]));

        //Cambiamos el volumen de la cancion (no se altera el volumen multimedia del dispositivo)
        cancion.setVolume(event.values[0]/max_l,event.values[0]/max_l);



    }
}
