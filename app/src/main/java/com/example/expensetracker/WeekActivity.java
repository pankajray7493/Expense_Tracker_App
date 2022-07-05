package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeekActivity extends AppCompatActivity {

    private TextView textView;
    private RecyclerView recyclerView;

    private WeekAdapter weekAdapter;
    private List<Data> mydatalist;

    private FirebaseAuth Auth;
    private String userid = "";
    private DatabaseReference weekyref;

    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        textView = findViewById(R.id.totalexpenseweek);
        recyclerView = findViewById(R.id.recycle4);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Auth = FirebaseAuth.getInstance();
        userid =Auth.getCurrentUser().getUid();


        mydatalist = new ArrayList<>();
        weekAdapter = new WeekAdapter(WeekActivity.this,mydatalist);
        recyclerView.setAdapter(weekAdapter);

        if (getIntent().getExtras()!= null){
            type = getIntent().getStringExtra("type");
            if (type.equals("week")){
                readspenditem();
            }
            else{
                readmonditem();
            }
        }



    }

    private void readmonditem() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Months month = Months.monthsBetween(poch,now);
        weekyref = FirebaseDatabase.getInstance().getReference("expenses").child(userid);

        Query query = weekyref.orderByChild("month").equalTo(month.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mydatalist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Data data = dataSnapshot.getValue(Data.class);
                    mydatalist.add(data);
                }
                weekAdapter.notifyDataSetChanged();

                int totalamount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int ptotal = Integer.parseInt(String.valueOf(total));
                    totalamount += ptotal;

                    textView.setText("Total Expenses This Month ₹ " + totalamount);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readspenditem() {


        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Weeks week = Weeks.weeksBetween(poch,now);
        weekyref = FirebaseDatabase.getInstance().getReference("expenses").child(userid);

        Query query = weekyref.orderByChild("week").equalTo(week.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mydatalist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Data data = dataSnapshot.getValue(Data.class);
                    mydatalist.add(data);
                }
                weekAdapter.notifyDataSetChanged();

                int totalamount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int ptotal = Integer.parseInt(String.valueOf(total));
                    totalamount += ptotal;

                    textView.setText("Total Expenses This Week ₹ " + totalamount);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}