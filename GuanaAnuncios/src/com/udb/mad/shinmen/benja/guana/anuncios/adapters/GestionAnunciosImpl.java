package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.ArrayList;
import java.util.HashMap;

public class GestionAnunciosImpl implements GestionAnuncios {

	@Override
	public ArrayList<HashMap<String, Object>> obtenerAnunciosCercanos(
			Double latitud, Double altitud) {
		
		ArrayList<HashMap<String, Object>> anunciosList = new ArrayList<HashMap<String, Object>>();
		
		/*Dummy data*/
		HashMap<String, Object> anuncio = new HashMap<String, Object>();
		anuncio.put("titulo", "Mouse inalambrico");
		anuncio.put("alias", "malaCara1");
		anuncio.put("descripcion", "El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña tocaba el saxofón detrás del palenque de paja.");
		anuncio.put("imagen", "");		
		anuncio.put("latitud", "13.690340434877442");
		anuncio.put("longitud", "-89.22508481559748");
		anunciosList.add(anuncio);
		
		anuncio = new HashMap<String, Object>();
		anuncio.put("titulo", "Galaxy S5");
		anuncio.put("alias", "ratero01");
		anuncio.put("descripcion", "El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña tocaba el saxofón detrás del palenque de paja.");
		anuncio.put("imagen", "");
		anuncio.put("latitud", "13.690903331255466");
		anuncio.put("longitud", "-89.2275739055633");
		anunciosList.add(anuncio);
		
		anuncio = new HashMap<String, Object>();
		anuncio.put("titulo", "Adaptadores USB");
		anuncio.put("alias", "timorato");
		anuncio.put("descripcion", "El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña tocaba el saxofón detrás del palenque de paja.");
		anuncio.put("imagen", "");
		anuncio.put("latitud", "13.687817808638007");
		anuncio.put("longitud", "-89.22544959602351");
		anunciosList.add(anuncio);
		
		anuncio = new HashMap<String, Object>();
		anuncio.put("titulo", "Tarjetas de red");
		anuncio.put("alias", "cachada");
		anuncio.put("descripcion", "El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña tocaba el saxofón detrás del palenque de paja.");
		anuncio.put("imagen", "");
		anuncio.put("latitud", "13.689172941798928");
		anuncio.put("longitud", "-89.2257714611053");
		anunciosList.add(anuncio);
		
		anuncio = new HashMap<String, Object>();
		anuncio.put("titulo", "Monitor LED");
		anuncio.put("alias", "descuentazo");
		anuncio.put("descripcion", "El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña tocaba el saxofón detrás del palenque de paja.");
		anuncio.put("imagen", "");
		anuncio.put("latitud", "13.684419517304311");
		anuncio.put("longitud", "-89.22313216743464");
		anunciosList.add(anuncio);
		
		return anunciosList;
	}

}
