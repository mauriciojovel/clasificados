package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.udb.mad.shinmen.benja.guana.anuncios.AnunciosCercanosActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.RegistroActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.MD5Utility;

public class GestionUsuariosImpl implements GestionUsuarios,
		JSONDownloaderTask.OnFinishDownload<JSONArray>,
		JSONDownloaderTask.OnFinishDownloadJSONObject<JSONObject> {

	public static final String EXITO = "1";
	public static final String ID = "id";
	public static final String NOMBRE = "nombre";
	public static final String CODIGO_PAIS = "codigoPais";
	public static final String RESULTADO = "resultado";
	public static final String PAIS_ID = "pais_id";
	public static final String CORREO_ELECTRONICO = "correo_electronico";
	public static final String CLAVE = "clave";
	public static final String LATITUD = "latitud";
	public static final String ALTITUD = "altitud";
	public static final String ESTADO = "estado";
	public static final String TOKEN = "token";
	public static final String USUARIO = "usuario";

	ArrayList<Map<String, Object>> paises;
	private PaisCustomAdapter adapter;
	private Spinner spnPaises;
	private int textViewResourceId;
	private RegistroActivity activity;
	public int position;
	private JSONDownloaderTask<JSONArray> jsonTask;
	private JSONDownloaderTask<JSONObject> jsonTaskObj;
	private SharedPreferences prefs;
	private String usuario;

	public GestionUsuariosImpl(RegistroActivity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void registrarUsuario(String pais, String correoElectronicoUsuario,
			String claveUsuario, String alias, SharedPreferences prefs, String latitud, String altitud) {

		this.prefs = prefs;
		this.usuario = alias;
		
		String encryptedPass = MD5Utility.md5(claveUsuario);
		
		

		/* Lista de parametros a enviar en el POST */
		List<NameValuePair> parametros = new ArrayList<NameValuePair>(1);
		parametros.add(new BasicNameValuePair(PAIS_ID, pais));
		parametros.add(new BasicNameValuePair(NOMBRE, alias));
		parametros.add(new BasicNameValuePair(CORREO_ELECTRONICO,
				correoElectronicoUsuario));
		parametros.add(new BasicNameValuePair(CLAVE, encryptedPass));
		parametros.add(new BasicNameValuePair(LATITUD, latitud));
		parametros.add(new BasicNameValuePair(ALTITUD, altitud));

		/* URL del servicio */
		String url = activity.getResources().getString(
				R.string.guardarUsuarioService);

		/* Tarea asincrona que recupera los datos */
		jsonTaskObj = new JSONDownloaderTask<JSONObject>(url,
				JSONDownloaderTask.METODO_POST, parametros);
		jsonTaskObj.setOnFinishDownloadJSONObject(this);
		jsonTaskObj.execute();
	}

	@Override
	public void obtenerPoblarPaises(Spinner spnPaises, int textViewResourceId,
			int pos, String url) {

		this.spnPaises = spnPaises;
		this.textViewResourceId = textViewResourceId;
		
		position = pos;

		// new JSONTask().execute();
		jsonTask = new JSONDownloaderTask<JSONArray>(url,
				JSONDownloaderTask.METODO_GET, null);
		jsonTask.setOnFinishDownload(this);
		jsonTask.setJsonArray(true);
		jsonTask.execute();
	}

	@Override
	public Map<String, String> buscarUsuario(String correo, String alias) {
		Map<String, String> result = null;

		/* aqui realizar consulta para verificar si el alias o correo existen */

		/* dummy data */
		boolean encontrado = false;

		if (encontrado) {

			result = new HashMap<String, String>();

			result.put(RESULTADO, "2"); // indica que el alias o correo ya
										// existen
		}

		return result;
	}

	@Override
	public void onFinishDownload(JSONArray jsonArray) {

		try {
			paises = new ArrayList<Map<String, Object>>();

			Map<String, Object> pais = new HashMap<String, Object>();
			pais.put(CODIGO_PAIS, "-1");
			pais.put(NOMBRE, activity.getResources().getString(R.string.lbl_seleccione));
			paises.add(pais);

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonObject = jsonArray.getJSONObject(i);

				pais = new HashMap<String, Object>();
				pais.put(CODIGO_PAIS, jsonObject.getString(ID));
				pais.put(NOMBRE, jsonObject.getString(NOMBRE));
				paises.add(pais);

			}

			adapter = new PaisCustomAdapter(activity, textViewResourceId,
					paises);

			spnPaises.setAdapter(adapter);

			if (position != 0) {
				spnPaises.setSelection(position);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onFinishDownloadJSONObject(JSONObject jsonData) {

		try {

			String status = jsonData.getString(ESTADO);

			if (status.equals(EXITO)) {


				 /*
				 * Guardando el token en los shared preferences para que la proxima vez no pida logearse al usuario
				 * sino lo envie directamente a la lista de anuncios
				 * 
				 * */
				String token = jsonData.getString(TOKEN);
				
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(TOKEN, token);
				editor.putString(USUARIO, usuario);
				editor.commit();

				/*Si el registro es exitoso se inicia la siguiente actividad*/
				Intent intento = new Intent(activity, AnunciosCercanosActivity.class);
				activity.startActivity(intento);
				
				/*
				 * se termina la actividad de registro para que vuelva a la
				 * pantalla de login
				 */
				activity.finish();

			} else {

				JSONObject errores = jsonData.getJSONObject("errors");

				/*
				 * String usuario = errores.getString("usuario"); String clave =
				 * errores.getString("clave");
				 */
				Toast.makeText(activity,
						"Error al guardar usuario: " + errores.getString("mensaje"),
						Toast.LENGTH_LONG).show();
				
				Log.e(RegistroActivity.class.toString(),
						errores.toString());

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(activity,
					"Error al guardar usuario: " + e.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}
}
