package com.udb.mad.shinmen.benja.guana.anuncios.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.udb.mad.shinmen.benja.guana.anuncios.adapters.AnuncioCustomAdapter;
import com.udb.mad.shinmen.benja.guana.anuncios.listeners.EndlessScrollListener;
import com.udb.mad.shinmen.benja.guana.anuncios.listeners.EndlessScrollListener.onScrollEndListener;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Anuncio;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;

public class AnunciosListFragment extends ListFragment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8964105318347238707L;

	EndlessScrollListener scrollListener;
	List<Anuncio> anuncios;
	AnuncioCustomAdapter adapter;
	Activity activity;
	SharedPreferences prefs;
	String token;
	String usuario;
	String query;
	String PAGE_MARK = "{page}";
	String LIMIT_MARK = "{limit}";
	int page = 0;
	int limit = 8;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		anuncios = new ArrayList<Anuncio>();
		
		prefs = getActivity().getSharedPreferences("GuanaAnunciosPreferences",
				Context.MODE_PRIVATE);
		token = /* prefs.getString(GestionLoginImpl.TOKEN,null) */"e10adc3949ba59abbe56e057f20f883e";
		usuario = /* prefs.getString(GestionLoginImpl.USUARIO, null) */"test";

		if(adapter == null){
			adapter = new AnuncioCustomAdapter(getActivity());
		}
		
		setListAdapter(adapter);
		
		scrollListener = new EndlessScrollListener(new onScrollEndListener() {
			@Override
			public void onEnd(int page) {
				cargarAnuncios(page);
			}
		});
		
		getListView().setOnScrollListener(scrollListener);
		
		setListShownNoAnimation(false);
		
		handleIntent(getActivity().getIntent()); 

	}

	private void handleIntent(Intent intent){
		if(Intent.ACTION_SEARCH.equals(intent.getAction())){
			query = intent.getExtras().getString(SearchManager.QUERY);
			buscar();
		}else{
			cargarAnuncios(page);
		}
	}
	
	private void buscar() {
		setListShownNoAnimation(false);
		page = 0;
		adapter.clear();
		scrollListener.setCurrentPage(page);
		scrollListener.setPreviousTotal(0);
		cargarAnuncios(page);
	}

	private void cargarAnuncios(int page) {
		
		this.page = page;

		List<NameValuePair> parametros = new ArrayList<NameValuePair>(3);
		parametros.add(new BasicNameValuePair("usuario", usuario));
		parametros.add(new BasicNameValuePair("token", token));
		parametros.add(new BasicNameValuePair("texto", query));

		String url = "http://guananuncio.madxdesign.com/index.php/anuncio/anunciosbusqueda/{page}/{limit}";

		url = url.replace(PAGE_MARK, page + "");
		url = url.replace(LIMIT_MARK, limit + "");

		JSONDownloaderTask<JSONArray> jdt = new JSONDownloaderTask<JSONArray>(
				url, JSONDownloaderTask.METODO_POST, parametros, true);
		jdt.setOnFinishDownload(new BusquedaAnunciosListener());

		jdt.execute();

	}

	public void refrescarLista(){
		setListShownNoAnimation(false);
		query = null;
		page = 0;
		adapter.clear();
		scrollListener.setCurrentPage(page);
		scrollListener.setPreviousTotal(0);
		cargarAnuncios(page);
	}
	
	private class BusquedaAnunciosListener implements
			JSONDownloaderTask.OnFinishDownload<JSONArray> {

		@Override
		public void onFinishDownload(JSONArray json) {
			Anuncio anuncio;
			
			try {
				for (int i = 0; i < json.length(); i++) {

					JSONObject jsonObject = json.getJSONObject(i);

					anuncio = new Anuncio();
					anuncio.setCodigoAnuncio(jsonObject.getString("id"));
					anuncio.setTituloAnuncio(jsonObject.getString("titulo"));
					anuncio.setDescripcionAnuncio(jsonObject.getString("descripcion"));
					adapter.add(anuncio);
				}
			} catch (Exception e) {
				Log.e("error", e.getMessage());
			}
			
			adapter.notifyDataSetChanged();
			AnunciosListFragment.this.setListShownNoAnimation(true);
		}
	}
}