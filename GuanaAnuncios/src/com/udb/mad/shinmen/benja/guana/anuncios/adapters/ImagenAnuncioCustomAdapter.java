package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.udb.mad.shinmen.benja.guana.anuncios.R;

public class ImagenAnuncioCustomAdapter extends ArrayAdapter<String> {

	private Activity activity;
	
	static class Holder{
		ImageView imagen;
		ImageButton boton;
	}
	
	public ImagenAnuncioCustomAdapter(Activity activity) {
		super(activity, R.layout.imagen_anuncio_list_item);
		this.activity = activity;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Holder h = null;
		View vi= null;
		
        if(convertView == null) {
        	LayoutInflater inflater = activity.getLayoutInflater();
            vi = inflater.inflate(R.layout.imagen_anuncio_list_item, null);
            h = new Holder();
            h.imagen = (ImageView) vi.findViewById(R.id.imagen_publicar);
            h.boton = (ImageButton) vi.findViewById(R.id.btnBorrarImagen);
            vi.setTag(h);
        }else{
        	vi = convertView;
			h = (Holder) vi.getTag();
        }
       
        String ruta = getItem(position);
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bm = BitmapFactory.decodeFile(ruta, options);
        h.imagen.setImageBitmap(bm);
        h.boton.setTag(ruta);
        
        return vi;
	}
	
}
