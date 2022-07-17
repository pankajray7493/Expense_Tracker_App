package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SingupActivity extends AppCompatActivity {

    private EditText email,password,user;
    private Button singup;
    private TextView login;

    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        email = findViewById(R.id.email);
        password = findViewById(R.id.pass1);
        singup = findViewById(R.id.btnsingup);
        login = findViewById(R.id.singin);
        user = findViewById(R.id.user);
        database=FirebaseDatabase.getInstance();

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        progressDialog =new ProgressDialog(this);


        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st = email.getText().toString();
                String pass = password.getText().toString();
                String name = user.getText().toString();

                if (TextUtils.isEmpty(name)){
                    user.setError("Name is Required");
                }

                if (TextUtils.isEmpty(st)){
                    email.setError("Email is Required");
                }
                if (TextUtils.isEmpty(pass)){
                    password.setError("Password is Required");
                }
                else {
                    progressDialog.setMessage("Login in progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    auth.createUserWithEmailAndPassword(st, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Data data = new Data(user.getText().toString(), email.getText().toString(),
                                        password.getText().toString());

                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("User").child(id).setValue(data);

                                Intent intent = new Intent(SingupActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(SingupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



    }
    public void onBackPressed() {
        new AlertDialog.Builder(SingupActivity.this)
                .setIcon(R.drawable.alert)
                .setTitle("Exit")
                .setMessage("Do you wants to exit this app")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finishAffinity();
                        System.exit(0);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}