package tecsup.example.receptoresv3;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //punto18
    private ListView lstContactos;
    private String[] elementosLista = null;
    private Map<String, ?> elementosGuardados = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int READ_PHONE_STATE = 123;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
        }
        final int READ_CALL_LOG = 123;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, READ_CALL_LOG);
        }
        final int READ_SMS = 0;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, READ_SMS);
        }

        //final int RECEIVE_SMS = 123;   OPCION 2 PARA RUNTIME PERMISSION DE SMS
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant  = ContextCompat.checkSelfPermission(this, permission);
        if ( grant != PackageManager.PERMISSION_GRANTED){
            String[] permission_list = new String [1];
            permission_list[0]=permission;
            ActivityCompat.requestPermissions(this, permission_list,1);
        }
        //punto19
        lstContactos = findViewById(R.id.lstContactos);

// datos almacenados por la actividad
        SharedPreferences datos = this.getSharedPreferences("DatosDeReceptor", Context.MODE_PRIVATE);
        elementosGuardados = datos.getAll();
        elementosLista = (String[])elementosGuardados.values().toArray(new String[elementosGuardados.size()]);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, elementosLista);
        lstContactos.setAdapter(adaptador);


        //punto 23
        registerForContextMenu(lstContactos);


    }
    //punto24
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_opciones, menu);
    }
    //punto25
    //el primer @se agrego manualmente
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int seleccionlista;
        seleccionlista = info.position;
        switch (item.getItemId()) {
            case R.id.llamar:
                String numero = lstContactos.getAdapter().getItem(seleccionlista).toString();
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 123);
                    }
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numero)));
                }catch(Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.mensaje:
                //Toast.makeText(getBaseContext(), "Envio de mensaje a configurar", Toast.LENGTH_LONG).show();
                //PASO31
                String numero1 = lstContactos.getAdapter().getItem(seleccionlista).toString();
                mostrarVentanaPersonalizada(numero1);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //punto 30
    public void mostrarVentanaPersonalizada(final String numero){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.ventana_personalizada, null);
        dialogBuilder.setView(dialogView);

        final EditText lblMensaje = dialogView.findViewById(R.id.mensaje);

        dialogBuilder.setTitle("Mensaje a " + numero);
        dialogBuilder.setMessage("Ingrese contenido del mensaje");
        dialogBuilder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 123);
                    }
                    Intent envioSMS = new Intent(Intent.ACTION_SEND, Uri.parse("smsto:" + numero));
                    envioSMS.putExtra("sms_body", lblMensaje.getText().toString());
                    startActivity(envioSMS);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void cerrar(View v){
        finish();
    }

    public void mensajesLista(View v){

        Intent mostrarActividad = new Intent(this,mainMensajeActivity.class);
        this.startActivity(mostrarActividad);
    }

}
