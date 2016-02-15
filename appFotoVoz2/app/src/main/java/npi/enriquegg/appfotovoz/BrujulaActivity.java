package npi.enriquegg.appfotovoz;

import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public  class BrujulaActivity extends AppCompatActivity implements SensorEventListener {

    //Flechas de la brujula
    private Drawable roja;
    private Drawable verde;

    //String que almacenan los puntos ordinales
    private String N,S,W,E;

    //Recuadro de informacion
    private TextView informa;
    //Imagen de la flecha de la brujula
    private ImageView flecha;
    //El sensor manager del dispositivo
    private SensorManager manager;
    // Guarda el valor del azimut
    float azimut;
    // guarda el angulo  actual de la brujula
    private float currentDegree = 0f;
    float degree;

    //Sensores necesarios para la brujula:magnetico y acelerometro
    private Sensor accelerometer;
    private Sensor magnetometer;
    //Guardam los datos que cambian con la variacion de los sensores
    float[] datos_acelerometro;
    float[] datos_magnetico;

    //Variables de comunicacion entre las activities
    private String cardinal;
    private float p_error;

    //Variable que guarda el grado al que debe apuntar
    private float grado_cardinal;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brujula);

        //Referenciamos los objetos del layout
        flecha=(ImageView)findViewById(R.id.im_Flecha);

        informa=(TextView)findViewById(R.id.informacion);


        //Inicializacion de los sensores
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        datos_acelerometro=null;
        datos_magnetico=null;

        //Inicializamos las flechas de la brujula
        roja= getResources().getDrawable(R.drawable.roja);
        verde= getResources().getDrawable(R.drawable.verde);

        //Inicializamos la flecha
        flecha.setImageDrawable(roja);

        cardinal = getIntent().getStringExtra("Cardinal");
        p_error =getIntent().getIntExtra("Error", 0);

        //Iniciamos el angulo qeu debemos sumar para que en vez de que la brujula apunte al norte
        //apunte a la direccion que hemos dicho
        N=getResources().getString(R.string.N);
        S=getResources().getString(R.string.S);
        E=getResources().getString(R.string.E);
        W=getResources().getString(R.string.W);


        if(cardinal.equals(N)) grado_cardinal=0;
        if(cardinal.equals(E)) grado_cardinal=270;
        if(cardinal.equals(S)) grado_cardinal=180;
        if(cardinal.equals(W)) grado_cardinal=90;

        String aux=getResources().getString(R.string.Info1)+ " " + cardinal + "\n"+
                getResources().getString(R.string.Info2) + " " +p_error;

        informa.setText(aux);



    }

    @Override
    protected void onResume(){
        super.onResume();

        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        manager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Se comprueba que tipo de sensor está activo en cada momento
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                datos_acelerometro = event.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                datos_magnetico = event.values;
                break;
        }

        if ((datos_magnetico != null) && (datos_acelerometro != null)) {
            float RotationMatrix[] = new float[16];
            boolean success = SensorManager.getRotationMatrix(RotationMatrix,null, datos_acelerometro, datos_magnetico);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(RotationMatrix, orientation);
                azimut = orientation[0] * (180 / (float) Math.PI);
            }
        }
        degree = azimut;

        //flecha.setText("Angle: " + Float.toString(degree) + " degrees");
        // se crea la animacion de la rottacion (se revierte el giro en grados, negativo)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                degree+grado_cardinal,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        // el tiempo durante el cual la animación se llevará a cabo
        ra.setDuration(1000);
        // establecer la animación después del final de la estado de reserva
        ra.setFillAfter(true);
        // Inicio de la animacion
        flecha.startAnimation(ra);
        currentDegree = -(degree+grado_cardinal);
        //Hacemos que el grado de la brujula este entre -180 y 180
        while (currentDegree<-180){
            currentDegree+=360;
        }
        while (currentDegree>180){
            currentDegree-=360;
        }

        //Comprobamos si la brujula esta en el angulo adecuado y cambiamos el color de la flecha
        //segun corresponda
        if(currentDegree<(+360*(p_error/100)) &&
                currentDegree>(-360*(p_error/100)) ){
            flecha.setImageDrawable(verde);

        }
        else{
            flecha.setImageDrawable(roja);
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}
