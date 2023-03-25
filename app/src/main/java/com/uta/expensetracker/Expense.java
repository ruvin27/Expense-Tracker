package com.uta.expensetracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Expense {
    public String name;
    public String category;
    public Double amount;

    public String description;

    //public Date date;
    SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");

  public String date;

    public Expense(String name,Double amount,String description,Date date, String category ){
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.date = ISO_8601_FORMAT.format(date);
        this.category = category;

    }
}
