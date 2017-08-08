/*
Goal:
This class contains a method 'isConnected' that checks if the phone is connected to a network.

Variables:
context(Context)

Methods:
isConnected()

 */
package com.example.owner.weatherme;
//IMPORTS
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//Class that contains a methodd checks if there is an internet connection
public class DetectConnection {

    Context context;

    public DetectConnection(Context context){
        this.context = context;
    }

    //Checks to see if phone is connected to a network
    public boolean isConnected(){
        ConnectivityManager connectivity  = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);

        if(connectivity!= null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info != null){
                if(info.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }
}
