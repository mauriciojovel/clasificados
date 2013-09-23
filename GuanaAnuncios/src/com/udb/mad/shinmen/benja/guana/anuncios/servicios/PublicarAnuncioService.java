package com.udb.mad.shinmen.benja.guana.anuncios.servicios;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.udb.mad.shinmen.benja.guana.anuncios.AnuncioActivity;
import com.udb.mad.shinmen.benja.guana.anuncios.R;
import com.udb.mad.shinmen.benja.guana.anuncios.model.Anuncio;
import com.udb.mad.shinmen.benja.guana.anuncios.utilidades.PreferenciasUsuario;

public class PublicarAnuncioService extends IntentService {
	
	public final static String ANUNCIO_EXTRA ="com.udb.mad.shinmen.benja.guana.anuncios.Anuncio";
	public final static String ANUNCIO_IMAGENES_EXTRA ="com.udb.mad.shinmen.benja.guana.anuncios.Imagenes";
	private final static String NOMBRE_SERVICIO = "GuanaAnuncios.PublicarAnuncio";
	NotificationManager manager;
	Notification notification;
	NotificationCompat.Builder mBuilder;
	Intent notificationIntent;
	PendingIntent pendingIntent;

	public PublicarAnuncioService() {
		super(NOMBRE_SERVICIO);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		boolean falloImagen = false;
		
		Anuncio anuncio = (Anuncio) intent.getSerializableExtra(ANUNCIO_EXTRA);
		ArrayList<String> imagenes = intent.getExtras().containsKey(ANUNCIO_IMAGENES_EXTRA)?intent.getStringArrayListExtra(ANUNCIO_IMAGENES_EXTRA):null;
		
		List<NameValuePair> parametros = new ArrayList<NameValuePair>(8);
		parametros.add(new BasicNameValuePair("usuario", PreferenciasUsuario.getUsuario(this)));
		parametros.add(new BasicNameValuePair("token", PreferenciasUsuario.getToken(this)));
		parametros.add(new BasicNameValuePair("titulo", anuncio.getTituloAnuncio()));
		parametros.add(new BasicNameValuePair("descripcion",anuncio.getDescripcionAnuncio()));
		parametros.add(new BasicNameValuePair("categoria_id",anuncio.getCategoriaId()));
		parametros.add(new BasicNameValuePair("precio", anuncio.getPrecio()));
		parametros.add(new BasicNameValuePair("telefono", anuncio.getTelefono()));
		parametros.add(new BasicNameValuePair("latitud", anuncio.getLatitud()+""));
		parametros.add(new BasicNameValuePair("altitud", anuncio.getAltitud()+""));

		JSONObject respuesta = postRequest(getResources().getString(R.string.guardarAnuncioService), parametros);
		
		String status = null;
		try {
			status = respuesta.getString("estado");
			if (status.equals("1")) {
				if(imagenes != null){
					String idAnuncio = respuesta.getString("id");
					parametros = new ArrayList<NameValuePair>(4);
					parametros.add(new BasicNameValuePair("usuario", PreferenciasUsuario.getUsuario(this)));
					parametros.add(new BasicNameValuePair("token", PreferenciasUsuario.getToken(this)));
					parametros.add(new BasicNameValuePair("anuncio_id", idAnuncio));
					for (Iterator<String> iterator = imagenes.iterator(); iterator
							.hasNext();) {
						String imagePath = iterator
								.next();
						Bitmap bm = BitmapFactory.decodeFile(imagePath, null);
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bm.compress(Bitmap.CompressFormat.JPEG, 40, stream);
						byte[] byte_arr = stream.toByteArray();
						String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
						parametros.add(new BasicNameValuePair("imagen", image_str));
						respuesta = postRequest(getResources().getString(R.string.guardarImagenAnuncioService), parametros);
						parametros.remove(3);//removemos la imagen para reutilizar la lista de parametros
						status = respuesta.getString("estado");
						if (!status.equals("1")) {
							falloImagen = true;
						}
					}
				}
				String mensajeRespuesta = null;
				if(falloImagen){
					mensajeRespuesta = "Anuncio publicado. Sin embargo, algunas imagenes no pudieron adjuntarse.";
				}else{
					mensajeRespuesta = "Anuncio publicado con exito";
				}
				notificar(mensajeRespuesta);
			}else {
				if (status.equals("-1")) {
					Toast.makeText(
							this,
							"Ocurrio un error inesperado. Vuelva a intentar.",
							Toast.LENGTH_SHORT).show();
				}
			}
		} catch (Exception e) {
			Log.e(NOMBRE_SERVICIO, e.getMessage());
		}
	}
	
	private void notificar(String mensaje){
		mBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.guana_logo_g)
		.setContentTitle("GuanaAnuncios")
		.setContentText(mensaje);
		
		notificationIntent = new Intent(this, AnuncioActivity.class);
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(AnuncioActivity.class);
		stackBuilder.addNextIntent(notificationIntent);

		pendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		mBuilder.setContentIntent(pendingIntent);
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		manager.notify(1, mBuilder.build());
	}
	
	private JSONObject postRequest(String url, List<NameValuePair> parametrosLista){
		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream inputStream = null;
		JSONObject jsonData = null;
		String json = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			if(parametrosLista != null){
				post.setEntity(new UrlEncodedFormEntity(parametrosLista));
			}
			response = httpClient.execute(post);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				entity = response.getEntity();
				inputStream = entity.getContent();
				// obtuvimos respuesta, a transformar...
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						inputStream, "utf-8"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line)
					  .append("\n");
				}
				inputStream.close();
				json = sb.toString();
				jsonData = new JSONObject(json);
			} else {
				Log.e(NOMBRE_SERVICIO,
						"Failed to download JSON");
				jsonData = new JSONObject("{'estado':'0','errors':{'status':'Status code = "+ statusCode +"'}}");
			}
		}catch (Exception e) {
			Log.e(NOMBRE_SERVICIO, "Ocurrio un error al parsear los datos"
					+ e.toString());
		}
		return jsonData;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Publicando anuncio...",
				Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

}
