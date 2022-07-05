package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AnalyticActivity extends AppCompatActivity {

    private CardView todayanalysis,weekanalysis,monthanalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytic);

        todayanalysis = findViewById(R.id.todaycard);
        weekanalysis = findViewById(R.id.todaycard1);
        monthanalysis = findViewById(R.id.todaycard3);

        todayanalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalyticActivity.this,DailyAnalysisActivity.class);
                startActivity(intent);
            }
        });
        weekanalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalyticActivity.this,WeeklyAnalyticsActivity.class);
                startActivity(intent);
            }
        });
        monthanalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalyticActivity.this,MonthlyAnalyticsActivity.class);
                startActivity(intent);
            }
        });

    }
}