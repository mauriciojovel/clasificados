package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.udb.mad.shinmen.benja.guana.anuncios.AnunciosCercanosActivity;

public interface GestionAnuncios {

	public ArrayList<HashMap<String, Object>> obtenerAnunciosCercanos(
			Double latitud, Double altitud, String usuario, String token,
			String url, AnunciosCercanosActivity activity, ListView listview,
			GoogleMap mMap);
}
