package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.udb.mad.shinmen.benja.guana.anuncios.AnuncioActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.LoginActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask.OnStartDownload;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.MD5Utility;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.PreferenciasUsuario;

public class GestionLoginImpl implements GestionLogin
            , JSONDownloaderTask.OnFinishDownload<JSONObject> {

	public static final String EXITO = "1";
	public static final String ESTADO = "estado";
	public static final String TOKEN = "token";
	public static final String USUARIO = "usuario";
	public static final String CLAVE = "clave";
	public static final String DISPOSITIVO = "dispositivo";
	ProgressDialog pd;
	
	private JSONDownloaderTask<JSONObject> jsonTask;
	private LoginActivity activity;
	private String usuario;
	
	
	@Override
	public void loginUsuario(String usuario, String password, String url
	        , final LoginActivity activity) {

		this.activity = activity;
		this.usuario = usuario;
		
		String encryptedPass = MD5Utility.md5(password);
				
		List<NameValuePair> parametros = new ArrayList<NameValuePair>(1);
		parametros.add(new BasicNameValuePair(USUARIO, usuario));
		parametros.add(new BasicNameValuePair(CLAVE, encryptedPass));
		parametros.add(new BasicNameValuePair(DISPOSITIVO
				, android.os.Build.MODEL));
		 
		jsonTask = new JSONDownloaderTask<JSONObject>(url
		        , JSONDownloaderTask.METODO_POST, parametros);		
		jsonTask.setOnFinishDownload(this);
		jsonTask.setOnStartDownloadListener(new OnStartDownload() {
            @Override
            public void onStartDownload() {
                pd = new ProgressDialog(activity);
                pd.setTitle(activity.getResources()
                        .getString(R.string.titulo_procesando));
                pd.setMessage(activity.getResources()
                        .getString(R.string.titulo_espere));
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                pd.show();
            }
        });
		jsonTask.execute();
	}
	

	@Override
	public void onFinishDownload(JSONObject jsonData) {
		try {
			String status = jsonData.getString(ESTADO);
			
			if(pd != null) {
			    pd.dismiss();
			}
			
			if(status.equals(EXITO)){
				
				String token = jsonData.getString(TOKEN);

				/*
				 * Guardando el token en los shared preferences para 
				 * que la proxima vez no pida logearse al usuario
				 * sino lo envie directamente a la lista de anuncios
				 * */
				PreferenciasUsuario.setToken(token, activity);
				PreferenciasUsuario.setUsuario(usuario, activity);
				
				/*Si el logeo es exitoso se inicia la siguiente actividad*/
				Intent intento = new Intent(activity, AnuncioActivity.class);
				activity.startActivity(intento);
				
				/*se termina la actividad anterior*/
				activity.finish();
				
			}else{
				Toast.makeText(activity, "Usuario/Contraseņa invalidos!!.."
				        , Toast.LENGTH_SHORT).show();
				/*			
				JSONObject errores = jsonData.getJSONObject("errors");
				
				String usuario = errores.getString("usuario");
				String clave = errores.getString("clave");
				*/
			}
						
		} catch (Exception e) {
			Log.e("GestionLoginImpl", e.getMessage(), e);
		} 
	}


	@Override
	public void loadError() {}

}
