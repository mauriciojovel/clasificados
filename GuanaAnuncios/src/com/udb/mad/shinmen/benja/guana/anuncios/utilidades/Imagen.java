package com.udb.mad.shinmen.benja.guana.anuncios.utilidades;

import java.io.ByteArrayInputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Utilidades de imagen.
 * @author Mauricio Jovel.
 *
 */
public class Imagen {

    /**
     * Envia una imagen y la escala para que ocupe menor espacio.
     * @param image ruta fisica de la imagen que quiere ser escalada.
     * @return imagen escalada.
     */
    public static Bitmap compress(String image) {
        return compress(BitmapFactory.decodeFile(image));
    }
    
    /**
     * Envia una imagen y la escala para que ocupe menor espacio.
     * @param bm imagen a ser escalada.
     * @return imagen escalada.
     */
    public static Bitmap compress(Bitmap bm) {        
        Bitmap resizedBitmap = null;
        int width = bm.getWidth();
        int height = bm.getHeight();
        /*int newWidth = width-100;
        int newHeight = height-100;

        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // createa matrix for the manipulation
        Matrix matrix = new Matrix();
        
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // rotate the Bitmap
        matrix.postRotate(45);*/

        // recreate the new Bitmap
        resizedBitmap = Bitmap.createScaledBitmap(bm, width, height, true);
        return resizedBitmap;
    }
    
    @Deprecated
    public static Bitmap decodeToLowResImage(byte [] b, int width, int height) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new ByteArrayInputStream(b), null, o);

            //The new size we want to scale to
            final int REQUIRED_SIZE_WIDTH=(int)(width*0.7);
            final int REQUIRED_SIZE_HEIGHT=(int)(height*0.7);

            //Find the correct scale value. It should be the power of 2.
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE_WIDTH 
                        || height_tmp/2<REQUIRED_SIZE_HEIGHT)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new ByteArrayInputStream(b)
                                , null, o2);
        } catch (OutOfMemoryError e) {
            Log.e("clasificados", "Error al comprimir la imagen", e);
        }
        return null;
    }
}
