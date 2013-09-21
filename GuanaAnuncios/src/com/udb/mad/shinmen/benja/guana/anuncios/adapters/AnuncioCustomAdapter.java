package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Anuncio;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.ImageDownloaderTask;

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
        ImageView imagen = (ImageView) vi.findViewById(R.id.list_image);
 
        Anuncio a = (Anuncio) getItem(position);
 
        title.setText(a.getTituloAnuncio());
        description.setText(a.getDescripcionAnuncio());
        codigo.setText(a.getCodigoAnuncio());
        
        /*Cargando la imagen asincronamente*/
        //obteniendo la direccion URL para descargar la imagen
       /* String url = "http://guananuncio.madxdesign.com/anuncio/single/{id}";//TODO
        url = url.replace("{id}", String.valueOf(a.getCodigoAnuncio()));
        
        //ejecutando la tarea asincrona
        if(imagen != null){
        	new ImageDownloaderTask(imagen).execute(url);
        } */
        
        return vi;
	}
}