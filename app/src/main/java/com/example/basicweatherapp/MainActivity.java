package com.example.basicweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner cityName;
    private Button searchButton;

    final String[] listOfCities = {
            "London",
            "Dubai",
            "Istanbul",
            "New York",
            "Shanghai"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        searchButton = findViewById(R.id.searchButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOfCities);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        cityName.setAdapter(adapter);
        cityName.setOnItemSelectedListener(this);



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = cityName.getSelectedItem().toString();
                Intent intent = new Intent(getApplicationContext(), CityWeather.class);
                intent.putExtra("cityName", name);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) parent.getChildAt(0)).setTextSize(24);
        ((TextView) parent.getChildAt(0)).setTypeface(Typeface.MONOSPACE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}
