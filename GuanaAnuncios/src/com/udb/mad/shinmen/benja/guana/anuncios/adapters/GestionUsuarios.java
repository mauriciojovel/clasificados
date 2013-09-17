package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.List;
import java.util.Map;

public interface GestionUsuarios {
	
	public Map<String,String> loginUsuario(String usuario, String password);
	
	public Map<String,String> registrarUsuario(Long pais, String correoElectronicoUsuario, 
			String claveUsuario, String alias);
	
	public List<Map<String, Object>> obtenerPaises();
	
	public Map<String,String> buscarUsuario(String correo, String alias);
	
}
