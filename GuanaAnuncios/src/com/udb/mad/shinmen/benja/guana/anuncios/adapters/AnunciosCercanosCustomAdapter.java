package com.udb.mad.shinmen.benja.guana.anuncios.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udb.mad.shinmen.benja.guana.anuncios.R;

public class AnunciosCercanosCustomAdapter extends BaseAdapter{

	private Activity activity;
    private ArrayList<HashMap<String, Object>> data;
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader; 
    
    public AnunciosCercanosCustomAdapter(Activity a, ArrayList<HashMap<String, Object>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		HashMap<String, Object> map = data.get(position);
		return map;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.anuncios_cercanos_layout, null);
 
        TextView title = (TextView)vi.findViewById(R.id.title);
        TextView description = (TextView)vi.findViewById(R.id.description); 
 
        HashMap<String, Object> dato = new HashMap<String, Object>();
        dato = data.get(position);
 
        // Setting all values in listview
        title.setText(String.valueOf(dato.get("titulo")));
        description.setText((String)dato.get("descripcion"));
        
        
        return vi;
	}
		

}
