package com.udb.mad.shinmen.benja.guana.anuncios.widget;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.udb.mad.shinmen.benja.guana.anuncios.R;

public class WidgetIntentReceiver extends BroadcastReceiver {

	public static int position = 19;
	ArrayList<HashMap<String, Object>> anunciosList;
	Context ctx;

	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(Context context, Intent intent) {
		this.ctx = context;

		anunciosList = (ArrayList<HashMap<String, Object>>) intent
				.getSerializableExtra("anunciosList");
		
		if (position > anunciosList.size()) {
			position = anunciosList.size() - 1;
		}
		Log.e("GuanaAnuncios", "Action="+intent.getAction());
		if (intent
				.getAction()
				.equals("com.udb.mad.shinmen.benja.guana.anuncios.CAMBIAR_ANUNCIO_LEFT")) {
			
			if (anunciosList != null && position <= anunciosList.size()) {
				position++;
				if (position >= anunciosList.size()) {
					position = anunciosList.size() - 1;
				}
				updateWidgetPictureAndButtonListener(context, position);
			}

		} else if (intent
				.getAction()
				.equals("com.udb.mad.shinmen.benja.guana.anuncios.CAMBIAR_ANUNCIO_RIGHT")) {

			if (position >= 1) {
				if (anunciosList != null && anunciosList.size() >= 1) {
					position--;
					updateWidgetPictureAndButtonListener(context, position);
				}
			}

		} else{
			updateWidgetPictureAndButtonListener(context, position);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context,
			int position) {
		
		if (anunciosList != null && anunciosList.size() > 0) {

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			HashMap<String, Object> anuncio = anunciosList.get(position);
			
			String titulo = anuncio.get("titulo").toString();
			String descripcion = anuncio.get("descripcion").toString();
			String codigo = anuncio.get("codigo").toString();

			remoteViews.setTextViewText(R.id.wgTitulo, titulo);
			remoteViews.setTextViewText(R.id.wgDescripcion, descripcion);
			remoteViews.setTextViewText(R.id.codigoAnuncio, codigo);
			remoteViews.setViewVisibility(R.id.wgTitulo, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.wgDescripcion, View.VISIBLE);
			remoteViews.setViewVisibility(R.id.wgProgress, View.INVISIBLE);		

			// REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
			remoteViews.setOnClickPendingIntent(R.id.leftArrow,
					GuanaAnuncioWidget.buildLeftButtonPendingIntent(context,
							anunciosList));

			remoteViews.setOnClickPendingIntent(R.id.rightArrow,
					GuanaAnuncioWidget.buildRightButtonPendingIntent(context,
							anunciosList));
			
			Log.e("GuanaAnuncios","Listener, position="+position);
			remoteViews.setOnClickPendingIntent(R.id.wgDescripcion,
					GuanaAnuncioWidget.buildDetalleAnuncioPendingIntent(context,
							anunciosList,position));
			
			GuanaAnuncioWidget.pushWidgetUpdate(
					context.getApplicationContext(), remoteViews);      

		}
	}

}
