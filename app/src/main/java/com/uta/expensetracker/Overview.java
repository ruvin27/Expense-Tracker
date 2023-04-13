package com.uta.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Overview extends AppCompatActivity {
    TextView totalAmount;
    TextView foodAmt;
    TextView rentAmt;
    TextView groceryAmt;
    TextView miscAmt;
    ImageButton addExpense;
    ImageButton dashboard;
    ImageButton charts;
    ImageButton history;
    ImageButton profile;

    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference expReference;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        totalAmount = findViewById(R.id.textView8);
        foodAmt = findViewById(R.id.textView5);
        rentAmt = findViewById(R.id.textView10);
        groceryAmt = findViewById(R.id.textView12);
        miscAmt = findViewById(R.id.textView14);
        addExpense = findViewById(R.id.imageButton7);
        dashboard = findViewById(R.id.imageButton1);
        charts = findViewById(R.id.imageButton2);
        history = findViewById(R.id.imageButton3);
        profile = findViewById(R.id.imageButton4);
        addExpense.setTooltipText("Click to Add Expense");

        mAuth = FirebaseAuth.getInstance();
        displayTotalAmount();


        addExpense.setOnClickListener(view -> goToAddExpense() );
        charts.setOnClickListener(view -> goToCharts());
        history.setOnClickListener(view -> goToHistory());
        profile.setOnClickListener(view -> goToProfile());
        dashboard.setOnClickListener(view -> goToDashboard());


    }

    public void displayTotalAmount(){
        userID = mAuth.getCurrentUser().getUid();
        expReference = database.getReference("users/"+userID+"/expenses");
        expReference.addValueEventListener(new ValueEventListener() {
            double totalExpenses = 0.0;
            double totalFoodExpenses = 0.0;
            double totalRentExpenses = 0.0;
            double totalGroceryExpenses = 0.0;
            double totalMiscExpenses = 0.0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                    Expense expense = expenseSnapshot.getValue(Expense.class);
                    if (expense != null){
                        totalExpenses += expense.getAmount();
                        double expenseAmount = expense.getAmount();
                        String getCategory = expense.getCategory();
                        switch (getCategory){
                            case "Food":
                                totalFoodExpenses += expenseAmount;
                                break;
                            case "Rent":
                                totalRentExpenses += expenseAmount;
                                break;
                            case "Grocery":
                                totalGroceryExpenses += expenseAmount;
                                break;
                            case "MISC":
                                totalMiscExpenses += expenseAmount;
                                break;
                            case "default":
                                break;

                        }

                    }

                }
               totalAmount.setText("$ "+String.format("%.2f", totalExpenses));
                foodAmt.setText("$ "+ String.format("%.2f", totalFoodExpenses));
                rentAmt.setText("$ "+ String.format("%.2f",totalRentExpenses));
                groceryAmt.setText("$ "+ String.format("%.2f",totalGroceryExpenses));
                miscAmt.setText("$ "+ String.format("%.2f",totalMiscExpenses));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("No amount to show, some error occurred");
            }
        });

    }
    public void goToDashboard(){
        startActivity(new Intent(Overview.this,Overview.class));
    }

    public void goToAddExpense(){
        startActivity(new Intent(Overview.this,AddExpense.class));
    }

    public void goToCharts(){
        startActivity(new Intent(Overview.this,Charts.class));
    }

    public void goToHistory(){
        startActivity(new Intent(Overview.this,History.class));
    }
    public void goToProfile(){
        startActivity(new Intent(Overview.this,Profile.class));
    }

}