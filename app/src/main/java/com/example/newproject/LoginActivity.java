package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    String url = "http://192.168.83.71/student_assignment_portal/student_assignment_portal/android/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.btn_login);

        // Set click listener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get username and password input
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();



                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

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
                                        // Login successful, move to the next activity
                                        Intent intent = new Intent(LoginActivity.this, TextEditor.class);
                                        startActivity(intent);
                                    } else {
                                        // Handle the case where success is false
                                        // For example, display an error message
                                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    // Handle JSON parsing exception
                                }
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

                        return params;
                    }
                };

                // Add the request to the RequestQueue.
                requestQueue.add(stringRequest);
            }
        });
    }

    // Custom validation methods (replace with your own validation logic)
    private boolean isValidUsername(String username) {
        // Add your username validation logic here
        return username.length() > 0;
    }

    private boolean isValidPassword(String password) {
        // Add your password validation logic here
        return password.length() > 0;
    }

    // Method to move to registration activity
    public void moveToRegistration(View view) {
        startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
        finish();
    }
}
