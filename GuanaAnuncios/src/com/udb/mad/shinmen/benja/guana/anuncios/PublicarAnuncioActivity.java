package com.udb.mad.shinmen.benja.guana.anuncios;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.udb.mad.shinmen.benja.guana.anuncios.adapters.ImagenAnuncioCustomAdapter;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Anuncio;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Categoria;
import com.udb.mad.shinmen.benja.guana.anuncios.servicios.PublicarAnuncioService;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.UbicacionUtility;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask.OnStartDownload;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.PreferenciasUsuario;

public class PublicarAnuncioActivity extends ActionBarActivity implements
		OnItemSelectedListener {

	private int RESULT_LOAD_IMAGE = 69;

	String categoriaSeleccionada;
	ImagenAnuncioCustomAdapter adapter;
	ProgressDialog pd;
	Spinner spCategorias;
	EditText edtTitulo;
	EditText edtDescripcion;
	EditText edtPrecio;
	EditText edtCorreo;
	EditText edtTelefono;
	ListView lvImagenes;
	List<Categoria> categorias;
	Activity activity;
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
		lvImagenes = (ListView) findViewById(R.id.lvImagenesPublicar);

		spCategorias.setOnItemSelectedListener(this);

		this.activity = this;

		token = PreferenciasUsuario.getToken(this);
		usuario = PreferenciasUsuario.getUsuario(this);

		if (adapter == null) {
			adapter = new ImagenAnuncioCustomAdapter(this);
		}

		lvImagenes.setAdapter(adapter);
		
		setupActionBar();

		List<NameValuePair> parametros = new ArrayList<NameValuePair>(2);
		parametros.add(new BasicNameValuePair("usuario", usuario));
		parametros.add(new BasicNameValuePair("token", token));
		JSONDownloaderTask<JSONArray> jdt = new JSONDownloaderTask<JSONArray>(
				getResources().getString(R.string.categoriasService),
				JSONDownloaderTask.METODO_POST, parametros, true);
		jdt.setOnFinishDownload(new CategoriaDownloadListener());
		jdt.setOnStartDownloadListener(new OnStartDownload() {
			@Override
			public void onStartDownload() {
				pd = new ProgressDialog(activity);
				pd.setTitle(getResources()
						.getString(R.string.titulo_procesando));
				pd.setMessage(getResources().getString(R.string.titulo_espere));
				pd.setCancelable(false);
				pd.setIndeterminate(true);
				pd.show();
			}
		});
		jdt.execute();
	}

	private void setupActionBar() {
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
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
		case R.id.action_adjuntar_imagen:
			adjuntarImagen();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void adjuntarImagen() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			try {
				adapter.add(picturePath);
				adapter.notifyDataSetChanged();
			} catch (Exception ex) {
				Log.e("Error", "Ocurrio un error grave", ex);
			}
		}
	}
	
	public void removerImagen(View v){
		adapter.remove((String) v.getTag());
		adapter.notifyDataSetChanged();
	}

	private void guardarAnuncio() {
		
		Anuncio anuncio = new Anuncio();
		anuncio.setTituloAnuncio(this.edtTitulo.getText().toString());
		anuncio.setDescripcionAnuncio(this.edtDescripcion.getText().toString());
		anuncio.setCategoriaId(categoriaSeleccionada);
		anuncio.setPrecio(this.edtPrecio.getText().toString());
		anuncio.setTelefono(this.edtTelefono.getText().toString());
		anuncio.setLatitud(UbicacionUtility.getLatitud(activity));
		anuncio.setAltitud(UbicacionUtility.getAltitud(activity));
		
		Intent intent = new Intent(this, PublicarAnuncioService.class);
		intent.putExtra(PublicarAnuncioService.ANUNCIO_EXTRA, anuncio);
		
		int totalImagenes = adapter.getCount();
		if(totalImagenes > 0){
			ArrayList<String> rutasImagenes = new ArrayList<String>();
			for (int i = 0; i < totalImagenes; i++) {
				rutasImagenes.add(adapter.getItem(i));
			}
			intent.putStringArrayListExtra(PublicarAnuncioService.ANUNCIO_IMAGENES_EXTRA, rutasImagenes);
		}
		startService(intent);
		finish();
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
			
			pd.dismiss();
		}

		@Override
		public void loadError() {
			// TODO Auto-generated method stub

		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Categoria c = categorias.get(pos);
		categoriaSeleccionada = c.getCodigoCategoria();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
