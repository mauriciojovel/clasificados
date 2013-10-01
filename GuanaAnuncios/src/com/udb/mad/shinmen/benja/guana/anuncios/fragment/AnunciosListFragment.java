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
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.udb.mad.shinmen.benja.guana.anuncios.AnuncioDetalleActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.LoginActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.adapters.AnuncioCustomAdapter;
import com.udb.mad.shinmen.benja.guana.anuncios.listeners.EndlessScrollListener;
import com.udb.mad.shinmen.benja.guana.anuncios.listeners.EndlessScrollListener.onScrollEndListener;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Anuncio;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.PreferenciasUsuario;

public class AnunciosListFragment extends ListFragment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8964105318347238707L;
	static final String CURR_POS_DETAIL = "com.udb.mad.shinmen.benja.guana.anuncios.fragment.AnunciosListFragment.CURR_POS";

	EndlessScrollListener scrollListener;
	List<Anuncio> anuncios;
	AnuncioCustomAdapter adapter;
	Activity activity;
	String token;
	String usuario;
	String query;
	String PAGE_MARK = "{page}";
	String LIMIT_MARK = "{limit}";
	int page = 0;
	int limit = 8;
	int currentPos=-1;
	boolean dualPane;
	boolean showMyCommercial = false;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean connected = false;
        ConnectivityManager connectivityManager = 
                (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!= null 
                &&connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState() == android.net.NetworkInfo.State.CONNECTED)
                || (connectivityManager.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE) != null && 
                        connectivityManager.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE).getState() 
                        == android.net.NetworkInfo.State.CONNECTING)
                || connectivityManager.getNetworkInfo(
                        ConnectivityManager.TYPE_WIFI).getState() 
                        == android.net.NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(
                        ConnectivityManager.TYPE_WIFI).getState() 
                        == android.net.NetworkInfo.State.CONNECTING) {
            // we are connected to a network

            connected = true;
        } else {
            connected = false;
        }
        if (connected) {
            if (PreferenciasUsuario.isUsuarioAutenticado(getActivity())) {
                anuncios = new ArrayList<Anuncio>();
                token = PreferenciasUsuario.getToken(getActivity());
                usuario = PreferenciasUsuario.getUsuario(getActivity());

                if (adapter == null) {
                    adapter = new AnuncioCustomAdapter(getActivity());
                }
                setListAdapter(adapter);
                scrollListener = new EndlessScrollListener(
                        new onScrollEndListener() {
                            @Override
                            public void onEnd(int page) {
                                cargarAnuncios(page);
                            }
                        });
                getListView().setOnScrollListener(scrollListener);
                setListShownNoAnimation(false);
                handleIntent(getActivity().getIntent());
                setHasOptionsMenu(true);
            } else {
                // Se tiene que levantar la actividad de login y finalizar esta
                // actividad.
                showLogin();
            }
        } else {
            showEditDialog();
        }
		/*if (PreferenciasUsuario.isUsuarioAutenticado(getActivity())) {
			anuncios = new ArrayList<Anuncio>();
			token = PreferenciasUsuario.getToken(getActivity());
			usuario = PreferenciasUsuario.getUsuario(getActivity());
			
			if (adapter == null) {
				adapter = new AnuncioCustomAdapter(getActivity());
			}
			setListAdapter(adapter);
			scrollListener = new EndlessScrollListener(
					new onScrollEndListener() {
						@Override
						public void onEnd(int page) {
							cargarAnuncios(page);
						}
					});
			getListView().setOnScrollListener(scrollListener);
			setListShownNoAnimation(false);
			handleIntent(getActivity().getIntent());
			setHasOptionsMenu(true);
		} else {
			// Se tiene que levantar la actividad de login y finalizar esta
			// actividad.
			showLogin();
		}*/
	}
	
	private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        ConexionDialogFragment dialog = new ConexionDialogFragment();
        dialog.show(fm, "conexion_dialog_fragment");
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_mis_anuncios:
			showMyCommercial = !showMyCommercial;
			if(showMyCommercial) {
				item.setTitle(R.string.todos_anuncios);
				item.setIcon(R.drawable.ic_action_todos_anuncios);
			} else {
				item.setTitle(R.string.mis_anuncios);
				item.setIcon(R.drawable.ic_action_mis_anuncios);
			}
			buscar();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
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

	private void showLogin() {
		Intent i = new Intent(getActivity(),LoginActivity.class);
		startActivity(i);
		getActivity().finish();
	}

	private void cargarAnuncios(int page) {
		
		this.page = page;

		List<NameValuePair> parametros = new ArrayList<NameValuePair>(3);
		parametros.add(new BasicNameValuePair("usuario", usuario));
		parametros.add(new BasicNameValuePair("token", token));
		parametros.add(new BasicNameValuePair("texto", query));
		if(showMyCommercial) {
			parametros.add(new BasicNameValuePair("nombre", usuario));
		}

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
					anuncio.setPrecio(jsonObject.getString("precio"));
					anuncio.setTelefono(jsonObject.getString("telefono"));
					anuncio.setUsuario(jsonObject.getString("nombre"));
					anuncio.setLatitud(jsonObject.getDouble("latitud"));
					anuncio.setAltitud(jsonObject.getDouble("altitud"));
					adapter.add(anuncio);
				}
			} catch (Exception e) {
				Log.e("error", e.getMessage());
			}
			
			if(adapter != null){
				adapter.notifyDataSetChanged();
			}
			if(AnunciosListFragment.this.getView() != null){
				AnunciosListFragment.this.setListShownNoAnimation(true);
			}
		}

		@Override
		public void loadError() {
			// supondremos que el error es porque no te has autenticado.
			showLogin();
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		this.currentPos = position;
		Anuncio a = null;
		if (currentPos >= 0) {
            a = adapter.getItem(currentPos);
            
            if (dualPane) {
            	//TODO add dual pane support
            } else {
                Intent i = new Intent();
                i.setClass(getActivity(), AnuncioDetalleActivity.class);
                i.putExtra(AnuncioDetalleFragment.ANUNCIO_DETAIL, a);
                i.putExtra(AnuncioDetalleFragment.DUAL_PANE, false);
                startActivity(i);
            }
        }
	}
}
