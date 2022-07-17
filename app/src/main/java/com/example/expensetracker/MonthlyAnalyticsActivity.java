package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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
import java.util.Map;

public class MonthlyAnalyticsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String userid = "";
    private DatabaseReference expenseref,personalref;

    private PieChart pieChart;

    private TextView totalbudegetamount,analytransport,analyfood,analyhouse,analyentertainment,analyeducation,analycharity;
    private TextView analyhealth,analypersonal,analyothers,monthspentamount;

    private RelativeLayout linearlayoutall,llTrans,llHouse,llEnter,llfood,llEdu,llCharity,llHelth,llPersonal,llOthers,lltodayspent;


    private TextView progressratioTrans,PRfood,PRhouse,PRent,PRedu,PRcharity,PRhelth,PRpersonal,PRothers,PRtotal,monthratiospent;
    private ImageView statusimageTrans,SIfood,SIhouse,SIent,SIedu,SIcharity,SIhelth,SIpersonal,SIothers,SItotal,monthspendingimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_analytics);

        getSupportActionBar().setTitle("Monthly Analytics");

        mAuth = FirebaseAuth.getInstance();
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        expenseref = FirebaseDatabase.getInstance().getReference("expenses").child(userid);
        personalref = FirebaseDatabase.getInstance().getReference("personal").child(userid);

        totalbudegetamount = findViewById(R.id.totalamountspent3);

        //general
        monthspentamount = findViewById(R.id.monthspentamount3);

        lltodayspent = findViewById(R.id.relativeanaly);


        analytransport = findViewById(R.id.analyticsTransportAmount3);
        analyfood = findViewById(R.id.analyticsFoodAmount3);
        analyhouse = findViewById(R.id.analyticsHouseExpensesAmount3);
        analyentertainment = findViewById(R.id.analyticsEntertainmentAmount3);
        analyeducation = findViewById(R.id.analyticsEducationAmount3);
        analycharity = findViewById(R.id.analyticsCharityAmount3);
        analyhealth = findViewById(R.id.analyticsHealthAmount3);

        analypersonal = findViewById(R.id.analyticsPersonalExpensesAmount3);
        analyothers = findViewById(R.id.analyticsOtherAmount3);

        // Relative layout

        llTrans = findViewById(R.id.linearLayoutTransport3);
        llfood = findViewById(R.id.linearLayoutFood3);
        llHouse = findViewById(R.id.linearLayoutFoodHouse3);
        llEnter = findViewById(R.id.linearLayoutEntertainment3);
        llEdu = findViewById(R.id.linearLayoutEducation3);
        llCharity = findViewById(R.id.linearLayoutCharity3);
        llHelth = findViewById(R.id.linearLayoutHealth3);
        llPersonal = findViewById(R.id.linearLayoutPersonalExp3);
        llOthers = findViewById(R.id.linearLayoutOther3);






        pieChart = findViewById(R.id.piechar3);

        getTotalDayTransportExpenses();
        getTotalDayFoodExpenses();
        getTotalDayHouseExpenses();
        getTotalDayEntertainmentExpenses();
        getTotalDayEducationExpenses();
        getTotalDayCharityExpenses();
        getTotalDayHealthExpenses();
        getTotalDayPersonalExpenses();
        getTotalDayOthersExpenses();
//
        getTotalDaySpending();

        loadgraph();



    }
    private void getTotalDayTransportExpenses() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Months months = Months.monthsBetween(poch,now);

        String itemmonth = "Transport"+months.getMonths();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemmonth").equalTo(itemmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analytransport.setText("Spent ₹" + totalAmount);
                    }
                    personalref.child("monthTrans").setValue(totalAmount);
                }
                else{
                    llTrans.setVisibility(View.GONE);
                    personalref.child("monthTrans").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalDayFoodExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Months months = Months.monthsBetween(poch,now);

        String itemmonth = "Food"+months.getMonths();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemmonth").equalTo(itemmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyfood.setText("Spent ₹" + totalAmount);
                    }
                    personalref.child("monthFood").setValue(totalAmount);
                }
                else{
                    llfood.setVisibility(View.GONE);
                    personalref.child("monthFood").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalDayHouseExpenses() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Months months = Months.monthsBetween(poch,now);

        String itemmonth = "House"+months.getMonths();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemmonth").equalTo(itemmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyhouse.setText("Spent ₹" + totalAmount);
                    }
                    personalref.child("monthHouse").setValue(totalAmount);
                }
                else{
                    llHouse.setVisibility(View.GONE);
                    personalref.child("monthHouse").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalDayEntertainmentExpenses() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Months months = Months.monthsBetween(poch,now);

        String itemmonth = "Entertainment"+months.getMonths();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemmonth").equalTo(itemmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyentertainment.setText("Spent ₹" + totalAmount);
                    }
                    personalref.child("monthEntertainment").setValue(totalAmount);
                }
                else{
                    llEnter.setVisibility(View.GONE);
                    personalref.child("monthEntertainment").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalDayEducationExpenses() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Months months = Months.monthsBetween(poch,now);

        String itemmonth = "Education"+months.getMonths();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemmonth").equalTo(itemmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyeducation.setText("Spent ₹" + totalAmount);
                    }
                    personalref.child("monthEducation").setValue(totalAmount);
                }
                else{
                    llEdu.setVisibility(View.GONE);
                    personalref.child("monthEducation").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDayCharityExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Months months = Months.monthsBetween(poch,now);

        String itemmonth = "Charity"+months.getMonths();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemmonth").equalTo(itemmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analycharity.setText("Spent ₹" + totalAmount);
                    }
                    personalref.child("monthCharity").setValue(totalAmount);
                }
                else{
                    llCharity.setVisibility(View.GONE);
                    personalref.child("monthCharity").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDayHealthExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Months months = Months.monthsBetween(poch,now);

        String itemmonth = "Health"+months.getMonths();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemmonth").equalTo(itemmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyhealth.setText("Spent ₹" + totalAmount);
                    }
                    personalref.child("monthHealth").setValue(totalAmount);
                }
                else{
                    llHelth.setVisibility(View.GONE);
                    personalref.child("monthHealth").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDayPersonalExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Months months = Months.monthsBetween(poch,now);

        String itemmonth = "Personal"+months.getMonths();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemmonth").equalTo(itemmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analypersonal.setText("Spent ₹" + totalAmount);
                    }
                    personalref.child("monthPersonal").setValue(totalAmount);
                }
                else{
                    llPersonal.setVisibility(View.GONE);
                    personalref.child("monthPersonal").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDayOthersExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Months months = Months.monthsBetween(poch,now);

        String itemmonth = "Others"+months.getMonths();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemmonth").equalTo(itemmonth);
        query.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;
                        analyothers.setText("Spent ₹" + totalAmount);
                    }
                    personalref.child("monthOthers").setValue(totalAmount);
                }
                else{
                    llOthers.setVisibility(View.GONE);
                    personalref.child("monthOthers").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getTotalDaySpending() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Months months = Months.monthsBetween(poch,now);




        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int ptotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += ptotal;

                    }
                    totalbudegetamount.setText("Total Spent This Month ₹" + totalAmount);
                    monthspentamount.setText("Spent ₹"+totalAmount);
                }
                else{
                    totalbudegetamount.setText("You have not spent today");
                    pieChart.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MonthlyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void loadgraph(){
        personalref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    int totalTrans;
                    if (snapshot.hasChild("monthTrans")) {
                        totalTrans = Integer.parseInt(snapshot.child("monthTrans").getValue().toString());
                    } else {
                        totalTrans = 0;
                    }

                    int totalfood;
                    if (snapshot.hasChild("monthFood")) {
                        totalfood = Integer.parseInt(snapshot.child("monthFood").getValue().toString());
                    } else {
                        totalfood = 0;
                    }

                    int totalhouse;
                    if (snapshot.hasChild("monthHouse")) {
                        totalhouse = Integer.parseInt(snapshot.child("monthHouse").getValue().toString());
                    } else {
                        totalhouse = 0;
                    }

                    int totalent;
                    if (snapshot.hasChild("monthEntertainment")) {
                        totalent = Integer.parseInt(snapshot.child("monthEntertainment").getValue().toString());
                    } else {
                        totalent = 0;
                    }

                    int totaledu;
                    if (snapshot.hasChild("monthEducation")) {
                        totaledu = Integer.parseInt(snapshot.child("monthEducation").getValue().toString());
                    } else {
                        totaledu = 0;
                    }

                    int totalcha;
                    if (snapshot.hasChild("monthCharity")) {
                        totalcha = Integer.parseInt(snapshot.child("monthCharity").getValue().toString());
                    } else {
                        totalcha = 0;
                    }

                    int totalhealth;
                    if (snapshot.hasChild("monthHealth")) {
                        totalhealth = Integer.parseInt(snapshot.child("monthHealth").getValue().toString());
                    } else {
                        totalhealth = 0;
                    }

                    int totalper;
                    if (snapshot.hasChild("monthPersonal")) {
                        totalper = Integer.parseInt(snapshot.child("monthPersonal").getValue().toString());
                    } else {
                        totalper = 0;
                    }

                    int totaloth;
                    if (snapshot.hasChild("monthOthers")) {
                        totaloth = Integer.parseInt(snapshot.child("monthOthers").getValue().toString());
                    }
                    else {
                        totaloth = 0;
                    }

                    int [] Mycolor = {Color.rgb(192,0,0),Color.rgb(255,0,0),Color.rgb(255,192,0),Color.rgb(153,51,255)
                            ,Color.rgb(102,102,255),Color.rgb(51,0,51),Color.rgb(96,96,96),Color.rgb(76,0,143)
                            ,Color.rgb(0,102,0)};
                    ArrayList<Integer> colors = new ArrayList<>();
                    for (int c : Mycolor){
                        colors.add(c);
                    }
                    ArrayList<PieEntry> entry = new ArrayList<>();
                    if (totalTrans!=0) {
                        PieEntry pieEntry = new PieEntry(totalTrans, "Transport");
                        entry.add(pieEntry);
                    }
                    if (totalfood !=0) {
                        PieEntry pieEntry1 = new PieEntry(totalfood, "Food");
                        entry.add(pieEntry1);
                    }
                    if (totalhouse !=0){
                        PieEntry pieEntry2 = new PieEntry(totalhouse,"House");
                        entry.add(pieEntry2);
                    }
                    if (totalent!=0) {
                        PieEntry pieEntry3 = new PieEntry(totalent, "Entertainment");
                        entry.add(pieEntry3);
                    }
                    if (totaledu!=0) {
                        PieEntry pieEntry4 = new PieEntry(totaledu, "Education");
                        entry.add(pieEntry4);
                    }
                    if (totalcha!=0) {
                        PieEntry pieEntry5 = new PieEntry(totalcha, "Charity");
                        entry.add(pieEntry5);
                    }
                    if (totalhealth!=0) {
                        PieEntry pieEntry6 = new PieEntry(totalhealth, "Health");
                        entry.add(pieEntry6);
                    }
                    if (totalper!=0) {
                        PieEntry pieEntry7 = new PieEntry(totalper, "Personal");
                        entry.add(pieEntry7);
                    }
                    if (totaloth!=0) {
                        PieEntry pieEntry8 = new PieEntry(totaloth, "Others");
                        entry.add(pieEntry8);
                    }





                    PieDataSet pieDataSet = new PieDataSet(entry,"Expenses");

                    pieDataSet.setColors(colors);

                    pieChart.setData(new PieData(pieDataSet));
                    pieDataSet.setValueTextSize(14f);
                    pieDataSet.setValueTextColor(Color.BLACK);

                    pieChart.animateXY(5000,5000);

                    pieChart.getDescription().setEnabled(false);

                } else {
                    Toast.makeText(MonthlyAnalyticsActivity.this, "Child Does Not Exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MonthlyAnalyticsActivity.this, "Child Does Not Exist", Toast.LENGTH_SHORT).show();
            }
        });
    }
}