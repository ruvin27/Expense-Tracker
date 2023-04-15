package com.uta.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class History extends AppCompatActivity {
    private ListView listview;
    private HistoryLayout historyLayout;



    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference expenseRef;
    DatabaseReference userReference;
    String userID;
    SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");

    private static final int MAX_ITEMS_PER_PAGE = 30; // Or any other value you choose



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("History class");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        listview = findViewById(R.id.listExpense);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        expenseRef = database.getReference("users/" + userID + "/expenses");

        //to display the history based on the filter applied from nthe overview page
        ArrayList<Expense> filterList = (ArrayList<Expense>) getIntent().getSerializableExtra("expenses");

        if (filterList == null || filterList.isEmpty()) {
            // If the expense list is null or empty, retrieve all expenses from the database
            expenseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Expense> listOfExpenses = new ArrayList<>();

                    // Iterate through the DataSnapshot objects to extract the expenses
                    if (snapshot.exists() && snapshot.hasChildren()) {
                        for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                            Expense expense = expenseSnapshot.getValue(Expense.class);
                            listOfExpenses.add(expense);
                        }
                    }
                    // Create and set the adapter for the RecyclerView
                    historyLayout = new HistoryLayout(History.this, R.layout.activity_history_layout, listOfExpenses);
                    listview.setAdapter(historyLayout);
                    historyLayout.notifyDataSetChanged();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                }
            });
        } else {
            // If the expense list is not null or empty, use it to populate the RecyclerView
            historyLayout = new HistoryLayout(History.this, R.layout.activity_history_layout, filterList);
            listview.setAdapter(historyLayout);
            historyLayout.notifyDataSetChanged();
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        getMenuInflater().inflate(R.menu.menu_pdf,menu);

        MenuItem search = menu.findItem(R.id.search);
        MenuItem pdf = menu.findItem(R.id.pdf);

        SearchView search_view = null;
        if (search != null) {
            search_view = (SearchView) search.getActionView();
        }
        System.out.println("search view: " + search_view);

        if (search_view != null) {
            search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    searchText(s);
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String s) {
                    searchText(s);
                    return false;
                }
            });
         }
      return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.pdf:
                //add the function to perform here
                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                return(true);

        }
        return(super.onOptionsItemSelected(item));
    }

    private void createPdf() throws FileNotFoundException {
        PdfDocument mydoc = new PdfDocument();
        PdfDocument.PageInfo pageinfo = new PdfDocument.PageInfo.Builder(700,750,1).create();
        PdfDocument.Page page = mydoc.startPage(pageinfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        Typeface boldTypeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        Paint boldPaint = new Paint(paint);
        boldPaint.setTypeface(boldTypeface);

        int x = 20;
        int y=50;
        Double tot_amount = 0.0;
//        final CountDownLatch latch = new CountDownLatch(1);
//        final String[] uname = new String[1];
//
//        userReference = database.getReference("users/"+userID);
//        userReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                uname[0] = snapshot.child("name").getValue(String.class);
//                System.out.println("uname[0]: " + uname[0]);
//                latch.countDown();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                System.out.println("No user in the database found");
//                latch.countDown();
//            }
//        });
//
//        try {
//            latch.await(); //
//            System.out.println("uname[0] outside onDataChange(): " + uname[0]);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        System.out.println("1uname[0]" + uname[0]);
        boldPaint.setTextSize(25f);
        paint.setColor(Color.BLACK);

        canvas.drawText("Expense Tracker: Expense Report",x,y,boldPaint);
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.baseline_attach_money_24);
//        canvas.drawBitmap(bmp, x+300, y, paint);
        paint.setTextSize(22f);
//        canvas.drawText("User Name: " + uname[0],x,y+30,boldPaint);

        paint.setTextSize(16f);
        canvas.drawLine(20, y+40, 650, y+40, paint);
        canvas.drawText("Name",x,y+70,paint);
        canvas.drawText("Description",x+120,y+70,paint);
        canvas.drawText("Date",x+340,y+70,paint);
        canvas.drawText("Category",x+440,y+70,paint);
        canvas.drawText("Amount",x+530,y+70,paint);

        canvas.drawLine(20, y+90, 650, y+90, paint);

        y = 160;
        for(int i=0; i<historyLayout.getCount();i++){
            Expense expense = historyLayout.getItem(i);
            tot_amount = tot_amount + expense.getAmount();
            String date_str = expense.getDate();

            Date formattedDate = null;
            try {
                formattedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US).parse(date_str);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String converted_date = new SimpleDateFormat("MMM dd yyyy", Locale.US).format(formattedDate);

            canvas.drawText(expense.getName(),x,y,paint);
            canvas.drawText(expense.getDescription(),x+120,y,paint);
            canvas.drawText(converted_date,x+340,y,paint);
            canvas.drawText(expense.getCategory(),x+440,y,paint);
            canvas.drawText(String.format("$%.2f", expense.getAmount()),x+530,y,paint);
            y +=40;

            if ((i + 1) % MAX_ITEMS_PER_PAGE == 0) {
                mydoc.finishPage(page);
                page = mydoc.startPage(pageinfo);
                canvas = page.getCanvas();
                y =70;
            }
        }
        System.out.println("y:" + y);
        canvas.drawLine(20, y+30, 650, y+30, paint);
        boldPaint.setTextSize(22f);
        canvas.drawText("Total Amount Spent: " +String.format("$%.2f",tot_amount),x,y+90,boldPaint);
        mydoc.finishPage(page);

            Date currentDate = new Date();
            String curr_date = new SimpleDateFormat("MMM dd yyyy", Locale.US).format(currentDate);
            File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File pdfFile = new File(downloadsFolder,"expenses_"+curr_date+".pdf");
        try{
            FileOutputStream fp = new FileOutputStream(pdfFile);
            mydoc.writeTo(fp);
            mydoc.close();
            fp.close();
        } catch (IOException e){
            System.out.println("Exception" + e);
            e.printStackTrace();
        }
        Toast.makeText(this, "PDF Created!!", Toast.LENGTH_SHORT).show();
    }

    public void searchText(String s){
        String search_text = s;
        System.out.println("text: " + search_text);
        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Expense> listOfExpenses = new ArrayList<>();

                if (snapshot.exists() && snapshot.hasChildren()) {
                    for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                        Expense expense = expenseSnapshot.getValue(Expense.class);
                        if (((expense.getName().toLowerCase()).equals(search_text.toLowerCase())) ||
                                (expense.getName().split(" ")[0].toLowerCase()).equals(search_text.toLowerCase())
                                ||((expense.getName().split(" ")[1].toLowerCase()).equals(search_text.toLowerCase()))) {
                                    listOfExpenses.add(expense);
                            }
                        }
                    }

                historyLayout = new HistoryLayout(History.this, R.layout.activity_history_layout, listOfExpenses);
                listview.setAdapter(historyLayout);
                historyLayout.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(History.this, "No data to display", Toast.LENGTH_SHORT).show();
            }
        });
    }

}