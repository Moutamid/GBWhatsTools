package com.moutamid.gb.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.hbb20.CountryCodePicker;
import com.hbb20.CountryCodePicker.OnCountryChangeListener;

import com.moutamid.gb.R;
import com.moutamid.gb.modules.Utils;

public class ChatDirect extends AppCompatActivity {
    EditText message;
    private EditText number;
    RelativeLayout SendMessage;
    CountryCodePicker CcP;
    private SharedPreferences preference;
    private com.facebook.ads.AdView faceBookBanner;
    ImageView image;
    private InterstitialAd finterstitialAd;

    private class btnSendMessageListner implements OnClickListener {
        public void onClick(View v) {
            String messege = ChatDirect.this.message.getText().toString();
            String number = ChatDirect.this.number.getText().toString();
            String mainNumber = ChatDirect.this.CcP.getSelectedCountryCode() + number;

            if (messege.length() == 0) {
                Toast.makeText(ChatDirect.this, "Please enter message", Toast.LENGTH_SHORT).show();
            } else if (number.length() == 0) {
                Toast.makeText(ChatDirect.this, R.string.message_number_empty, Toast.LENGTH_SHORT).show();
            } else if (number.length() < 7 || messege.length() <= 0) {
                Toast.makeText(ChatDirect.this, R.string.message_number_error, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    PackageManager packageManager = ChatDirect.this.getPackageManager();
                    Intent intent = new Intent("android.intent.action.VIEW");
                    try {
                        String str3 = "https://api.whatsapp.com/send?phone=" + mainNumber + "&text=" + messege;
                        intent.setPackage("com.whatsapp");
                        intent.setData(Uri.parse(str3));
                        if (intent.resolveActivity(packageManager) != null) {
                            ChatDirect.this.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    Toast.makeText(ChatDirect.this, "Error/n" + e2.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private class btnCcpListner implements OnCountryChangeListener {
        public void onCountrySelected() {
            ChatDirect.this.CcP.setCountryPreference(ChatDirect.this.CcP.getSelectedCountryNameCode());
            ChatDirect.this.preference.edit().putString("last_locale", ChatDirect.this.CcP.getSelectedCountryCode()).apply();
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        Utils.loadLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat_direct);

        AudienceNetworkAds.initialize(this);

        faceBookBanner = new AdView(this, getString(R.string.fb_ad_banner), AdSize.BANNER_HEIGHT_50);

        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        adContainer.addView(faceBookBanner);

        faceBookBanner.loadAd();

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

        if (!(ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_NETWORK_STATE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.SET_WALLPAPER") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.INTERNET") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.SYSTEM_ALERT_WINDOW") == 0) && VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.SET_WALLPAPER", "android.permission.INTERNET", "android.permission.SYSTEM_ALERT_WINDOW"}, 0);
        }
        image = findViewById(R.id.imBack);
        image.setOnClickListener(view -> onBackPressed());
        this.message = findViewById(R.id.msg);
        this.number = findViewById(R.id.input_text);
        this.CcP = findViewById(R.id.ccp);
        this.SendMessage = findViewById(R.id.go);
        this.SendMessage.setOnClickListener(v -> {
            String Num = CcP.getSelectedCountryCode() + number.getText().toString();
            String messg = message.getText().toString();
            if (number.getText().toString().isEmpty()){
                Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
                number.setError("Required");
            } else{
                openWhatsApp(Num, messg);
            }
        });
        this.preference = PreferenceManager.getDefaultSharedPreferences(this);
        this.CcP.setCountryForNameCode(Utils.getCurrentLocale(this));
        this.CcP.setOnCountryChangeListener(new btnCcpListner());
        if (getIntent().getStringExtra("number") != null) {
            this.number.setText(getIntent().getStringExtra("number"));
        }
    }

    private void openWhatsApp(String smsNumber, String text) {
        Log.d(TAG, "openWhatsApp: smsNumber: " + smsNumber);
        Log.d(TAG, "openWhatsApp: text: " + text);

        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                        smsNumber, text))));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }
}
