package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.List;
import java.util.Map;

import com.udb.mad.shinmen.benja.guana.anuncios.RegistroActivity;

import android.widget.Spinner;

public interface GestionUsuarios {

	public Map<String, String> loginUsuario(String usuario, String password);

	public Map<String, String> registrarUsuario(Long pais,
			String correoElectronicoUsuario, String claveUsuario, String alias);

	public void obtenerPoblarPaises(Spinner spnPaises, int textViewResourceId,
			RegistroActivity activity, int pos);

	public Map<String, String> buscarUsuario(String correo, String alias);

}
