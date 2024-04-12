package com.example.newproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {

    private Context context;
    private List<Assignment> assignmentList;

    public AssignmentAdapter(Context context, List<Assignment> assignmentList) {
        this.context = context;
        this.assignmentList = assignmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.assignment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Assignment assignment = assignmentList.get(position);
        holder.textViewAssignmentName.setText(assignment.getName());
        holder.textViewDeadline.setText("Deadline: " + assignment.getDeadline());
        holder.textViewTeacher.setText("Teacher: " + assignment.getTeacherUsername());

        // Set click listener if needed
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click
                try {

                    int assignmentId = assignment.getId();

                    SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    int userId = preferences.getInt("user_id", -1);
                    // -1 is the default value if user_id is not found

                    // Save assignment ID and user ID
                    saveAssignmentId(assignmentId);
                    saveUserId(userId);


                    // Start the FetchQuestion activity
                    Intent intent = new Intent(context, FetchQuestion.class);
                    intent.putExtra("user_id", userId);
                    intent.putExtra("assignment_id", assignmentId);

                    context.startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAssignmentName;
        TextView textViewDeadline;
        TextView textViewTeacher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAssignmentName = itemView.findViewById(R.id.textViewAssignmentName);
            textViewDeadline = itemView.findViewById(R.id.textViewDeadline);
            textViewTeacher = itemView.findViewById(R.id.textViewTeacher);
        }
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
