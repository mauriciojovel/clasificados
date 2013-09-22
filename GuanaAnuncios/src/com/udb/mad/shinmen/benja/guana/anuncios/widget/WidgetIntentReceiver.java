package com.udb.mad.shinmen.benja.guana.anuncios.widget;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.udb.mad.shinmen.benja.guana.anuncios.LoginActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.R;

public class WidgetIntentReceiver extends BroadcastReceiver {

	private static int position = 19;
	ArrayList<HashMap<String, Object>> anunciosList;
	Context ctx;

	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.ctx = context;
		
		anunciosList = (ArrayList<HashMap<String, Object>>) intent.getSerializableExtra("anunciosList");
		Log.e("GuanaAnuncio", "onReceive method, position = " + position);
		Log.e("GuanaAnuncio", "onReceive method, anunciosList.size = " + anunciosList.size());
		
		if(position > anunciosList.size()){
			position = anunciosList.size() - 1;
			Log.e("GuanaAnuncio", "onReceive method, position = " + position);
		}
		
		Log.e("GuanaAnuncio", "onReceive method, action = " + intent.getAction());
		if (intent
				.getAction()
				.equals("com.udb.mad.shinmen.benja.guana.anuncios.CAMBIAR_ANUNCIO_LEFT")) {
			Log.e("GuanaAnuncio", "onReceive method, entra al LEFT");
			
			if (anunciosList!=null && position <= anunciosList.size()) {
				if(anunciosList!=null && anunciosList.size()<=0) {
					position++;
					if(position > anunciosList.size()){
						position = anunciosList.size() - 1;
					}
					updateWidgetPictureAndButtonListener(context, position);
				}
			}

		} else if (intent
				.getAction()
				.equals("com.udb.mad.shinmen.benja.guana.anuncios.CAMBIAR_ANUNCIO_RIGHT")) {
			
			Log.e("GuanaAnuncio", "onReceive method, entra al RIGHT");
			if (position >= 1) {
				if(anunciosList!=null && anunciosList.size()>=1) {
					position--;
					updateWidgetPictureAndButtonListener(context, position);
				}				
			}

		}
	}

	private void updateWidgetPictureAndButtonListener(Context context,
			int position) {
		
		Log.e("GuanaAnuncio", "updateWidgetPictureAndButtonListener method");
		if (anunciosList != null && anunciosList.size() > 0) {

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			HashMap<String, Object> anuncio = anunciosList.get(position);
			
			Log.e("GuanaAnuncio", "updateWidgetPictureAndButtonListener method, titulo = " + anuncio.get("titulo").toString());
			String titulo = anuncio.get("titulo").toString();
			String descripcion = anuncio.get("descripcion").toString();

			remoteViews.setTextViewText(R.id.wgTitulo, titulo);
			remoteViews.setTextViewText(R.id.wgDescripcion, descripcion);

			// REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
			remoteViews.setOnClickPendingIntent(R.id.leftArrow,
					GuanaAnuncioWidget.buildLeftButtonPendingIntent(context, anunciosList));

			remoteViews.setOnClickPendingIntent(R.id.rightArrow,
					GuanaAnuncioWidget.buildRightButtonPendingIntent(context, anunciosList));

			GuanaAnuncioWidget.pushWidgetUpdate(
					context.getApplicationContext(), remoteViews);

		}
	}

	

	

	private void showLogin() {
		LoginActivity activity = new LoginActivity();
		Intent i = new Intent(activity, LoginActivity.class);
		activity.startActivity(i);
	}

	private void generarDummyData() {
		anunciosList = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> anuncio = new HashMap<String, Object>();
		anuncio.put("titulo", "1 Mouse inalambrico");
		anuncio.put("alias", "malaCara1");
		anuncio.put(
				"descripcion",
				"1 El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña tocaba el saxofón detrás del palenque de paja.");
		anuncio.put("imagen", "");
		anuncio.put("latitud", "13.690340434877442");
		anuncio.put("longitud", "-89.22508481559748");
		anunciosList.add(anuncio);

		anuncio = new HashMap<String, Object>();
		anuncio.put("titulo", "2 Galaxy S5");
		anuncio.put("alias", "ratero01");
		anuncio.put(
				"descripcion",
				"2 El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña tocaba el saxofón detrás del palenque de paja.");
		anuncio.put("imagen", "");
		anuncio.put("latitud", "13.690903331255466");
		anuncio.put("longitud", "-89.2275739055633");
		anunciosList.add(anuncio);

		anuncio = new HashMap<String, Object>();
		anuncio.put("titulo", "3 Adaptadores USB");
		anuncio.put("alias", "timorato");
		anuncio.put(
				"descripcion",
				"3 El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña tocaba el saxofón detrás del palenque de paja. El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña tocaba el saxofón detrás del palenque de paja. El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña tocaba el saxofón detrás del palenque de paja.");
		anuncio.put("imagen", "");
		anuncio.put("latitud", "13.687817808638007");
		anuncio.put("longitud", "-89.22544959602351");
		anunciosList.add(anuncio);

		anuncio = new HashMap<String, Object>();
		anuncio.put("titulo", "4 Tarjetas de red");
		anuncio.put("alias", "cachada");
		anuncio.put(
				"descripcion",
				"4 El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña tocaba el saxofón detrás del palenque de paja.");
		anuncio.put("imagen", "");
		anuncio.put("latitud", "13.689172941798928");
		anuncio.put("longitud", "-89.2257714611053");
		anunciosList.add(anuncio);

		anuncio = new HashMap<String, Object>();
		anuncio.put("titulo", "5 Monitor LED");
		anuncio.put("alias", "descuentazo");
		anuncio.put(
				"descripcion",
				"5 El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña tocaba el saxofón detrás del palenque de paja.");
		anuncio.put("imagen", "");
		anuncio.put("latitud", "13.684419517304311");
		anuncio.put("longitud", "-89.22313216743464");
		anunciosList.add(anuncio);
	}
}
