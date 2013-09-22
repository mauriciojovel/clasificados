package com.udb.mad.shinmen.benja.guana.anuncios;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AnuncioDetalleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anuncio_detalle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.anuncio_detalle, menu);
		return true;
	}

}
