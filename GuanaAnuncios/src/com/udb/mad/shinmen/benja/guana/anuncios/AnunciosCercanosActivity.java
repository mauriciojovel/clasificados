package com.udb.mad.shinmen.benja.guana.anuncios;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.udb.mad.shinmen.benja.guana.anuncios.adapters.AnunciosCercanosCustomAdapter;
import com.udb.mad.shinmen.benja.guana.anuncios.adapters.GestionAnunciosImpl;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Anuncio;

public class AnunciosCercanosActivity extends ActionBarActivity implements
		OnItemClickListener, ConnectionCallbacks, OnConnectionFailedListener,
		LocationListener, OnScrollListener {

	public static final String TOKEN = "token";
	public static final String USUARIO = "usuario";
	
	static final String ANUNCIO_DETAIL = "com.udb.mad.shinmen.benja.guana.anuncios.fragment.AnuncioDetalleFragment.ANUNCIO_DETAIL";
	static final String DUAL_PANE = "com.udb.mad.shinmen.benja.guana.anuncios.fragment.PersonajeDetailFragment.DUAL_PANE";

	private int page = 0;
	private int limit = 5;
	public static final String PAGE_MARK = "{page}";
	public static final String LIMIT_MARK = "{limit}";

	private GoogleMap mMap;
	ListView listview;

	ArrayList<HashMap<String, Object>> anunciosList;

	AnunciosCercanosCustomAdapter adapter;
	private boolean firstloadlocation = false;

	private LocationClient mLocationClient;
	private Double longitud;
	private Double latitud;
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
	private int visibleThreshold = 1;
    public boolean flag_loading  = true;
    private GestionAnunciosImpl ga;
    
    int firstVisibleItem, visibleItemCount, totalItemCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anuncios_cercanos);

		setUpMap();

		// Lista de anuncios cercanos
		listview = (ListView) findViewById(R.id.lstAnuncios);
		listview.setOnItemClickListener(this);
		listview.setOnScrollListener(this);

		// cargando la lista en el listview
		cargarListaAnunciosCercanos(false);
		setupActionBar();
	}
	
	private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	private void cargarListaAnunciosCercanos(boolean notifyDataSetChanged) {
		
		findViewById(R.id.progressBarAnuncios).setVisibility(View.VISIBLE);
		
		if(ga == null){
			ga = new GestionAnunciosImpl();
		}

		/* Obteniendo la longitud y latitud del usuario */
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			longitud = location.getLongitude();
			latitud = location.getLatitude();
		} else {
		    longitud = 0.0;
            latitud = 0.0;
		}

		/* Obteniendo el usuario y token de las shared preferences */
		SharedPreferences prefs = getSharedPreferences(
				"GuanaAnunciosPreferences", Context.MODE_PRIVATE);
		String alias = prefs.getString(USUARIO, "");
		String token = prefs.getString(TOKEN, "");

		/* URL del servicio de donde se obtienen los anuncios */
		String url = getResources().getString(R.string.anunciosCercanosService);
		
		url = url.replace(PAGE_MARK, page + "");
		url = url.replace(LIMIT_MARK, limit + "");
		
		/*esto es para que el adapter del listview sepa si se trata de una actualizacion
		 * de los item de la lista*/
		ga.notifyDataSetChanged = notifyDataSetChanged;
		
		anunciosList = ga.obtenerAnunciosCercanos(longitud, latitud, alias,
				token, url, this, listview, mMap);		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.anuncios_cercanos, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUpMap();
		setUpLocationClient();
		mLocationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	private void setUpMap() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
			}
		}
	}

	private void setUpLocationClient() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		@SuppressWarnings("unchecked")
        HashMap<String, Object> item = (HashMap<String, Object>) 
                                        listview.getItemAtPosition(position);
		
		Anuncio anuncio = new Anuncio();
		anuncio.setCodigoAnuncio(item.get("codigo").toString());
		anuncio.setTituloAnuncio(item.get("titulo").toString());
		anuncio.setDescripcionAnuncio(item.get("descripcion").toString());
		anuncio.setPrecio(item.get("precio").toString());
		anuncio.setTelefono(item.get("telefono").toString());
		anuncio.setUsuario(item.get("usuario").toString());
		anuncio.setLatitud(Double.valueOf(item.get("latitud").toString()));
		anuncio.setAltitud(Double.valueOf(item.get("altitud").toString()));
		
		Intent i = new Intent();
		i.setClass(this, AnuncioDetalleActivity.class);
		i.putExtra(ANUNCIO_DETAIL, anuncio);
		i.putExtra(DUAL_PANE, false);
		startActivity(i);
		
	}

	@Override
	public void onLocationChanged(Location location) {

		if (!firstloadlocation) {
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					latLng, 15);
			mMap.animateCamera(cameraUpdate);
			firstloadlocation = true;

			longitud = location.getLongitude();
			latitud = location.getLatitude();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {

		mLocationClient.requestLocationUpdates(REQUEST, this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
		
		this.firstVisibleItem = firstVisibleItem;
		this.visibleItemCount = visibleItemCount;
		this.totalItemCount = totalItemCount;
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		if(totalItemCount >= limit * (page + 1) - 1){
			if( (firstVisibleItem + visibleItemCount - 1) >= (totalItemCount - visibleThreshold) ){
				if(!flag_loading){
					flag_loading = true;
					page++;
					cargarListaAnunciosCercanos(true);
				}
				
			}
		}
	}

}
