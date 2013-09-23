package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.ImageDownloaderTask;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.PreferenciasUsuario;

public class ImagenRemotaAnuncioCustomAdapter extends ArrayAdapter<String> {

	private Activity activity;
	
	public ImagenRemotaAnuncioCustomAdapter(Activity activity) {
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
 
        String codigoImagen = getItem(position);

        String url = vi.getResources().getString(R.string.descargarImagenAnuncioService);
        url = url.replace("{id}", String.valueOf(codigoImagen));
        String usuario = PreferenciasUsuario.getUsuario(activity);
		String token = PreferenciasUsuario.getToken(activity);
		url = url + "?usuario="+usuario+"&token="+token;
		new ImageDownloaderTask(view).execute(url);
		
       /* BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bm = BitmapFactory.decodeFile(ruta, options);
        view.setImageBitmap(bm);*/
        
        return vi;
	}
	
}
