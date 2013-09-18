package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.ArrayList;
import java.util.HashMap;

public interface GestionAnuncios {
	
	public ArrayList<HashMap<String, Object>> obtenerAnunciosCercanos(Double latitud, Double altitud);
}
