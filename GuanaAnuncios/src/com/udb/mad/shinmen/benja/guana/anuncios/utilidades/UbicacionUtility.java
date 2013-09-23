package com.udb.mad.shinmen.benja.guana.anuncios.utilidades;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class UbicacionUtility {
    public static double getLongitud(Context ctx) {
        LocationManager lm = (LocationManager) ctx
                                    .getSystemService(Context.LOCATION_SERVICE);
        Location location = lm
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return location.getLongitude();
    }
    
    public static double getAltitud(Context ctx) {
        LocationManager lm = (LocationManager) ctx
                .getSystemService(Context.LOCATION_SERVICE);
        Location location = lm
        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return location.getLatitude();
    }
}
