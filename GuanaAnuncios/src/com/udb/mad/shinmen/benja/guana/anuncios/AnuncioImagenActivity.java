package com.udb.mad.shinmen.benja.guana.anuncios;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.ImageDownloader;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.PreferenciasUsuario;

public class AnuncioImagenActivity extends ActionBarActivity {

	public static String CODIGO_IMAGEN = "com.udb.mad.shinmen.benja.guana.anuncios.AnuncioImagenActivity.CodigoImagen";
	String codigoImagen = null;
	ImageView imgDetalleAnuncio;
	private ImageDownloader imageDownloader = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		if(bundle != null && bundle.containsKey(CODIGO_IMAGEN)){
			codigoImagen = bundle.getString(CODIGO_IMAGEN);
		}
		
		setContentView(R.layout.activity_anuncio_imagen);
		
		imgDetalleAnuncio = (ImageView) findViewById(R.id.ivImagenCompletaAnuncio);
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		
		imageDownloader = new ImageDownloader(width,height);
		
		String url = getResources().getString(R.string.descargarImagenAnuncioService);
        url = url.replace("{id}", String.valueOf(codigoImagen));
        String usuario = PreferenciasUsuario.getUsuario(this);
		String token = PreferenciasUsuario.getToken(this);
		url = url + "?usuario="+usuario+"&token="+token;
		imageDownloader.download(url,imgDetalleAnuncio);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.anuncio_imagen, menu);
		return true;
	}

}
