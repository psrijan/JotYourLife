package com.mycompany.jotyourlife;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Shristi on 3/22/2016.
 */
public class _ {
    public static void l(String msg){
         final String TAG ="MYAPP";
        Log.d(TAG, msg);
    }
    public static void t(String msg , Context context){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
    }

}
