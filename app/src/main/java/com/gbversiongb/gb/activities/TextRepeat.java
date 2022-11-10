package com.gbversiongb.gb.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.gbversiongb.gb.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class TextRepeat extends AppCompatActivity {

    SwitchMaterial newLineSwitch;
    CardView copyBtn, deleteBtn;
    ImageView backBtn;
    EditText text, num;
    Button repeat;
    TextView result;
    private InterstitialAd finterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_repeat);

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