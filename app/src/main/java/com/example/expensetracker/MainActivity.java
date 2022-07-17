package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class MainActivity extends AppCompatActivity {

    private CardView expense, dailycard, weekcard, monthcard, analytics,history;

    private TextView txt1, txt2, txt3, txt4, txt5;

    private FirebaseAuth mAuth;
    private DatabaseReference budgetref;
    private DatabaseReference expenseref;
    private DatabaseReference personalref;
    String userid = "";

    private int ttlamountmonth = 0;
    private int ttlamountbudget = 0;
    private int ttlamountbudgetD = 0;
    private int ttlamountbudgetC = 0;



//    int sumexpenses = 0;
//    int sumincome = 0;
//    int saving = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        expense = findViewById(R.id.card);
        dailycard = findViewById(R.id.card1);
        weekcard = findViewById(R.id.card2);
        monthcard = findViewById(R.id.card3);
        analytics = findViewById(R.id.card4);
        history = findViewById(R.id.card5);

        txt1 = findViewById(R.id.textView1);
        txt2 = findViewById(R.id.textView2);
        txt3 = findViewById(R.id.textView3);
        txt4 = findViewById(R.id.textView4);
        txt5 = findViewById(R.id.textView5);

        mAuth = FirebaseAuth.getInstance();
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        budgetref = FirebaseDatabase.getInstance().getReference("budget").child(userid);
        expenseref = FirebaseDatabase.getInstance().getReference("expenses").child(userid);
        personalref = FirebaseDatabase.getInstance().getReference("personal").child(userid);


        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
                startActivity(intent);
            }
        });
        dailycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DailyActivity.class);
                startActivity(intent);
            }
        });
        weekcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WeekActivity.class);
                intent.putExtra("type", "week");
                startActivity(intent);
            }
        });
        monthcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WeekActivity.class);
                intent.putExtra("type", "month");
                startActivity(intent);
            }
        });
        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AnalyticActivity.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(intent);
            }
        });

        budgetref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        ttlamountbudgetD += ptotal;
                    }
                    ttlamountbudgetC = ttlamountbudgetD;

                } else {
                    personalref.child("budget").setValue(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getBudgetAmount();
        getTodaySpentAmount();
        getWeekSpentAmount();
        getMonthSpentAmount();
        getSavings();


    }

    private void getBudgetAmount() {
        budgetref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total1=0;
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        total1 += ptotal;
                        txt1.setText("₹ " + String.valueOf(total1));

                    }
                    personalref.child("budget").setValue(total1);
                } else {
                    ttlamountbudget = 0;
                    txt1.setText("₹ " + String.valueOf(0));
                    personalref.child("budget").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTodaySpentAmount() {
        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(userid);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalamount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int ptotal = Integer.parseInt(String.valueOf(total));
                    totalamount += ptotal;
                    txt2.setText("₹ " + totalamount);

                }
                personalref.child("today").setValue(totalamount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }

    private void getWeekSpentAmount() {
        MutableDateTime poch = new MutableDateTime();
        poch.setDate(0);
        DateTime now = new DateTime();
        Weeks week = Weeks.weeksBetween(poch, now);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(userid);

        Query query = reference.orderByChild("week").equalTo(week.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalamount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int ptotal = Integer.parseInt(String.valueOf(total));
                    totalamount += ptotal;

                    txt3.setText("₹ " + totalamount);
                }
                personalref.child("week").setValue(totalamount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getMonthSpentAmount() {

        MutableDateTime poch = new MutableDateTime();
        poch.setDate(0);
        DateTime now = new DateTime();
        Months month = Months.monthsBetween(poch, now);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(userid);

        Query query = reference.orderByChild("month").equalTo(month.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalamount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int ptotal = Integer.parseInt(String.valueOf(total));
                    totalamount += ptotal;

                    txt4.setText("₹ " + totalamount);

                }
                personalref.child("month").setValue(totalamount);
                ttlamountmonth = totalamount;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getSavings(){
        personalref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int budget;
                    if (snapshot.hasChild("budget")){
                        budget=Integer.parseInt(snapshot.child("budget").getValue().toString());
                    }else {
                        budget=0;
                    }
                    int monthspending=0;
                    if (snapshot.hasChild("month")){
                        monthspending=Integer.parseInt(Objects.requireNonNull(snapshot.child("month").getValue().toString()));
                    }else {
                        monthspending=0;
                    }
                    int saving = budget-monthspending;
                    txt5.setText("₹ " +saving);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.account) {
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.drawable.alert)
                .setTitle("Exit")
                .setMessage("Do you wants to exit this app")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                        System.exit(0);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}