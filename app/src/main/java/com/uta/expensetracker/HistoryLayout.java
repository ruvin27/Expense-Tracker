package com.uta.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryLayout extends ArrayAdapter<Expense> {


    public HistoryLayout(Context context,int resource, List<Expense> expenses) {
        super(context, 0, expenses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Expense expense = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_history_layout, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.item_name);
        TextView amountTextView = convertView.findViewById(R.id.item_amount);
        TextView descriptionTextView = convertView.findViewById(R.id.item_description);
        TextView categoryTextView = convertView.findViewById(R.id.item_category);
        TextView dateTextView = convertView.findViewById(R.id.item_date);


       // String formattedDate = new SimpleDateFormat("MMM dd yyyy", Locale.US).format(expense.getDate());

        nameTextView.setText(expense.getName());
        descriptionTextView.setText(expense.getDescription());
        amountTextView.setText(String.format("$%.2f", expense.getAmount()));
        categoryTextView.setText(expense.getCategory());
        dateTextView.setText(expense.getDate());

        return convertView;
    }
}