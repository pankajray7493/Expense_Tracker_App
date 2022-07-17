package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.viewholder> {

    private Context context;
    private List<Data> mydata;

    public WeekAdapter(Context context, List<Data> mydata) {
        this.context = context;
        this.mydata = mydata;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.retreve_data,parent,false);
        return new WeekAdapter.viewholder(view);
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
