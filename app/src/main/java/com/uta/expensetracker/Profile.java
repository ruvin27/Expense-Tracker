package com.uta.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.view.ViewCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    ImageButton dashboard;
    ImageButton charts;
    ImageButton history;
    ImageButton profile;
    TextView name;
    TextView emailid;
    Button edit;
    ImageButton logout;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userReference;
   String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        dashboard = findViewById(R.id.imageButton1);
        charts = findViewById(R.id.imageButton2);
        history = findViewById(R.id.imageButton3);
        profile = findViewById(R.id.imageButton4);
        name = findViewById(R.id.textView4);
        emailid= findViewById(R.id.textView15);
        edit = findViewById(R.id.editbutton);
        logout = findViewById(R.id.logoutButton);
        logout.setTooltipText("Click to Logout");

       setProfileDetails();

        charts.setOnClickListener(view -> goToCharts());
        history.setOnClickListener(view -> goToHistory());
        profile.setOnClickListener(view -> goToProfile());
        dashboard.setOnClickListener(view -> goToDashboard());

        edit.setOnClickListener(view -> goToEditProfile());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mAuth.signOut();
//                Toast.makeText(Profile.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Profile.this, LoginActivity.class));
                userLogout();
            }
        });

    }

   public void userLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                dialogInterface.dismiss();
                Toast.makeText(Profile.this, "Logout Successfull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Profile.this, LoginActivity.class));
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
                Toast.makeText(Profile.this, "logout cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
   }


    public void setProfileDetails(){
        userID = mAuth.getCurrentUser().getUid();
        userReference = database.getReference("users/"+userID);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uname = snapshot.child("name").getValue(String.class);
                String uemail = snapshot.child("email").getValue(String.class);
                System.out.println("name"+ uname+ uemail);
                name.setText(uname);
                emailid.setText(uemail);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("No user in the database found");
            }
        });
    }

    public void goToEditProfile(){
        userID = mAuth.getCurrentUser().getUid();
        userReference = database.getReference("users/"+userID);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uname = snapshot.child("name").getValue(String.class);
                String uemail = snapshot.child("email").getValue(String.class);
                System.out.println("name"+ uname+ uemail);
                Intent intent = new Intent(Profile.this,EditProfile.class);
                intent.putExtra("name",uname);
                intent.putExtra("email",uemail);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("No user in the database found");
            }
        });
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