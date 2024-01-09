package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button buzz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.nameLabel);
        buzz = findViewById(R.id.buzz);

        buzz.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String userInput = editText.getText().toString();

                Toast.makeText(getApplicationContext(), "You entered: " + userInput, Toast.LENGTH_SHORT).show();
                editText.setText("");
            }
        });
    }
}