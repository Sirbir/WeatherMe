/*
Goal:
This activity is where the user can set there preferred settings.  Setting options available are
temperature units and gender.

Constants:
PREFS_NAME(String)

Variables:
celsius(TextView)
editor(SharedPreferences.Editor)
fahrenheit(TextView)
female(TextView)
male(TextView)


Methods:
done()
onCreate()
storeCelsius()
storeFahrenheit
storeFemale
storeMale
showAbout()
showLocation()
showSettings()
showWeather()
 */
package com.example.owner.weatherme;
//IMPORTS
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
//VARIABLES
    private SharedPreferences.Editor editor;

    public static final String PREFS_NAME = "MyPrefsFile"; //Name of SharedPreferences file

    TextView fahrenheit, celsius, male, female; //Various setting options user can select

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        //Maintains vertical orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();

        fahrenheit = (TextView) findViewById(R.id.fahrenheit);
        celsius = (TextView) findViewById(R.id.celsius);
        male = (TextView) findViewById(R.id.male);
        female = (TextView) findViewById(R.id.female);
        //Variable gSelected and unitSelected are the saved settings options when the user touches a settings option
        String gSelected = prefs.getString("g_selected", "male");
        String unitSelected = prefs.getString("unit_selected", "fahrenheit");
        //The following if/else statements shades the currently selected settings option white
        if(gSelected.equals("male")){
            male.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            female.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if(unitSelected.equals("fahrenheit")){
            fahrenheit.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            celsius.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }
    //Adds selected male option to SharedPreferences file and shades male settings option white
    //Var-view(View)
    //Returns no value
    public void storeMale(View view) {
        editor.putString("gender", "male");
        editor.putString("g_selected", "male");
        male.setBackgroundColor(Color.parseColor("#ffffff"));
        female.setBackgroundColor(Color.TRANSPARENT);
    }
    //Adds selected female option to SharedPreferences file and shades female settings option white
    //Var-view(View)
    //Returns no value
    public void storeFemale(View view) {
        editor.putString("gender", "female");
        editor.putString("g_selected", "female");
        female.setBackgroundColor(Color.parseColor("#ffffff"));
        male.setBackgroundColor(Color.TRANSPARENT);
    }
    //Adds selected fahrenheit option to SharedPreferences file and shades fahrenheit settings option white
    //Var-view(View)
    //Returns no value
    public void storeFahrenheit(View view) {
        editor.putString("unit", "fahrenheit");
        editor.putString("unit_selected", "fahrenheit");
        fahrenheit.setBackgroundColor(Color.parseColor("#ffffff"));
        celsius.setBackgroundColor(Color.TRANSPARENT);
    }
    //Adds selected celsius option to SharedPreferences file and shades celsius settings option white
    //Var-view(View)
    //Returns no value
    public void storeCelsius(View view) {
        editor.putString("unit", "celsius");
        editor.putString("unit_selected", "celsius");
        celsius.setBackgroundColor(Color.parseColor("#ffffff"));
        fahrenheit.setBackgroundColor(Color.TRANSPARENT);
    }
    //Saves user settings and takes user to location page when "Done" TextView is touched
    //Var-view(View)
    //Returns no value
    public void done(View view){
        editor.commit();
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
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
    //Takes user to about page when "About" TextView is touched
    //Var-view(View)
    //Returns no value
    public void showAbout(View view){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
