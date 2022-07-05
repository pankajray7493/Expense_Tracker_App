package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private RecyclerView recyclerView;

    private todaydata Todayadapter;
    private List<Data> mydatalist;

    private FirebaseAuth mAuth;
    private  String userid ="";
    private FirebaseDatabase expenseref,personalref;

    private Button search;
    private TextView totalamountspent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        search = findViewById(R.id.search);
        totalamountspent = findViewById(R.id.historytotalamountexpend);

        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        recyclerView =findViewById(R.id.recylerview_idfeed);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mydatalist = new ArrayList<>();
        Todayadapter = new todaydata(HistoryActivity.this,mydatalist);
        recyclerView.setAdapter(Todayadapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showdatepicker();
            }
        });
    }

    private void showdatepicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,this
        , Calendar.getInstance().get(Calendar.YEAR)
        ,Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
        int months = month+1;
        String date = dayofmonth+"-"+months+"-"+year;

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("expenses").child(userid);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                mydatalist.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()){
                    Data data = snapshot.getValue(Data.class);
                    mydatalist.add(data);
                }
                Todayadapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);

                int totalamount =0;
                for (DataSnapshot ds: datasnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int ptotal = Integer.parseInt(String.valueOf(total));
                    totalamount += ptotal;
                    if (totalamount>0){
                    totalamountspent.setVisibility(View.VISIBLE);
                    totalamountspent.setText("Spent Amount is: ₹" + totalamount);
                }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HistoryActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}