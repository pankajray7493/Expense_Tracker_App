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
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class WeeklyAnalyticsActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_weekly_analytics);

        getSupportActionBar().setTitle("Weekly Analytics");

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAuth = FirebaseAuth.getInstance();
        expenseref = FirebaseDatabase.getInstance().getReference("expenses").child(userid);
        personalref = FirebaseDatabase.getInstance().getReference("personal").child(userid);

        totalbudegetamount = findViewById(R.id.totalamountspent2);

        //general
        monthspentamount = findViewById(R.id.monthspentamount);

        lltodayspent = findViewById(R.id.relativeanaly);
//        monthratiospent = findViewById(R.id.monthspending);
//        monthspendingimg = findViewById(R.id.monthspendingimg);

        analytransport = findViewById(R.id.analyticsTransportAmount2);
        analyfood = findViewById(R.id.analyticsFoodAmount2);
        analyhouse = findViewById(R.id.analyticsHouseExpensesAmount2);
        analyentertainment = findViewById(R.id.analyticsEntertainmentAmount2);
        analyeducation = findViewById(R.id.analyticsEducationAmount2);
        analycharity = findViewById(R.id.analyticsCharityAmount2);
        analyhealth = findViewById(R.id.analyticsHealthAmount2);
        analypersonal = findViewById(R.id.analyticsPersonalExpensesAmount2);
        analyothers = findViewById(R.id.analyticsOtherAmount2);

        // Relative layout

        llTrans = findViewById(R.id.linearLayoutTransport2);
        llfood = findViewById(R.id.linearLayoutFood2);
        llHouse = findViewById(R.id.linearLayoutFoodHouse2);
        llEnter = findViewById(R.id.linearLayoutEntertainment2);
        llEdu = findViewById(R.id.linearLayoutEducation2);
        llCharity = findViewById(R.id.linearLayoutCharity2);
        llHelth = findViewById(R.id.linearLayoutHealth2);
        llPersonal = findViewById(R.id.linearLayoutPersonalExp2);
        llOthers = findViewById(R.id.linearLayoutOther2);



        //textview
//        progressratioTrans = findViewById(R.id.progress_ratio_transport);
//        PRfood = findViewById(R.id.progress_ratio_food);
//        PRhouse = findViewById(R.id.progress_ratio_house);
//        PRent = findViewById(R.id.progress_ratio_ent);
//        PRedu = findViewById(R.id.progress_ratio_edu);
//        PRcharity = findViewById(R.id.progress_ratio_cha);
//        PRhelth = findViewById(R.id.progress_ratio_hea);
//        PRpersonal = findViewById(R.id.progress_ratio_per);
//        PRothers = findViewById(R.id.progress_ratio_oth);
//
//
//        // imageview
//
//        statusimageTrans = findViewById(R.id.status_Image_transport);
//        SIfood = findViewById(R.id.status_Image_food);
//        SIhouse = findViewById(R.id.status_Image_house);
//        SIent = findViewById(R.id.status_Image_ent);
//        SIedu = findViewById(R.id.status_Image_edu);
//        SIcharity = findViewById(R.id.status_Image_cha);
//        SIhelth = findViewById(R.id.status_Image_hea);
//        SIpersonal = findViewById(R.id.status_Image_per);
//        SIothers = findViewById(R.id.status_Image_oth);



        pieChart =findViewById(R.id.piechart1);


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


        loadgraph();


    }
    private void getTotalDayTransportExpenses() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Weeks weeks = Weeks.weeksBetween(poch,now);

        String itemweek = "Transport"+weeks.getWeeks();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemweek").equalTo(itemweek);
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
                    personalref.child("weekTrans").setValue(totalAmount);
                }
                else{
                    llTrans.setVisibility(View.GONE);
                    personalref.child("weekTrans").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalDayFoodExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Weeks weeks = Weeks.weeksBetween(poch,now);

        String itemweek = "Food"+weeks.getWeeks();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemweek").equalTo(itemweek);
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
                    personalref.child("weekFood").setValue(totalAmount);
                }
                else{
                    llfood.setVisibility(View.GONE);
                    personalref.child("weekFood").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalDayHouseExpenses() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Weeks weeks = Weeks.weeksBetween(poch,now);

        String itemweek = "House"+weeks.getWeeks();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemweek").equalTo(itemweek);
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
                    personalref.child("weekHouse").setValue(totalAmount);
                }
                else{
                    llHouse.setVisibility(View.GONE);
                    personalref.child("weekHouse").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalDayEntertainmentExpenses() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Weeks weeks = Weeks.weeksBetween(poch,now);

        String itemweek = "Entertainment"+weeks.getWeeks();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemweek").equalTo(itemweek);
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
                    personalref.child("weekEntertainment").setValue(totalAmount);
                }
                else{
                    llEnter.setVisibility(View.GONE);
                    personalref.child("weekEntertainment").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalDayEducationExpenses() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Weeks weeks = Weeks.weeksBetween(poch,now);

        String itemweek = "Education"+weeks.getWeeks();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemweek").equalTo(itemweek);
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
                    personalref.child("weekEducation").setValue(totalAmount);
                }
                else{
                    llEdu.setVisibility(View.GONE);
                    personalref.child("weekEducation").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDayCharityExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Weeks weeks = Weeks.weeksBetween(poch,now);

        String itemweek = "Charity"+weeks.getWeeks();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemweek").equalTo(itemweek);
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
                    personalref.child("weekCharity").setValue(totalAmount);
                }
                else{
                    llCharity.setVisibility(View.GONE);
                    personalref.child("weekCharity").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDayHealthExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Weeks weeks = Weeks.weeksBetween(poch,now);

        String itemweek = "Health"+weeks.getWeeks();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemweek").equalTo(itemweek);
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
                    personalref.child("weekHealth").setValue(totalAmount);
                }
                else{
                    llHelth.setVisibility(View.GONE);
                    personalref.child("weekHealth").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDayPersonalExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Weeks weeks = Weeks.weeksBetween(poch,now);

        String itemweek = "Personal"+weeks.getWeeks();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemweek").equalTo(itemweek);
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
                    personalref.child("weekPersonal").setValue(totalAmount);
                }
                else{
                    llPersonal.setVisibility(View.GONE);
                    personalref.child("weekPersonal").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDayOthersExpenses() {
        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Weeks weeks = Weeks.weeksBetween(poch,now);

        String itemweek = "Others"+weeks.getWeeks();


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("itemweek").equalTo(itemweek);
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
                    personalref.child("weekOthers").setValue(totalAmount);
                }
                else{
                    llOthers.setVisibility(View.GONE);
                    personalref.child("weekOthers").setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getTotalDaySpending() {

        MutableDateTime poch =new MutableDateTime();
        poch.setDate(0);
        DateTime now =new DateTime();
        Weeks weeks = Weeks.weeksBetween(poch,now);


        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        DatabaseReference reference= databse.getReference("expenses").child(userid);
        Query query = reference.orderByChild("week").equalTo(weeks.getWeeks());
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
                    totalbudegetamount.setText("Total Spent This Week ₹" + totalAmount);
                    monthspentamount.setText("Spent ₹"+totalAmount);
                }
                else{
                    totalbudegetamount.setText("You have not spent this week");
                    pieChart.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(WeeklyAnalyticsActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void loadgraph(){
        personalref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    int totalTrans;
                    if (snapshot.hasChild("weekTrans")) {
                        totalTrans = Integer.parseInt(snapshot.child("weekTrans").getValue().toString());
                    } else {
                        totalTrans = 0;
                    }

                    int totalfood;
                    if (snapshot.hasChild("weekFood")) {
                        totalfood = Integer.parseInt(snapshot.child("weekFood").getValue().toString());
                    } else {
                        totalfood = 0;
                    }

                    int totalhouse;
                    if (snapshot.hasChild("weekHouse")) {
                        totalhouse = Integer.parseInt(snapshot.child("weekHouse").getValue().toString());
                    } else {
                        totalhouse = 0;
                    }

                    int totalent;
                    if (snapshot.hasChild("weekEntertainment")) {
                        totalent = Integer.parseInt(snapshot.child("weekEntertainment").getValue().toString());
                    } else {
                        totalent = 0;
                    }

                    int totaledu;
                    if (snapshot.hasChild("weekEducation")) {
                        totaledu = Integer.parseInt(snapshot.child("weekEducation").getValue().toString());
                    } else {
                        totaledu = 0;
                    }

                    int totalcha;
                    if (snapshot.hasChild("weekCharity")) {
                        totalcha = Integer.parseInt(snapshot.child("weekCharity").getValue().toString());
                    } else {
                        totalcha = 0;
                    }

                    int totalhealth;
                    if (snapshot.hasChild("weekHealth")) {
                        totalhealth = Integer.parseInt(snapshot.child("weekHealth").getValue().toString());
                    } else {
                        totalhealth = 0;
                    }

                    int totalper;
                    if (snapshot.hasChild("weekPersonal")) {
                        totalper = Integer.parseInt(snapshot.child("weekPersonal").getValue().toString());
                    } else {
                        totalper = 0;
                    }

                    int totaloth;
                    if (snapshot.hasChild("weekOthers")) {
                        totaloth = Integer.parseInt(snapshot.child("weekOthers").getValue().toString());
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
                    Toast.makeText(WeeklyAnalyticsActivity.this, "Child Does Not Exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(WeeklyAnalyticsActivity.this, "Child Does Not Exist", Toast.LENGTH_SHORT).show();
            }
        });
    }
}