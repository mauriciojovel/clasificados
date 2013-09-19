package com.udb.mad.shinmen.benja.guana.anuncios;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.udb.mad.shinmen.benja.guana.anuncios.model.Categoria;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;

public class PublicarAnuncioActivity extends ActionBarActivity {

	Spinner spCategorias;
	EditText edtDescripcion;
	EditText edtPrecio;
	EditText edtCorreo;
	EditText edtTelefono;
	List<Categoria> categorias;
	Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publicar_anuncio);

		spCategorias = (Spinner) findViewById(R.id.spCategorias);
		edtDescripcion = (EditText) findViewById(R.id.edtDescripcion);
		edtDescripcion = (EditText) findViewById(R.id.edtPrecio);
		edtDescripcion = (EditText) findViewById(R.id.edtCorreo);
		edtDescripcion = (EditText) findViewById(R.id.edtTelefono);
		
		this.activity = this;

		List<NameValuePair> parametros = new ArrayList<NameValuePair>(1);
		parametros.add(new BasicNameValuePair("token",
				"e10adc3949ba59abbe56e057f20f883e"));
		JSONDownloaderTask<JSONArray> jdt = new JSONDownloaderTask<JSONArray>("http://guananuncio.madxdesign.com/index.php/pais"
				/*"http://guananuncio.madxdesign.com/index.php/anuncio/categorias"*/,
				JSONDownloaderTask.METODO_GET, /*parametros*/null, true);
		jdt.setOnFinishDownload(new CategoriaDownloadListener());
		try {
			jdt.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.publicar_anuncio, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_publicar_anuncio:
	        	guardarAnuncio();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void guardarAnuncio(){
		List<NameValuePair> parametros = new ArrayList<NameValuePair>(6);
		parametros.add(new BasicNameValuePair("titulo", this.edtDescripcion.getText().toString()));
		parametros.add(new BasicNameValuePair("descripcion", this.edtDescripcion.getText().toString()));
		parametros.add(new BasicNameValuePair("categoria_id", this.edtDescripcion.getText().toString()));
		/*parametros.add(new BasicNameValuePair("titulo", this.edtDescripcion.getText().toString()));
		parametros.add(new BasicNameValuePair("titulo", this.edtDescripcion.getText().toString()));*/
		parametros.add(new BasicNameValuePair("token",
				"e10adc3949ba59abbe56e057f20f883e"));
		JSONDownloaderTask<JSONArray> jdt = new JSONDownloaderTask<JSONArray>("http://guananuncio.madxdesign.com/index.php/pais"
				/*"http://guananuncio.madxdesign.com/index.php/anuncio/save"*/,
				JSONDownloaderTask.METODO_GET, /*parametros*/null, true);
		jdt.setOnFinishDownload(new PublicarAnuncioListener());
	}
	
	private class PublicarAnuncioListener implements JSONDownloaderTask.OnFinishDownload<JSONArray>{
		@Override
		public void onFinishDownload(JSONArray json) {
			
		}
	}

	private class CategoriaDownloadListener implements JSONDownloaderTask.OnFinishDownload<JSONArray>{
		
		@Override
		public void onFinishDownload(JSONArray json) {
			
			/** DUMMY **/
			try {
				json = new JSONArray("[{\"id\":\"1\",\"nombre\":\"Categoria 1\"},{\"id\":\"2\",\"nombre\":\"Categoria 2\"},{\"id\":\"3\",\"nombre\":\"Categoria 3\"}]");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			Categoria categoria;

			categorias = new ArrayList<Categoria>();

			try {
				for (int i = 0; i < json.length(); i++) {

					JSONObject jsonObject = json.getJSONObject(i);

					categoria = new Categoria();
					categoria.setCodigoCategoria(jsonObject.getString("id"));
					categoria.setNombreCategoria(jsonObject.getString("nombre"));
					categorias.add(categoria);
				}
			} catch (Exception e) {
				// TODO
			}

			SpinnerAdapter adapter = new ArrayAdapter<Categoria>(activity,
					android.R.layout.simple_spinner_dropdown_item, categorias);
			spCategorias.setAdapter(adapter);
			/*
			 * new ArrayAdapter<T>(this, categorias,
			 * android.R.layout.simple_spinner_item, new String[] { "id", "nombre"},
			 * new int[] { R.id.name, R.id.email, R.id.mobile });
			 */
		}
	}
}
