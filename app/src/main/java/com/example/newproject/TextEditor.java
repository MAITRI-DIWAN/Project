package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class TextEditor extends AppCompatActivity {

    private EditText codeEditor,input;
    private TextView lineNumbers;

    String url="http://192.168.83.71/student_assignment_portal/student_assignment_portal/android/compile.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);

        codeEditor = findViewById(R.id.code_editor);
        lineNumbers = findViewById(R.id.line_numbers);
        input = findViewById(R.id.input);

        Button runBtn = findViewById(R.id.run_button);
        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= codeEditor.getText().toString();
                String inputText= input.getText().toString();

                JSONObject jsonBody = new JSONObject();
                try {
                    if(inputText.length()>0){
                        jsonBody.put("input",inputText);
                    }
                    jsonBody.put("c_code", code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("code",jsonBody.toString());
                RequestQueue requestQueue = Volley.newRequestQueue(TextEditor.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle response from the backend
                                Log.v("response",response.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                                Log.v("error",error.toString());
                            }
                        }
                );

                requestQueue.add(jsonObjectRequest);
//                startActivity(new Intent(TextEditor.this, SecondActivity.class));
            }
        });


        // Set text change listener for code editor
        codeEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateLineNumbers();
            }
        });

        // Set initial line numbers
        updateLineNumbers();
    }

    // Method to highlight the current line
    private void highlightCurrentLine() {
        int selectionStart = codeEditor.getSelectionStart();
        int selectionEnd = codeEditor.getSelectionEnd();

        // Highlight the current line
        int lineNumber = codeEditor.getLayout().getLineForOffset(selectionStart);
        int start = codeEditor.getLayout().getLineStart(lineNumber);
        int end = codeEditor.getLayout().getLineEnd(lineNumber);
        codeEditor.setSelection(start, end);
    }

    // Method to update line numbers
    private void updateLineNumbers() {
        String lines = "";
        int lineCount = codeEditor.getLineCount();
        for (int i = 1; i <= lineCount; i++) {
            lines += i + "\n";
        }
        lineNumbers.setText(lines);
    }

    // Your existing methods for saving text and running code
    // ...

    // Method to handle inserting symbols
    // Method to handle inserting symbols
    public void insertSymbol(View view) {
        // Implement logic to insert symbols at the cursor position
        Button symbolButton = (Button) view;
        String symbol = symbolButton.getText().toString();
        int cursorPosition = codeEditor.getSelectionStart();
        codeEditor.getText().insert(cursorPosition, symbol);
    }

    // Method to handle saving text
    public void saveText(View view) {
        // Implement logic to save the code
        String code = codeEditor.getText().toString();

        // You can save the code to a file or perform other operations here
    }
}
