package com.uta.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Charts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        getSupportActionBar().setTitle("Charts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}