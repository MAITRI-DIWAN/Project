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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FetchQuestion extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textViewEmpty;

    private List<Question> questionList;
    private QuestionAdapter adapter;

    private int assignment_id;

    private int user_id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fetch_question);

        // Assuming you receive the assignment id from the previous activity
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("assignment_id") && intent.hasExtra("user_id")) {

            assignment_id = intent.getIntExtra("assignment_id", -1);
user_id = intent.getIntExtra("user_id",-1);
        }

        recyclerView = findViewById(R.id.recyclerViewQuestions);
        progressBar = findViewById(R.id.progressBar);
        textViewEmpty = findViewById(R.id.textViewEmpty);

        questionList = new ArrayList<>();
        adapter = new QuestionAdapter(this, questionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchQuestions();
    }

//    private void fetchQuestions() {
//        Log.d("question9","done");
//        progressBar.setVisibility(View.VISIBLE);
//        Log.d("question10","done");
//        // Make API call to fetch questions
//        ApiService service = RetroFitClient.getRetrofitInstance().create(ApiService.class);
//        Log.d("question11","done");
//        Call<List<Question>> call = service.getQuestions(assignment_id);
//        Log.d("question12","done");
//        call.enqueue(new Callback<List<Question>>() {
//
//            @Override
//            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
//
//                Log.d("question13","done");
//                if (response.isSuccessful()) {
//                    Log.d("question14","done");
//                    questionList.clear();
//                    Log.d("question15","done");
//                    questionList.addAll(response.body());
//                    Log.d("question16","done");
//                    adapter.notifyDataSetChanged();
//                    Log.d("question17","done");
//                    progressBar.setVisibility(View.GONE);
//                    Log.d("question18","done");
//                    if (questionList.isEmpty()) {
//                        Log.d("question19","done");
//                        textViewEmpty.setVisibility(View.VISIBLE);
//                    } else {
//                        Log.d("question20","done");
//                        textViewEmpty.setVisibility(View.GONE);
//                    }
//                } else {
//                    Log.d("question21","done");
//                    Toast.makeText(FetchQuestion.this, "Failed to fetch questions", Toast.LENGTH_SHORT).show();
//                    Log.d("question22","done");
//                    progressBar.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Question>> call, Throwable t) {
//                Toast.makeText(FetchQuestion.this, "Network error", Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//    }

    private void fetchQuestions() {

        progressBar.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(FetchQuestion.this);

        String url = "http://192.168.67.19/student_assignment_portal/student_assignment_portal/android/question.php?assignment_id=" + assignment_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            questionList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Question question = new Question(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("question"),
                                        jsonObject.getString("answer"),
                                        jsonObject.getInt("is_active")
                                );
                                questionList.add(question);
                            }

                            adapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);

                            if (questionList.isEmpty()) {

                                textViewEmpty.setVisibility(View.VISIBLE);
                            } else {
                                textViewEmpty.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(FetchQuestion.this, "Failed to parse JSON response", Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FetchQuestion.this, "Network error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

        // Add the request to the RequestQueue
//        Volley.getInstance(this).addToRequestQueue(stringRequest);
        requestQueue.add(stringRequest);
    }

}
