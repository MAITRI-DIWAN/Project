package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    String url="http://localhost/login.php";
         // String url=  "C:\xamppNew\htdocs\project\login.php"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);

        startActivity(new Intent(this, RegistrationActivity.class));

    }
}