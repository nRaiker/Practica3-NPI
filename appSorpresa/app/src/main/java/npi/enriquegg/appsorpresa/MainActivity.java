package npi.enriquegg.appsorpresa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


//Esta aplicacion con el sensor luminico del dispositivo cambia el volumen de una cancion que se
//reproduce de fondo, aumentando el volumen cuando la iluminacion es mas alta
public class MainActivity extends AppCompatActivity {

    Button comenzar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        comenzar=(Button)findViewById(R.id.bt_comenzar);

        comenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ReproductorLuminico.class);
                startActivity(intent);
            }
        });

    }


}
