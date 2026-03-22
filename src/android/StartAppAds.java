package com.startapp.ads;

import org.apache.cordova.*;
import org.json.JSONArray;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;

public class StartAppAds extends CordovaPlugin {

    private Activity activity;
    private StartAppAd startAppAd;
    private Banner banner;

    private Handler bannerHandler;
    private Runnable bannerRefresh;

    @Override
    protected void pluginInitialize() {

        activity = cordova.getActivity();

        try {

            ApplicationInfo ai = activity.getPackageManager()
                    .getApplicationInfo(activity.getPackageName(),
                            PackageManager.GET_META_DATA);

            Bundle bundle = ai.metaData;

            String appId = bundle.getString("com.startapp.sdk.APPLICATION_ID");

            StartAppSDK.init(activity, appId, false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        StartAppAd.disableSplash();
        startAppAd = new StartAppAd(activity);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if (action.equals("loadInterstitial")) {

            startAppAd.loadAd();
            return true;
        }

        if (action.equals("showInterstitial")) {

            activity.runOnUiThread(() -> {

                startAppAd.showAd(new AdDisplayListener() {

                    @Override
                    public void adDisplayed(com.startapp.sdk.adsbase.Ad ad) {
                        sendEvent("interstitial_open");
                    }

                    @Override
                    public void adHidden(com.startapp.sdk.adsbase.Ad ad) {
                        sendEvent("interstitial_closed");
                    }

                    @Override
                    public void adClicked(com.startapp.sdk.adsbase.Ad ad) {}

                    @Override
                    public void adNotDisplayed(com.startapp.sdk.adsbase.Ad ad) {}

                });

            });

            return true;
        }

        if (action.equals("showReward")) {

            startAppAd.setVideoListener(new VideoListener() {

                @Override
                public void onVideoCompleted() {

                    activity.runOnUiThread(() -> {

                        webView.getEngine().evaluateJavascript(
                                "startapp.rewardComplete()", null);

                        sendEvent("reward_completed");

                    });
                }
            });

            activity.runOnUiThread(() -> {

                startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO);
                startAppAd.showAd("REWARDED_VIDEO");

            });

            return true;
        }

        if (action.equals("showBanner")) {
            showBanner();
            return true;
        }

        if (action.equals("hideBanner")) {
            hideBanner();
            return true;
        }

        return false;
    }

    private void showBanner() {

        activity.runOnUiThread(() -> {

            try {

                if (banner != null) return;

                banner = new Banner(activity);

                View webViewView = getWebView();

                ViewGroup root = (ViewGroup) webViewView.getRootView();

                FrameLayout.LayoutParams params =
                        new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT
                        );

                params.gravity = Gravity.BOTTOM;

                root.addView(banner, params);

                startBannerRefresh();

                sendEvent("banner_loaded");

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    private void hideBanner() {

        activity.runOnUiThread(() -> {

            if (banner != null) {

                ViewGroup parent = (ViewGroup) banner.getParent();

                if (parent != null) parent.removeView(banner);

                banner = null;

            }

            stopBannerRefresh();

        });
    }

    private void startBannerRefresh() {

        bannerHandler = new Handler();

        bannerRefresh = new Runnable() {

            @Override
            public void run() {

                if (banner != null) {

                    banner.loadAd();

                }

                bannerHandler.postDelayed(this, 30000);

            }
        };

        bannerHandler.postDelayed(bannerRefresh, 30000);
    }

    private void stopBannerRefresh() {

        if (bannerHandler != null && bannerRefresh != null) {

            bannerHandler.removeCallbacks(bannerRefresh);

        }
    }

    private void sendEvent(String event) {

        activity.runOnUiThread(() ->
                webView.getEngine().evaluateJavascript(
                        "startapp.onAdEvent('" + event + "')", null)
        );
    }

    private View getWebView() {

        CordovaWebView webView = this.webView;

        try {

            return (View) webView.getClass()
                    .getMethod("getView")
                    .invoke(webView);

        } catch (Exception e) {

            return (View) webView;

        }
    }
}