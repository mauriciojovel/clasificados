package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Anuncio;

public class AnuncioCustomAdapter extends BaseAdapter {

	private Activity activity;
	private List<Anuncio> anuncios;
	private static LayoutInflater inflater;
	
	public AnuncioCustomAdapter(Activity activity, List<Anuncio> anuncios) {
		super();
		this.activity = activity;
		this.anuncios = anuncios;
		this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return anuncios.size();
	}

	@Override
	public Object getItem(int position) {
		return anuncios.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi= convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.anuncios_cercanos_layout, null);
 
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