package com.udb.mad.shinmen.benja.guana.anuncios.utilidades;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Ejemplo de uso:<br/><br/>
 * 
 * List<NameValuePair> parametros = new ArrayList<NameValuePair>(1);<br/>
 * parametros.add(new BasicNameValuePair("param", "valor"));<br/>
 * JSONDownloaderTask jdt = new JSONDownloaderTask(url, JSONDownloaderTask.METODO_POST, null);<br/>
 * JSONArray jsonArray = jdt.execute().get();<br/>
 * 
 *
 */
public class JSONDownloaderTask extends AsyncTask<String, String, JSONArray> {

	private String url;
	private String metodo;
	private List<NameValuePair> parametrosLista;
	public static final String METODO_POST = "POST";
	public static final String METODO_GET = "GET";
	private final String ENCODING = "utf-8";

	public JSONDownloaderTask(String url, String metodo,
			List<NameValuePair> parametrosLista) {
		this.url = url;
		this.metodo = metodo;
		this.parametrosLista = parametrosLista;
	}

	@Override
	protected JSONArray doInBackground(String... parametros) {
		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream inputStream = null;
		JSONArray jsonArray = null;
		String json = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			if (this.metodo.equals(METODO_POST)) {
				HttpPost post = new HttpPost(url);
				if(parametrosLista != null){
					post.setEntity(new UrlEncodedFormEntity(this.parametrosLista));
				}
				response = httpClient.execute(post);
			} else {
				if(parametrosLista != null){
					String paramString = URLEncodedUtils
						      .format(parametrosLista, "utf-8");
				    this.url += "?" + paramString;
				}
			    HttpGet get = new HttpGet(url);
			    response = httpClient.execute(get);
			}
			entity = response.getEntity();
			inputStream = entity.getContent();
			// obtuvimos respuesta, a transformar...
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, ENCODING), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			inputStream.close();
			json = sb.toString();
			jsonArray = new JSONArray(json);
		}catch (Exception e) {
			Log.e("JSON Downloader", "Ocurrio un error al parsear los datos"
					+ e.toString());
		}
		return jsonArray;
	}

}
