package com.example.basicweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CityWeather extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RequestQueue requestQueue;

    private String cityName = "";
    private TextView cityTextView;
    private ImageView image;
    private TextView temperature;
    private TextView condition;
    private TextView windSpeed;
    private TextView humidity;
    private TextView seaLevel;
    private Spinner daySpinner;
    private Button doneButton;

    public static final DecimalFormat formatter = new DecimalFormat("#.##");

    final String[] listOfDays = {
            "Day 1",
            "Day 2",
            "Day 3",
            "Day 4",
            "Day 5"
    };

    private JSONArray weatherList;
    private JSONObject day;
    private JSONObject main;
    private JSONArray weather;
    private JSONObject weatherDetail;
    private JSONObject wind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        cityTextView = findViewById(R.id.cityName);
        image = findViewById(R.id.cityImage);
        temperature = findViewById(R.id.temperature);
        condition = findViewById(R.id.condition);
        windSpeed = findViewById(R.id.windSpeed);
        humidity = findViewById(R.id.humidity);
        seaLevel = findViewById(R.id.seaLevel);
        daySpinner = findViewById(R.id.daySpinner);
        doneButton = findViewById(R.id.doneButton);

        //Set up the spinner for different days
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOfDays);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        daySpinner.setAdapter(adapter);
        daySpinner.setOnItemSelectedListener(this);

        //Get the data from previous screen
        Intent intent = getIntent();
        if (!intent.getStringExtra("cityName").isEmpty())
        {
            cityName = intent.getStringExtra("cityName");
            cityTextView.setText(cityName);
        }

        //Update images according to specific city names
        if (cityName.equals("New York"))
        {
            image.setImageResource(R.drawable.new_york);
        }
        else if (cityName.equals("Dubai"))
        {
            image.setImageResource(R.drawable.dubai);
        }
        else if (cityName.equals("London"))
        {
            image.setImageResource(R.drawable.london);
        }
        else if (cityName.equals("Istanbul"))
        {
            image.setImageResource(R.drawable.istanbul);
        }
        else if (cityName.equals("Shanghai"))
        {
            image.setImageResource(R.drawable.shanghai);
        }

        //Instantiate the request queue
        requestQueue = Volley.newRequestQueue(this);

        //Return back to main activity
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    //Update weather data according to the day selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //create object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&appid=33a0541eb8aff0a86e359892beccea97",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //this prints the WHOLE string
                        Log.i("JSON response", response.toString());

                        try {

                            //Get the whole list array from JSON
                            weatherList = response.getJSONArray("list");
                            Log.i("JSON response", weatherList.toString());

                            //Get the whole day object depending on specific day
                            if (daySpinner.getSelectedItem().toString().equals("Day 1"))
                            {
                                day = weatherList.getJSONObject(0);
                            }
                            else if (daySpinner.getSelectedItem().toString().equals("Day 2"))
                            {
                                day = weatherList.getJSONObject(1);
                            }
                            else if (daySpinner.getSelectedItem().toString().equals("Day 3"))
                            {
                                day = weatherList.getJSONObject(2);
                            }
                            else if (daySpinner.getSelectedItem().toString().equals("Day 4"))
                            {
                                day = weatherList.getJSONObject(3);
                            }
                            else if (daySpinner.getSelectedItem().toString().equals("Day 5"))
                            {
                                day = weatherList.getJSONObject(4);
                            }


                            //Get specific data using acquired data object
                            main = day.getJSONObject("main");
                            weather = day.getJSONArray("weather");
                            weatherDetail = weather.getJSONObject(0);
                            wind = day.getJSONObject("wind");
                            temperature.setText(convertTemperature(main.getString("temp")) + " °F");
                            condition.setText(weatherDetail.getString("main"));
                            windSpeed.setText(convertWind(wind.getString("speed")) + " mph");
                            humidity.setText(main.getString("humidity") + "%");
                            seaLevel.setText(main.getString("sea_level") + " hpa");

                        } catch (JSONException e) {
                            Log.e("JSON Error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(jsonObjectRequest);

        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#76FF03"));
        ((TextView) parent.getChildAt(0)).setTextSize(24);
        ((TextView) parent.getChildAt(0)).setTypeface(Typeface.MONOSPACE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Convert from Kelvin to Fahrenheit
    public String convertTemperature(String temp)
    {
        double x = Double.parseDouble(temp);
        x = ((x-273.15) * (9/5) + 32);
        return formatter.format(x);
    }

    //Convert from m/s to mph
    public String convertWind(String temp)
    {
        double x = Double.parseDouble(temp);
        x = x * 2.237;
        return formatter.format(x);
    }
}

