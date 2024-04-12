package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.android.volley.Response;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
//import retrofit2.Response;

public class FetchAssignment extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private int student_id;

    private TextView textViewEmpty;
    private List<Assignment> assignmentList;
    private AssignmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fetch_assignment);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("user_id")) {

            student_id = intent.getIntExtra("user_id", -1); // -1 is the default value if user_id is not found
        }


        recyclerView = findViewById(R.id.recyclerViewAssignments);
        progressBar = findViewById(R.id.progressBar);
        textViewEmpty = findViewById(R.id.textViewEmpty);

        assignmentList = new ArrayList<>();
        adapter = new AssignmentAdapter(this, assignmentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);





        fetchAssignments();
    }

//    private void fetchAssignments() {
//
//        // Show progress bar
//        progressBar.setVisibility(View.VISIBLE);
//
//        // Make API call to fetch assignments
//        ApiService service = RetroFitClient.getRetrofitInstance().create(ApiService.class);
//
//        Call<List<Assignment>> call = service.getAssignments(student_id);
//        // Pass studentId if needed
//        call.enqueue(new Callback<List<Assignment>>() {
//            @Override
//            public void onResponse(Call<List<Assignment>> call, Response<List<Assignment>> response) {
//
//                Log.d("done",response.toString());
//
//                if (response.isSuccessful()) {
//                    Log.d("done1",response.toString());
//                    assignmentList.clear();
//                    assignmentList.addAll(response.body());
//                    adapter.notifyDataSetChanged();
//                    // Hide progress bar
//                    progressBar.setVisibility(View.GONE);
//                    // Show empty message if no assignments
//                    if (assignmentList.isEmpty()) {
//
//                        textViewEmpty.setVisibility(View.VISIBLE);
//                    } else {
//
//                        textViewEmpty.setVisibility(View.GONE);
//                    }
//                } else {
//                    // Handle unsuccessful response
//
//                    Toast.makeText(FetchAssignment.this, "Failed to fetch assignments", Toast.LENGTH_SHORT).show();
//                    // Hide progress bar
//                    progressBar.setVisibility(View.GONE);
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Assignment>> call, Throwable t) {
//
//                // Handle network errors
//                Toast.makeText(FetchAssignment.this, "Network error", Toast.LENGTH_SHORT).show();
//                // Hide progress bar
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//    }
//


    private void fetchAssignments() {

        progressBar.setVisibility(View.VISIBLE);

        String url = "http://192.168.67.19/student_assignment_portal/student_assignment_portal/android/assignment.php?student_id=" + student_id;

        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(FetchAssignment.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("this11","this");

                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            assignmentList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Assignment assignment = new Assignment(
                                        jsonObject.getInt("id"),
                                        jsonObject.getInt("teacher_id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getInt("batch_id"),
                                        jsonObject.getString("deadline"),
                                        jsonObject.getInt("is_active"),
                                        jsonObject.getString("username")
                                );

                                assignmentList.add(assignment);
                            }

                            adapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);

                            if (assignmentList.isEmpty()) {

                                textViewEmpty.setVisibility(View.VISIBLE);
                            } else {
                                textViewEmpty.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(FetchAssignment.this, "Failed to parse JSON response", Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FetchAssignment.this, "Network error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

        // Add the request to the RequestQueue
//        Volley.getInstance(this).addToRequestQueue(stringRequest);
        requestQueue.add(stringRequest);
    }


}
