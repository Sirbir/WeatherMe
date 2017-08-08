/*
Goal:
The purpose of this activity is to display the weather parameters for a location the user entered
on the Location Activity.  An appropriate animation in the form of a GIF is also displayed in the
background.

Constants:
API_KEY(String)
CELSIUS_UNITS(String)
DEFAULT_UNITS(String)
DRIZZLE(String)
DEFAULT_UNITS(String)
FOG(String)
HAZE(String)
MIST(String)
OPEN_WEATHER_API(String)
RAIN(String)
SNOW(String)
THUNDER_STORM(String)

Variables:
currCond(String)
currTemp(TextView)
currentUnit(String)
dayCond(String)
highTemp(TextView)
humidity(TextView)
lowTemp(TextView)
simpleDraweeView(SimpleDraweeView)
windSpeed(TextView)

Inner Classes:
GetWeatherTask

Methods:
onCreate()
showAbout()
showLocation()
showSettings()
showWear()
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
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.owner.weatherme.LocationActivity.aLocation;
import static com.example.owner.weatherme.LocationActivity.lat;
import static com.example.owner.weatherme.LocationActivity.lon;
import static com.example.owner.weatherme.SettingsActivity.PREFS_NAME;

public class WeatherActivity extends AppCompatActivity {
    //CONSTANTS
    private final static String API_KEY = BuildConfig.API_KEY;
    private final static String OPEN_WEATHER_API = "http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s"; //Weather API from which weather information will be retrieved
    private final static String DEFAULT_UNITS = "imperial"; //The default temperature unit-Farenheit
    private final static String FAHRENHEIT_UNITS = "fahrenheit"; //Temperature unit Fahrenheit
    private final static String CELSIUS_UNITS = "celsius"; //Temperature unit Celsius

    private final static String RAIN = "Rain"; //Rain condition
    private final static String DRIZZLE = "Drizzle"; //Drizzle condition
    private final static String THUNDER_STORM = "Thunderstorm"; //Thunderstorm condition
    private final static String MIST = "Mist"; //Mist condition
    private final static String HAZE = "Haze"; //Haze condition
    private final static String SNOW = "Snow"; //Snow condition
    private final static String FOG = "Fog"; //Snow condition
    //VARIABLES
    TextView currTemp, highTemp, lowTemp, currCond, dayCond, windSpeed, humidity; // Weather parameters displayed to user

    SimpleDraweeView simpleDraweeView; //View that displays animated background

    String currentUnit; //Unit user currently selected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);
        //Maintains vertical orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.gif);

        TextView currPlace = (TextView) findViewById(R.id.currentplace); //Current location user wanted weather information about

        currTemp = (TextView) findViewById(R.id.currtemp);
        highTemp = (TextView) findViewById(R.id.hightemp);
        lowTemp = (TextView) findViewById(R.id.lowtemp);
        currCond = (TextView) findViewById(R.id.currcond);
        dayCond = (TextView) findViewById(R.id.daycond);
        windSpeed = (TextView) findViewById(R.id.windspeed);
        humidity = (TextView) findViewById(R.id.humidity);


        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentUnit = prefs.getString("unit", ""); //Getting saved or default temperature unit from settings

        currPlace.setText(aLocation); //Shows location user entered on Location page

        String url = String.format(OPEN_WEATHER_API, lat, lon, DEFAULT_UNITS, API_KEY);
        new GetWeatherTask(currTemp, highTemp, lowTemp, currCond, windSpeed, dayCond, humidity).execute(url);
    }
    //Formats double value to a specified number of decimal places
    //Var-x(double)
    //Var-numberofDecimals(int)
    //Returns BigDecimal value
    private BigDecimal truncateDecimal(double x, int numberOfDecimals) {
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimals, BigDecimal.ROUND_CEILING);
        }
    }

    //This class asynchronously gets weather information from 'Open Weather API'
    private class GetWeatherTask extends AsyncTask<String, Object, List<String>> {

        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        private TextView textView5;
        private TextView textView6;
        private TextView textView7;

        public GetWeatherTask(TextView textView1, TextView textView2, TextView textView3,
                              TextView textView4, TextView textView5, TextView textView6,
                                      TextView textView7){
            this.textView1 = textView1;
            this.textView2 = textView2;
            this.textView3 = textView3;
            this.textView4 = textView4;
            this.textView5 = textView5;
            this.textView6 = textView6;
            this.textView7 = textView7;
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            String temp;
            String tempMax;
            String tempMin;
            String description;
            String speed;
            String humid;
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
                    tempMax = String.valueOf(main.getDouble("temp_max"));
                    tempMin = String.valueOf(main.getDouble("temp_min"));
                    weather.add(temp);
                    weather.add(tempMax);
                    weather.add(tempMin);

                    JSONArray weatherStuff = weatherData.getJSONArray("weather");
                    JSONObject descriptionJSON = weatherStuff.getJSONObject(0);
                    description = descriptionJSON.getString("description");
                    weather.add(description);

                    JSONObject wind = weatherData.getJSONObject("wind");

                    speed = String.valueOf(wind.getDouble("speed"));
                    weather.add(speed);

                    dayCondition = descriptionJSON.getString("main");
                    weather.add(dayCondition);

                    humid = String.valueOf(main.getInt("humidity"));
                    weather.add(humid);

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
                String cTemp = weather.get(0); // current temperature
                String hTemp = weather.get(1); // highest temperature
                String lTemp = weather.get(2); // lowest temperature
                String cCond = weather.get(3); // current weather conditions
                String wSpeed = weather.get(4); // wind speed
                String dCond = weather.get(5); // day condition
                String hum = weather.get(6); // humidity
                //The following if/elseif/else statements determine the background based on weather conditions
                //Utilizes Facebook Fresco library to obtain GIFs from the internet to set as background
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
                                Fresco.newDraweeControllerBuilder().setImageRequest(ImageRequest.fromUri("https://i.amz.mshcdn.com/BsYZAoFwRrIRdhBdticmuaOHj9U=/fit-in/850x850/2014%2F05%2F22%2F4f%2Ftrpoical.46833.gif"))
                                        .setAutoPlayAnimations(true)
                                        .build());
                    }
                }
                //The following else/elseif/else statements determine the temperature units based on user or default settings
                if (currentUnit.equals(FAHRENHEIT_UNITS)) {
                    textView1.setText(cTemp);
                    textView2.setText(hTemp);
                    textView3.setText(lTemp);
                } else if (currentUnit.equals(CELSIUS_UNITS)) {

                    Double cTempDouble = ((Double.parseDouble(cTemp) - 32.0) / (1.8));
                    Double hTempDouble = ((Double.parseDouble(hTemp) - 32.0) / (1.8));
                    Double lTempDouble = ((Double.parseDouble(lTemp) - 32.0) / (1.8));

                    BigDecimal cTempDecimal = truncateDecimal(cTempDouble, 2);
                    BigDecimal hTempDecimal = truncateDecimal(hTempDouble, 2);
                    BigDecimal lTempDecimal = truncateDecimal(lTempDouble, 2);

                    String cTempCelsius = String.valueOf(cTempDecimal);
                    String hTempCelsius = String.valueOf(hTempDecimal);
                    String lTempCelsius = String.valueOf(lTempDecimal);

                    textView1.setText(cTempCelsius);
                    textView2.setText(hTempCelsius);
                    textView3.setText(lTempCelsius);
                } else {
                    textView1.setText(cTemp);
                    textView2.setText(hTemp);
                    textView3.setText(lTemp);
                }
                textView4.setText(cCond);
                textView5.setText(wSpeed);
                textView6.setText(dCond);
                textView7.setText(hum);
            }
        }
    }
    //Takes user to wear page when "Wear" TextView is touched
    //Var-view(View)
    //Returns no value
    public void showWear(View view){
            Intent intent = new Intent(this, WearActivity.class);
            startActivity(intent);
    }
    //Refreshes weather page "Wear" TextView is touched
    //Var-view(View)
    //Returns no value
    public void showWeather(View view) {
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
