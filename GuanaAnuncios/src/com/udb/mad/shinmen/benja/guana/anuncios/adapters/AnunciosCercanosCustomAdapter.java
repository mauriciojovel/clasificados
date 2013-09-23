package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.ImageDownloader;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.PreferenciasUsuario;

public class AnunciosCercanosCustomAdapter extends BaseAdapter{

	private Activity activity;
    private ArrayList<HashMap<String, Object>> data;
    private static LayoutInflater inflater=null;
    private final ImageDownloader imageDownloader;
    static class Holder {
        TextView title;
        TextView description;
        TextView codigo;
        ImageView imagen;
    }
    
    public AnunciosCercanosCustomAdapter(Activity a
            , ArrayList<HashMap<String, Object>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageDownloader = new ImageDownloader();
    }

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		HashMap<String, Object> map = data.get(position);
		return map;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		Holder h;
		String url = activity.getResources().getString(R.string.imagenAnuncioService);/*vi.getResources().getString(R.string.imagenAnuncioService);*/
		
        if(convertView==null) {
            vi = inflater.inflate(R.layout.anuncios_cercanos_layout, null);
            h = new Holder();
            h.title = (TextView)vi.findViewById(R.id.title);
            h.description = (TextView)vi.findViewById(R.id.description);
            h.codigo = (TextView) vi.findViewById(R.id.codigo);
            h.imagen = (ImageView) vi.findViewById(R.id.list_image);
            vi.setTag(h);
        } else {
            h = (Holder) vi.getTag();
        }
 
        HashMap<String, Object> dato = new HashMap<String, Object>();
        dato = data.get(position);
 
        // Setting all values in listview
        h.title.setText(String.valueOf(dato.get("titulo")));
        h.description.setText(String.valueOf(dato.get("descripcion")));
        h.codigo.setText(String.valueOf(dato.get("codigo")));
        
        /*Cargando la imagen asincronamente*/
        url = url.replace("{id}", String.valueOf(dato.get("codigo")));
		url = url + "?usuario="+PreferenciasUsuario.getUsuario(activity)
		        +"&token="+PreferenciasUsuario.getToken(activity);
		
		imageDownloader.download(url,h.imagen);         
        
        return vi;
	}
	
	public ImageDownloader getImageDownloader() {
        return imageDownloader;
    }
		

}
