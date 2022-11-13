package com.moutamid.gb;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;


import java.util.ArrayList;
import java.util.List;

import com.moutamid.gb.activities.BlankMessage;
import com.moutamid.gb.activities.Captions;
import com.moutamid.gb.activities.ChatDirect;
import com.moutamid.gb.activities.Emotions;
import com.moutamid.gb.activities.TextMagic;
import com.moutamid.gb.activities.TextRepeat;
import com.moutamid.gb.activities.TextToEmoji;
import com.moutamid.gb.activities.WhatsAppWeb;
import com.moutamid.gb.modules.AdController;
import com.moutamid.gb.modules.Utils;
import com.moutamid.gb.activities.MyStatusActivity;
import com.moutamid.gb.activities.StatusActivity;
import com.moutamid.gb.modules.adsManager;

import static com.moutamid.gb.modules.AdController.LoadFBInterstitial;
import static com.moutamid.gb.modules.Utils.createFileFolder;

public class MainActivity extends AppCompatActivity {
    MainActivity activity;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private FrameLayout adContainerView;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    //private InterstitialAd mInterstitialAd;
    //AdRequest adRequest;
    int AdCounter = 0;
    public static MainActivity ma;
    Dialog dialog, dialogLang;

    private LinearLayout bannerBox;
    private com.facebook.ads.AdView faceBookBanner;
    private com.google.android.gms.ads.AdView googleBanner;

    private InterstitialAd finterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.loadLocale(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        ma = this;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        adsManager ads = new adsManager(MainActivity.this, true);
        bannerBox = (LinearLayout) findViewById(R.id.BannerBox);

        AdController.loadAdFifty(this, bannerBox);

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

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(0);
        }
        createFileFolder();

        findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LoadFBInterstitial(MainActivity.this);
                if (Utils.hasPermissions(MainActivity.this, Utils.permissions)) {
                    ActivityCompat.requestPermissions(MainActivity.this, Utils.permissions, Utils.perRequest);
                } else {
                    Intent i = new Intent(MainActivity.this, MyStatusActivity.class);
                    startActivity(i);
                }

            }
        });
        findViewById(R.id.rvWhatsApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoadFBInterstitial(MainActivity.this);
                if (Utils.hasPermissions(MainActivity.this, Utils.permissions)) {
                    ActivityCompat.requestPermissions(MainActivity.this, Utils.permissions, Utils.perRequest);
                } else {
                    Intent i = new Intent(MainActivity.this, StatusActivity.class);
                    i.putExtra("type", "WA");
                    startActivity(i);
                }
            }
        });
        findViewById(R.id.captions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoadFBInterstitial(MainActivity.this);
                if (Utils.hasPermissions(MainActivity.this, Utils.permissions)) {
                    ActivityCompat.requestPermissions(MainActivity.this, Utils.permissions, Utils.perRequest);
                } else {
                    Intent i = new Intent(MainActivity.this, Captions.class);
                    i.putExtra("type", "WA");
                    startActivity(i);
                }
            }
        });
        findViewById(R.id.textEmoji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoadFBInterstitial(MainActivity.this);
                if (Utils.hasPermissions(MainActivity.this, Utils.permissions)) {
                    ActivityCompat.requestPermissions(MainActivity.this, Utils.permissions, Utils.perRequest);
                } else {
                    Intent i = new Intent(MainActivity.this, TextToEmoji.class);
                    i.putExtra("type", "WA");
                    startActivity(i);
                }
            }
        });
        findViewById(R.id.emotion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoadFBInterstitial(MainActivity.this);
                if (Utils.hasPermissions(MainActivity.this, Utils.permissions)) {
                    ActivityCompat.requestPermissions(MainActivity.this, Utils.permissions, Utils.perRequest);
                } else {
                    Intent i = new Intent(MainActivity.this, Emotions.class);
                    i.putExtra("type", "WA");
                    startActivity(i);
                }
            }
        });
        findViewById(R.id.textMagic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoadFBInterstitial(MainActivity.this);
                if (Utils.hasPermissions(MainActivity.this, Utils.permissions)) {
                    ActivityCompat.requestPermissions(MainActivity.this, Utils.permissions, Utils.perRequest);
                } else {
                    Intent i = new Intent(MainActivity.this, TextMagic.class);
                    i.putExtra("type", "WA");
                    startActivity(i);
                }
            }
        });

        langAlert();

        findViewById(R.id.changeLng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLang.show();
            }
        });

        findViewById(R.id.quotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WhatsAppWeb.class);
                startActivity(i);
            }
        });

        findViewById(R.id.rvWhatsAppB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.hasPermissions(MainActivity.this, Utils.permissions)) {
                    ActivityCompat.requestPermissions(MainActivity.this, Utils.permissions, Utils.perRequest);
                } else {
                    Intent i = new Intent(MainActivity.this, StatusActivity.class);
                    i.putExtra("type", "WABS");
                    startActivity(i);

                    LoadFBInterstitial(MainActivity.this);
                }
            }
        });

        findViewById(R.id.rvWhatsAppG).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.hasPermissions(MainActivity.this, Utils.permissions)) {
                    ActivityCompat.requestPermissions(MainActivity.this, Utils.permissions, Utils.perRequest);
                } else {
                    Intent i = new Intent(MainActivity.this, StatusActivity.class);
                    i.putExtra("type", "WAGB");
                    startActivity(i);

                    LoadFBInterstitial(MainActivity.this);
                }
            }
        });
        findViewById(R.id.chatDirect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoadFBInterstitial(MainActivity.this);
                Intent i = new Intent(MainActivity.this, ChatDirect.class);
                startActivity(i);
            }
        });
        findViewById(R.id.blankMsg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFBInterstitial(MainActivity.this);
                Intent i = new Intent(MainActivity.this, BlankMessage.class);
                startActivity(i);
            }
        });
        findViewById(R.id.textRepeat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFBInterstitial(MainActivity.this);
                Intent i = new Intent(MainActivity.this, TextRepeat.class);
                startActivity(i);
            }
        });
        wAppAlert();
        findViewById(R.id.openWA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        LinearLayout bannerBox = findViewById(R.id.banner_container);

        AdController.loadAdFifty(this, bannerBox);
    }

    boolean isOpenWapp = false, isOpenWbApp = false, isOpenWgApp = false;

    void wAppAlert() {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.popup_lay);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RelativeLayout btnWapp = dialog.findViewById(R.id.btn_wapp);
        RelativeLayout btnWappBus = dialog.findViewById(R.id.btn_wapp_bus);
        RelativeLayout btnWappGb = dialog.findViewById(R.id.btn_wapp_gb);

        btnWapp.setOnClickListener(arg0 -> {
            try {
                isOpenWapp = true;
                startActivity(getPackageManager().getLaunchIntentForPackage("com.whatsapp"));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Please Install WhatsApp For Download Status!!!!!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();

        });

        btnWappBus.setOnClickListener(arg0 -> {
            try {
                isOpenWbApp = true;
                startActivity(getPackageManager().getLaunchIntentForPackage("com.whatsapp.w4b"));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Please Install WhatsApp Business For Download Status!!!!!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        btnWappGb.setOnClickListener(arg0 -> {
            try {
                isOpenWgApp = true;
                startActivity(getPackageManager().getLaunchIntentForPackage("com.gbwhatsapp"));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Please Install WhatsApp GB For Download Status!!!!!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

    }

    public Intent openTelegram() {
        Intent intent;
        try {
            try {
                getPackageManager().getPackageInfo("org.telegram.messenger", 0);
            } catch (PackageManager.NameNotFoundException e) {
                getPackageManager().getPackageInfo("org.thunderdog.challegram", 0);
            }
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=gblatest"));
        } catch (PackageManager.NameNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/gblatest"));
        }
        return intent;

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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean checkPermissions(int type) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) (activity),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), type);
            return false;
        }
        return true;
    }

    public void langAlert() {
        dialogLang = new Dialog(MainActivity.this);
        dialogLang.setContentView(R.layout.lang_lay);

        dialogLang.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txtEn = dialogLang.findViewById(R.id.txt_en);
        TextView txtHi = dialogLang.findViewById(R.id.txt_hi);
        TextView txtAr = dialogLang.findViewById(R.id.txt_ar);

        txtEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveLocale(MainActivity.this, "en");
                dialogLang.dismiss();
                refresh();
            }
        });

        txtHi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveLocale(MainActivity.this, "hi");
                dialogLang.dismiss();
                refresh();
            }
        });

        txtAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveLocale(MainActivity.this, "ar");
                dialogLang.dismiss();
                refresh();
                Log.e("language", "ar");
            }
        });

    }

    public void refresh() {
        finish();
        startActivity(getIntent());
    }
}