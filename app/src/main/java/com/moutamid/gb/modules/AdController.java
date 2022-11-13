package com.moutamid.gb.modules;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;

public class AdController {



    public static String FacebookBanner = "1111035279658792_1112019856227001";
    public static String FacebookInterstitialAdCode = "1111035279658792_1112019976226989";

    public static void LoadFBInterstitial(Context c){
        InterstitialAd interstitialAd;
        interstitialAd = new InterstitialAd(c, FacebookInterstitialAdCode);
        AbstractAdListener adListener = new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                super.onError(ad, error);

            }
            @Override
            public void onAdLoaded(Ad ad) {
                super.onInterstitialDisplayed(ad);{
                interstitialAd.show();

                }
            }
            @Override
            public void onAdClicked(Ad ad) {
                super.onAdClicked(ad);
            }
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                super.onInterstitialDisplayed(ad);
            }
            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);
            }
        };
        InterstitialAd.InterstitialLoadAdConfig interstitialLoadAdConfig = interstitialAd.buildLoadAdConfig()
                .withAdListener(adListener)
                .build();
        interstitialAd.loadAd(interstitialLoadAdConfig);
    }
    public static void loadAdFifty(Context c, LinearLayout bannerBox) {
        AdView adView = new AdView(c, FacebookBanner, AdSize.BANNER_HEIGHT_50);
        bannerBox.addView(adView);
        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        AdView.AdViewLoadConfig loadAdConfig = adView.buildLoadAdConfig()
                .withAdListener(adListener)
                .build();
        adView.loadAd(loadAdConfig);
    }


    static void startActivity(Activity context, Intent intent, int requestCode) {
        if (intent != null) {
            context.startActivityForResult(intent, requestCode);
        }
    }
    static void startActivity(Fragment context, Intent intent, int requestCode) {
        if (intent != null) {
            context.startActivityForResult(intent, requestCode);
        }
    }

}
