package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anychart.AnyChartView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WeeklyAnalyticsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String onlineuser = "";
    private DatabaseReference expenseref,personalref;

    private TextView totalbudegetamount,analytransport,analyfood,analyhouse,analyentertainment,analyeducation,analycharity;
    private TextView analyhealth,analypersonal,analyothers,monthspentamount;

    private RelativeLayout linearlayoutall,llTrans,llHouse,llEnter,llfood,llEdu,llCharity,llHelth,llPersonal,llOthers,lltodayspent;

    private AnyChartView anyChartView;
    private TextView progressratioTrans,PRfood,PRhouse,PRent,PRedu,PRcharity,PRhelth,PRpersonal,PRothers,PRtotal,monthratiospent;
    private ImageView statusimageTrans,SIfood,SIhouse,SIent,SIedu,SIcharity,SIhelth,SIpersonal,SIothers,SItotal,monthspendingimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_analytics);

        getSupportActionBar().setTitle("Weekly Analytics");

        mAuth = FirebaseAuth.getInstance();
        expenseref = FirebaseDatabase.getInstance().getReference("expenses").child(onlineuser);
        personalref = FirebaseDatabase.getInstance().getReference("personal").child(onlineuser);

        totalbudegetamount = findViewById(R.id.totalamountspent1);

        //general
        monthspentamount = findViewById(R.id.monthspentamount1);

        lltodayspent = findViewById(R.id.relativeanaly);
        monthratiospent = findViewById(R.id.monthspending);
        monthspendingimg = findViewById(R.id.monthspendingimg);

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
        progressratioTrans = findViewById(R.id.progress_ratio_transport);
        PRfood = findViewById(R.id.progress_ratio_food);
        PRhouse = findViewById(R.id.progress_ratio_house);
        PRent = findViewById(R.id.progress_ratio_ent);
        PRedu = findViewById(R.id.progress_ratio_edu);
        PRcharity = findViewById(R.id.progress_ratio_cha);
        PRhelth = findViewById(R.id.progress_ratio_hea);
        PRpersonal = findViewById(R.id.progress_ratio_per);
        PRothers = findViewById(R.id.progress_ratio_oth);


        // imageview

        statusimageTrans = findViewById(R.id.status_Image_transport);
        SIfood = findViewById(R.id.status_Image_food);
        SIhouse = findViewById(R.id.status_Image_house);
        SIent = findViewById(R.id.status_Image_ent);
        SIedu = findViewById(R.id.status_Image_edu);
        SIcharity = findViewById(R.id.status_Image_cha);
        SIhelth = findViewById(R.id.status_Image_hea);
        SIpersonal = findViewById(R.id.status_Image_per);
        SIothers = findViewById(R.id.status_Image_oth);

        anyChartView = findViewById(R.id.anychart);


    }
}