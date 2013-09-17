package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestionUsuariosImpl implements GestionUsuarios {

	@Override
	public Map<String, String> loginUsuario(String usuario, String password) {
		
		Map<String, String> result = new HashMap<String, String>();
		
		/*Dummy data*/
		result.put("resultado", "1"); //indica que se logeo sin problemas
		
		
		/*
		 * Solo deberia pedir logearse la primera vez, en cuya accion se recibe
		 * un token el cual se guarda en los SharedPreferences y asi la siguiente
		 * vez que ingrese a la aplicacion no pida logeo
		 *  
		 * */
		result.put("token", "123456");
		
		
		return result;
	}

	@Override
	public Map<String, String> registrarUsuario(Long pais, String correoElectronicoUsuario,
			String claveUsuario, String alias) {

		
		Map<String, String> result = null;
		
		/*Revisando si el usuario no existe ya, ya sea el alias o correo*/
		result = buscarUsuario(correoElectronicoUsuario, alias);
		
		/*si result es nulo quiere decir que no existe el usuario y se puede crear*/
		if(result == null){
			/*Guardar registro*/
			result = new HashMap<String, String>();
			result.put("resultado", "1");
		}
		
		return result;
	}

	@Override
	public List<Map<String, Object>> obtenerPaises() {
		
		List<Map<String, Object>> paises = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> pais = new HashMap<String, Object>();
		
		/*Dummy data*/
		
		pais.put("codigoPais", 1L);
		pais.put("nombre", "El Salvador");
		paises.add(pais);
		
		pais = new HashMap<String, Object>();
		pais.put("codigoPais", 2L);
		pais.put("nombre", "Guatemala");
		paises.add(pais);
		
		pais = new HashMap<String, Object>();
		pais.put("codigoPais", 3L);
		pais.put("nombre", "Honduras");
		paises.add(pais);
		
		pais = new HashMap<String, Object>();
		pais.put("codigoPais", 4L);
		pais.put("nombre", "Nicaragua");
		paises.add(pais);
		
		pais = new HashMap<String, Object>();
		pais.put("codigoPais", 5L);
		pais.put("nombre", "Costa Rica");
		paises.add(pais);
		
		pais = new HashMap<String, Object>();
		pais.put("codigoPais", 6L);
		pais.put("nombre", "Panama");
		paises.add(pais);
		
		/*End dummy data*/
		
		return paises;
		
	}

	@Override
	public Map<String, String> buscarUsuario(String correo, String alias) {
		Map<String, String> result = null;
		
		/*aqui realizar consulta para verificar si el alias o correo existen*/
		
		/*dummy data*/
		boolean encontrado = false;
		
		if(encontrado){
			
			result = new HashMap<String, String>();
			
			result.put("resultado", "2"); //indica que el alias o correo ya existen
		}
		
		return result;
	}

}
