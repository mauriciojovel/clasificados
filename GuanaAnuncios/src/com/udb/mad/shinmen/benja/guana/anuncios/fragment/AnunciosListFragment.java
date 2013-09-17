package com.udb.mad.shinmen.benja.guana.anuncios.fragment;

import java.io.Serializable;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import com.udb.mad.shinmen.benja.guana.anuncios.R;

public class AnunciosListFragment extends ListFragment implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8964105318347238707L;
	
	private String[] dummyData = { "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit."};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(getActivity(),
	            R.layout.anuncio_list_item,R.id.tituloAnuncio, dummyData));
	}

}
