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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class mainMensajeActivity extends AppCompatActivity {
    private ListView milistView;

    //elementos de prueba
    private String[] elementosLista = null;
    private Map<String, ?> elementosGuardados = null;
    String numeroObtenido="";
    String mensajeObtenido="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mensaje);

        milistView = findViewById(R.id.listView);
        ArrayList<Contact> contactList = new ArrayList<>();;

        SharedPreferences datos2 = this.getSharedPreferences("DatosDeReceptor2", Context.MODE_PRIVATE);
       //elementosGuardados = datos.getAll();
        numeroObtenido = datos2.getString("numero","xx");
        mensajeObtenido = datos2.getString("mensaje", "yy");
        Contact nuevo = new Contact(numeroObtenido, mensajeObtenido);
        contactList.add(nuevo);
        ContactsListAdapter adapter = new ContactsListAdapter(this, R.layout.adapter_view_layout, contactList);
        milistView.setAdapter(adapter);

        /*
        elementosLista = (String[])elementosGuardados.values().toArray(new String[elementosGuardados.size()]);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, elementosLista);
        milistView.setAdapter(adaptador);
        */


        /*
        Bundle datos = this.getIntent().getExtras();
        if (datos != null) {
            String numeroObtenido = datos.getString("numero");
            String mensajeObtenido = datos.getString("mensaje");
            //Contact carla = new Contact("98233239", "Hola");
            //Contact john = new Contact("34235435", "Hola mmgvo");
            Contact nuevo = new Contact(numeroObtenido, mensajeObtenido);
            //contactList.add(carla);
            //contactList.add(john);
            contactList.add(nuevo);
            }
            ContactsListAdapter adapter = new ContactsListAdapter(this, R.layout.adapter_view_layout, contactList);
            milistView.setAdapter(adapter);

        */

        registerForContextMenu(milistView);

    }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mensaje, menu);
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

            case R.id.mensajeResponse:
                //Toast.makeText(getBaseContext(), "Envio de mensaje a configurar", Toast.LENGTH_LONG).show();
                //PASO31
                String numero = ((TextView)info.targetView.findViewById(R.id.tvNumero)).getText().toString();
                //String numero1 = milistView.getAdapter().getItem(seleccionlista).toString();
                mostrarVentanaPersonalizada(numero);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


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










}
