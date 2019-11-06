package tecsup.example.receptoresv3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactsListAdapter extends ArrayAdapter<Contact> {

    private static final String TAG ="ContactsListAdapter";
    private Context mContext;
    int mResource;

    public ContactsListAdapter(Context context, int resource, ArrayList<Contact> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String numero = getItem(position).getNumero();
        String mensaje = getItem(position).getMensaje();

        Contact contacto = new Contact(numero,mensaje);


        LayoutInflater inflater= LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvNumero = (TextView) convertView.findViewById(R.id.tvNumero);
        TextView tvMensaje = (TextView) convertView.findViewById(R.id.tvMensaje);

        tvNumero.setText(numero);
        tvMensaje.setText(mensaje);

        return convertView;
    }


}
