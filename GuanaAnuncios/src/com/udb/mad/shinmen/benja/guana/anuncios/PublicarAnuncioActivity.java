package com.udb.mad.shinmen.benja.guana.anuncios;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.Spinner;

import com.udb.mad.shinmen.benja.guana.anuncios.model.Categoria;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;

public class PublicarAnuncioActivity extends ActionBarActivity implements
		JSONDownloaderTask.OnFinishDownload<JSONArray> {

	Spinner spCategorias;
	List<Categoria> categorias;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publicar_anuncio);

		spCategorias = (Spinner) findViewById(R.id.spCategorias);

		List<NameValuePair> parametros = new ArrayList<NameValuePair>(1);
		parametros.add(new BasicNameValuePair("token",
				"e10adc3949ba59abbe56e057f20f883e"));
		JSONDownloaderTask<JSONArray> jdt = new JSONDownloaderTask<JSONArray>(
				"http://guananuncio.madxdesign.com/index.php/anuncio/categorias",
				JSONDownloaderTask.METODO_POST, parametros,true);
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
	public void onFinishDownload(JSONArray json) {
		
		Categoria categoria;
		
		this.categorias = new ArrayList<Categoria>();
		
		try{
			for (int i = 0; i < json.length(); i++) {

				JSONObject jsonObject = json.getJSONObject(i);

				categoria = new Categoria();
				categoria.setCodigoCategoria(jsonObject.getString("id"));
				categoria.setNombreCategoria(jsonObject.getString("nombre"));
				categorias.add(categoria);
			}
		}catch(Exception e){
			//TODO
		}
		
		/*ListAdapter adapter = new SimpleAdapter(this, categorias,
				android.R.layout.simple_spinner_item,
                new String[] { "id", "nombre"}, new int[] {
                        R.id.name, R.id.email, R.id.mobile });*/
 
       // setListAdapter(adapter);
	}

}
