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

//import com.anychart.AnyChart;
//import com.anychart.AnyChartView;
//import com.anychart.chart.common.dataentry.DataEntry;
//import com.anychart.chart.common.dataentry.ValueDataEntry;
//import com.anychart.charts.Pie;
//import com.anychart.enums.Align;
//import com.anychart.enums.LegendLayout;
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
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class DailyAnalysisActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String userid = "";
    private DatabaseReference expenseref,personalref;

    private TextView totalbudegetamount,analytransport,analyfood,analyhouse,analyentertainment,analyeducation,analycharity;
    private TextView analyhealth,analypersonal,analyothers,monthspentamount;

    private RelativeLayout linearlayoutall,llTrans,llHouse,llEnter,llfood,llEdu,llCharity,llHelth,llPersonal,llOthers,lltodayspent;

   // private AnyChartView anyChartView;

    private PieChart pieChart;

    private TextView progressratioTrans,PRfood,PRhouse,PRent,PRedu,PRcharity,PRhelth,PRpersonal,PRothers,PRtotal,monthratiospent;
    private ImageView statusimageTrans,SIfood,SIhouse,SIent,SIedu,SIcharity,SIhelth,SIpersonal,SIothers,SItotal,monthspendingimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_analysis);

        getSupportActionBar().setTitle("Today Analytics");

        mAuth = FirebaseAuth.getInstance();
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        expenseref = FirebaseDatabase.getInstance().getReference("expenses").child(userid);
        personalref = FirebaseDatabase.getInstance().getReference("personal").child(userid);

        totalbudegetamount = findViewById(R.id.totalamountspent1);

        //general
        monthspentamount = findViewById(R.id.monthspentamount1);

        lltodayspent = findViewById(R.id.relativeanaly);


        analytransport = findViewById(R.id.analyticsTransportAmount);
        analyfood = findViewById(R.id.analyticsFoodAmount);
        analyhouse = findViewById(R.id.analyticsHouseExpensesAmount);
        analyentertainment = findViewById(R.id.analyticsEntertainmentAmount);
        analyeducation = findViewById(R.id.analyticsEducationAmount);
        analycharity = findViewById(R.id.analyticsCharityAmount);
        analyhealth = findViewById(R.id.analyticsHealthAmount);

        analypersonal = findViewById(R.id.analyticsPersonalExpensesAmount);
        analyothers = findViewById(R.id.analyticsOtherAmount);

        // Relative layout

        llTrans = findViewById(R.id.linearLayoutTransport);
        llfood = findViewById(R.id.linearLayoutFood);
        llHouse = findViewById(R.id.linearLayoutFoodHouse);
        llEnter = findViewById(R.id.linearLayoutEntertainment);
        llEdu = findViewById(R.id.linearLayoutEducation);
        llCharity = findViewById(R.id.linearLayoutCharity);
        llHelth = findViewById(R.id.linearLayoutHealth);
        llPersonal = findViewById(R.id.linearLayoutPersonalExp);
        llOthers = findViewById(R.id.linearLayoutOther);



        //textview



        pieChart = findViewById(R.id.piechart);



        getTotalDayTransportExpenses();
        getTotalDayFoodExpenses();
        getTotalDayHouseExpenses();
        getTotalDayEntertainmentExpenses();
        getTotalDayEducationExpenses();
        getTotalDayCharityExpenses();
        getTotalDayHealthExpenses();
        getTotalDayPersonalExpenses();
        getTotalDayOthersExpenses();

        getTotalDaySpending();



       new java.util.Timer().schedule(new TimerTask() {
           @Override
           public void run() {

           }
       },2000
       );
        loadgraph();

    }

    private void getTotalDayTransportExpenses() {

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
                int totalAmount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int ptotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += ptotal;
                    analytransport.setText("Spent ₹" + totalAmount);
                }
                personalref.child("dayTrans").setValue(totalAmount);
            }
                else{
                    llTrans.setVisibility(View.GONE);
                    personalref.child("dayTrans").setValue(0);
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalysisActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalDayFoodExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemday = "Food"+date;


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemday").equalTo(itemday);
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
                    personalref.child("dayFood").setValue(totalAmount);
                }
                else{
                    llfood.setVisibility(View.GONE);
                    personalref.child("dayFood").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalysisActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getTotalDayHouseExpenses() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemday = "House"+date;


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemday").equalTo(itemday);
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
                    personalref.child("dayHouse").setValue(totalAmount);
                }
                else{
                    llHouse.setVisibility(View.GONE);
                    personalref.child("dayHouse").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalysisActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalDayEntertainmentExpenses() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemday = "Entertainment"+date;


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemday").equalTo(itemday);
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
                    personalref.child("dayEntertainment").setValue(totalAmount);
                }
                else{
                    llEnter.setVisibility(View.GONE);
                    personalref.child("dayEntertainment").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalysisActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalDayEducationExpenses() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemday = "Education"+date;


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemday").equalTo(itemday);
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
                    personalref.child("dayEducation").setValue(totalAmount);
                }
                else{
                    llEdu.setVisibility(View.GONE);
                    personalref.child("dayEducation").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalysisActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDayCharityExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemday = "Charity"+date;


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemday").equalTo(itemday);
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
                    personalref.child("dayCharity").setValue(totalAmount);
                }
                else{
                    llCharity.setVisibility(View.GONE);
                    personalref.child("dayCharity").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalysisActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDayHealthExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemday = "Health"+date;


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemday").equalTo(itemday);
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
                    personalref.child("dayHealth").setValue(totalAmount);
                }
                else{
                    llHelth.setVisibility(View.GONE);
                    personalref.child("dayHealth").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalysisActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDayPersonalExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemday = "Personal"+date;


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemday").equalTo(itemday);
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
                    personalref.child("dayPersonal").setValue(totalAmount);
                }
                else{
                    llPersonal.setVisibility(View.GONE);
                    personalref.child("dayPersonal").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalysisActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDayOthersExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemday = "Others"+date;


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemday").equalTo(itemday);
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
                    personalref.child("dayOthers").setValue(totalAmount);
                }
                else{
                    llOthers.setVisibility(View.GONE);
                    personalref.child("dayOthers").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalysisActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getTotalDaySpending() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("date").equalTo(date);
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
                    totalbudegetamount.setText("Total Spent Today ₹" + totalAmount);
                    monthspentamount.setText("Spent ₹"+totalAmount);
                }
                else{
                    totalbudegetamount.setText("You have not spent today");
                    pieChart.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalysisActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void loadgraph(){
        personalref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    int totalTrans;
                    if (snapshot.hasChild("dayTrans")) {
                        totalTrans = Integer.parseInt(snapshot.child("dayTrans").getValue().toString());
                    } else {
                        totalTrans = 0;
                    }

                    int totalfood;
                    if (snapshot.hasChild("dayFood")) {
                        totalfood = Integer.parseInt(snapshot.child("dayFood").getValue().toString());
                    } else {
                        totalfood = 0;
                    }

                    int totalhouse;
                    if (snapshot.hasChild("dayHouse")) {
                        totalhouse = Integer.parseInt(snapshot.child("dayHouse").getValue().toString());
                    } else {
                        totalhouse = 0;
                    }

                    int totalent;
                    if (snapshot.hasChild("dayEntertainment")) {
                        totalent = Integer.parseInt(snapshot.child("dayEntertainment").getValue().toString());
                    } else {
                        totalent = 0;
                    }

                    int totaledu;
                    if (snapshot.hasChild("dayEducation")) {
                        totaledu = Integer.parseInt(snapshot.child("dayEducation").getValue().toString());
                    } else {
                        totaledu = 0;
                    }

                    int totalcha;
                    if (snapshot.hasChild("dayCharity")) {
                        totalcha = Integer.parseInt(snapshot.child("dayCharity").getValue().toString());
                    } else {
                        totalcha = 0;
                    }

                    int totalhealth;
                    if (snapshot.hasChild("dayHealth")) {
                        totalhealth = Integer.parseInt(snapshot.child("dayHealth").getValue().toString());
                    } else {
                        totalhealth = 0;
                    }

                    int totalper;
                    if (snapshot.hasChild("dayPersonal")) {
                        totalper = Integer.parseInt(snapshot.child("dayPersonal").getValue().toString());
                    } else {
                        totalper = 0;
                    }

                    int totaloth;
                    if (snapshot.hasChild("dayOthers")) {
                        totaloth = Integer.parseInt(snapshot.child("dayOthers").getValue().toString());
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
                    Toast.makeText(DailyAnalysisActivity.this, "Child Does Not Exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalysisActivity.this, "Child Does Not Exist", Toast.LENGTH_SHORT).show();
            }
        });
    }





}