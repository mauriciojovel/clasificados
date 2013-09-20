package com.udb.mad.shinmen.benja.guana.anuncios;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.udb.mad.shinmen.benja.guana.anuncios.fragment.AnunciosListFragment;

public class AnuncioActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anuncio);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.anuncio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_nuevo_anuncio:
			openPublicarAnuncioActivity();
			return true;
		case R.id.action_refrescar:
			AnunciosListFragment alf = (AnunciosListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.listAnuncios);
			alf.refrescarLista();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openPublicarAnuncioActivity() {
		Intent i = new Intent();
		i.setClass(this, PublicarAnuncioActivity.class);
		startActivity(i);
	}

}
