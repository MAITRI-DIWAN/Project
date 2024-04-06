package com.example.newproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.newproject.databinding.ActivityOutputBinding;

public class OutputActivity extends AppCompatActivity {

    private ActivityOutputBinding binding;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setContentView(R.layout.activity_output);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

//        String responseData = getIntent().getStringExtra("response");
        String output = getIntent().getStringExtra("output");
String error = getIntent().getStringExtra("error");

        // Display the response in a TextView
        TextView responseTextView = findViewById(R.id.response_text_view);
        responseTextView.setText(output);

       TextView errorTextView = findViewById(R.id.error_text_view);
        errorTextView.setText(error);
    }

}