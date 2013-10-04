package com.udb.mad.shinmen.benja.guana.anuncios;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.udb.mad.shinmen.benja.guana.anuncios.adapters.GestionUsuariosImpl;

public class RegistroActivity extends ActionBarActivity implements
		OnEditorActionListener, OnKeyListener,
		OnItemSelectedListener {

	private EditText edtRegCorreo, edtRegPass, edtRegAlias;
	private Spinner spnPaises;
	private String codigoPais = "-1";
	//private Button btnRegistrar, btnCancelar;
	private int spinnerPositionSelected = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		// Show the Up button in the action bar.
		setupActionBar();

		edtRegAlias = (EditText) findViewById(R.id.edtRegAlias);
		edtRegCorreo = (EditText) findViewById(R.id.edtRegCorreo);
		edtRegPass = (EditText) findViewById(R.id.edtRegPass);
		spnPaises = (Spinner) findViewById(R.id.spnPaises);

		/* poblando el spinner de paises */
		if (savedInstanceState != null) {
			spinnerPositionSelected = savedInstanceState.getInt("position");
		}

		addItemsOnSpinner();

		edtRegAlias.setOnEditorActionListener(this);
		edtRegAlias.setOnKeyListener(this);

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("position", spinnerPositionSelected);

	}

	private void registrarUsuario() {

		GestionUsuariosImpl gu = new GestionUsuariosImpl(this);

		if (codigoPais != null && !codigoPais.equals("")
				&& !codigoPais.equals("-1")
				&& !edtRegAlias.getText().toString().equals("")
				&& !edtRegCorreo.getText().toString().equals("")
				&& !edtRegPass.getText().toString().equals("")) {

			/* Obteniendo la longitud y latitud del usuario */
			LocationManager lm = 
			        (LocationManager) getSystemService(Context.LOCATION_SERVICE)
			        ;
			Location location = lm
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			Double longitud = 0D;
			Double latitud = 0D;
			if (location != null) {
				longitud = location.getLongitude();
				latitud = location.getLatitude();
			}

			gu.registrarUsuario(codigoPais, edtRegCorreo.getText().toString(),
					edtRegPass.getText().toString(), edtRegAlias.getText()
							.toString(), null, latitud.toString(), longitud
							.toString());

		} else {
			Toast.makeText(this, "Por favor llene todos los campos!",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void addItemsOnSpinner() {

		GestionUsuariosImpl gu = new GestionUsuariosImpl(this);

		String url = getResources().getString(R.string.paisesService);

		/* Aqui se obtinen los paises mediante JSON y se puebla el spinner */
		gu.obtenerPoblarPaises(spnPaises, R.layout.pais_spinner_layout,
				spinnerPositionSelected, url);

		spnPaises.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View v,
					int position, long id) {

				if (v != null) {
					TextView txt = (TextView) v
							.findViewById(R.id.txtCodigoPais);
					// Get selected row data to show on screen
					codigoPais = txt.getText().toString();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {}

		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	private void setupActionBar() {
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.registro, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
		case R.id.action_cancelar:
		    NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_registrar:
		    registrarUsuario();
		    return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

		if (actionId == EditorInfo.IME_ACTION_DONE) {
			registrarUsuario();
		}

		return false;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {

		if (event.getKeyCode() == EditorInfo.IME_ACTION_DONE) {
			registrarUsuario();
		}

		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {

		spinnerPositionSelected = pos;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}