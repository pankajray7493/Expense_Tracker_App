package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();  //tool baar hide;

        Thread thread=new Thread(){
            public void run(){
                try{
                    sleep(5000);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent =new Intent(SplashActivity.this,SingupActivity.class);
                    startActivity(intent);
                }
            }
        };thread.start();


    }
    public void onBackPressed() {
        new AlertDialog.Builder(SplashActivity.this)
                .setIcon(R.drawable.alert)
                .setTitle("Exit")
                .setMessage("Do you wants to exit this app")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}