package com.example.elfaroukomar.ntl_movie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Elfarouk Omar on 13/10/2017.
 */

public class NetworkStateReceiver extends BroadcastReceiver
{


    static boolean Connected =true;
    public void onReceive(Context context, Intent intent)
    {
        Log.d("app","Network connectivity change");


        if(intent.getExtras() != null)
        {
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(ni != null && ni.getState() == NetworkInfo.State.CONNECTED)
            {
                Intent n = new Intent("Broadcast");
                n.putExtra("State",true);
                context.sendBroadcast(n);
                Toast.makeText(context,  "Network " + ni.getTypeName() + " connected", Toast.LENGTH_SHORT).show();
                Log.i("app", "Network " + ni.getTypeName() + " connected");
                Connected=true;
             }
        }

        if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE))
        {
            Intent n = new Intent("Broadcast");
            n.putExtra("State",false);
            context.sendBroadcast(n);
            Toast.makeText(context, "There's no network connectivity", Toast.LENGTH_SHORT).show();
            Log.d("app", "There's no network connectivity");
            Connected=false;
        }
    }
}
