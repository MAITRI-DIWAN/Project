package com.example.newproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.newproject.databinding.ActivityFetchAssignmentBinding;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if (intent != null && intent.hasExtra("id")) {
            student_id = intent.getIntExtra("id", -1); // -1 is the default value if user_id is not found
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

    private void fetchAssignments() {

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Make API call to fetch assignments
        ApiService service = RetroFitClient.getRetrofitInstance().create(ApiService.class);

        Call<List<Assignment>> call = service.getAssignments(student_id);
        // Pass studentId if needed
        call.enqueue(new Callback<List<Assignment>>() {
            @Override
            public void onResponse(Call<List<Assignment>> call, Response<List<Assignment>> response) {

                Log.d("done",response.toString());

                if (response.isSuccessful()) {
                    Log.d("done1",response.toString());
                    assignmentList.clear();
                    assignmentList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);
                    // Show empty message if no assignments
                    if (assignmentList.isEmpty()) {

                        textViewEmpty.setVisibility(View.VISIBLE);
                    } else {

                        textViewEmpty.setVisibility(View.GONE);
                    }
                } else {
                    // Handle unsuccessful response

                    Toast.makeText(FetchAssignment.this, "Failed to fetch assignments", Toast.LENGTH_SHORT).show();
                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<List<Assignment>> call, Throwable t) {

                // Handle network errors
                Toast.makeText(FetchAssignment.this, "Network error", Toast.LENGTH_SHORT).show();
                // Hide progress bar
                progressBar.setVisibility(View.GONE);
            }
        });
    }



}
