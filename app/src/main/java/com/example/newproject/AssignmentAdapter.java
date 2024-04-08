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
                    Log.d("ad", "done");
                    int assignmentId = assignment.getId();
                    Log.d("ad1", "done");
                    // Save assignment ID
                    saveAssignmentId(assignmentId);
                    Log.d("ad2", "done");
                    // Start the FetchQuestion activity
                    Intent intent = new Intent(context, FetchQuestion.class);
                    Log.d("ad3", "done");
                    intent.putExtra("id", assignmentId);
                    Log.d("ad4", "done");
                    context.startActivity(intent);
                    Log.d("ad5", "done");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Adapter onClick", "Error: " + e.getMessage());
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
        Log.d("kruti","done");

        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Log.d("kruti1","done");
        SharedPreferences.Editor editor = preferences.edit();
        Log.d("kruti2","done");
        editor.putInt("assignment_id", assignmentId);
        Log.d("kruti3","done");
        editor.apply();
        Log.d("kruti4","done");
    }

}
