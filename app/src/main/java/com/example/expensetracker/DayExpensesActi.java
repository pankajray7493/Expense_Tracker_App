package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class DayExpensesActi extends AppCompatActivity {

    private TextView textView;
    String userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_expenses);

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        textView = findViewById(R.id.totalbbb);

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemday = "Transport"+date;


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemday").equalTo(itemday);
        query.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()){
                   // Toast.makeText(MainActivity.this, "i am eorking", Toast.LENGTH_SHORT).show();

                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        textView.setText("Spent" + totalAmount);
                    }
                //    personalref.child("dayTrans").setValue(totalAmount);
                }
                else{
                    textView.setText("Spent" + 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //Toast.makeText(DailyAnalysisActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}