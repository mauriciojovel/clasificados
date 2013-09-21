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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.udb.mad.shinmen.benja.guana.anuncios.model.Categoria;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask.OnStartDownload;

public class PublicarAnuncioActivity extends ActionBarActivity implements OnItemSelectedListener{

	String categoriaSeleccionada;
	ProgressDialog pd;
	Spinner spCategorias;
	EditText edtTitulo;
	EditText edtDescripcion;
	EditText edtPrecio;
	EditText edtCorreo;
	EditText edtTelefono;
	List<Categoria> categorias;
	Activity activity;
	SharedPreferences prefs;
	String token;
	String usuario;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publicar_anuncio);

		spCategorias = (Spinner) findViewById(R.id.spCategorias);
		edtTitulo = (EditText) findViewById(R.id.edtTitulo);
		edtDescripcion = (EditText) findViewById(R.id.edtDescripcion);
		edtPrecio = (EditText) findViewById(R.id.edtPrecio);
		edtCorreo = (EditText) findViewById(R.id.edtCorreo);
		edtTelefono = (EditText) findViewById(R.id.edtTelefono);
		
		spCategorias.setOnItemSelectedListener(this);

		this.activity = this;
		
		prefs = getSharedPreferences(
				"GuanaAnunciosPreferences", Context.MODE_PRIVATE);
		token = /*prefs.getString(GestionLoginImpl.TOKEN,null)*/ "e10adc3949ba59abbe56e057f20f883e";
		usuario = /*prefs.getString(GestionLoginImpl.USUARIO, null)*/ "test";

		List<NameValuePair> parametros = new ArrayList<NameValuePair>(2);
		parametros.add(new BasicNameValuePair("usuario",usuario));
		parametros.add(new BasicNameValuePair("token",token));
		JSONDownloaderTask<JSONArray> jdt = new JSONDownloaderTask<JSONArray>(
				 "http://guananuncio.madxdesign.com/index.php/anuncio/categorias" ,
				JSONDownloaderTask.METODO_POST, parametros, true);
		jdt.setOnFinishDownload(new CategoriaDownloadListener());
		
		try {
			jdt.execute().get();
		} catch (InterruptedException e) {
			Log.e("error", e.getMessage());
		} catch (ExecutionException e) {
			Log.e("error", e.getMessage());
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

	private void guardarAnuncio() {
		List<NameValuePair> parametros = new ArrayList<NameValuePair>(8);
		parametros.add(new BasicNameValuePair("usuario",usuario));
		parametros.add(new BasicNameValuePair("token",token));
		parametros.add(new BasicNameValuePair("titulo", this.edtTitulo.getText().toString()));
		parametros.add(new BasicNameValuePair("descripcion",this.edtDescripcion.getText().toString()));
		parametros.add(new BasicNameValuePair("categoria_id",categoriaSeleccionada));
		parametros.add(new BasicNameValuePair("precio",this.edtPrecio.getText().toString()));
		parametros.add(new BasicNameValuePair("telefono",this.edtTelefono.getText().toString()));

		JSONDownloaderTask<JSONObject> jdt = new JSONDownloaderTask<JSONObject>(
				"http://guananuncio.madxdesign.com/index.php/anuncio/save",
				JSONDownloaderTask.METODO_POST,parametros);
		jdt.setOnFinishDownloadJSONObject(new PublicarAnuncioListener());
		jdt.setOnStartDownloadListener(new OnStartDownload() {
			@Override
			public void onStartDownload() {
				pd = new ProgressDialog(activity);
				pd.setTitle(getResources().getString(R.string.titulo_procesando));
				pd.setMessage(getResources().getString(R.string.titulo_espere));
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}
		});
		
		try {
			jdt.execute().get();
		} catch (InterruptedException e) {
			Log.e("error", e.getMessage());
		} catch (ExecutionException e) {
			Log.e("error", e.getMessage());
		}
	}

	private class PublicarAnuncioListener implements
			JSONDownloaderTask.OnFinishDownloadJSONObject<JSONObject> {
		@Override
		public void onFinishDownloadJSONObject(JSONObject jsonObject) {
			pd.dismiss();
			try {
				String status = jsonObject.getString("estado");
				if(status.equals("1")){
					Toast.makeText(activity, "Anuncio Publicado", Toast.LENGTH_LONG).show();
					finish();
				}
			} catch (JSONException e) {
				Log.e("error", e.getMessage());
			}
		}

		@Override
		public void loadError() {
			// TODO Auto-generated method stub
			
		}
	}

	private class CategoriaDownloadListener implements
			JSONDownloaderTask.OnFinishDownload<JSONArray> {

		@Override
		public void onFinishDownload(JSONArray json) {

			Categoria categoria;

			categorias = new ArrayList<Categoria>();

			try {
				for (int i = 0; i < json.length(); i++) {

					JSONObject jsonObject = json.getJSONObject(i);

					categoria = new Categoria();
					categoria.setCodigoCategoria(jsonObject.getString("id"));
					categoria
							.setNombreCategoria(jsonObject.getString("nombre"));
					categorias.add(categoria);
				}
			} catch (Exception e) {
				Log.e("error", e.getMessage());
			}

			SpinnerAdapter adapter = new ArrayAdapter<Categoria>(activity,
					android.R.layout.simple_spinner_dropdown_item, categorias);
			spCategorias.setAdapter(adapter);
		}

		@Override
		public void loadError() {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		Categoria c = categorias.get(pos);
		categoriaSeleccionada = c.getCodigoCategoria();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
}