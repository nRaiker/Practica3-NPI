package npi.enrique.appmovimientosonido;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;


import java.util.ArrayList;



// En esta acrivity solamente se muestra un mensaje de como utilizar la aplicacion en la
//pantalla del dispositivo
public class MainActivity extends AppCompatActivity  {

    TextView info;
    Button comenzar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info=(TextView)findViewById(R.id.tv_Info);
        comenzar=(Button)findViewById(R.id.bt_comenzar);


        comenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DetectarGesto.class);

                startActivity(intent);
            }
        });

    }
}
