package com.moutamid.gb.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.moutamid.gb.R;
import com.moutamid.gb.adapters.CustomAdapter1;

public class CaptionsList extends AppCompatActivity {

    String name;
    String[] items;
    ListView listViews;
    int position;
    TextView title;
    ImageView backBtn;
    private InterstitialAd finterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captions_list);

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

        name = getIntent().getStringExtra("image");

        title = findViewById(R.id.headingtext);
        title.setText(name);

        backBtn = findViewById(R.id.imBack);
        backBtn.setOnClickListener(view -> onBackPressed());

        this.position = getIntent().getIntExtra("P", 0);

        if (this.position == 0) {
            this.items = getResources().getStringArray(R.array.s_best);
        } else if (this.position == 1) {
            this.items = getResources().getStringArray(R.array.s_clever);
        } else if (this.position == 2) {
            this.items = getResources().getStringArray(R.array.s_cool);
        } else if (this.position == 3) {
            this.items = getResources().getStringArray(R.array.s_cute);
        } else if (this.position == 4) {
            this.items = getResources().getStringArray(R.array.s_fitness);
        } else if (this.position == 5) {
            this.items = getResources().getStringArray(R.array.s_funny);
        } else if (this.position == 6) {
            this.items = getResources().getStringArray(R.array.s_life);
        } else if (this.position == 7) {
            this.items = getResources().getStringArray(R.array.s_love);
        } else if (this.position == 8) {
            this.items = getResources().getStringArray(R.array.s_motivation);
        } else if (this.position == 9) {
            this.items = getResources().getStringArray(R.array.s_sad);
        } else if (this.position == 10) {
            this.items = getResources().getStringArray(R.array.s_selfie);
        } else if (this.position == 11) {
            this.items = getResources().getStringArray(R.array.s_song);
        }

        /*else if (this.position == 10) {
            this.items = getResources().getStringArray(R.array.s_savage);
        }*/

        this.listViews = findViewById(R.id.simpleListView);
        this.listViews.setAdapter(new CustomAdapter1(getApplicationContext(), this.items));

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