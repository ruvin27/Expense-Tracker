package com.uta.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class History extends AppCompatActivity {
    private ListView listview;
    private HistoryLayout historyLayout;

    ImageButton dashboard;
    ImageButton charts;
    ImageButton history;
    ImageButton profile;

    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference expenseRef;
    String userID;
    SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("History class");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listview = findViewById(R.id.listExpense);
        dashboard = findViewById(R.id.imageButton1);
        charts = findViewById(R.id.imageButton2);
        history = findViewById(R.id.imageButton3);
        profile = findViewById(R.id.imageButton4);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        expenseRef = database.getReference("users/" + userID + "/expenses");
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Expense> listOfExpenses = new ArrayList<>();

                // Iterate through the DataSnapshot objects to extract the expenses
                if (snapshot.exists() && snapshot.hasChildren()) {
                    for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                        //System.out.println(expenseSnapshot);
                       Expense expense = expenseSnapshot.getValue(Expense.class);
                        listOfExpenses.add(expense);

                    }
                }
                historyLayout = new HistoryLayout(History.this,R.layout.activity_history_layout, listOfExpenses);
                listview.setAdapter(historyLayout);
                historyLayout.notifyDataSetChanged();

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                        Expense expense = (Expense) parent.getItemAtPosition(position);

                        Intent intent = new Intent(History.this,DeleteUpdateExpense.class);
                        intent.putExtra("expense",expense);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
        charts.setOnClickListener(view -> goToCharts());
        history.setOnClickListener(view -> goToHistory());
        profile.setOnClickListener(view -> goToProfile());
        dashboard.setOnClickListener(view -> goToDashboard());

    }
    public void goToDashboard(){
        startActivity(new Intent(History.this,Overview.class));
    }
    public void goToCharts(){
        startActivity(new Intent(History.this,Charts.class));
    }

    public void goToHistory(){
        startActivity(new Intent(History.this,History.class));
    }
    public void goToProfile(){
        startActivity(new Intent(History.this,Profile.class));
    }



}