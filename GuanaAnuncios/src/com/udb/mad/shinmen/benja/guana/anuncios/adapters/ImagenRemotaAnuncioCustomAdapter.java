package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.ImageDownloader;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.PreferenciasUsuario;

public class ImagenRemotaAnuncioCustomAdapter extends ArrayAdapter<String> {

	private Activity activity;
	private final ImageDownloader imageDownloader;
	
	public ImagenRemotaAnuncioCustomAdapter(Activity activity) {
		super(activity, R.layout.imagen_anuncio_list_item);
		this.activity = activity;

        int scaleWidth = scaledImage(100, activity);
        int scaleHeight = scaledImage(100, activity);
		imageDownloader = new ImageDownloader(scaleWidth,scaleHeight);
	}
	
	public int scaledImage(int px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px2 = (int) (px*metrics.density+0.5f);
        return px2;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View vi= convertView;
        if(convertView==null) {
        	LayoutInflater inflater = activity.getLayoutInflater();
            vi = inflater.inflate(R.layout.imagen_detalle_anuncio_list_item, null);
        }	
        ImageView view = (ImageView) vi.findViewById(R.id.imgDetalleAnuncio);
 
        String codigoImagen = getItem(position);

        String url = vi.getResources().getString(R.string.descargarImagenAnuncioService);
        url = url.replace("{id}", String.valueOf(codigoImagen));
        String usuario = PreferenciasUsuario.getUsuario(activity);
		String token = PreferenciasUsuario.getToken(activity);
		url = url + "?usuario="+usuario+"&token="+token;
		imageDownloader.download(url,view);
		      
        return vi;
	}
	
	public ImageDownloader getImageDownloader() {
        return imageDownloader;
    }
	
}
