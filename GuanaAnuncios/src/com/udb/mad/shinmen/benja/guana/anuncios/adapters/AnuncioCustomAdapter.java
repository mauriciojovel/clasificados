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
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.ImageDownloader;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.PreferenciasUsuario;

public class AnuncioCustomAdapter extends ArrayAdapter<Anuncio> {

	private Activity activity;
	private final ImageDownloader imageDownloader;

	
	static class Holder{
		TextView title;
		TextView description;
		TextView codigo;
		ImageView imagen;
	}
	
	public AnuncioCustomAdapter(Activity activity) {
		super(activity, R.layout.anuncios_cercanos_layout);
		this.activity = activity;
		imageDownloader = new ImageDownloader();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder h = null;
		View vi= null;
		
        if(convertView == null) {
        	LayoutInflater inflater = activity.getLayoutInflater();
            vi = inflater.inflate(R.layout.anuncios_cercanos_layout, null);
            h = new Holder();
            h.title = (TextView)vi.findViewById(R.id.title);
            h.description = (TextView)vi.findViewById(R.id.description);
            h.codigo = (TextView) vi.findViewById(R.id.codigo);
            h.imagen = (ImageView) vi.findViewById(R.id.list_image);
            vi.setTag(h);
        }else{
        	vi = convertView;
			h = (Holder) vi.getTag();
        }
 
        Anuncio a = (Anuncio) getItem(position);
 
        h.title.setText(a.getTituloAnuncio());
        h.description.setText(a.getDescripcionAnuncio());
        h.codigo.setText(a.getCodigoAnuncio());
        h.imagen.setImageDrawable(activity.getResources().getDrawable(R.drawable.guana_logo));
        
        /*Cargando la imagen asincronamente*/
        //obteniendo la direccion URL para descargar la imagen
        String url = vi.getResources().getString(R.string.imagenAnuncioService);
        url = url.replace("{id}", String.valueOf(a.getCodigoAnuncio()));
        url = url + "?usuario="+PreferenciasUsuario.getUsuario(activity)+"&token="+PreferenciasUsuario.getToken(activity);
        
        imageDownloader.download(url,h.imagen);
        
        return vi;
	}
	
	public ImageDownloader getImageDownloader() {
        return imageDownloader;
    }
}