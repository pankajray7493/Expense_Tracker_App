package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class DailyActivity extends AppCompatActivity { //Today spand

    private TextView textView;
  //  private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FloatingActionButton fabe;
    private ProgressDialog loader;

    private FirebaseAuth Auth;
    private String userid = "";
    private DatabaseReference dailyref;


    private todaydata today;
    private List<Data> mydata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        getSupportActionBar().setTitle("Today Expenses");

        textView = findViewById(R.id.totaltodayexpense);
       // progressBar = findViewById(R.id.progress);

        recyclerView = findViewById(R.id.recycle3);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        fabe = findViewById(R.id.fab3);

        loader = new ProgressDialog(this);

        Auth = FirebaseAuth.getInstance();
        userid =Auth.getCurrentUser().getUid();
        dailyref = FirebaseDatabase.getInstance().getReference("expenses").child(userid);



        mydata = new ArrayList<>();
        today = new todaydata(DailyActivity.this, mydata);
        recyclerView.setAdapter(today);

        readitem();

        
        fabe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additem();
            }
        });
    }

    private void readitem() {
        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

       // FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("expenses").child(userid);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mydata.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Data data = dataSnapshot.getValue(Data.class);
                    mydata.add(data);
                }
                today.notifyDataSetChanged();
                // progressBar.setVisibility(View.GONE);

                int totalamount =0;
                for (DataSnapshot ds : snapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>)ds.getValue();
                    Object total = map.get("amount");
                    int ptotal = Integer.parseInt(String.valueOf(total));
                    totalamount += ptotal;

                    textView.setText("Total Expenses ₹ " +totalamount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void additem() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myview = inflater.inflate(R.layout.input_layout,null);
        alert.setView(myview);

        final AlertDialog dialog = alert.create();
        dialog.setCancelable(true);


        final Spinner spinner = myview.findViewById(R.id.item);
        final EditText amount = myview.findViewById(R.id.amount1);
        final EditText note = myview.findViewById(R.id.note);
        final Button cancel = myview.findViewById(R.id.cancel);
        final Button save = myview.findViewById(R.id.save);
        note.setVisibility(View.VISIBLE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Amount = amount.getText().toString();
                String Item = spinner.getSelectedItem().toString();
                String notes = note.getText().toString();


                if (TextUtils.isEmpty(Amount)){
                    amount.setError("Amount is require");
                    return;
                }
                if (Item.equals("Select Item")){
                    Toast.makeText(DailyActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();

                }
                if (TextUtils.isEmpty(notes)){
                    note.setError("Notes is required");
                    return;
                }
                else {
                    loader.setMessage("Adding an expense item");
                    loader.setCanceledOnTouchOutside(true);
                    loader.show();

                    String id = dailyref.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());



                    MutableDateTime poch =new MutableDateTime();
                    poch.setDate(0);
                    DateTime now =new DateTime();
                    Weeks week = Weeks.weeksBetween(poch,now);
                    Months months = Months.monthsBetween(poch,now);

                    String itemday = Item+date;
                    String itemweek = Item+week.getWeeks();
                    String itemmonth = Item+months.getMonths();



                    Data data = new Data(Item,date,id,itemday,itemweek,itemmonth,Integer.parseInt(Amount),months.getMonths(),week.getWeeks(),notes);
                    dailyref.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(DailyActivity.this, "Expense Item Added", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(DailyActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                            loader.dismiss();

                        }
                    });

                    dialog.dismiss();


                }
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });

        dialog.show();

    }
}