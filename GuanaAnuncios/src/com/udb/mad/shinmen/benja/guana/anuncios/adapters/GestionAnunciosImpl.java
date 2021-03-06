package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.udb.mad.shinmen.benja.guana.anuncios.AnunciosCercanosActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;

public class GestionAnunciosImpl implements GestionAnuncios,
		JSONDownloaderTask.OnFinishDownload<JSONArray> {

    public static final String LATITUD = "latitud";
    public static final String ALTITUD = "altitud";
    public static final String TOKEN = "token";
    public static final String USUARIO = "usuario";
    public static final String TITULO = "titulo";
    public static final String DESCRIPCION = "descripcion";
    public static final String CODIGO = "codigo";
    public static final String ID = "id";
    public static final String TELEFONO = "telefono";
    public static final String PRECIO = "precio";
    public static final String NOMBRE = "nombre";

	private JSONDownloaderTask<JSONArray> jsonTask;
	private AnunciosCercanosActivity activity;
	private AnunciosCercanosCustomAdapter adapter;
	private ListView listview;
	private GoogleMap mMap;
	public boolean notifyDataSetChanged = false;

	ArrayList<HashMap<String, Object>> anunciosList;

	@Override
	public ArrayList<HashMap<String, Object>> obtenerAnunciosCercanos(
			Double latitud, Double altitud, String usuario, String token,
			String url, AnunciosCercanosActivity activity, ListView listview,
			GoogleMap mMap) {

		this.activity = activity;
		this.listview = listview;
		this.mMap = mMap;

		/* Lista de parametros a enviar en el POST */
		List<NameValuePair> parametros = new ArrayList<NameValuePair>(1);
		parametros.add(new BasicNameValuePair(USUARIO, usuario));
		parametros.add(new BasicNameValuePair(TOKEN, token));
		parametros.add(new BasicNameValuePair(LATITUD, latitud.toString()));
		parametros.add(new BasicNameValuePair(ALTITUD, altitud.toString()));

		if(anunciosList == null || anunciosList.size()==0){
			anunciosList = new ArrayList<HashMap<String, Object>>();
		}

		/* Tarea asincrona que recupera los datos */
		jsonTask = new JSONDownloaderTask<JSONArray>(url,
				JSONDownloaderTask.METODO_POST, parametros);
		jsonTask.setOnFinishDownload(this);
		jsonTask.setJsonArray(true);
		jsonTask.execute();
		
		ArrayList<HashMap<String, Object>> anunciosList = new ArrayList<HashMap<String, Object>>();

		return anunciosList;
	}

	@Override
	public void onFinishDownload(JSONArray jsonArray) {

		HashMap<String, Object> anuncio;

		try {
			/*
			 * Obtenemos la lista de anuncios recibidos para pasarlo
			 * al adapter
			 * */
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonObject = jsonArray.getJSONObject(i);

				anuncio = new HashMap<String, Object>();
				anuncio.put(TITULO, jsonObject.getString(TITULO));
				anuncio.put(DESCRIPCION, jsonObject.getString(DESCRIPCION));
				anuncio.put(CODIGO, jsonObject.getString(ID));
				anuncio.put(LATITUD, jsonObject.getString(LATITUD));
				anuncio.put(ALTITUD, jsonObject.getString(ALTITUD));
				anuncio.put(TELEFONO, jsonObject.getString(TELEFONO));
                anuncio.put(PRECIO, jsonObject.getString(PRECIO));
                anuncio.put(USUARIO, jsonObject.getString(NOMBRE));
				
				String url = activity.getResources()
						.getString(R.string.imagenAnuncioService);//activity.findViewById(R.string.imagenAnuncioService).toString();
		        url = url.replace("{id}", jsonObject.getString(ID));
		        
		        anuncio.put(ALTITUD, jsonObject.getString(ALTITUD));
		        
				anunciosList.add(anuncio);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!notifyDataSetChanged){
			/*
			 * Se pasa la lista de anuncios al adapter para que los cargue en el
			 * listview
			 * */
			adapter = new AnunciosCercanosCustomAdapter(activity, anunciosList);
			listview.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		
		activity.findViewById(R.id.progressBarAnuncios).setVisibility(View.GONE);
		
		/*
		 * dibujamos los puntos en el mapa correspondientes a cada anuncio
		 * */
		dibujarPuntosAnuncios();
		
	}

	/*private void dibujarPuntosAnuncios() {

		if (anunciosList != null && anunciosList.size() > 0) {

			mMap.clear();

			for (HashMap<String, Object> anuncio : anunciosList) {

				CircleOptions circleOptions = new CircleOptions();
				Double lat = Double.valueOf(anuncio.get(LATITUD).toString());
				Double lon = Double.valueOf(anuncio.get(ALTITUD).toString());
				circleOptions.center(new LatLng(lat, lon));
				circleOptions.fillColor(Color.RED);
				circleOptions.radius(50);
				circleOptions.strokeWidth(1);
				@SuppressWarnings("unused")
				Circle circle = mMap.addCircle(circleOptions);

			}
			
		}		
		activity.flag_loading = false;
	}*/
    private void dibujarPuntosAnuncios() {

        mMap.clear();
        double lat = 0;
        double lon = 0;
        if (anunciosList != null && anunciosList.size() > 0) {

            for (HashMap<String, Object> anuncio : anunciosList) {

                CircleOptions circleOptions = new CircleOptions();
                lat = Double.valueOf(anuncio.get(LATITUD).toString());
                lon = Double.valueOf(anuncio.get(ALTITUD).toString());
                circleOptions.center(new LatLng(lat, lon));
                circleOptions.fillColor(Color.RED);
                circleOptions.radius(50);
                circleOptions.strokeWidth(1);
                
                mMap.addCircle(circleOptions);

            }
        }
        try {
	        LocationManager lm = (LocationManager) activity
	                .getSystemService(Context.LOCATION_SERVICE);
	        Location location = lm
	                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	        lat = location.getLatitude();
	        lon = location.getLongitude();
	     // Circulo que representa el area de auncios visibles
	        CircleOptions circleArea = new CircleOptions();
	        
	        Log.e("GuanaAnuncios", "Latitud=" + lat);
	        Log.e("GuanaAnuncios", "Longitud=" + lon);
	        circleArea.center(new LatLng(lat, lon));
	        circleArea.fillColor(0x3381F781);
	        circleArea.radius(1000);
	        circleArea.strokeWidth(1);
	        mMap.addCircle(circleArea);
        } catch(Exception e) {
        	Log.e("GestionAnuncioImpl", "No se pudo obtener la ubicacion actual", e);
        }

        /* para indicar que ya se cargo la data en el listview */
        activity.flag_loading = false;
    }
	
	

	@Override
	public void loadError() {
		// TODO Auto-generated method stub
	}

}
