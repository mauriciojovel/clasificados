package com.udb.mad.shinmen.benja.guana.anuncios;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.udb.mad.shinmen.benja.guana.anuncios.adapters.AnunciosCercanosCustomAdapter;
import com.udb.mad.shinmen.benja.guana.anuncios.adapters.GestionAnunciosImpl;

public class AnunciosCercanosActivity extends ActionBarActivity implements
		OnItemClickListener, ConnectionCallbacks, OnConnectionFailedListener,
		LocationListener {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anuncios_cercanos);

		setUpMap();

		// Lista de anuncios cercanos
		listview = (ListView) findViewById(R.id.lstAnuncios);
		listview.setOnItemClickListener(this);

		// cargando la lista en el listview
		cargarListaAnunciosCercanos();

		// dibujando puntos en el mapa
		dibujarPuntosAnuncios();
	}

	private void cargarListaAnunciosCercanos() {

		GestionAnunciosImpl ga = new GestionAnunciosImpl();

		// latitud=13.689423119374966
		// longitud=-89.22594312248225
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			longitud = location.getLongitude();
			latitud = location.getLatitude();
		}
		anunciosList = ga.obtenerAnunciosCercanos(longitud, latitud);

		adapter = new AnunciosCercanosCustomAdapter(this, anunciosList);
		listview.setAdapter(adapter);

	}
	
	private void dibujarPuntosAnuncios() {

		if (anunciosList.size() > 0) {
			
			mMap.clear();
			
			for( HashMap<String, Object> anuncio : anunciosList ){
				
				CircleOptions circleOptions = new CircleOptions();
				Double lat = Double.valueOf(anuncio.get("latitud").toString());
				Double lon = Double.valueOf(anuncio.get("longitud").toString());
				circleOptions.center(new LatLng(lat, lon));
				circleOptions.fillColor(Color.RED);
				circleOptions.radius(50);
				circleOptions.strokeWidth(1);
				@SuppressWarnings("unused")
				Circle circle = mMap.addCircle(circleOptions);
				
			}
		}
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

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

}
