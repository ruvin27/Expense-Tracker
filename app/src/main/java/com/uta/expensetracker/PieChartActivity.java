package com.uta.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PieChartActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference expenseRef;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        getSupportActionBar().setTitle("Monthly Expense");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PieChart pieChart = findViewById(R.id.pieChart);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        expenseRef = database.getReference("users/" + userID + "/expenses");

        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Float> Category = new HashMap<String, Float>();

                // Iterate through the DataSnapshot objects to extract the expenses
                if (snapshot.exists() && snapshot.hasChildren()) {
                    for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                        Expense expense = expenseSnapshot.getValue(Expense.class);
                        if (Category.containsKey(expense.category)){
                            Float amt = Category.get(expense.category);
                            Category.put(expense.category, new Float(expense.amount+amt));
                        }
                        else{
                            Category.put(expense.category, new Float(expense.amount));
                        }
                        System.out.println(Category);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });


        ArrayList<PieEntry> categories = new ArrayList<>();
        categories.add(new PieEntry(500,"Food"));
        categories.add(new PieEntry(500,"Grocery"));
        categories.add(new PieEntry(500,"Misc"));
        categories.add(new PieEntry(500,"Rent"));

        PieDataSet pieDataSet = new PieDataSet(categories, "Categories");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        PieData pieData = new PieData((pieDataSet));

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Categories");
        pieChart.setCenterTextSize(22f);
        pieChart.animate();
    }
}