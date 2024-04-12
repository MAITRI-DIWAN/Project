package com.example.newproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private Context context;
    private List<Question> questionList;

    public QuestionAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.textViewTitle.setText("Question");
        holder.textViewQuestion.setText(question.getQuestionText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click, navigate to text editor activity or fragment
                try {

                    int questionId = question.getId();

                    SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    int userId = preferences.getInt("user_id", -1);
                    int assignmentId = preferences.getInt("assignment_id", -1);

                    // Save assignment ID
                    saveQuestionId(questionId);
                    saveAssignmentId(assignmentId);
                    saveUserId(userId);

                    Intent intent = new Intent(context, TextEditor.class);
                    intent.putExtra("user_id", userId);
                    intent.putExtra("assignment_id", assignmentId);
                    intent.putExtra("question_id", questionId);

                    context.startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Adapter onClick", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion;

        TextView textViewTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }
    }

    private void saveQuestionId(int questionId) {

        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("question_id", questionId);

        editor.apply();

    }

    private void saveAssignmentId(int assignmentId) {

        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("assignment_id", assignmentId);

        editor.apply();

    }

    private void saveUserId(int userId) {
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("user_id", userId);
        editor.apply();
    }
}
