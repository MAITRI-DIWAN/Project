package com.example.newproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchQuestion extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textViewEmpty;

    private List<Question> questionList;
    private QuestionAdapter adapter;

    private int assignment_id; // Assuming you will pass the assignment id from the previous activity

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("question","done");
        super.onCreate(savedInstanceState);
        Log.d("question1","done");
        setContentView(R.layout.activity_fetch_question);
        Log.d("question2","done");

        // Assuming you receive the assignment id from the previous activity
        Intent intent = getIntent();
        Log.d("question3","done");
        if (intent != null && intent.hasExtra("id")) {
            Log.d("question4","done");
            assignment_id = intent.getIntExtra("id", -1);
            Log.d("question5","done");
        }
        Log.d("question6","done");
        recyclerView = findViewById(R.id.recyclerViewQuestions);
        progressBar = findViewById(R.id.progressBar);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        Log.d("question7","done");
        questionList = new ArrayList<>();
        adapter = new QuestionAdapter(this, questionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Log.d("question8","done");
        fetchQuestions();
    }

    private void fetchQuestions() {
        Log.d("question9","done");
        progressBar.setVisibility(View.VISIBLE);
        Log.d("question10","done");
        // Make API call to fetch questions
        ApiService service = RetroFitClient.getRetrofitInstance().create(ApiService.class);
        Log.d("question11","done");
        Call<List<Question>> call = service.getQuestions(assignment_id);
        Log.d("question12","done");
        call.enqueue(new Callback<List<Question>>() {

            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {

                Log.d("question13","done");
                if (response.isSuccessful()) {
                    Log.d("question14","done");
                    questionList.clear();
                    Log.d("question15","done");
                    questionList.addAll(response.body());
                    Log.d("question16","done");
                    adapter.notifyDataSetChanged();
                    Log.d("question17","done");
                    progressBar.setVisibility(View.GONE);
                    Log.d("question18","done");
                    if (questionList.isEmpty()) {
                        Log.d("question19","done");
                        textViewEmpty.setVisibility(View.VISIBLE);
                    } else {
                        Log.d("question20","done");
                        textViewEmpty.setVisibility(View.GONE);
                    }
                } else {
                    Log.d("question21","done");
                    Toast.makeText(FetchQuestion.this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
                    Log.d("question22","done");
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Toast.makeText(FetchQuestion.this, "Network error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
