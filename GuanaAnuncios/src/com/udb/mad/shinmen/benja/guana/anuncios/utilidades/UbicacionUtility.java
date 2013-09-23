package com.udb.mad.shinmen.benja.guana.anuncios.utilidades;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class UbicacionUtility {
    public static double getLongitud(Context ctx) {
        try {
            LocationManager lm = (LocationManager) ctx
                                    .getSystemService(Context.LOCATION_SERVICE);
            Location location = lm
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location.getLongitude();
        } catch(Exception e) {
            Log.e("UbicacionUtility", "Pudo ser porque no tiene gps", e);
            return 0.0;
        }
    }
    
    public static double getAltitud(Context ctx) {
        
        Location location;
        try {
            LocationManager lm = (LocationManager) ctx
                    .getSystemService(Context.LOCATION_SERVICE);
            location = lm
            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location.getLatitude();
        } catch (Exception e) {
            Log.e("UbicacionUtility", "Pudo ser porque no tiene gps", e);
            return 0.0;
        }
    }
}
