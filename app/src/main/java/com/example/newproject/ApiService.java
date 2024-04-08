package com.example.newproject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {
    @GET("assignment.php")
    Call<List<Assignment>> getAssignments(@Query("student_id") int student_id);

    @GET("question.php")
    Call<List<Question>> getQuestions(@Query("assignment_id") int assignment_id);
}

//public interface ApiService {
//    @GET("assignment.php")
//    Call<List<Assignment>> getAssignments(@Query("student_id") int studentId);
//}
