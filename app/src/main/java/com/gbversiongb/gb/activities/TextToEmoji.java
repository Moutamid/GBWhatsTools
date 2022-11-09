package com.gbversiongb.gb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gbversiongb.gb.R;

import java.io.IOException;
import java.io.InputStream;

public class TextToEmoji extends AppCompatActivity {
    Button clearTxtBtn;
    Button convertButton;
    EditText convertedText;
    Button copyBtn;
    EditText emojeeText;
    EditText inputText;
    Button shareButton;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_emoji);
        this.inputText = findViewById(R.id.inputText);
        this.emojeeText = findViewById(R.id.emojeeTxt);
        this.convertedText = findViewById(R.id.convertedEmojeeTxt);
        this.convertButton = findViewById(R.id.convertEmojeeBtn);
        this.copyBtn = findViewById(R.id.copyTxtBtn);
        this.shareButton = findViewById(R.id.shareTxtBtn);
        this.clearTxtBtn = findViewById(R.id.clearTxtBtn);
        this.convertButton.setOnClickListener(new btnConvertListner());
        this.clearTxtBtn.setOnClickListener(new btnClearTextListner());
        this.convertedText.setOnClickListener(new btnConvertedTextListner());
        this.copyBtn.setOnClickListener(new btnCopyButtonListner());
        this.shareButton.setOnClickListener(new btnShareListner());

        backBtn = findViewById(R.id.imBack);
        backBtn.setOnClickListener(view -> onBackPressed());
    }

    //button convert click event listener
    private class btnConvertListner implements View.OnClickListener {
        public void onClick(View view) {
            if (TextToEmoji.this.inputText.getText().toString().isEmpty()) {
                Toast.makeText(TextToEmoji.this.getApplicationContext(), "Enter text", Toast.LENGTH_SHORT).show();
                return;
            }
            char[] charArray = TextToEmoji.this.inputText.getText().toString().toCharArray();
            TextToEmoji.this.convertedText.setText(".\n");
            for (char character : charArray) {
                byte[] localObject3;
                InputStream localObject2;
                if (character == '?') {
                    try {
                        InputStream localObject1 = TextToEmoji.this.getBaseContext().getAssets().open("ques.txt");
                        localObject3 = new byte[localObject1.available()];
                        localObject1.read(localObject3);
                        localObject1.close();
                        TextToEmoji.this.convertedText.append(new String(localObject3).replaceAll("[*]", TextToEmoji.this.emojeeText.getText().toString()) + "\n\n");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } else if (character == ((char) (character & 95)) || Character.isDigit(character)) {
                    try {
                        localObject2 = TextToEmoji.this.getBaseContext().getAssets().open(character + ".txt");
                        localObject3 = new byte[localObject2.available()];
                        localObject2.read(localObject3);
                        localObject2.close();
                        TextToEmoji.this.convertedText.append(new String(localObject3).replaceAll("[*]", TextToEmoji.this.emojeeText.getText().toString()) + "\n\n");
                    } catch (IOException ioe2) {
                        ioe2.printStackTrace();
                    }
                } else {
                    try {
                        localObject2 = TextToEmoji.this.getBaseContext().getAssets().open("low" + character + ".txt");
                        localObject3 = new byte[localObject2.available()];
                        localObject2.read(localObject3);
                        localObject2.close();
                        TextToEmoji.this.convertedText.append(new String(localObject3).replaceAll("[*]", TextToEmoji.this.emojeeText.getText().toString()) + "\n\n");
                    } catch (IOException ioe22) {
                        ioe22.printStackTrace();
                    }
                }
            }
        }
    }

    //Button - clear Text click listener method
    private class btnClearTextListner implements View.OnClickListener {
        public void onClick(View view) {
            TextToEmoji.this.convertedText.setText("");
        }
    }

    //Button  - Convert Text click listener method
    private class btnConvertedTextListner implements View.OnClickListener {
        @SuppressLint({"WrongConstant"})
        public void onClick(View view) {
            if (!TextToEmoji.this.convertedText.getText().toString().isEmpty()) {
                ((ClipboardManager) TextToEmoji.this.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(TextToEmoji.this.inputText.getText().toString(), TextToEmoji.this.convertedText.getText().toString()));
                Toast.makeText(TextToEmoji.this.getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Copy button click listener method
    private class btnCopyButtonListner implements View.OnClickListener {
        @SuppressLint({"WrongConstant"})
        public void onClick(View view) {
            if (TextToEmoji.this.convertedText.getText().toString().isEmpty()) {
                Toast.makeText(TextToEmoji.this.getApplicationContext(), "Convert text before copy", Toast.LENGTH_SHORT).show();
                return;
            }
            ((ClipboardManager) TextToEmoji.this.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(TextToEmoji.this.inputText.getText().toString(), TextToEmoji.this.convertedText.getText().toString()));
            Toast.makeText(TextToEmoji.this.getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    //Share Button click listener method
    private class btnShareListner implements View.OnClickListener {
        public void onClick(View view) {
            if (TextToEmoji.this.convertedText.getText().toString().isEmpty()) {
                Toast.makeText(TextToEmoji.this.getApplicationContext(), "Convert text to share", Toast.LENGTH_LONG).show();
                return;
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction("android.intent.action.SEND");
            shareIntent.setPackage("com.whatsapp");
            shareIntent.putExtra("android.intent.extra.TEXT", TextToEmoji.this.convertedText.getText().toString());
            shareIntent.setType("text/plain");
            TextToEmoji.this.startActivity(Intent.createChooser(shareIntent, "Select an app to share"));
        }
    }
}