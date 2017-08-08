/*
Goal:
This activity tells user about the goal of the Weather Me app as well as very basic instructions.

Methods:
onCreate()
showLocation()
showSettings()
showWeather()
 */
package com.example.owner.weatherme;
//IMPORTS
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //Maintains vertical orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    //Takes user to location page when "Location" TextView is touched
    //Var-view(View)
    //Returns no value
    public void showLocation(View view){
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }
    //Takes user to weather page when "Weather" TextView is touched
    //Var-view(View)
    //Returns no value
    public void showWeather(View view){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
    }
    //Takes user to settings page when "Settings" TextView is touched
    //Var-view(View)
    //Returns no value
    public void showSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
