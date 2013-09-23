package com.udb.mad.shinmen.benja.guana.anuncios.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.udb.mad.shinmen.benja.guana.anuncios.AnuncioDetalleActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Anuncio;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.JSONDownloaderTask;

public class GuanaAnuncioWidget extends AppWidgetProvider {

	static ArrayList<HashMap<String, Object>> anunciosList;
	Context ctx;
	RemoteViews remoteViews;
	private JSONDownloaderTask<JSONArray> jsonTask;
	static final String ANUNCIO_DETAIL = "com.udb.mad.shinmen.benja.guana.anuncios.fragment.AnuncioDetalleFragment.ANUNCIO_DETAIL";
	static final String DUAL_PANE = "com.udb.mad.shinmen.benja.guana.anuncios.fragment.PersonajeDetailFragment.DUAL_PANE";

	String PAGE_MARK = "{page}";
	String LIMIT_MARK = "{limit}";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		this.ctx = context;
		
		remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
				
		remoteViews.setViewVisibility(R.id.wgTitulo, View.INVISIBLE);
		remoteViews.setViewVisibility(R.id.wgDescripcion, View.INVISIBLE);
		remoteViews.setViewVisibility(R.id.wgProgress, View.VISIBLE);
		
		pushWidgetUpdate(context,remoteViews);
		
		cargarData();
		
		
	}

	public static PendingIntent buildLeftButtonPendingIntent(Context context, ArrayList<HashMap<String, Object>> anunciosList) {
		Intent intent = new Intent();
		intent.setAction("com.udb.mad.shinmen.benja.guana.anuncios.CAMBIAR_ANUNCIO_LEFT");
		intent.putExtra("anunciosList", anunciosList);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static PendingIntent buildRightButtonPendingIntent(Context context, ArrayList<HashMap<String, Object>> anunciosList) {
		Intent intent = new Intent();
		intent.setAction("com.udb.mad.shinmen.benja.guana.anuncios.CAMBIAR_ANUNCIO_RIGHT");
		intent.putExtra("anunciosList", anunciosList);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildDetalleAnuncioPendingIntent(Context ctx, ArrayList<HashMap<String, Object>> anunciosList,int position) {
		Intent intent = new Intent(ctx, AnuncioDetalleActivity.class);
		
		Log.e("GuanaAnuncios","Building pendin intent, position="+position);
		intent.putExtra(DUAL_PANE, false);
		
		if(position>=anunciosList.size()) position = anunciosList.size() - 1;
		
		HashMap<String, Object> item = anunciosList.get(position);
		
		Anuncio anuncio = new Anuncio();
		anuncio.setCodigoAnuncio(item.get("codigo").toString());
		anuncio.setTituloAnuncio(item.get("titulo").toString());
		anuncio.setDescripcionAnuncio(item.get("descripcion").toString());
		anuncio.setPrecio(item.get("precio").toString());
		anuncio.setTelefono(item.get("telefono").toString());
		anuncio.setUsuario(item.get("usuario").toString());
		anuncio.setLatitud(Double.valueOf(item.get("latitud").toString()));
		anuncio.setAltitud(Double.valueOf(item.get("altitud").toString()));
        
		intent.putExtra(ANUNCIO_DETAIL, anuncio);
		
		return PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
		
	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context,
				GuanaAnuncioWidget.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		
		manager.updateAppWidget(myWidget, remoteViews);
	}

	private void cargarData() {
		
		SharedPreferences prefs = ctx.getSharedPreferences(
				"GuanaAnunciosPreferences", Context.MODE_PRIVATE);
		String usuario = prefs.getString("usuario", "");
		String token = prefs.getString("token", "");
		
		List<NameValuePair> parametros = new ArrayList<NameValuePair>(2);
		parametros.add(new BasicNameValuePair("usuario", usuario));
		parametros.add(new BasicNameValuePair("token", token));
		// parametros.add(new BasicNameValuePair("texto", query));

		String url = "http://guananuncio.madxdesign.com/index.php/anuncio/anunciosbusqueda/{page}/{limit}";

		url = url.replace(PAGE_MARK, "0");
		url = url.replace(LIMIT_MARK, "20");

		if (anunciosList == null || anunciosList.size() == 0) {
			anunciosList = new ArrayList<HashMap<String, Object>>();			
		}

		/* Tarea asincrona que recupera los datos */
		jsonTask = new JSONDownloaderTask<JSONArray>(url,
				JSONDownloaderTask.METODO_POST, parametros);
		jsonTask.setOnFinishDownload(new BusquedaAnunciosListener());
		jsonTask.setJsonArray(true);
		jsonTask.execute();

	}
	
	

	private class BusquedaAnunciosListener implements
			JSONDownloaderTask.OnFinishDownload<JSONArray> {

		@Override
		public void onFinishDownload(JSONArray json) {
			
			Log.e("GuanaAnuncio", "onFinishDownload method");
			try {
				for (int i = 0; i < json.length(); i++) {
					JSONObject jsonObject = json.getJSONObject(i);

					HashMap<String, Object> anuncio = new HashMap<String, Object>();
					anuncio.put("titulo", jsonObject.getString("titulo"));
					anuncio.put("descripcion",
							jsonObject.getString("descripcion"));
					anuncio.put("codigo", jsonObject.getString("id"));
					anuncio.put("precio", jsonObject.getString("precio"));
					anuncio.put("telefono", jsonObject.getString("telefono"));
					anuncio.put("usuario", jsonObject.getString("nombre"));
					anuncio.put("latitud", jsonObject.getString("latitud"));
					anuncio.put("altitud", jsonObject.getString("altitud"));
					anunciosList.add(anuncio);
				}
			} catch (Exception e) {
				Log.e("error", e.getMessage());
			}
			
			if(anunciosList.size()>0){
				
				HashMap<String, Object> anuncio = anunciosList.get(anunciosList.size()-1);

				String titulo = anuncio.get("titulo").toString();
				String descripcion = anuncio.get("descripcion").toString();

				remoteViews.setTextViewText(R.id.wgTitulo, titulo);
				remoteViews.setTextViewText(R.id.wgDescripcion, descripcion);
				remoteViews.setViewVisibility(R.id.wgTitulo, View.VISIBLE);
				remoteViews.setViewVisibility(R.id.wgDescripcion, View.VISIBLE);
				remoteViews.setViewVisibility(R.id.wgProgress, View.INVISIBLE);	
				
			}
			// boton flecha izquierda
			remoteViews.setOnClickPendingIntent(R.id.leftArrow,
					buildLeftButtonPendingIntent(ctx, anunciosList));

			// boton flech derecha
			remoteViews.setOnClickPendingIntent(R.id.rightArrow,
					buildRightButtonPendingIntent(ctx, anunciosList));
			
			
			remoteViews.setOnClickPendingIntent(R.id.wgDescripcion,
					buildDetalleAnuncioPendingIntent(ctx, anunciosList, WidgetIntentReceiver.position));
			
			
			pushWidgetUpdate(ctx, remoteViews);
		}

		@Override
		public void loadError() {
			// supondremos que el error es porque no te has autenticado.
			// showLogin();
		}
	}

}
