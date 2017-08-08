/*
Goal:
The purpose of this activity is to display useful clothing suggestions and images based on the
weather parameters for the location the user entered on the Location Activity.  An appropriate
background animation in the form of a GIF is also displayed.

Constants:
API_KEY(String)
DEFAULT_UNITS(String)
DRIZZLE(String)
FOG(String)
HAZE(String)
MIST(String)
OPEN_WEATHER_API(String)
RAIN(String)
SNOW(String)
THUNDER_STORM(String)

Variables:
genderWear(String)
simpleDraweeView(SimpleDraweeView)
suggestion(TextView)
wearImage(ImageView)
wearThis(TextView)

Inner Classes:
GetInfo

Methods:
onCreate()
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.owner.weatherme.LocationActivity.lat;
import static com.example.owner.weatherme.LocationActivity.lon;
import static com.example.owner.weatherme.R.id.wearthis;
import static com.example.owner.weatherme.SettingsActivity.PREFS_NAME;

public class WearActivity extends AppCompatActivity {
    //CONSTANTS
    private final static String API_KEY = BuildConfig.API_KEY;
    private final static String OPEN_WEATHER_API = "http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s"; //Weather API from which weather information will be retrieved
    private final static String DEFAULT_UNITS = "imperial"; //The default temperature unit-Farenheit

    private final static String RAIN = "Rain"; //Rain condition
    private final static String DRIZZLE = "Drizzle"; //Drizzle condition
    private final static String THUNDER_STORM = "Thunderstorm"; //Thunderstorm condition
    private final static String MIST = "Mist"; //Mist condition
    private final static String HAZE = "Haze"; //Haze condition
    private final static String SNOW = "Snow"; //Snow condition
    private final static String FOG = "Fog"; //Snow condition
    //VARIABLES
    TextView wearThis,  suggestion; //Suggestion and elaboration for appropriate clothing for the weather

    SimpleDraweeView simpleDraweeView; //View that displays animated background

    ImageView wearImage; //Informational clothing picture displayed to user

    String genderWear; //Saved or default gender setting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        genderWear = prefs.getString("gender", "male"); //Obtains user selected gender settings

        setContentView(R.layout.activity_wear);
        //Maintains vertical orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        wearImage =(ImageView) findViewById(R.id.wearimage);

        wearThis = (TextView) findViewById(wearthis);
        suggestion = (TextView) findViewById(R.id.suggestion);

        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.gif);

        String url = String.format(OPEN_WEATHER_API, lat, lon, DEFAULT_UNITS, API_KEY); //OPEN WEATHER API from which weather parameters will be obtained
        new GetInfo(wearThis, suggestion).execute(url);
    }
    //This class obtains current temperature and day condition asynchronously from 'Open Weather API'
    private class GetInfo extends AsyncTask<String, Object, List<String>> {

        private TextView textView1;
        private TextView textView2;


        public GetInfo(TextView textView1, TextView textView2){
            this.textView1 = textView1;
            this.textView2 = textView2;
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            String temp;
            String dayCondition;
            List<String> weather = new ArrayList<>();

            try {
                //The following code obtains weather information from Open Weather API
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                JSONObject weatherData = new JSONObject(builder.toString());

                JSONObject main = weatherData.getJSONObject("main");
                temp = String.valueOf(main.getDouble("temp"));
                weather.add(temp);


                JSONArray weatherStuff = weatherData.getJSONArray("weather");
                JSONObject descriptionJSON = weatherStuff.getJSONObject(0);
                dayCondition = descriptionJSON.getString("main");
                weather.add(dayCondition);

                urlConnection.disconnect();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
            return weather;
        }

        @Override
        protected void onPostExecute(final List<String> weather) {
            super.onPostExecute(weather);
            if (weather != null) {
                //Initializing weather parameters
                String cTemp = weather.get(0); //Current temperature
                String dCond = weather.get(1); //Day condition
                //Creating arrays containing clothing images for specific weather conditions
                int[] g30Array = new int[5];
                g30Array[0] = R.drawable.g_30_1;
                g30Array[1] = R.drawable.g_30_2;
                g30Array[2] = R.drawable.g_30_3;
                g30Array[3] = R.drawable.g_30_4;
                g30Array[4] = R.drawable.g_30_5;

                int[] g40Array = new int[5];
                g40Array[0] = R.drawable.g_40_1;
                g40Array[1] = R.drawable.g_40_2;
                g40Array[2] = R.drawable.g_40_3;
                g40Array[3] = R.drawable.g_40_4;
                g40Array[4] = R.drawable.g_40_5;

                int[] g50Array = new int[5];
                g50Array[0] = R.drawable.g_50_1;
                g50Array[1] = R.drawable.g_50_2;
                g50Array[2] = R.drawable.g_50_3;
                g50Array[3] = R.drawable.g_50_4;
                g50Array[4] = R.drawable.g_50_5;

                int[] g60Array = new int[5];
                g60Array[0] = R.drawable.g_60_1;
                g60Array[1] = R.drawable.g_60_2;
                g60Array[2] = R.drawable.g_60_3;
                g60Array[3] = R.drawable.g_60_4;
                g60Array[4] = R.drawable.g_60_5;

                int[] g70Array = new int[5];
                g70Array[0] = R.drawable.g_70_1;
                g70Array[1] = R.drawable.g_70_2;
                g70Array[2] = R.drawable.g_70_3;
                g70Array[3] = R.drawable.g_70_4;
                g70Array[4] = R.drawable.g_70_5;

                int[] b30Array = new int[5];
                b30Array[0] = R.drawable.b_30_1;
                b30Array[1] = R.drawable.b_30_2;
                b30Array[2] = R.drawable.b_30_3;
                b30Array[3] = R.drawable.b_30_4;
                b30Array[4] = R.drawable.b_30_5;

                int[] b40Array = new int[5];
                b40Array[0] = R.drawable.b_40_1;
                b40Array[1] = R.drawable.b_40_2;
                b40Array[2] = R.drawable.b_40_3;
                b40Array[3] = R.drawable.b_40_4;
                b40Array[4] = R.drawable.b_40_5;

                int[] b50Array = new int[5];
                b50Array[0] = R.drawable.b_50_1;
                b50Array[1] = R.drawable.b_50_2;
                b50Array[2] = R.drawable.b_50_3;
                b50Array[3] = R.drawable.b_50_4;
                b50Array[4] = R.drawable.b_50_5;

                int[] b60Array = new int[5];
                b60Array[0] = R.drawable.b_60_1;
                b60Array[1] = R.drawable.b_60_2;
                b60Array[2] = R.drawable.b_60_3;
                b60Array[3] = R.drawable.b_60_4;
                b60Array[4] = R.drawable.b_60_5;

                int[] b70Array = new int[5];
                b70Array[0] = R.drawable.b_70_1;
                b70Array[1] = R.drawable.b_70_2;
                b70Array[2] = R.drawable.b_70_3;
                b70Array[3] = R.drawable.b_70_4;
                b70Array[4] = R.drawable.b_70_5;

                Random random = new Random(); //Creating variable to create random array index
                int index = random.nextInt(5); //Random array index so randomly selected clothing image shows up each time on wear page based on weather conditions
                //The following if/elseif/else statements determine the clothing suggestions displayed to the user based on weather conditions
                if(Double.parseDouble(cTemp) >= 0.0 && Double.parseDouble(cTemp) < 30.0 ){
                    textView1.setText("Wear layers as today will be cold");
                    if (dCond.equals("Rain")||dCond.equals("Drizzle")||dCond.equals("Thunderstorm")) {
                        if (genderWear.equals("male")) {
                            textView2.setText(R.string.s_30_r);
                            wearImage.setBackgroundResource(b30Array[index]);
                        } else {
                            textView2.setText(R.string.s_30_r);
                            wearImage.setBackgroundResource(g30Array[index]);
                        }
                    } else {
                        if (genderWear.equals("male")) {
                            textView2.setText(R.string.s_30_nr);
                            wearImage.setBackgroundResource(b30Array[index]);
                        } else {
                            textView2.setText(R.string.s_30_nr);
                            wearImage.setBackgroundResource(g30Array[index]);
                        }
                    }
                }

                if(Double.parseDouble(cTemp) >= 30.0 && Double.parseDouble(cTemp) < 50.0 ){
                    textView1.setText("Wear moderate clothing as today will be moderately cold");
                    if (dCond.equals("Rain") || dCond.equals("Drizzle")||dCond.equals("Thunderstorm")) {
                        if (genderWear.equals("male")) {
                            textView2.setText(R.string.s_40_r);
                            wearImage.setBackgroundResource(b40Array[index]);
                        } else {
                            textView2.setText(R.string.s_40_r);
                            wearThis.setBackgroundResource(g40Array[index]);
                        }
                    } else {
                        if (genderWear.equals("male")) {
                            textView2.setText(R.string.s_40_nr);
                            wearImage.setBackgroundResource(b40Array[index]);
                        } else {
                            textView2.setText(R.string.s_40_nr);
                            wearImage.setBackgroundResource(g40Array[index]);
                        }
                    }
                }

                if(Double.parseDouble(cTemp) >= 50.0 && Double.parseDouble(cTemp) < 60.0){
                    textView1.setText("Wear light layers as today will be chilly");
                    if (dCond.equals("Rain") || dCond.equals("Drizzle")||dCond.equals("Thunderstorm")) {
                        if (genderWear.equals("male")) {
                            textView2.setText(R.string.s_50_r);
                            wearImage.setBackgroundResource(b50Array[index]);
                        } else {
                            textView2.setText(R.string.s_50_r);
                            wearImage.setBackgroundResource(g50Array[index]);
                        }
                    } else {
                        if (genderWear.equals("male")) {
                            textView2.setText(R.string.s_50_nr);
                            wearImage.setBackgroundResource(b50Array[index]);
                        } else {
                            textView2.setText(R.string.s_50_nr);
                            wearImage.setBackgroundResource(g50Array[index]);
                        }
                    }
                }

                if(Double.parseDouble(cTemp) >= 60.0 && Double.parseDouble(cTemp) < 70.0){
                    textView1.setText("Wear removable layers as today will be warm but slightly chilly");
                    if (dCond.equals("Rain") || dCond.equals("Drizzle")||dCond.equals("Thunderstorm")) {
                        if (genderWear.equals("male")) {
                            textView2.setText(R.string.s_60_r_b);
                            wearImage.setBackgroundResource(b60Array[index]);
                        } else {
                            textView2.setText(R.string.s_60_r_g);
                            wearImage.setBackgroundResource(g60Array[index]);
                        }
                    } else {
                        if (genderWear.equals("male")) {
                            textView2.setText(R.string.s_60_nr_b);
                            wearImage.setBackgroundResource(b60Array[index]);

                        } else {
                            textView2.setText(R.string.s_60_nr_g);
                            wearImage.setBackgroundResource(g60Array[index]);
                        }
                    }
                }

                if(Double.parseDouble(cTemp) >= 70.0) {
                    textView1.setText("Wear light clothing as today will be hot");
                    if (dCond.equals("Rain")||dCond.equals("Drizzle")||dCond.equals("Thunderstorm")) {
                        if (genderWear.equals("male")) {
                            textView2.setText(R.string.s_70_r_b);
                            wearImage.setBackgroundResource(b70Array[index]);

                        } else {
                            textView2.setText(R.string.s_70_r_g);
                            wearImage.setBackgroundResource(g70Array[index]);
                        }
                    } else {
                        if (genderWear.equals("male")) {
                            textView2.setText(R.string.s_70_nr);
                            wearImage.setBackgroundResource(b70Array[index]);

                        } else {
                            textView2.setText(R.string.s_70_nr_g);
                            wearImage.setBackgroundResource(g70Array[index]);
                        }
                    }
                }
                //The following if/elseif/else statements determine the background based on weather conditions
                //Each if/elseif/else statement utilizes the Facebook Fresco library to obtain GIFs from the internet to set as background
                if(dCond.equals(RAIN)) {
                    simpleDraweeView.setController(
                            Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("http://media.giphy.com/media/LSczyS2limONa/giphy.gif"))
                                    .setAutoPlayAnimations(true)
                                    .build());
                }
                else if(dCond.equals(DRIZZLE)) {
                    simpleDraweeView.setController(
                            Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("https://s-media-cache-ak0.pinimg.com/originals/1a/cd/fd/1acdfd720bcb161ffa69416c4eafb29e.jpg"))
                                    .setAutoPlayAnimations(true)
                                    .build());
                }
                else if(dCond.equals(THUNDER_STORM)) {
                    simpleDraweeView.setController(
                            Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("https://media2.giphy.com/media/BFif4Uu3v5Dz2/giphy.gif"))
                                    .setAutoPlayAnimations(true)
                                    .build());
                }
                else if(dCond.equals(MIST)) {
                    simpleDraweeView.setController(
                            Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("https://68.media.tumblr.com/f04c328d79d509c1dbdfd527a3c3234f/tumblr_otbe0hbxVX1ukofkbo1_540.gif"))
                                    .setAutoPlayAnimations(true)
                                    .build());
                }
                else if(dCond.equals(HAZE)) {
                    simpleDraweeView.setController(
                            Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("https://68.media.tumblr.com/067f4a73b3018d8e7eba2e39963eb571/tumblr_nn1d0bWLgu1qlwl18o1_500.gif"))
                                    .setAutoPlayAnimations(true)
                                    .build());
                }
                else if(dCond.equals(FOG)) {
                    simpleDraweeView.setController(
                            Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("https://68.media.tumblr.com/6b02d5b25aa0114688e8c1842e7b7dc8/tumblr_o2x93nvG9W1tchrkco1_500.gif"))
                                    .setAutoPlayAnimations(true)
                                    .build());
                }
                else if(dCond.equals(SNOW)) {
                    simpleDraweeView.setController(
                            Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("https://media.giphy.com/media/4yOSZvJVQuwDu/giphy.gif"))
                                    .setAutoPlayAnimations(true)
                                    .build());
                }

                else{

                    if(Double.parseDouble(cTemp) >= 0.0 && Double.parseDouble(cTemp) < 30.0 ) {
                        simpleDraweeView.setController(
                                Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("https://68.media.tumblr.com/7a9c5c225c73ed1039698b516c1410bd/tumblr_oj4gpceEsF1tliyzbo1_540.gif"))
                                        .setAutoPlayAnimations(true)
                                        .build());
                    }
                    if(Double.parseDouble(cTemp) >= 30.0 && Double.parseDouble(cTemp) < 50.0 ){
                        simpleDraweeView.setController(
                                Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("https://www.9to5animations.com/wp-content/uploads/2016/03/Windows10-gif-wallpaper.gif"))
                                        .setAutoPlayAnimations(true)
                                        .build());
                    }
                    if(Double.parseDouble(cTemp) >= 50.0 && Double.parseDouble(cTemp) < 60.0){
                        simpleDraweeView.setController(
                                Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("https://68.media.tumblr.com/7e3edc16bc31d089ecbb595e37a93c60/tumblr_nxgkt450dB1uf885mo1_500.gif"))
                                        .setAutoPlayAnimations(true)
                                        .build());
                    }
                    if(Double.parseDouble(cTemp) >= 60.0 && Double.parseDouble(cTemp) < 70.0){
                        simpleDraweeView.setController(
                                Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("https://68.media.tumblr.com/3e86158dfdeaf03a641cfc9340a31119/tumblr_oo9q8fg5541qassaoo1_500.gif"))
                                        .setAutoPlayAnimations(true)
                                        .build());
                    }
                    if(Double.parseDouble(cTemp) >= 70.0) {
                        simpleDraweeView.setController(
                                Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("http://i.imgur.com/1ok872f.gif"))
                                        .setAutoPlayAnimations(true)
                                        .build());
                    }
                }
            }
        }
    }

    //Refreshes page when "Wear" TextView is touched
    //Var-view(View)
    //Returns no value
    public void refresh(View view){
        Intent intent = new Intent(this, WearActivity.class);
        startActivity(intent);
    }
    //Takes user to wear page when "Weather" TextView is touched
    //Var-view(View)
    //Returns no value
    public void showWeather(View view){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
    }
    //Takes user to location page when "Location" TextView is touched
    //Var-view(View)
    //Returns no value
    public void showLocation(View view){
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
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
}

