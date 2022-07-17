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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

public class ExpenseActivity extends AppCompatActivity {

    private TextView totalexpense;
    private RecyclerView recycle;

    private FloatingActionButton action;

    private DatabaseReference expenseref;
    private FirebaseAuth Auth;
    private ProgressDialog loader;

    private String postkey = "";
    private String item = "";
    private int amount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);


        getSupportActionBar().setTitle("My Income");

        Auth =FirebaseAuth.getInstance();
        expenseref = FirebaseDatabase.getInstance().getReference().child("budget").child(Auth.getCurrentUser().getUid());
        loader =new ProgressDialog(this);

        totalexpense =findViewById(R.id.totalexpense);
        recycle =findViewById(R.id.recycle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(linearLayoutManager);


        expenseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              int totalamount =0;
              for (DataSnapshot snap : snapshot.getChildren()){
                  Data data =snap.getValue(Data.class);
                  totalamount += data.getAmount();
                  String stotal = String.valueOf("Total Income ₹" + totalamount);
                  totalexpense.setText(stotal);
              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        action= findViewById(R.id.fab2);

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additem();
            }
        });

    }
    private void additem(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myview = inflater.inflate(R.layout.incomelayout,null);
        alert.setView(myview);

        final AlertDialog dialog = alert.create();
        dialog.setCancelable(true);


        final Spinner spinner = myview.findViewById(R.id.item1);
        final EditText amount = myview.findViewById(R.id.amount2);
        final Button cancel = myview.findViewById(R.id.cancel1);
        final Button save = myview.findViewById(R.id.save1);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expense = amount.getText().toString();
                String expensetype = spinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(expense)){
                    amount.setError("Amount is require");
                    return;
                }
                if (expensetype.equals("Select Item")){
                    Toast.makeText(ExpenseActivity.this, "Select a valid item", Toast.LENGTH_SHORT).show();

                }
                else {
                    loader.setMessage("Adding a Income item");
                    loader.setCanceledOnTouchOutside(true);
                    loader.show();

                    String id = expenseref.push().getKey();
                    DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String date = dateFormat.format(cal.getTime());

                    MutableDateTime poch =new MutableDateTime();
                    poch.setDate(0);
                    DateTime now =new DateTime();
                    Weeks week = Weeks.weeksBetween(poch,now);
                    Months months = Months.monthsBetween(poch,now);

                    String itemday = expensetype+date;
                    String itemweek = expensetype+week.getWeeks();
                    String itemmonth = expensetype+months.getMonths();

                    Data data = new Data(expensetype,date,id,itemday,itemweek,itemmonth,Integer.parseInt(expense),months.getMonths(),week.getWeeks(),null);
                    expenseref.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                            Toast.makeText(ExpenseActivity.this, "Income Item Added", Toast.LENGTH_SHORT).show();
                        }
                        else{
                                Toast.makeText(ExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options =new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(expenseref,Data.class).build();

        FirebaseRecyclerAdapter<Data, myViewholder> adapter =new FirebaseRecyclerAdapter<Data, myViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull Data model) {
                holder.setamount("Amount:  ₹"+ model.getAmount());
                holder.setdate("On:  "+model.getDate());
                holder.setitem("Income Type:  "+model.getItem());

                holder.note.setVisibility(View.GONE);

                switch (model.getItem()){
                    case "Salary":
                        holder.imageView.setImageResource(R.drawable.bonus);
                        break;
                    case "Pocket Money":
                        holder.imageView.setImageResource(R.drawable.piggy);
                        break;
                    case "Bonus":
                        holder.imageView.setImageResource(R.drawable.bonus2);
                        break;
//                    case "Entertainment":
//                        holder.imageView.setImageResource(R.drawable.ic_entertainment);
//                        break;
//                    case "Education":
//                        holder.imageView.setImageResource(R.drawable.ic_education);
//                        break;
//                    case "Charity":
//                        holder.imageView.setImageResource(R.drawable.ic_consultancy);
//                        break;
//                    case "Health":
//                        holder.imageView.setImageResource(R.drawable.ic_health);
//                        break;
//                    case "Personal":
//                        holder.imageView.setImageResource(R.drawable.ic_personalcare);
//                        break;
                    case "Others":
                        holder.imageView.setImageResource(R.drawable.ic_other);
                        break;

                }
                holder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postkey = getRef(position).getKey();
                        item = model.getItem();
                        amount = model.getAmount();
                        updatedata();
                    }
                });



            }

            @NonNull
            @Override
            public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retreve_data,parent,false);
                return new myViewholder(view);
            }
        };
        recycle.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

    }
    public class myViewholder extends RecyclerView.ViewHolder{
        View myview;
        public ImageView imageView;
        public TextView note;

        public myViewholder(@NonNull View itemView) {
            super(itemView);
            myview =itemView;
            imageView=itemView.findViewById(R.id.imageview);
            note =itemView.findViewById(R.id.note1);
        }
        public void setitem(String itemname){
            TextView item = myview.findViewById(R.id.itemselect);
            item.setText(itemname);
        }
        public void setamount(String itemamount){
            TextView amount =myview.findViewById(R.id.amount);
            amount.setText(itemamount);
        }
        public void setdate(String itemdate){
            TextView date = myview.findViewById(R.id.date);
            date.setText(itemdate);
        }
    }

    private void updatedata(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_layout,null);

        mydialog.setView(view);
        final  AlertDialog dialog = mydialog.create();

        final TextView mitem = view.findViewById(R.id.text);
        final EditText mamount = view.findViewById(R.id.amount2);
        final EditText mnote = view.findViewById(R.id.note3);

        mnote.setVisibility(View.GONE);

        mitem.setText(item);
        mamount.setText(String.valueOf(amount));
        mamount.setSelection(String.valueOf(amount).length());


        Button delbtn = view.findViewById(R.id.btndelete);
        Button update = view.findViewById(R.id.btnupdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(mamount.getText().toString());



                DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                MutableDateTime poch =new MutableDateTime();
                poch.setDate(0);
                DateTime now =new DateTime();
                Weeks week = Weeks.weeksBetween(poch,now);
                Months months = Months.monthsBetween(poch,now);

                String itemday = item+date;
                String itemweek = itemday+week.getWeeks();
                String itemmonth = item+months.getMonths();

                Data data = new Data(item,date,postkey,itemday,itemweek,itemmonth,amount,months.getMonths(),week.getWeeks(),null);
                expenseref.child(postkey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ExpenseActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseref.child(postkey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ExpenseActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ExpenseActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();



    }
}




