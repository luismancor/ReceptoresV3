package tecsup.example.receptoresv3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class ReceptorMensaje extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");

                String senderNumber = "";
                String message="";
                for (int i = 0; i < pdus.length; i++) {
                    //extrae el texto del sms 160 chars incluye all
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    //extrae el numero de remintente
                    senderNumber = sms.getOriginatingAddress();
                    //extrae el mensaje cuerpo  SE PUEDE OPTIMIZAR PARA MENSAJES MAYORES a 160 caracteres
                    message = sms.getDisplayMessageBody();
                    //prueba
                    Toast.makeText(context, "De: " + senderNumber + " Mensaje: " + message, Toast.LENGTH_LONG).show();
                }
                //obtiene las settings depfecto como proveedor, etc
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(senderNumber, null, "Disculpa estoy ocupado, te llamo luego", null, null);

                //ENVIAR LOS DATOS AL INTENT
                Intent mostrarActividad = new Intent(context,mensaje.class);
                mostrarActividad.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mostrarActividad.putExtra("numero",senderNumber);
                mostrarActividad.putExtra("mensaje",message);
                context.startActivity(mostrarActividad);
            }
        }



        /*

        Toast.makeText(context, "SMS entrante detectado", Toast.LENGTH_LONG).show();
        // Se extrae información del evento recepcionado
        //punto11
        String estado = "";
        String numero = "";
        String contenido="";
        Bundle extras = intent.getExtras();
        if (extras != null) {
            estado = extras.getString(TelephonyManager.EXTRA_STATE);
            if (estado.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                numero = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                contenido = extras.getString(Telephony.Sms.Intents.SM)
                Intent mostrarActividad = new Intent(context,llamada.class);
                mostrarActividad.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mostrarActividad.putExtra("numero",numero);
                context.startActivity(mostrarActividad);
            }
        }


        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String smsSender = "";
            String smsBody = "";

                Bundle smsBundle = intent.getExtras();
                if (smsBundle != null) {
                    Object[] pdus = (Object[]) smsBundle.get("pdus");
                    if (pdus == null) {
                        // Display some error to the user
                        Log.e(TAG, "SmsBundle had no pdus key");
                        return;
                    }
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        smsBody += messages[i].getMessageBody();
                    }
                    smsSender = messages[0].getOriginatingAddress();
                }


                if (listener != null) {
                    listener.onTextReceived(smsBody);
                }

        }

        */

    }
}



