package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextEditor extends AppCompatActivity {

    private EditText codeEditor;
    private TextView lineNumbers;

    private int user_id;

    private int assignment_id;

    private int question_id;




    String url="http://192.168.43.37/student_assignment_portal/student_assignment_portal/android/compile.php";
    String url1 = "http://192.168.43.37/student_assignment_portal/student_assignment_portal/android/dataentry.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);

        codeEditor = findViewById(R.id.code_editor);
        lineNumbers = findViewById(R.id.line_numbers);

        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user_id") && intent.hasExtra("assignment_id") && intent.hasExtra("question_id")) {

            user_id = intent.getIntExtra("user_id", -1);

            assignment_id = intent.getIntExtra("assignment_id", -1);

            question_id = intent.getIntExtra("question_id", -1);

            // Now you have all three IDs, you can use them as needed
        }


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

        String code = codeEditor.getText().toString();

        if (isInputRequiredForCode(code)) {
            Log.d("hello1","yes");
            if(code.contains("printf(") && !code.contains("scanf(") && !code.contains("fgets(") &&
                    !code.contains("gets(") &&
                    !code.contains("getchar(") &&
                    !code.contains("getch(") &&
                    !code.contains("getc(") &&
                    !code.contains("getche(") &&
                    !code.contains("getchar()") &&
                    !code.contains("getch()") &&
                    !code.contains("getc()") &&
                    !code.contains("getche()") &&
                    !code.contains("stdin")){
                Log.d("hello13","yes");
              executeCode(code);
            }
            else if(code.contains("printf(")){
                if (printfContainsFormatSpecifiers(code)) {
                    Log.d("hello13", "Printf lines contain format specifiers");
                    executeCode(code);
                }
            }
            else {
                Log.d("hello2","yes");
                showInputTextDialog(code);
            }
        } else {
            Log.d("hello","yes");
            // If input is not required, execute the code without input
            executeCode(code);
        }
    }

    private boolean printfContainsFormatSpecifiers(String code) {
        // Split the code into lines
        String[] lines = code.split("\\r?\\n");
        // Iterate through the lines and check if any printf statement contains format specifiers
        for (String line : lines) {
            if (line.contains("printf(")) {
                if (line.matches(".*%[df].*")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInputRequiredForCode(String code) {
        // Check if the code contains any of the keywords/patterns indicating input requirement
        return code.contains("scanf(") ||
                code.contains("printf(") ||
                code.contains("fgets(") ||
                code.contains("gets(") ||
                code.contains("getchar(") ||
                code.contains("getch(") ||
                code.contains("getc(") ||
                code.contains("getche(") ||
                code.contains("getchar()") ||
                code.contains("getch()") ||
                code.contains("getc()") ||
                code.contains("getche()") ||
                code.contains("stdin");
    }


    private void showInputTextDialog(String code) {
        // Split the code into lines
        String[] lines = code.split("\\r?\\n");

        List<String> promptMessagesList = new ArrayList<>();
        List<String> userInputList = new ArrayList<>();

        // Boolean flag to track if scanf statement has been encountered
        boolean scanfEncountered = false;

        // Iterate through the lines of code
        for (String line : lines) {
            // Check if the line contains a printf statement
            if (line.contains("printf(")) {
                // Extract the format string from the printf statement
                String formatString = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
                // Convert the format string to a prompt message

                    String message = formatString.replaceAll("%[a-zA-Z]", "").trim();
                    // Check if the prompt message is not empty
                    if (!message.isEmpty()) {
                        promptMessagesList.add(message);
                    }

            }
            // Check if the line contains a scanf statement
            else if (line.contains("scanf(")) {
                // If a previous scanf statement has been encountered, stop scanning
                if (scanfEncountered) {
                    break;
                }
                scanfEncountered = true;
                // Extract the format string from the scanf statement
                String formatString = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
                // Convert the format string to a prompt message
                String message = formatString.replaceAll("%[a-zA-Z]", "").trim();
                // Check if the prompt message is not empty
                if (!message.isEmpty()) {
                    promptMessagesList.add(message);
                }
            }
        }

        // Show input messages dialog with the list of prompt messages and user inputs
        showInputMessages(promptMessagesList, userInputList);
    }



    private void showInputMessages(final List<String> promptMessagesList, final List<String> userInputList) {
        // If there are no prompt messages left, execute the code with accumulated user inputs
        if (promptMessagesList.isEmpty()) {

            String userInput = TextUtils.join(" ", userInputList);

            executeCode(userInput);
            return;
        }

        // Show AlertDialog with the first prompt message
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Input");
        final EditText input = new EditText(this);
        input.setHint(promptMessagesList.get(0)); // Set the first prompt message as hint
        builder.setView(input);
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If user provides input, add it to the userInputList and remove the first prompt message
                String userInput = input.getText().toString();
                userInputList.add(userInput);
                promptMessagesList.remove(0);
                // Show the next prompt message recursively
                showInputMessages(promptMessagesList, userInputList);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }



    public void executeCode(String inputText) {
Log.d("mes1",inputText);

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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_save) {
//            saveTextToFile();
//            return true;
//        }
//        if(item.getItemId() == R.id.action_run){
//            showInputDialog();
//return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void saveText(View view) {
//        saveTextToFile();
        saveDataToDatabase();
    }

    public void runCode(View view) {
        showInputDialog();

    }

//    private void saveTextToFile() {
//        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("text/plain");
//        startActivityForResult(intent, 1);
//    }

    public void saveDataToDatabase() {
        // Convert answer given to byte array
        byte[] answerGiven = codeEditor.getText().toString().getBytes();

        // Create JSON object with user_id, assignment_id, question_id, and answer_given
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_id", user_id);
            jsonBody.put("assignment_id", assignment_id);
            jsonBody.put("question_id", question_id);

            // Convert answer given byte array to Base64 string before sending
            jsonBody.put("answer_given", Base64.encodeToString(answerGiven, Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send HTTP POST request to your PHP server
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url1,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("here", String.valueOf(response));
                        // Handle response from the server
                        Toast.makeText(TextEditor.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(TextEditor.this, "Error saving data", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 1) {
//                if (data != null) {
//                    Uri uri = data.getData();
//                    writeToFile(uri);
//                }
//            }
//        }
//    }
//
//
//    private void writeToFile(Uri uri) {
//        try {
//            OutputStream outputStream = getContentResolver().openOutputStream(uri);
//            if (outputStream != null) {
//                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
//                writer.write(codeEditor.getText().toString());
//                writer.close();
//                Toast.makeText(this, "Text saved to file", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error saving text", Toast.LENGTH_SHORT).show();
//        }
//    }
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


    // Method to handle inserting symbols
    public void insertSymbol(View view) {
        // Implement logic to insert symbols at the cursor position
        Button symbolButton = (Button) view;
        String symbol = symbolButton.getText().toString();
        int cursorPosition = codeEditor.getSelectionStart();
        codeEditor.getText().insert(cursorPosition, symbol);
    }

}
