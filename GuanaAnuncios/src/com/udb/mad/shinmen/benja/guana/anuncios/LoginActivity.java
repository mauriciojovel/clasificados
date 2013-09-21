package com.udb.mad.shinmen.benja.guana.anuncios;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.udb.mad.shinmen.benja.guana.anuncios.adapters.GestionLoginImpl;

public class LoginActivity extends ActionBarActivity implements
		OnClickListener, OnEditorActionListener, OnKeyListener {

	private EditText edtUsuario;
	private EditText edtPassword;
	private Button btnLogin;
	private Button btnRegistrar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		edtUsuario = (EditText) findViewById(R.id.edtUsuario);
		edtPassword = (EditText) findViewById(R.id.edtPassword);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

		btnLogin.setOnClickListener(this);
		btnRegistrar.setOnClickListener(this);
		edtPassword.setOnKeyListener(this);
		edtPassword.setOnEditorActionListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onClick(View view) {

		if (btnLogin.getId() == view.getId()) {			
			validar();

		} else if (btnRegistrar.getId() == view.getId()) {
			// Toast.makeText(this, "Presiono Registrar!!..",
			// Toast.LENGTH_SHORT).show();
			Intent intento = new Intent(this, RegistroActivity.class);
			startActivity(intento);
		}

	}
	
	private void validar(){
		findViewById(R.id.btnLogin).setVisibility(View.GONE);
		findViewById(R.id.btnRegistrar).setVisibility(View.GONE);
		findViewById(R.id.progressBarLogin).setVisibility(View.VISIBLE);
		
		String usuario = edtUsuario.getText().toString();
		String pass = edtPassword.getText().toString();

		if (usuario != null && !usuario.equals("") && pass != null
				&& !pass.equals("")) {
			logear(usuario, pass);
		} else {
			Toast.makeText(this, "Por favor ingrese los campos requeridos",
					Toast.LENGTH_SHORT).show();
		}
		
	}

	private void logear(String usuario, String pass) {

		GestionLoginImpl gl = new GestionLoginImpl();
		
		String url = getResources().getString(R.string.loginService);
		
		gl.loginUsuario(usuario, pass, url, this);		
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		
		if(actionId == EditorInfo.IME_ACTION_DONE){
			validar();
		}
		
		return false;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		
		if(event.getKeyCode()==EditorInfo.IME_ACTION_DONE){
			validar();
		}
		
		return false;
	}

}
