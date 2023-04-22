package com.uta.expensetracker;


import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {
    BarChart barChart;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference expenseRef;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        getSupportActionBar().setTitle("Bar Chart expenses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        expenseRef = database.getReference("users/" + userID + "/expenses");

         barChart = findViewById(R.id.BarChart);
        int groupCount = 12;
        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Jan");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Apr");
        xVals.add("May");
        xVals.add("Jun");
        xVals.add("Jul");
        xVals.add("Aug");
        xVals.add("Sep");
        xVals.add("Oct");
        xVals.add("Nov");
        xVals.add("Dec");

        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        double[] monthlyExpenses = new double[12];

        // Get monthly expenses from Firebase
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalExpenses = 0.0;
                for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                    Expense expense = expenseSnapshot.getValue(Expense.class);
                    if (expense != null) {
                        String expenseDate = expense.getDate();
                        String[] parts = expenseDate.split("-");
                        int year = Integer.parseInt(parts[0]);
                        if (year == LocalDate.now().getYear()) {
                            int month = Integer.parseInt(parts[1]) - 1; // months are zero-indexed
                            monthlyExpenses[month] += expense.getAmount();
                        }
                    }
                }

                // Add monthly expenses to yVals1
                for (int i = 0; i < 12; i++) {
                    yVals1.add(new BarEntry(i, (float) monthlyExpenses[i]));
                    System.out.println("hello:" + monthlyExpenses[i]);
                }

                //BarDataSet barDataSet = new BarDataSet(yVals1, "Monthly Expenses");
                BarDataSet barDataSet = new BarDataSet(yVals1, "Monthly Expenses");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(0f);

                BarData barData = new BarData(barDataSet);
                //barChart.setFitBars(true);
                barChart.setData(barData);
                barChart.getDescription().setText("Monthly Expenses");
                barChart.animateY(1000);



                XAxis xAxis = barChart.getXAxis();
                xAxis.setGranularity(1f);
                xAxis.setGranularityEnabled(false);
                xAxis.setDrawGridLines(false);
                xAxis.setAxisMaximum(12);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
                xAxis.setLabelCount(xVals.size());
                //xAxis.setDescription("X-axis description");
                //xAxis.setAxisDescription("X-axis description");

                barChart.getDescription().setText("Monthly Expenses");

                //Y-axis
                barChart.getAxisRight().setEnabled(false);
                YAxis leftAxis = barChart.getAxisLeft();
                //leftAxis.setValueFormatter(new LargeValueFormatter());
                leftAxis.setDrawGridLines(false);
                leftAxis.setSpaceTop(35f);
                leftAxis.setAxisMinimum(0f);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BarChartActivity", "Error getting monthly expenses from Firebase", error.toException());
            }
        });
    }
}