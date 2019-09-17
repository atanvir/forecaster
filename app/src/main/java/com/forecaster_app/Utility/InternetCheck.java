package com.forecaster_app.Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class InternetCheck extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        if (isConnect()) {
//            Toast.makeText(context, "Connected to Internet.", Toast.LENGTH_LONG).show();
            if (internetCheckInterface != null)
                internetCheckInterface.isNetConnected(true);
        } else {
            Toast.makeText(context, "Internet connection lost.", Toast.LENGTH_LONG).show();
            if (internetCheckInterface != null)
                internetCheckInterface.isNetConnected(false);
        }
    }

    private Context context;
    private InternetCheckInterface internetCheckInterface = null;

    public InternetCheckInterface getInternetCheckInterface() {
        return internetCheckInterface;
    }

    public void setInternetCheckInterface(InternetCheckInterface internetCheckInterface) {
        this.internetCheckInterface = internetCheckInterface;
    }

    public InternetCheck(Context context){
        this.context=context;
    }



    public boolean isConnect()
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnectedOrConnecting())
            {
                return true;
            }
        }
        return false;
    }

    public interface InternetCheckInterface {

        void isNetConnected(boolean isConnected);
    }

}