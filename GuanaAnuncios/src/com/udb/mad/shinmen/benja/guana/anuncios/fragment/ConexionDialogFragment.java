package com.udb.mad.shinmen.benja.guana.anuncios.fragment;

import com.udb.mad.shinmen.benja.guana.anuncios.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

public class ConexionDialogFragment extends DialogFragment{
	
	private Switch swWifi;
	private Switch sw3g;
	
	public ConexionDialogFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.connection_dialog, container);
		swWifi = (Switch) view.findViewById(R.id.swWifi);
		sw3g = (Switch) view.findViewById(R.id.sw3g);
		getDialog().setTitle("Dialogo de Conexión");
		
		return view;
	}
	
	
}
