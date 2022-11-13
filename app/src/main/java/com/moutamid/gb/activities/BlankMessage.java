package com.moutamid.gb.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.moutamid.gb.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class BlankMessage extends AppCompatActivity {

    TextView five, ten, tewnty, fifty, hund, blank;
    SwitchMaterial newLineSwitch;
    CardView copyBtn, deleteBtn;
    ImageView backBtn;

    private InterstitialAd finterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_message);

        AudienceNetworkAds.initialize(this);

        finterstitialAd = new InterstitialAd(this, getResources().getString(R.string.fb_ad_inters));
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                showAdWithDelay();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };
        finterstitialAd.loadAd(
                finterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

        five = findViewById(R.id.fiveSpace);
        ten = findViewById(R.id.tenSpace);
        tewnty = findViewById(R.id.twntySpace);
        fifty = findViewById(R.id.fiftySpace);
        hund = findViewById(R.id.hundSpace);
        blank = findViewById(R.id.blankText);
        newLineSwitch = findViewById(R.id.newLineSwitch);
        copyBtn = findViewById(R.id.copyBtn);
        deleteBtn = findViewById(R.id.deleteText);
        backBtn = findViewById(R.id.imBack);
        backBtn.setOnClickListener(view -> onBackPressed());

        copyBtn.setOnClickListener(v -> {
            int sdk = android.os.Build.VERSION.SDK_INT;
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            if (blank.getText().toString().isEmpty()){
                Toast.makeText(this, "Nothing To Copy", Toast.LENGTH_SHORT).show();
            } else {
                if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    clipboard.setText(blank.getText().toString());
                    Toast.makeText(this, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
                } else {
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Blank Message", blank.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(this, "Copied To Clipboard", Toast.LENGTH_SHORT).show();
                }
            }

        });

        deleteBtn.setOnClickListener(v -> {
            five.setBackground(null);
            five.setTextColor(getResources().getColor(R.color.white));
            ten.setBackground(null);
            ten.setTextColor(getResources().getColor(R.color.white));
            tewnty.setBackground(null);
            tewnty.setTextColor(getResources().getColor(R.color.white));
            fifty.setBackground(null);
            fifty.setTextColor(getResources().getColor(R.color.white));
            hund.setBackground(null);
            hund.setTextColor(getResources().getColor(R.color.white));
            blank.setText("");
            blank.setVisibility(View.GONE);
        });

        five.setOnClickListener(view -> {
            String space = "";

            if (five.getBackground() == null){
                five.setTextColor(getResources().getColor(R.color.colorPrimary));
                five.setBackground(getResources().getDrawable(R.drawable.text_selected_bg));
                ten.setBackground(null);
                ten.setTextColor(getResources().getColor(R.color.white));
                tewnty.setBackground(null);
                tewnty.setTextColor(getResources().getColor(R.color.white));
                fifty.setBackground(null);
                fifty.setTextColor(getResources().getColor(R.color.white));
                hund.setBackground(null);
                hund.setTextColor(getResources().getColor(R.color.white));
            }

            if (newLineSwitch.isChecked()){
                for (int i =0; i<5; i++){
                    space = space + "\u3164" + "\n";
                }
            } else {
                for (int i =0; i<5; i++){
                    space = space + "\u3164";
                }
            }
            blank.setVisibility(View.VISIBLE);
            blank.setText(space);
        });

        ten.setOnClickListener(view -> {
            String space = "";
            if (ten.getBackground() == null){
                ten.setTextColor(getResources().getColor(R.color.colorPrimary));
                ten.setBackground(getResources().getDrawable(R.drawable.text_selected_bg));
                five.setBackground(null);
                five.setTextColor(getResources().getColor(R.color.white));
                tewnty.setBackground(null);
                tewnty.setTextColor(getResources().getColor(R.color.white));
                fifty.setBackground(null);
                fifty.setTextColor(getResources().getColor(R.color.white));
                hund.setBackground(null);
                hund.setTextColor(getResources().getColor(R.color.white));
            }

            if (newLineSwitch.isChecked()){
                for (int i =0; i<10; i++){
                    space = space + "\u3164" + "\n";
                }
            } else {
                for (int i =0; i<10; i++){
                    space = space + "\u3164";
                }
            }
            blank.setVisibility(View.VISIBLE);
            blank.setText(space);
        });

        tewnty.setOnClickListener(view -> {
            String space = "";
            if (tewnty.getBackground() == null){
                tewnty.setTextColor(getResources().getColor(R.color.colorPrimary));
                tewnty.setBackground(getResources().getDrawable(R.drawable.text_selected_bg));
                five.setBackground(null);
                five.setTextColor(getResources().getColor(R.color.white));
                ten.setBackground(null);
                ten.setTextColor(getResources().getColor(R.color.white));
                fifty.setBackground(null);
                fifty.setTextColor(getResources().getColor(R.color.white));
                hund.setBackground(null);
                hund.setTextColor(getResources().getColor(R.color.white));
            }

            if (newLineSwitch.isChecked()){
                for (int i =0; i<20; i++){
                    space = space + "\u3164" + "\n";
                }
            } else {
                for (int i =0; i<20; i++){
                    space = space + "\u3164";
                }
            }
            blank.setVisibility(View.VISIBLE);
            blank.setText(space);
        });

        fifty.setOnClickListener(view -> {
            String space = "";
            if (fifty.getBackground() == null){
                fifty.setTextColor(getResources().getColor(R.color.colorPrimary));
                fifty.setBackground(getResources().getDrawable(R.drawable.text_selected_bg));
                five.setBackground(null);
                five.setTextColor(getResources().getColor(R.color.white));
                ten.setBackground(null);
                ten.setTextColor(getResources().getColor(R.color.white));
                tewnty.setBackground(null);
                tewnty.setTextColor(getResources().getColor(R.color.white));
                hund.setBackground(null);
                hund.setTextColor(getResources().getColor(R.color.white));
            }

            if (newLineSwitch.isChecked()){
                for (int i =0; i<50; i++){
                    space = space + "\u3164" + "\n";
                }
            } else {
                for (int i =0; i<50; i++){
                    space = space + "\u3164";
                }
            }
            blank.setVisibility(View.VISIBLE);
            blank.setText(space);
        });

        hund.setOnClickListener(view -> {
            String space = "";
            if (hund.getBackground() == null){
                hund.setTextColor(getResources().getColor(R.color.colorPrimary));
                hund.setBackground(getResources().getDrawable(R.drawable.text_selected_bg));
                five.setBackground(null);
                five.setTextColor(getResources().getColor(R.color.white));
                ten.setBackground(null);
                ten.setTextColor(getResources().getColor(R.color.white));
                tewnty.setBackground(null);
                tewnty.setTextColor(getResources().getColor(R.color.white));
                fifty.setBackground(null);
                fifty.setTextColor(getResources().getColor(R.color.white));
            }

            if (newLineSwitch.isChecked()){
                for (int i =0; i<100; i++){
                    space = space + "\u3164" + "\n";
                }
            } else {
                for (int i =0; i<100; i++){
                    space = space + "\u3164";
                }
            }
            blank.setVisibility(View.VISIBLE);
            blank.setText(space);
        });
    }

    private void showAdWithDelay() {
        /**
         * Here is an example for displaying the ad with delay;
         * Please do not copy the Handler into your project
         */
        // Handler handler = new Handler();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                // Check if interstitialAd has been loaded successfully
                if(finterstitialAd == null || !finterstitialAd.isAdLoaded()) {
                    return;
                }
                // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
                if(finterstitialAd.isAdInvalidated()) {
                    return;
                }
                // Show the ad
                finterstitialAd.show();
            }
        }, 5000); // Show the ad after 5 minutes
    }

    @Override
    protected void onDestroy() {
        if (finterstitialAd != null) {
            finterstitialAd.destroy();
        }
        super.onDestroy();
    }
}