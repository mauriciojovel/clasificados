package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Spinner;

import com.udb.mad.shinmen.benja.guana.anuncios.RegistroActivity;

public class GestionUsuariosImpl implements GestionUsuarios {

	ArrayList<Map<String, Object>> paises;
	private PaisCustomAdapter adapter;
	private Spinner spnPaises;
	private int textViewResourceId;
	private RegistroActivity activity;
	public int position;

	@Override
	public Map<String, String> loginUsuario(String usuario, String password) {

		Map<String, String> result = new HashMap<String, String>();

		/* Dummy data */
		result.put("resultado", "1"); // indica que se logeo sin problemas

		/*
		 * Solo deberia pedir logearse la primera vez, en cuya accion se recibe
		 * un token el cual se guarda en los SharedPreferences y asi la
		 * siguiente vez que ingrese a la aplicacion no pida logeo
		 */
		result.put("token", "123456");

		return result;
	}

	@Override
	public Map<String, String> registrarUsuario(Long pais,
			String correoElectronicoUsuario, String claveUsuario, String alias) {

		Map<String, String> result = null;

		/* Revisando si el usuario no existe ya, ya sea el alias o correo */
		result = buscarUsuario(correoElectronicoUsuario, alias);

		/*
		 * si result es nulo quiere decir que no existe el usuario y se puede
		 * crear
		 */
		if (result == null) {
			/* Guardar registro */
			result = new HashMap<String, String>();
			result.put("resultado", "1");
		}

		return result;
	}

	@Override
	public void obtenerPoblarPaises(Spinner spnPaises, int textViewResourceId,
			RegistroActivity activity, int pos) {

		this.spnPaises = spnPaises;
		this.textViewResourceId = textViewResourceId;
		this.activity = activity;
		position = pos;

		new JSONTask().execute();

		/*
		 * Dummy data
		 * 
		 * pais.put("codigoPais", 1L); pais.put("nombre", "El Salvador");
		 * paises.add(pais);
		 * 
		 * pais = new HashMap<String, Object>(); pais.put("codigoPais", 2L);
		 * pais.put("nombre", "Guatemala"); paises.add(pais);
		 * 
		 * pais = new HashMap<String, Object>(); pais.put("codigoPais", 3L);
		 * pais.put("nombre", "Honduras"); paises.add(pais);
		 * 
		 * pais = new HashMap<String, Object>(); pais.put("codigoPais", 4L);
		 * pais.put("nombre", "Nicaragua"); paises.add(pais);
		 * 
		 * pais = new HashMap<String, Object>(); pais.put("codigoPais", 5L);
		 * pais.put("nombre", "Costa Rica"); paises.add(pais);
		 * 
		 * pais = new HashMap<String, Object>(); pais.put("codigoPais", 6L);
		 * pais.put("nombre", "Panama"); paises.add(pais);
		 * 
		 * /* End dummy data
		 */

	}

	@Override
	public Map<String, String> buscarUsuario(String correo, String alias) {
		Map<String, String> result = null;

		/* aqui realizar consulta para verificar si el alias o correo existen */

		/* dummy data */
		boolean encontrado = false;

		if (encontrado) {

			result = new HashMap<String, String>();

			result.put("resultado", "2"); // indica que el alias o correo ya
											// existen
		}

		return result;
	}

	private class JSONTask extends AsyncTask<Void, Void, String> {

		protected void onPostExecute(String string) {
			String readFeed = string;

			paises = new ArrayList<Map<String, Object>>();

			Map<String, Object> pais = new HashMap<String, Object>();

			try {

				JSONArray jsonArray = new JSONArray(readFeed);
				
				for (int i = 0; i < jsonArray.length(); i++) {

					JSONObject jsonObject = jsonArray.getJSONObject(i);

					pais = new HashMap<String, Object>();
					pais.put("codigoPais", jsonObject.getString("id"));
					pais.put("nombre", jsonObject.getString("nombre"));
					paises.add(pais);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			adapter = new PaisCustomAdapter(activity, textViewResourceId,
					paises);

			spnPaises.setAdapter(adapter);
			
			if(position!=0){
				spnPaises.setSelection(position);
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return readFeed();
		}

		public String readFeed() {
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(
					"http://guananuncio.madxdesign.com/index.php/pais");
			try {
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
				} else {
					Log.e(RegistroActivity.class.toString(),
							"Failed to download JSON");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return builder.toString();
		}
	}
}
