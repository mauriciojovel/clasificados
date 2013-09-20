package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Anuncio;

public class AnuncioCustomAdapter extends ArrayAdapter<Anuncio> {

	private Activity activity;
	
	public AnuncioCustomAdapter(Activity activity) {
		super(activity, R.layout.anuncios_cercanos_layout);
		this.activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi= convertView;
        if(convertView==null) {
        	LayoutInflater inflater = activity.getLayoutInflater();
            vi = inflater.inflate(R.layout.anuncios_cercanos_layout, null);
        }	
        TextView title = (TextView)vi.findViewById(R.id.title);
        TextView description = (TextView)vi.findViewById(R.id.description);
        TextView codigo = (TextView) vi.findViewById(R.id.codigo);
 
        Anuncio a = (Anuncio) getItem(position);
 
        title.setText(a.getTituloAnuncio());
        description.setText(a.getDescripcionAnuncio());
        codigo.setText(a.getCodigoAnuncio());
        
        return vi;
	}
}