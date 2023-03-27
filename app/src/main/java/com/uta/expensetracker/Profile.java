package com.uta.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class Profile extends AppCompatActivity {
    ImageButton dashboard;
    ImageButton charts;
    ImageButton history;
    ImageButton profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dashboard = findViewById(R.id.imageButton1);
        charts = findViewById(R.id.imageButton2);
        history = findViewById(R.id.imageButton3);
        profile = findViewById(R.id.imageButton4);

        charts.setOnClickListener(view -> goToCharts());
        history.setOnClickListener(view -> goToHistory());
        profile.setOnClickListener(view -> goToProfile());
        dashboard.setOnClickListener(view -> goToDashboard());

    }

    public void goToDashboard(){
        startActivity(new Intent(Profile.this,Overview.class));
    }

    public void goToAddExpense(){
        startActivity(new Intent(Profile.this,AddExpense.class));
    }

    public void goToCharts(){
        startActivity(new Intent(Profile.this,Charts.class));
    }

    public void goToHistory(){
        startActivity(new Intent(Profile.this,History.class));
    }
    public void goToProfile(){
        startActivity(new Intent(Profile.this,Profile.class));
    }
}