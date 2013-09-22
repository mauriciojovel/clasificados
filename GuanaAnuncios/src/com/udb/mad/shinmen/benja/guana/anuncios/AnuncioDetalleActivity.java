package com.udb.mad.shinmen.benja.guana.anuncios;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.udb.mad.shinmen.benja.guana.anuncios.fragment.AnuncioDetalleFragment;

public class AnuncioDetalleActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState == null) {
			AnuncioDetalleFragment f = new AnuncioDetalleFragment();
			f.setArguments(getIntent().getExtras());
			FragmentTransaction ft = 
					getSupportFragmentManager().beginTransaction();
			ft.add(
					android.R.id.content, f);
			ft.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.anuncio_detalle, menu);
		return true;
	}

}
