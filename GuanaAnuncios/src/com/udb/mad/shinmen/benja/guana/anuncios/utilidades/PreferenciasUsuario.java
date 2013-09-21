package com.udb.mad.shinmen.benja.guana.anuncios.utilidades;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenciasUsuario {
	public static final String USUARIO = "usuario";
	public static final String TOKEN = "token";
	public static final String GUANANUNCIO_PREFERENCES = 
												"GuanaAnunciosPreferences";
	private static SharedPreferences prefs;
	
	public static boolean isUsuarioAutenticado(Context ctx) {
		return (getPrefs(ctx).getString(USUARIO, null) != null 
									&& prefs.getString(TOKEN, null) != null);
	}
	
	public static String getUsuario(Context ctx) {
		return getPrefs(ctx).getString(USUARIO, null);
	}
	
	public static String getToken(Context ctx) {
		return getPrefs(ctx).getString(TOKEN, null);
	}
	
	public static boolean setUsuario(String usuario,Context ctx) {
		return getPrefs(ctx).edit().putString(USUARIO, usuario).commit();
	}
	
	public static boolean setToken(String token,Context ctx) {
		return getPrefs(ctx).edit().putString(TOKEN, token).commit();
	}
	
	private static SharedPreferences getPrefs(Context ctx) {
		if(prefs == null) {
			prefs = ctx.getSharedPreferences(
					GUANANUNCIO_PREFERENCES, Context.MODE_PRIVATE);
		}
		return prefs;
	}
	
}
