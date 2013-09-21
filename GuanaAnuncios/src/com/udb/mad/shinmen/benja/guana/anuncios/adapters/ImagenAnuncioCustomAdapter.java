package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.udb.mad.shinmen.benja.guana.anuncios.R;

public class ImagenAnuncioCustomAdapter extends ArrayAdapter<String> {

	private Activity activity;
	
	public ImagenAnuncioCustomAdapter(Activity activity) {
		super(activity, R.layout.imagen_anuncio_list_item);
		this.activity = activity;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View vi= convertView;
        if(convertView==null) {
        	LayoutInflater inflater = activity.getLayoutInflater();
            vi = inflater.inflate(R.layout.imagen_anuncio_list_item, null);
        }	
        ImageView view = (ImageView) vi.findViewById(R.id.imagen_publicar);
 
        String ruta = getItem(position);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bm = BitmapFactory.decodeFile(ruta, options);
        view.setImageBitmap(bm);
        
        return vi;
	}
	
}
