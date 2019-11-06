package tecsup.example.receptoresv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class mensaje extends AppCompatActivity {

    int contadorLlamada = 0;
    TextView lblNumero;
    TextView lblMensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);

        lblNumero = findViewById(R.id.lblNumero);
        lblMensaje = findViewById(R.id.lblMensaje);

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null) {
            String numero = getIntent().getExtras().getString("numero");
            lblNumero.setText(numero + "");

            String mensaje = getIntent().getExtras().getString("mensaje");
            lblMensaje.setText(mensaje + "");
        }


    }

    public void guardarNumero(View v){


        SharedPreferences datos2 = getSharedPreferences("DatosDeReceptor2", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = datos2.edit();
        contadorLlamada= datos2.getAll().size()+1;
        editor.putString("numero" + contadorLlamada++,lblNumero.getText().toString());
        editor.putString("mensaje", lblMensaje.getText().toString());

        editor.commit();

        Intent mostrarMensajes = new Intent(this,mainMensajeActivity.class);
        mostrarMensajes.putExtra("a","numero"+contadorLlamada);
        startActivity(mostrarMensajes);
        finish();

        /*
        Intent intent = new Intent(this, mainMensajeActivity.class);
        intent.putExtra("numero", lblNumero.getText().toString());
        intent.putExtra("mensaje", lblMensaje.getText().toString());
        startActivity(intent);

        */

    }

    public void cerrar(View v){
        finish();
    }
}
