package com.example.newproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class TextEditor extends AppCompatActivity {

    private EditText codeEditor;
    private TextView lineNumbers;

    String url="http://192.168.67.19/student_assignment_portal/student_assignment_portal/android/compile.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);


        codeEditor = findViewById(R.id.code_editor);
        lineNumbers = findViewById(R.id.line_numbers);
//        input = findViewById(R.id.input);


        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);


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


    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dynamic Input");
        builder.setMessage("Do you need to provide dynamic input?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If user needs dynamic input, show input dialog
                showInputTextDialog();
            }
        });
        builder.setNegativeButton("Run", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If user doesn't need dynamic input, execute the code without input
                executeCode(null);
            }
        });
        builder.show();
    }

    private void showInputTextDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Input");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Run", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If user provides input, execute the code with input
                String userInput = input.getText().toString();
                executeCode(userInput);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    public void executeCode(String inputText){
        String code= codeEditor.getText().toString();
//        String inputText= input.getText().toString();

        JSONObject jsonBody = new JSONObject();

        try {
            if (inputText != null && !inputText.isEmpty()) {
                jsonBody.put("input", inputText);
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

                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                String output = response.getString("output");
                                Log.v("output", output);
                                Intent intent = new Intent(TextEditor.this, OutputActivity.class);
                                intent.putExtra("output",output);
                                startActivity(intent);

                                // Now you can use the 'output' variable as needed
                            } else {
                                // Handle case where success is false
                                Log.v("response", "Request failed");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        Log.v("response",response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.v("error",error.toString());
                        Intent intent = new Intent(TextEditor.this, OutputActivity.class);
                        intent.putExtra("error", error.toString());
                        startActivity(intent);
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            saveTextToFile();
            return true;
        }
        if(item.getItemId() == R.id.action_run){
            showInputDialog();
return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTextToFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {
                    Uri uri = data.getData();
                    writeToFile(uri);
                }
            }
        }
    }


    private void writeToFile(Uri uri) {
        try {
            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            if (outputStream != null) {
                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                writer.write(codeEditor.getText().toString());
                writer.close();
                Toast.makeText(this, "Text saved to file", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving text", Toast.LENGTH_SHORT).show();
        }
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
