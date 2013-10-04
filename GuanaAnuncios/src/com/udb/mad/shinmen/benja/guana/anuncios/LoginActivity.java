package com.udb.mad.shinmen.benja.guana.anuncios;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.udb.mad.shinmen.benja.guana.anuncios.adapters.GestionLoginImpl;

public class LoginActivity extends ActionBarActivity implements 
    OnEditorActionListener, OnKeyListener {

	private EditText edtUsuario;
	private EditText edtPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		edtUsuario = (EditText) findViewById(R.id.edtUsuario);
		edtPassword = (EditText) findViewById(R.id.edtPassword);

		/*btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

		btnLogin.setOnClickListener(this);
		btnRegistrar.setOnClickListener(this);*/
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
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
        case R.id.action_registrar:
            registrar();
            return true;
        case R.id.action_login:
            validar();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}
	
	private void registrar() {
	    Intent intento = new Intent(this, RegistroActivity.class);
        startActivity(intento);
        // Finalizamos esta forma por si se hace todo el proceso y despues
        // se hace back
        finish();
	}
	
	private void validar(){
		String usuario = edtUsuario.getText().toString();
		String pass = edtPassword.getText().toString();
		showProgressBar();
		
		if (usuario != null && !usuario.equals("") && pass != null
				&& !pass.equals("")) {
			logear(usuario, pass);
		} else {
			Toast.makeText(this, "Por favor ingrese los campos requeridos",
					Toast.LENGTH_SHORT).show();
			hiddenProgressBar();
		}
	}
	
	private void showProgressBar() {
	    /*findViewById(R.id.btnLogin).setVisibility(View.GONE);
        findViewById(R.id.btnRegistrar).setVisibility(View.GONE);*/
        findViewById(R.id.progressBarLogin).setVisibility(View.VISIBLE);
	}
	
	private void hiddenProgressBar() {
	    /*findViewById(R.id.btnLogin).setVisibility(View.VISIBLE);
        findViewById(R.id.btnRegistrar).setVisibility(View.VISIBLE);*/
        findViewById(R.id.progressBarLogin).setVisibility(View.GONE);
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
