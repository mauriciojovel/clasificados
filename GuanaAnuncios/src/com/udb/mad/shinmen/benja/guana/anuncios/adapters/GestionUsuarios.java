package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.Map;

import android.widget.Spinner;

public interface GestionUsuarios {

	public void registrarUsuario(String pais, String correoElectronicoUsuario,
			String claveUsuario, String alias);

	public void obtenerPoblarPaises(Spinner spnPaises, int textViewResourceId,
			int pos, String url);

	public Map<String, String> buscarUsuario(String correo, String alias);

}
