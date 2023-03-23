package com.uta.expensetracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Expense {
    public String name;
    public String category;
    public Float amount;
    SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");

    public String now = ISO_8601_FORMAT.format(new Date());

    public Expense(String name, String category, Float amount){
        this.name = name;
        this.category = category;
        this.amount = amount;
    }
}
