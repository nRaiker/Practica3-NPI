package npi.enriquegg.appfotovoz;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1;
    //Boton para empezar el reconocimiento de voz
    private Button bt_rdv;
    //String que guarda el punto cardinal
    private String p_cardinal;
    //Porcentaje de error
    private int p_error;
    //Cuadro de texto que muestra mensajes al usuario
    private TextView texto;
    //String que almacenan los puntos ordinales
    private String N,S,W,E;
    //Cadena que almacena los errores
    private String cadenar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Asignamos los valores de los puntos cardinales
        N=getResources().getString(R.string.N);
        S=getResources().getString(R.string.S);
        E=getResources().getString(R.string.E);
        W=getResources().getString(R.string.W);

        //Referenciamos el cuadro de texto
        texto=(TextView)findViewById(R.id.texto);

        // Referenciamos el boton del XML
        bt_rdv=(Button)findViewById(R.id.bt_rdv);
        //Metodo para que se active el boton al pulsarlo y llame al reconocimiento de voz
        bt_rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startVoiceRecognitionActivity();

            }
        });

    }

    // Metodo que llama al activity encargado del reconocimiento de voz
    private void startVoiceRecognitionActivity() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Diga un punto cardenal y un numero");

        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override


    // Metodo que se encarga de regoger los datos del reconocimiento de voz
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Comprobamos que el reconocimiento de voz ha sido exitoso
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {

            //Recogemos los resultados del reconocimiento de voz
            ArrayList<String> matches = data.getStringArrayListExtra
                    (RecognizerIntent.EXTRA_RESULTS);

            // Extraemos las palabras de la cadena
            String [ ] palabras = matches.get(0).toString().split(" ");

            boolean r_correcto=false;


            //Guardamos los valores del reconocimiento y comprobamos si son correctos
            try {
                //parseInt convierte una cadena de caracteres en un numero entero. La cadena
                //solo debe contener caracteres de numero
                p_error=Integer.parseInt(palabras[1]);
                p_cardinal=palabras[0].toLowerCase();
                r_correcto=true;

            //La segunda palabra debe de ser un numero, en caso de que no lo sea la funcion parseInt
            //provoca un error
            }catch (NumberFormatException e){
                cadenar=getResources().getString(R.string.Error1)+"\n" +
                        getResources().getString(R.string.Texto)+matches.get(0);

            }catch (ArrayIndexOutOfBoundsException excepcion){
                cadenar=getResources().getString(R.string.Error2)+"\n" +
                        getResources().getString(R.string.Texto)+matches.get(0);
            }

            //Comprobamos si la primera palabra es un punto cardianl y en caso de que asi sea
            //llamamos a la brujula
            if(r_correcto){
                if(p_cardinal.equals(N) || p_cardinal.equals(S) || p_cardinal.equals(W) ||p_cardinal.equals(E)){

                    Intent intent = new Intent(this, BrujulaActivity.class);
                    intent.putExtra("Cardinal",p_cardinal);
                    intent.putExtra("Error",p_error);
                    startActivity(intent);

                }

                else{
                    cadenar=getResources().getString(R.string.Error3) + "\n" +
                            getResources().getString(R.string.Texto)+matches.get(0);
                    texto.setText(cadenar);

                }

            }

            //En caso de que la cadena no sea correcta se imprime por pantalla

            else{
                texto.setText(cadenar);
            }


        }
    }
}
