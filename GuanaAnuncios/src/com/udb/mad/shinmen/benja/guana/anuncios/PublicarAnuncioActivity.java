package com.udb.mad.shinmen.benja.guana.anuncios;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
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
import android.widget.Toast;

import com.udb.mad.shinmen.benja.guana.anuncios.adapters.ImagenAnuncioCustomAdapter;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Categoria;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;
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

		List<NameValuePair> parametros = new ArrayList<NameValuePair>(2);
		parametros.add(new BasicNameValuePair("usuario", usuario));
		parametros.add(new BasicNameValuePair("token", token));
		JSONDownloaderTask<JSONArray> jdt = new JSONDownloaderTask<JSONArray>(
				"http://guananuncio.madxdesign.com/index.php/anuncio/categorias",
				JSONDownloaderTask.METODO_POST, parametros, true);
		jdt.setOnFinishDownload(new CategoriaDownloadListener());
		jdt.execute();
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

	private void guardarAnuncio() {
		List<NameValuePair> parametros = new ArrayList<NameValuePair>(8);
		parametros.add(new BasicNameValuePair("usuario", usuario));
		parametros.add(new BasicNameValuePair("token", token));
		parametros.add(new BasicNameValuePair("titulo", this.edtTitulo
				.getText().toString()));
		parametros.add(new BasicNameValuePair("descripcion",
				this.edtDescripcion.getText().toString()));
		parametros.add(new BasicNameValuePair("categoria_id",
				categoriaSeleccionada));
		parametros.add(new BasicNameValuePair("precio", this.edtPrecio
				.getText().toString()));
		parametros.add(new BasicNameValuePair("telefono", this.edtTelefono
				.getText().toString()));

		JSONDownloaderTask<JSONObject> jdt = new JSONDownloaderTask<JSONObject>(
				"http://guananuncio.madxdesign.com/index.php/anuncio/save",
				JSONDownloaderTask.METODO_POST, parametros);
		jdt.setOnFinishDownloadJSONObject(new PublicarAnuncioListener());
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

	private boolean subirImagenes(String idAnuncio) {

		Bitmap bm = BitmapFactory.decodeFile(adapter.getItem(0), null);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, stream);
		byte[] byte_arr = stream.toByteArray();
		String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);

		List<NameValuePair> parametros = new ArrayList<NameValuePair>(4);
		parametros.add(new BasicNameValuePair("usuario", usuario));
		parametros.add(new BasicNameValuePair("token", token));
		parametros.add(new BasicNameValuePair("imagen", image_str));
		parametros.add(new BasicNameValuePair("anuncio_id", idAnuncio));

		JSONDownloaderTask<JSONObject> jdt = new JSONDownloaderTask<JSONObject>(
				"http://guananuncio.madxdesign.com/index.php/imagen/save",
				JSONDownloaderTask.METODO_POST, parametros);
		jdt.setOnFinishDownloadJSONObject(new SubirImagenListener());
		jdt.execute();

		return false;
	}

	private class SubirImagenListener implements
			JSONDownloaderTask.OnFinishDownloadJSONObject<JSONObject> {
		@Override
		public void onFinishDownloadJSONObject(JSONObject jsonObject) {
			pd.dismiss();
			try {
				String status = jsonObject.getString("estado");
				if (status.equals("1")) {
					Toast.makeText(activity, "Anuncio Publicado",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					if (status.equals("-1")) {
						Toast.makeText(
								activity,
								"Ocurrio un error inesperado. Vuelva a intentar.",
								Toast.LENGTH_SHORT).show();
					}
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

	private class PublicarAnuncioListener implements
			JSONDownloaderTask.OnFinishDownloadJSONObject<JSONObject> {
		@Override
		public void onFinishDownloadJSONObject(JSONObject jsonObject) {
			try {
				String status = jsonObject.getString("estado");
				if (status.equals("1")) {
					String id = jsonObject.getString("id");
					subirImagenes(id);
				} else {
					if (status.equals("-1")) {
						pd.dismiss();
						Toast.makeText(
								activity,
								"Ocurrio un error inesperado. Vuelva a intentar.",
								Toast.LENGTH_SHORT).show();
					}
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
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Categoria c = categorias.get(pos);
		categoriaSeleccionada = c.getCodigoCategoria();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}