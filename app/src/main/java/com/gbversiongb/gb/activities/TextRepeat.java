package com.gbversiongb.gb.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gbversiongb.gb.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class TextRepeat extends AppCompatActivity {

    SwitchMaterial newLineSwitch;
    CardView copyBtn, deleteBtn;
    ImageView backBtn;
    EditText text, num;
    Button repeat;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_repeat);

        text = findViewById(R.id.etText);
        num = findViewById(R.id.etTimes);
        newLineSwitch = findViewById(R.id.newLineSwitch);
        copyBtn = findViewById(R.id.copyBtn);
        deleteBtn = findViewById(R.id.deleteText);
        repeat = findViewById(R.id.btnRep);
        result = findViewById(R.id.resultTV);
        backBtn = findViewById(R.id.imBack);
        backBtn.setOnClickListener(view -> onBackPressed());

        repeat.setOnClickListener(v -> {
            if (newLineSwitch.isChecked()){
                String s = "";
                int t = Integer.parseInt(num.getText().toString());
                for (int i = 0; i < t; i++) {
                    s = s + text.getText().toString() + "\n";
                }
                result.setText(s);
            } else {
                String s = "";
                int t = Integer.parseInt(num.getText().toString());
                for (int i = 0; i < t; i++) {
                    s = s + text.getText().toString() + " ";
                }
                result.setText(s);
            }
        });

        copyBtn.setOnClickListener(v -> {
            int sdk = android.os.Build.VERSION.SDK_INT;
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            if (result.getText().toString().isEmpty()){
                Toast.makeText(this, "Nothing To Copy", Toast.LENGTH_SHORT).show();
            } else {
                if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    clipboard.setText(result.getText().toString());
                    Toast.makeText(this, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
                } else {
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Blank Message", result.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(this, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteBtn.setOnClickListener(v -> {
            result.setText("");
        });

    }
}