package com.example.kronos.practica1;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * Created by kronos on 17/03/2015.
 */
public class UtilIO {

    private UtilIO(){

    }

    public static boolean isLegible() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isModificable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static Toast tostada(Context c,String t) {
        Toast toast = Toast.makeText(c, t + "", Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }
}
