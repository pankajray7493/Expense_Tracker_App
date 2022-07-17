package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class todaydata extends RecyclerView.Adapter<todaydata.viewholder>{

    private Context context;
    private List<Data> mydata;

    private String postkey = "";
    private String item = "";
    private int amount = 0;
    private String notes = "";

    public todaydata(Context context, List<Data> mydata) {
        this.context = context;
        this.mydata = mydata;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.retreve_data,parent,false);
        return new todaydata.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        final  Data data = mydata.get(position);

        holder.item.setText("Type: "+ data.getItem());
        holder.amount.setText("Amount: ₹"+data.getAmount());
        holder.date.setText("On: "+data.getDate());
        holder.notes.setText("Note: "+data.getNotes());

        switch (data.getItem()){
            case "Transport":
                holder.imageView.setImageResource(R.drawable.ic_transport);
                break;
            case "Food":
                holder.imageView.setImageResource(R.drawable.ic_food);
                break;
            case "House":
                holder.imageView.setImageResource(R.drawable.ic_house);
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.ic_entertainment);
                break;
            case "Education":
                holder.imageView.setImageResource(R.drawable.ic_education);
                break;
            case "Charity":
                holder.imageView.setImageResource(R.drawable.ic_consultancy);
                break;
            case "Health":
                holder.imageView.setImageResource(R.drawable.ic_health);
                break;
            case "Personal":
                holder.imageView.setImageResource(R.drawable.ic_personalcare);
                break;
            case "Others":
                holder.imageView.setImageResource(R.drawable.ic_other);
                break;

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postkey = data.getId();
                item = data.getItem();
                amount = data.getAmount();
                notes = data.getNotes();
                updatedata();
            }
        });
    }

    private void updatedata() {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.update_layout,null);

        mydialog.setView(view);
        final  AlertDialog dialog = mydialog.create();

        final TextView mitem = view.findViewById(R.id.text);
        final EditText mamount = view.findViewById(R.id.amount2);
        final EditText mnote = view.findViewById(R.id.note3);



        mitem.setText(item);
        mamount.setText(String.valueOf(amount));
        mamount.setSelection(String.valueOf(amount).length());
        mnote.setText(notes);
        mnote.setSelection(notes.length());

        Button delbtn = view.findViewById(R.id.btndelete);
        Button update = view.findViewById(R.id.btnupdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(mamount.getText().toString());
                notes = mnote.getText().toString();


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

                Data data = new Data(item,date,postkey,itemday,itemweek,itemmonth,amount,months.getMonths(),week.getWeeks(),notes);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                reference.child(postkey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Update successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.child(postkey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public int getItemCount() {

        return mydata.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{

        public TextView item,amount,date,notes;
        public ImageView imageView;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.itemselect);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.imageview);
            notes = itemView.findViewById(R.id.note1);

        }
    }
}
