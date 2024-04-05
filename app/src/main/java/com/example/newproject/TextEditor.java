package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TextEditor extends AppCompatActivity {

    private EditText codeEditor;
    private TextView lineNumbers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);

        codeEditor = findViewById(R.id.code_editor);
        lineNumbers = findViewById(R.id.line_numbers);

        Button button1 = findViewById(R.id.run_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TextEditor.this, SecondActivity.class));
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
