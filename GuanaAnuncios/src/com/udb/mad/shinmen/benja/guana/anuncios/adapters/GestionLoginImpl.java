package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.udb.mad.shinmen.benja.guana.anuncios.AnunciosCercanosActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.LoginActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.MD5Utility;

public class GestionLoginImpl implements GestionLogin, JSONDownloaderTask.OnFinishDownload<JSONObject> {

	public static final String EXITO = "1";
	public static final String ESTADO = "estado";
	public static final String TOKEN = "token";
	public static final String USUARIO = "usuario";
	public static final String CLAVE = "clave";
	
	private JSONDownloaderTask<JSONObject> jsonTask;
	private SharedPreferences prefs;
	private LoginActivity activity;
	
	
	@Override
	public void loginUsuario(String usuario, String password, String url, SharedPreferences prefs, LoginActivity activity) {

		this.prefs = prefs;
		this.activity = activity;
		
		String encryptedPass = MD5Utility.md5(password);
				
		List<NameValuePair> parametros = new ArrayList<NameValuePair>(1);
		parametros.add(new BasicNameValuePair(USUARIO, usuario));
		parametros.add(new BasicNameValuePair(CLAVE, encryptedPass));
		 
		jsonTask = new JSONDownloaderTask<JSONObject>(url, JSONDownloaderTask.METODO_POST, parametros);		
		jsonTask.setOnFinishDownload(this);
		jsonTask.execute();
	}
	

	@Override
	public void onFinishDownload(JSONObject jsonData) {
		try {
			
			String status = jsonData.getString(ESTADO);
			
			if(status.equals(EXITO)){
				
				String token = jsonData.getString(TOKEN);

				/*
				 * Guardando el token en los shared preferences para que la proxima vez no pida logearse al usuario
				 * sino lo envie directamente a la lista de anuncios
				 * 
				 * */
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(TOKEN, token);
				editor.commit();
				
				/*Si el logeo es exitoso se inicia la siguiente actividad*/
				Intent intento = new Intent(activity, AnunciosCercanosActivity.class);
				activity.startActivity(intento);
				
				/*se termina la actividad anterior*/
				activity.finish();
				
			}else{
				
				Toast.makeText(activity, "Usuario/Contrase�a invalidos!!..", Toast.LENGTH_SHORT)
				.show();
				
				/*			
				JSONObject errores = jsonData.getJSONObject("errors");
				
				String usuario = errores.getString("usuario");
				String clave = errores.getString("clave");
				*/
				
			}
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} 
	}

}