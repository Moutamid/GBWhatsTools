package com.gbversiongb.gb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.gbversiongb.gb.R;
import com.gbversiongb.gb.adapters.CustomAdapter;

public class Captions extends AppCompatActivity {
    String[] logos = new String[]{"Best", "Clever", "Cool", "Cute", "Fitness", "Funny", "Life", "Love", "Motivation", "Sad", "Savage", "Selfie", "Song"};
    GridView simpleGrid;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captions);
        this.simpleGrid = findViewById(R.id.simpleGridView);
        backBtn = findViewById(R.id.imBack);
        backBtn.setOnClickListener(view -> onBackPressed());
        this.simpleGrid.setAdapter(new CustomAdapter(getApplicationContext(), this.logos));
        this.simpleGrid.setOnItemClickListener(new simpleGridListner());
    }

    //It's called While click on gridview
    private class simpleGridListner implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(Captions.this, CaptionsList.class);
            intent.putExtra("image", Captions.this.logos[position]);
            intent.putExtra("P", position);
            startActivity(intent);
        }
    }
}