package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText, fullNameEditText, emailEditText, passwordEditText, semesterEditText, academicYearEditText;
    private Spinner departmentSpinner;
    private Button registerButton;
    String url = "http://192.168.27.53/project/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        fullNameEditText = findViewById(R.id.fullname);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        semesterEditText = findViewById(R.id.semester);
        academicYearEditText = findViewById(R.id.academic_year);
        departmentSpinner = findViewById(R.id.department);
        registerButton = findViewById(R.id.btn_register);

        // Setup department spinner with options
        String[] departments = {"IT", "EC", "Electrical"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, departments);
        departmentSpinner.setAdapter(adapter);

        // Set click listener for register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs
                String username = usernameEditText.getText().toString().trim();
                String fullName = fullNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String semester = semesterEditText.getText().toString().trim();
                String academicYear = academicYearEditText.getText().toString().trim();
                String department = departmentSpinner.getSelectedItem().toString();

                // Validate inputs
                if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty() || semester.isEmpty() || academicYear.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Instantiate the RequestQueue
                RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);

                // Define the POST request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Response from the server
                                Log.d("Response", response);
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    Log.d("Response", "Success: " + success);

                                    if (success) {
                                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // Handle the case where success is false
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    // Handle JSON parsing exception
                                }
                                // Handle the response here
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        Log.e("Error", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Post parameters to your PHP script
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", password);
                        params.put("semester", semester);
                        params.put("email", email);
                        params.put("academic_year", academicYear);
                        params.put("fullname", fullName);
                        params.put("department", department);
                        return params;
                    }
                };

                // Add the request to the RequestQueue.
                requestQueue.add(stringRequest);
            }
        });
    }

    // Move to login activity
    public void moveToLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

}
