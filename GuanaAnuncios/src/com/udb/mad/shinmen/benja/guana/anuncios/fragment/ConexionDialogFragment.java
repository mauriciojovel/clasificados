package com.udb.mad.shinmen.benja.guana.anuncios.fragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.udb.mad.shinmen.benja.guana.anuncios.AnuncioActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.LoginActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.adapters.AnuncioCustomAdapter;
import com.udb.mad.shinmen.benja.guana.anuncios.listeners.EndlessScrollListener;
import com.udb.mad.shinmen.benja.guana.anuncios.listeners.EndlessScrollListener.onScrollEndListener;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Anuncio;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.PreferenciasUsuario;

public class ConexionDialogFragment extends DialogFragment {

	private ToggleButton wifi;
	private ToggleButton tresGe;
	private boolean finishActivity = true;

	public ConexionDialogFragment() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.connection_dialog, null);

		// wifi = (ToggleButton) view.findViewById(R.id.togWifi);
		tresGe = (ToggleButton) view.findViewById(R.id.tog3g);

		return builder
				.setView(
						getActivity().getLayoutInflater().inflate(
								R.layout.connection_dialog, null))
				.setTitle("Dialogo de Conexión")
				.setPositiveButton("Aceptar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								wifi = (ToggleButton) getDialog().findViewById(
										R.id.togWifi);
								tresGe = (ToggleButton) getDialog()
										.findViewById(R.id.tog3g);

								if (wifi.isChecked()) {
									WifiManager wifiManager = (WifiManager) (WifiManager) getActivity()
											.getSystemService(
													Context.WIFI_SERVICE);
									boolean result = wifiManager
											.setWifiEnabled(true);
									if (result) {
										autenticado();
									} else {
										getActivity().finish();
										Toast.makeText(
												getActivity(),
												"No es posible activar la WiFi",
												Toast.LENGTH_SHORT).show();
									}
								} else if (tresGe.isChecked()) {
									setMobileDataEnabled(getActivity(), true);
									autenticado();
								} else {

									getActivity().finish();
									Toast.makeText(getActivity(),
											"No hay conexión a internet",
											Toast.LENGTH_SHORT).show();
								}

							}
						})
				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								getActivity().finish();
							}
						}).create();
	}

	private void setMobileDataEnabled(Context context, boolean enabled) {
		try {
			final ConnectivityManager conman = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			final Class conmanClass = Class
					.forName(conman.getClass().getName());
			final Field iConnectivityManagerField = conmanClass
					.getDeclaredField("mService");
			iConnectivityManagerField.setAccessible(true);
			final Object iConnectivityManager = iConnectivityManagerField
					.get(conman);
			final Class iConnectivityManagerClass = Class
					.forName(iConnectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);

			setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		} catch (Exception e) {
			getActivity().finish();
			Toast.makeText(getActivity(),
					"Imposible cambiar estado de conexión", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void autenticado() {
		if (PreferenciasUsuario.isUsuarioAutenticado(getActivity())) {
			finishActivity = false;
			getDialog().dismiss();
		} else {
			// Se tiene que levantar la actividad de login y finalizar esta
			// actividad.
			showLogin();
		}
	}

	private void showLogin() {
		Intent i = new Intent(getActivity(), LoginActivity.class);
		startActivity(i);
		getActivity().finish();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		//si el dialogo se esta cerrando y el usuario esta logeado y hay conexion a internet
		//no es necesario terminar la actividad
		if(finishActivity){
			getActivity().finish();
		}
		
		super.onDismiss(dialog);
	}
	
	
}
