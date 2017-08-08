/*
Goal:
The purpose of this activity is to obtain a desired location from the user that will be used to
obtain weather parameters for the entered location that will be used to initialize the Wear
and Weather activities.

Variables:
aLocation(String)
lat(double)
lon(double)
enterLocation(EditText)
editor(SharedPreferences.Editor)

Methods:
clear()
getCoordinates()
onCreate()
showWearInfo()
showWeather()
showSettings()
showAbout()
 */
package com.example.owner.weatherme;
//IMPORTS
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

public class LocationActivity extends AppCompatActivity {
    //VARIABLES
    static String aLocation; //Location user enters that is passed to getCoordinates method
    static double lat, lon; //Geographical coordinates of entered address that will used by 'Open Weather API' in Wear and Weather activities
    EditText enterLocation; // Text box where user enters their location

    private SharedPreferences.Editor editor;

    DetectConnection dc; //Variable that wil be manipulated to check for internet connection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initializing Fresco library
        Fresco.initialize(this);

        setContentView(R.layout.activity_location);
        //Maintains vertical orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        enterLocation = (EditText) findViewById(R.id.entercity);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        editor = prefs.edit();

        String locationString = prefs.getString("location",""); //Gets previously entered location

        enterLocation.setText(locationString); //Displays previously entered location

        dc = new DetectConnection(this);

    }
    //User is taken to weather page after a location is entered and the 'Weather Me' button is pressed
    public void showWearInfo(View view){
        //Checks if there is an internet connection
        if (dc.isConnected()) {
            aLocation = enterLocation.getText().toString();
            getCoordinates(aLocation);

            editor.putString("location", aLocation+"");
            //Saves entered location
            editor.commit();
            //Takes user to wear page
            Intent intent = new Intent(this, WearActivity.class);
            startActivity(intent);
        } else {
            //Notifies user that they are not connected to a network
            Toast.makeText(LocationActivity.this, "No Network Connection", Toast.LENGTH_SHORT).show();
        }
    }
    //Clears location text box when "Location" TextView is pressed
    //Var-view(View)
    //Returns no value
    public void clear(View view){
      enterLocation.setText("");
    }
    //Takes user to wear page when "Weather" TextView is touched
    public void showWeather(View view){
        if(dc.isConnected()) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(LocationActivity.this, "No Network Connection", Toast.LENGTH_SHORT).show();
        }
    }
    //Takes user to settings page when "Settings" TextView is touched
    //Var-view(View)
    //Returns no value
    public void showSettings(View view){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
    }
    //Takes user to about page when "About" TextView is touched
    //Var-view(View)
    //Returns no value
    public void showAbout(View view){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
    //Gets longitude and latitude of location user enters in the text box
    //Var-addressStr(String)
    //Returns no value
    public void getCoordinates(String addressStr) {
        Geocoder geo = new Geocoder(this);
        Address address = null;
        List<Address> addressList = null;

        try {
            if (!TextUtils.isEmpty(addressStr)) {
                addressList = geo.getFromLocationName(addressStr, 5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != addressList && addressList.size() > 0) {
            address = addressList.get(0);
        }
        if (null != address && address.hasLatitude()
                && address.hasLongitude()) {
            lat = address.getLatitude();
            lon = address.getLongitude();
        }
    }
}
