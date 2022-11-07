package com.gbversiongb.gb.modules;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;

public class adsManager {

    private int maxFBAdClicksPerDay = 7;
    private int maxGoogleAdClicksPerDay = 2;
    private int maxCTRPerDay = 30;
    private String ONESIGNAL_APP_ID;


    public String FacebookBanner1, FacebookBanner2, FacebookBanner3;
    public String FacebookInterstitialAdCode, FacebookRewardedAdCode;
    public InterstitialAd FacebookinterstitialAd;

    public String GoogleBanner1, GoogleBanner2, GoogleBanner3;
    public String GoogleInterstitialAdCode;
    public String GoogleRewardedAdCode;
    public com.google.android.gms.ads.interstitial.InterstitialAd GoogleinterstitialAd;

    public RewardedAd mRewardedAd;

    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private String SHARED_PREF;

    private Context context;
    private Activity activity;
    private boolean isRewarded = false;


    public adsManager(Activity activity, boolean testMode) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
        AudienceNetworkAds.initialize(activity.getApplicationContext());

        this.ONESIGNAL_APP_ID = "";
        this.SHARED_PREF = "adsSettings";
        this.sp = context.getSharedPreferences(this.SHARED_PREF, Context.MODE_PRIVATE);
        this.ed = sp.edit();

        if (testMode) {

            AdSettings.setTestMode(false);
            this.FacebookBanner1 = "IMG_16_9_APP_INSTALL#1111035279658792_1112019856227001";
            this.FacebookBanner2 = "IMG_16_9_APP_INSTALL#1111035279658792_1112019856227001";
            this.FacebookBanner3 = "IMG_16_9_APP_INSTALL#1111035279658792_1112019856227001";
            this.FacebookInterstitialAdCode = "IMG_16_9_APP_INSTALL#1111035279658792_1112019976226989";
            this.FacebookRewardedAdCode = "YOUR_PLACEMENT_ID";


            this.GoogleBanner1 = "ca-app-pub-4187292153162583/8730890772";
            this.GoogleBanner2 = "ca-app-pub-4187292153162583/8730890772";
            this.GoogleBanner3 = "ca-app-pub-4187292153162583/8730890772";
            this.GoogleInterstitialAdCode = "ca-app-pub-4187292153162583/6104727431";
            this.GoogleRewardedAdCode = "ca-app-pub-4187292153162583/4791645764";
        } else {

            this.FacebookBanner1 = "1111035279658792_1112019856227001";
            this.FacebookBanner2 = "1111035279658792_1112019856227001";
            this.FacebookBanner3 = "1111035279658792_1112019856227001";

            this.FacebookInterstitialAdCode = "1111035279658792_1112019976226989";

            this.FacebookRewardedAdCode = "";


            this.GoogleBanner1 = "ca-app-pub-4187292153162583/8730890772";
            this.GoogleBanner2 = "ca-app-pub-4187292153162583/8730890772";
            this.GoogleBanner3 = "ca-app-pub-4187292153162583/8730890772";

            this.GoogleInterstitialAdCode = "ca-app-pub-4187292153162583/6104727431";

            this.GoogleRewardedAdCode = "ca-app-pub-4187292153162583/4791645764";
        }

        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));
                }

            }
        });
    }


    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String date = dateFormat.format(new Date());
        return date;
    }


    public com.google.android.gms.ads.AdView createGoogleBanner(String adCode, LinearLayout bannerBox) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        com.google.android.gms.ads.AdView adViewG = new com.google.android.gms.ads.AdView(context);
        adViewG.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
        adViewG.setAdUnitId(adCode);
        bannerBox.addView(adViewG);
        return adViewG;
    }

    public void showGoogleBanner(com.google.android.gms.ads.AdView adViewG, com.facebook.ads.AdView fbBanner) {
        adViewG.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                adViewG.setVisibility(View.GONE);
                adViewG.destroy();
                if (fbBanner != null && canShowFacebookAds()) {
                    fbBanner.setVisibility(View.VISIBLE);
                    showFacebookBanner(fbBanner);
                }
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                if (fbBanner != null) {
                    adViewG.setVisibility(View.VISIBLE);
                    fbBanner.setVisibility(View.GONE);
                    fbBanner.destroy();
                }
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                updateGoogleClicks();
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                updateGoogleImpressions();
                super.onAdImpression();
            }
        });
        adViewG.loadAd(new AdRequest.Builder().build());
    }

    public void showGoogleBanner(com.google.android.gms.ads.AdView adViewG) {
        showGoogleBanner(adViewG, null);
    }

    public com.facebook.ads.AdView createFacebookBanner(String adCode, LinearLayout bannerBox) {
        com.facebook.ads.AdView adViewF = new AdView(context, adCode, AdSize.BANNER_HEIGHT_50);
        bannerBox.addView(adViewF);
        return adViewF;
    }

    public void showFacebookBanner(com.facebook.ads.AdView adV, com.google.android.gms.ads.AdView ReplacementGoogleBanner) {
        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

                adV.setVisibility(View.GONE);
                adV.destroy();
                if (ReplacementGoogleBanner != null && canShowGoogleAds()) {
                    ReplacementGoogleBanner.setVisibility(View.VISIBLE);
                    showGoogleBanner(ReplacementGoogleBanner);
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                adV.setVisibility(View.VISIBLE);
                if (ReplacementGoogleBanner != null) {
                    ReplacementGoogleBanner.destroy();
                    ReplacementGoogleBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                updateFacebookClicks();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                updateFacebookImpressions();
            }
        };
        adV.loadAd(adV.buildLoadAdConfig().withAdListener(adListener).build());
    }

    public void showFacebookBanner(com.facebook.ads.AdView adV) {
        showFacebookBanner(adV, null);
    }

    public void showBannerAds(com.facebook.ads.AdView fBBanner, com.google.android.gms.ads.AdView googleBanner) {
        if (canShowFacebookAds())
            showFacebookBanner(fBBanner, googleBanner);
        else if (canShowGoogleAds())
            showGoogleBanner(googleBanner, fBBanner);
    }



    public void showFacebookInterstitial(String adCode) {
        showFacebookInterstitial(adCode, "");
    }

    public void showFacebookInterstitial(String adCode, String googleCode) {


        InterstitialAd intad = new InterstitialAd(context, adCode);

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {

            @Override
            public void onInterstitialDisplayed(Ad ad) {


            }

            @Override

            public void onInterstitialDismissed(Ad ad) {

            }

            @Override

            public void onError(Ad ad, AdError adError) {


                if (!googleCode.equals("") && canShowGoogleAds())
                    showGoogleInterstitial(googleCode);

            }

            @Override

            public void onAdLoaded(Ad ad) {

                intad.show();

            }

            @Override

            public void onAdClicked(Ad ad) {

                updateFacebookClicks();

            }

            @Override

            public void onLoggingImpression(Ad ad) {

                updateFacebookImpressions();

            }

        };

        intad.loadAd(
                intad.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    public void showGoogleInterstitial(String googleAdCode) {
        showGoogleInterstitial(googleAdCode, "");
    }

    public void showGoogleInterstitial(String googleAdCode, String fbAdCode) {
        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(context, googleAdCode, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                GoogleinterstitialAd = interstitialAd;
                GoogleinterstitialAd.show(activity);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                GoogleinterstitialAd = null;
                if (!fbAdCode.equals("") && canShowFacebookAds())
                    showFacebookInterstitial(fbAdCode);
            }
        });
    }

    public void showInterstitialAds(String facebookAdCode, String googleAdCode) {
        if (canShowFacebookAds())
            showFacebookInterstitial(facebookAdCode, googleAdCode);
        else if (canShowGoogleAds())
            showGoogleInterstitial(googleAdCode, facebookAdCode);
    }

    public void showInterstitialAds() {
        showInterstitialAds(FacebookInterstitialAdCode, GoogleInterstitialAdCode);
    }


    public void showGooglerewarded(String adCode, String fbADCode, Callable<Void> onRewared) {
        isRewarded = false;
        RewardedAd.load(context, adCode, new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                mRewardedAd = rewardedAd;
                Log.d("adsTest", "Google Rewarded Ad was loaded.");

                if (mRewardedAd != null) {
                    mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d("adsTest", "Google Rewarded Ad was shown.");
                            updateGoogleImpressions();
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            mRewardedAd = null;
                            if (isRewarded)
                            {
                                try {
                                    onRewared.call();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    mRewardedAd.show(activity, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            Log.d("adsTest", "The user earned the Google reward.");
                            isRewarded = true;

                        }
                    });
                } else {
                    Log.d("adsTest", "The Google rewarded ad wasn't ready yet.");
                    isRewarded = false;
                    if (!fbADCode.equals("") && canShowFacebookAds())
                        showFacebookRewarded(fbADCode, onRewared);
                }


            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mRewardedAd = null;
                isRewarded = false;
                if (!fbADCode.equals("") && canShowFacebookAds())
                    showFacebookRewarded(fbADCode, onRewared);
            }
        });

    }

    public void showGooglerewarded(String googleAdCode, Callable<Void> onRewared) {
        showGooglerewarded(googleAdCode, "", onRewared);
    }

    public void showFacebookRewarded(String adCode, Callable<Void> onRewared) {
        showFacebookRewarded(adCode, "", onRewared);
    }

    public void showFacebookRewarded(String adCode, String googleAdCode, Callable<Void> onRewared) {
        isRewarded = false;
        RewardedVideoAd rewardedVideoAd = new RewardedVideoAd(context, adCode);
        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
                Log.d("adsTest", "Fb Rewarded Failed");
                if (!googleAdCode.equals("") && canShowGoogleAds())
                    showGooglerewarded(googleAdCode, onRewared);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d("adsTest", "Fb Rewarded Loaded");
                rewardedVideoAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

                isRewarded = true;
                updateFacebookClicks();
                Log.d("adsTest", "Fb Rewarded Clicked");
            }

            @Override
            public void onLoggingImpression(Ad ad) {

                Log.d("adsTest", "Fb Rewarded video ad impression logged!");
                updateFacebookImpressions();
            }

            @Override
            public void onRewardedVideoCompleted() {

                Log.d("adsTest", "Rewarded video completed!");
                isRewarded = true;


            }

            @Override
            public void onRewardedVideoClosed() {

                if (isRewarded) {
                    try {
                        onRewared.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Please Watch Video to get Reward", Toast.LENGTH_SHORT).show();
                }


            }
        };

        rewardedVideoAd.loadAd(
                rewardedVideoAd.buildLoadAdConfig()
                        .withAdListener(rewardedVideoAdListener)
                        .build());

    }

    public void showRewardedAds(String fbAdCode, String GoogleAdCode, Callable<Void> onRewared) {
        isRewarded = false;
        if (canShowFacebookAds())
            showFacebookRewarded(fbAdCode, GoogleAdCode, onRewared);
        else if (canShowGoogleAds())
            showGooglerewarded(GoogleAdCode, fbAdCode, onRewared);

    }

    public void showRewardedAds(Callable<Void> onRewared) {
        showRewardedAds(this.FacebookRewardedAdCode, this.GoogleRewardedAdCode, onRewared);
    }

    public void showRewardedAds() {
        showRewardedAds(this.FacebookRewardedAdCode, this.GoogleRewardedAdCode, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                return null;
            }
        });
    }

    public boolean canShowFacebookAds() {
        int clicks = sp.getInt("fbclicks_" + getDate(), 6) + 1;
        int impressions = sp.getInt("fbimp_" + getDate(), 100) + 1;

        if (clicks + impressions <= 100)
            return true;

        if (clicks >= this.maxFBAdClicksPerDay)
            return false;



        return true;
    }

    public boolean canShowGoogleAds() {
        int clicks = sp.getInt("googleclicks_" + getDate(), 0);
        int impressions = sp.getInt("googleimp_" + getDate(), 0);

        if (clicks + impressions <= 100)
            return true;

        if (clicks >= this.maxGoogleAdClicksPerDay)
            return false;



        return true;
    }

    private void updateFacebookClicks() {
        int clicks = sp.getInt("fbclicks_" + getDate(), 6) + 1;
        ed.putInt("fbclicks_" + getDate(), clicks);
        ed.commit();
    }

    private void updateFacebookImpressions() {
        int clicks = sp.getInt("fbimp_" + getDate(), 100) + 1;
        ed.putInt("fbimp_" + getDate(), clicks);
        ed.commit();
    }

    private void updateGoogleClicks() {
        int clicks = sp.getInt("googleclicks_" + getDate(), 2) + 1;
        ed.putInt("googleclicks_" + getDate(), clicks);
        ed.commit();
    }

    private void updateGoogleImpressions() {
        int clicks = sp.getInt("googleimp_" + getDate(), 100) + 1;
        ed.putInt("googleimp_" + getDate(), clicks);
        ed.commit();
    }

}
