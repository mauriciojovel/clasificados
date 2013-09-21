package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import com.udb.mad.shinmen.benja.guana.anuncios.LoginActivity;

public interface GestionLogin {

	public void loginUsuario(String usuario, String password,
			String url, /*SharedPreferences prefs,*/ LoginActivity activity);
}
