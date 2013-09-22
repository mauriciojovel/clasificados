package com.udb.mad.shinmen.benja.guana.anuncios.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Anuncio;

public class AnuncioDetalleFragment extends Fragment {
	
	static final String ANUNCIO_DETAIL = "com.udb.mad.shinmen.benja.guana.anuncios.fragment.AnuncioDetalleFragment.ANUNCIO_DETAIL";
	static final String DUAL_PANE = "com.udb.mad.shinmen.benja.guana.anuncios.fragment.PersonajeDetailFragment.DUAL_PANE";
	
	
	public static AnuncioDetalleFragment newInstance(Anuncio a
	        , boolean dualPane) {
		AnuncioDetalleFragment p = new AnuncioDetalleFragment();
		Bundle b = new Bundle();
		b.putSerializable(ANUNCIO_DETAIL, a);
		b.putBoolean(DUAL_PANE, dualPane);
		p.setArguments(b);
		return p;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		TextView textView = null;
		Anuncio a  = getAnuncio();
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

}
