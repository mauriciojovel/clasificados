package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.RegistroActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.model.PaisSpinner;

public class PaisCustomAdapter extends BaseAdapter {

	private ArrayList<Map<String, Object>> data;
	PaisSpinner tempValues = null;
	LayoutInflater inflater;

	public PaisCustomAdapter(RegistroActivity activity, int textViewResourceId,
			ArrayList<Map<String, Object>> objects) {
		
		data = objects;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		Map<String, Object> map = data.get(position);
		return map;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// This funtion called for each row ( Called data.size() times )
	public View getView(int position, View convertView, ViewGroup parent) {

		/********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
		View row = inflater.inflate(R.layout.pais_spinner_layout, parent, false);

		/***** Get each Model object from Arraylist ********/
		Map<String, Object> tempValues = null;
		tempValues = data.get(position);

		TextView codigo = (TextView) row.findViewById(R.id.txtCodigoPais);
		TextView nombre = (TextView) row.findViewById(R.id.txtNombrePais);

		// Set values for spinner each row
		codigo.setText(tempValues.get("codigoPais").toString());
		nombre.setText(tempValues.get("nombre").toString());
		
		return row;
	}
}