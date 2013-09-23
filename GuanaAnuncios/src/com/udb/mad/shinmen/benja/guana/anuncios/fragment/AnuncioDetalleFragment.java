package com.udb.mad.shinmen.benja.guana.anuncios.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.adapters.ImagenRemotaAnuncioCustomAdapter;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Anuncio;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.PreferenciasUsuario;

public class AnuncioDetalleFragment extends Fragment {
	
	static final String ANUNCIO_DETAIL = "com.udb.mad.shinmen.benja.guana.anuncios.fragment.AnuncioDetalleFragment.ANUNCIO_DETAIL";
	static final String DUAL_PANE = "com.udb.mad.shinmen.benja.guana.anuncios.fragment.PersonajeDetailFragment.DUAL_PANE";
	String token;
	String usuario;
	Activity activity;
	ImagenRemotaAnuncioCustomAdapter adapter;
	ListView lvImagenes;
	Anuncio a;
	
	public static AnuncioDetalleFragment newInstance(Anuncio anuncio
	        , boolean dualPane) {
		AnuncioDetalleFragment p = new AnuncioDetalleFragment();
		Bundle b = new Bundle();
		b.putSerializable(ANUNCIO_DETAIL, anuncio);
		b.putBoolean(DUAL_PANE, dualPane);
		p.setArguments(b);
		return p;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		activity = getActivity();
		
		if (adapter == null) {
			adapter = new ImagenRemotaAnuncioCustomAdapter(activity);
		}
		
		lvImagenes = (ListView) getView().findViewById(R.id.lvImagenesDetalle);
		
		lvImagenes.setAdapter(adapter);
				
		token = PreferenciasUsuario.getToken(activity);
		usuario = PreferenciasUsuario.getUsuario(activity);
		
		String url = getResources().getString(R.string.listaImagenesAnuncioService).replace("{id}", a.getCodigoAnuncio());
		
		List<NameValuePair> parametros = new ArrayList<NameValuePair>(2);
		parametros.add(new BasicNameValuePair("usuario", usuario));
		parametros.add(new BasicNameValuePair("token", token));
		JSONDownloaderTask<JSONObject> jdt = new JSONDownloaderTask<JSONObject>(
				url,
				JSONDownloaderTask.METODO_GET, parametros);
		jdt.setOnFinishDownloadJSONObject(new ListaImagenesDownloadListener());
		jdt.execute();
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);
		if(getAnuncio().getUsuario() != null) {
			if(getAnuncio().getUsuario()
					.equalsIgnoreCase(PreferenciasUsuario.getUsuario(activity)))
			{
				inflater.inflate(R.menu.anuncio_detalle, menu);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TextView textView = null;
		a  = getAnuncio();
		View v = inflater.inflate(R.layout.activity_anuncio_detalle,container, false);
		
		textView = (TextView) v.findViewById(R.id.tvTituloDetalle);
		textView.setText(a.getTituloAnuncio());
		
		textView = (TextView) v.findViewById(R.id.tvDescripcionDetalle);
		textView.setText(a.getDescripcionAnuncio());
		
		textView = (TextView) v.findViewById(R.id.tvPrecioDetalle);
		textView.setText(a.getPrecio());
		
		textView = (TextView) v.findViewById(R.id.tvCorreoDetalle);
		textView.setText(a.getCorreo());
		
		textView = (TextView) v.findViewById(R.id.tvTelefonoDetalle);
		textView.setText(a.getTelefono());
			
		return v;
	}
	
	private Anuncio getAnuncio(){
		return ((Anuncio) getArguments().getSerializable(ANUNCIO_DETAIL));
	}
	
	public boolean isDualPane() {
		return getArguments().getBoolean(DUAL_PANE);
	}

	private class ListaImagenesDownloadListener implements
	JSONDownloaderTask.OnFinishDownloadJSONObject<JSONObject> {
	
		@Override
		public void onFinishDownloadJSONObject(JSONObject json) {
			
			try {
				
				String status = json.getString("estado");
				
				if(status.equals("1")){
					JSONArray imagenes = json.getJSONArray("imagenes");
					
					for (int i = 0; i < imagenes.length(); i++) {
						adapter.add(imagenes.getString(i));
					}
				}else{
					//TODO log errors
				}
			} catch (JSONException e) {
				Log.e("GuanaAnuncios", e.getMessage());
			}
		
			adapter.notifyDataSetChanged();
		}
		
		@Override
		public void loadError() {
			// TODO Auto-generated method stub
		
		}

	}
	
}
