package com.gbversiongb.gb.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.gbversiongb.gb.R;
import com.gbversiongb.gb.textmagic.Font;
import com.gbversiongb.gb.textmagic.FontAdapter;
import com.gbversiongb.gb.textmagic.c;

import java.util.ArrayList;

public class TextMagic extends AppCompatActivity {

    EditText etText;
    ArrayList<Font> fontList;
    String fontText;
    RecyclerView rvStylishFonts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_magic);

        this.etText = findViewById(R.id.et_text);
        this.rvStylishFonts = findViewById(R.id.rv_fonts);
        this.fontList = new ArrayList<>();

        ImageView backBtn = findViewById(R.id.imBack);
        backBtn.setOnClickListener(view -> onBackPressed());

        this.etText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                TextMagic.this.makeStylishOf(charSequence);
            }
        });
    }
    public void makeStylishOf(CharSequence charSequence) {
        char[] charArray = charSequence.toString().toLowerCase().toCharArray();
        String[] strArr = new String[44];
        for (int i = 0; i < 44; i++) {
            strArr[i] = applyStyle(charArray, c.strings[i]);
        }
        styleTheFont(strArr);
    }

    private String applyStyle(char[] cArr, String[] strArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < cArr.length; i++) {
            if (cArr[i] - 'a' < 0 || cArr[i] - 'a' > 25) {
                stringBuffer.append(cArr[i]);
            } else {
                stringBuffer.append(strArr[cArr[i] - 'a']);
            }
        }
        return stringBuffer.toString();
    }

    private void styleTheFont(String[] strArr) {
        this.fontList.clear();
        this.fontText = this.etText.getText().toString().trim();
        if (!this.fontText.isEmpty()) {
            for (int i = 0; i < 44; i++) {
                Font font = new Font();
                font.fontText = strArr[i];
                this.fontList.add(font);
            }
            this.rvStylishFonts.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            this.rvStylishFonts.setAdapter(new FontAdapter(this, this.fontList, (adapterView, view, i, j) -> {
            }));
        }
    }
}