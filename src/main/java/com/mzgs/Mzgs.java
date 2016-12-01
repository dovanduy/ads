package com.mzgs;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.appevents.AppEventsLogger;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.pixplicity.easyprefs.library.Prefs;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.banner.banner3d.Banner3D;

import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;


public class Mzgs extends AppCompatActivity {


    //region Variables
    public String admobBannerID = "";
    public String admobInterstitialID = "";
    public String facebookInterstitialID = "";
    public String facebookBannerID = "";
    public String startAppID = "";
    
    public String defaultAd = "admob";
    public String defaultBannerAd = "admob";



    Bundle savedInstanceState;
    public boolean admobTest = false;
    //endregion


    Map<String, String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);

        FacebookAnalyticInit();


    }


    //region Choose Ads
    public void InitAds()
    {

        AdmobLoadInterstitial();
        FacebookInit();
        StartAppInit();

    }


    /**
     * param can be : <br/>
     * admob , startapp, facebook, facebook_splash, applovin
     * @param ad
     */
    public void ShowAd(String ad)
    {

        switch (ad)
        {

            case "admob":
                AdmobShowInterstitial();
                break;

            case "startapp":
                StartAppShowInterstitial();
                break;

            case "facebook":
                FacebookShowInterstitial();
                break;

            case "facebook_splash":
                FacebookShowSplash();
                break;



            case "none":
                break;

            default:

                ShowAd(defaultAd);
                break;

        }


    }

    public void ShowOnBackAd(String ad, Class<?> activity )
    {

        switch (ad)
        {

            case "admob":
                AdmobOnBack(activity);
                break;

            case "startapp":
                StartAppOnBack(activity);
                break;

            case "facebook":
                FacebookOnBack(activity);
                break;

            case "none":
                break;

            default:
                ShowOnBackAd(defaultAd, activity);

                break;

        }


    }

    public void ShowOnBackAd(String ad)
    {

        switch (ad)
        {

            case "admob":
                AdmobOnBack();
                break;

            case "startapp":
                StartAppOnBack();
                break;

            case "facebook":
                FacebookOnBack();
                break;

            case "none":
                break;

            default:
                ShowOnBackAd(defaultAd);

                break;

        }


    }


    /**
     * param can be : <br/>
     * admob , startapp, facebook
     * @param ad
     */
    public void ShowBannerAd(String ad, int relativeLayoutID )
    {

        switch (ad)
        {

            case "admob":
                 AdmobShowBanner(relativeLayoutID);
                break;

            case "startapp":
                StartAppShowBanner(relativeLayoutID);
                break;

            case "facebook":
                FacebookShowBanner(relativeLayoutID);
                break;


            case "none":
                break;

            default:

                ShowBannerAd(defaultBannerAd, relativeLayoutID);
                break;

        }


    }




    //endregion

    //region FACEBOOK ADS
    public com.facebook.ads.InterstitialAd fbInterstitialAd;

    InterstitialAdListener fbListener =  new InterstitialAdListener() {
        @Override
        public void onError(com.facebook.ads.Ad ad, AdError adError) {

            Log.v("Facebook_ad_error", "yes" );


        }

        @Override
        public void onAdLoaded(com.facebook.ads.Ad ad) {

            Log.v("Facebook_ad_loaded", "yes" );

        }

        @Override
        public void onAdClicked(com.facebook.ads.Ad ad) {

        }

        @Override
        public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

        }

        @Override
        public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
            fbInterstitialAd.loadAd();
        }


    };


    public void FacebookInit()
    {
        fbInterstitialAd = new com.facebook.ads.InterstitialAd(this, facebookInterstitialID );
        fbInterstitialAd.setAdListener(fbListener);
        fbInterstitialAd.loadAd();

    }


    public void FacebookShowInterstitial(String errorAd)
    {

        if (fbInterstitialAd.isAdLoaded())
            fbInterstitialAd.show();
        else
            ShowAd(errorAd);


    }

    public void FacebookShowInterstitial()
    {
        FacebookShowInterstitial(defaultAd);

    }


    public void FacebookShowSplash()
    {
        FacebookShowSplash(defaultAd);

    }

    public void FacebookShowSplash(final String errorAd)
    {

        fbInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                ShowAd(errorAd);

            }

            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                fbInterstitialAd.show();
                fbInterstitialAd.setAdListener(fbListener);

            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {

            }

            @Override
            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                fbInterstitialAd.loadAd();
            }


        });
//        fbInterstitialAd.loadAd();

    }



    public void FacebookShowBanner(final int relativeLayoutID, final String adOnError)
    {

        com.facebook.ads.AdView fbBanner = new com.facebook.ads.AdView(this, facebookBannerID, com.facebook.ads.AdSize.BANNER_HEIGHT_50);

        RelativeLayout rl = (RelativeLayout)findViewById(relativeLayoutID);
        rl.addView(fbBanner);
        fbBanner.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(com.facebook.ads.Ad ad, AdError adError) {

                ShowBannerAd(adOnError, relativeLayoutID );

            }

            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {

            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {

            }
        });

        fbBanner.loadAd();

    }

    public void FacebookShowBanner(int relativeLayoutID )
    {
        FacebookShowBanner(relativeLayoutID, defaultBannerAd);
    }

    public void FacebookOnBack(final Class<?> activity)
    {
        if (fbInterstitialAd.isAdLoaded()) {
            fbInterstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onError(com.facebook.ads.Ad ad, AdError adError) {

                }

                @Override
                public void onAdLoaded(com.facebook.ads.Ad ad) {

                }

                @Override
                public void onAdClicked(com.facebook.ads.Ad ad) {

                }

                @Override
                public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(com.facebook.ads.Ad ad) {

                     Go(activity);
                }


            });
            fbInterstitialAd.show();
        }
        else
            ShowOnBackAd(defaultAd, activity);

    }

    public void FacebookOnBack()
    {
        if (fbInterstitialAd.isAdLoaded()) {
            fbInterstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onError(com.facebook.ads.Ad ad, AdError adError) {

                }

                @Override
                public void onAdLoaded(com.facebook.ads.Ad ad) {

                }

                @Override
                public void onAdClicked(com.facebook.ads.Ad ad) {

                }

                @Override
                public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                    finish();

                }


            });
            fbInterstitialAd.show();
        }
        else
            ShowOnBackAd(defaultAd);

    }


    //endregion

    //region ADMOB
    public void AdmobShowBanner(int relativeLayout) {

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(admobBannerID);
        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        RelativeLayout layout = (RelativeLayout)findViewById(relativeLayout);
        layout.addView(adView);
        AdRequest adRequest;

        if (admobTest)
            adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();

        else
            adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);


    }

    public InterstitialAd mInterstitial;


    public void AdmobShowInterstitial() {

        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(admobInterstitialID);

        if (mInterstitial.isLoaded()) {
            mInterstitial.show();
            AdmobLoadInterstitial();

        }
        else
        {
            AdmobLoadInterstitial();

            mInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    AdmobShowloadedInterstitial();
                    AdmobLoadInterstitial();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {

                }


            });


        }



    }

    public void AdmobNewInterstitial()
    {
        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(admobInterstitialID);
        mInterstitial.setAdListener(null);

    }

    public void AdmobLoadInterstitial() {


        AdmobNewInterstitial();

        AdRequest adRequest;
        if (admobTest)
            adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();

        else
            adRequest = new AdRequest.Builder().build();


        mInterstitial.loadAd(adRequest);
    }

    public void AdmobShowloadedInterstitial() {
        if (mInterstitial.isLoaded()) {
            mInterstitial.show();


        }
    }

    public void AdmobOnBack(final Class<?> activity)
    {

        if (mInterstitial.isLoaded())
        {
            mInterstitial.show();
            mInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    Go(activity);
                }
            });

        }
        else
            Go(activity);



    }


    public void AdmobOnBack()
    {

        if (mInterstitial.isLoaded())
        {
            mInterstitial.show();
            mInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    finish();
                }
            });

        }
        else
            finish();


    }



    //endregion

    //region STARTAPP
    public StartAppAd startAppAd;

    public void StartAppInit() {

        StartAppSDK.init(this, startAppID, false);
        startAppAd = new StartAppAd(this);

        StartAppLoadAds();

    }

    public void StartAppShowInterstitial() {

        if(startAppAd.isReady() )
        {
            StartAppShowAds();
            StartAppLoadAds();
        }
        else
            startAppAd.loadAd(new AdEventListener() {
                @Override
                public void onReceiveAd(Ad ad) {
                    startAppAd.showAd(); // show the ad
                    StartAppLoadAds();

                }

                @Override
                public void onFailedToReceiveAd(Ad ad) {

                }
            });


    }

    public void StartAppLoadAds() {
        startAppAd.loadAd();



    }

    public void StartAppShowAds() {
        startAppAd.showAd();


    }

    public void StartAppLoadVideo() {

        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO);

    }

    public void StartAppShowSplash() {


        StartAppAd.showSplash(this, null);


    }

    public void StartAppOnBack (final Class<?> activity)
    {

        if (!startAppAd.isNetworkAvailable())
            Go( activity );

        if (startAppAd.isReady())
            startAppAd.showAd(new AdDisplayListener() {
            @Override
            public void adHidden(Ad ad) {
                 Go( activity );
            }

            @Override
            public void adDisplayed(Ad ad) {

            }

            @Override
            public void adClicked(Ad ad) {

            }

            @Override
            public void adNotDisplayed(Ad ad) {

            }
        });
        else
            Go(activity);

    }


    public void StartAppOnBack ()
    {

        if (!startAppAd.isNetworkAvailable())
            finish();

        if (startAppAd.isReady())
            startAppAd.showAd(new AdDisplayListener() {
                @Override
                public void adHidden(Ad ad) {
                    finish();
                }

                @Override
                public void adDisplayed(Ad ad) {

                }

                @Override
                public void adClicked(Ad ad) {

                }

                @Override
                public void adNotDisplayed(Ad ad) {

                }
            });
        else
            finish();

    }


    public void StartAppShowBanner(int relativeLayoutID)
    {

        // Get the Main relative layout of the entire activity
        RelativeLayout mainLayout = (RelativeLayout)findViewById(relativeLayoutID);
        // Define StartApp Banner
        Banner3D startAppBanner = new Banner3D(this);

        RelativeLayout.LayoutParams bannerParameters =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
        bannerParameters.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        // Add to main Layout
        mainLayout.addView(startAppBanner, bannerParameters);

    }




    //endregion


    //region Events
    @Override
    public void onStart() {
        super.onStart();
        AnalyticStart();


    }

    @Override
    public void onStop() {
        super.onStop();
        AnalyticStop();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {

        super.onPause();


    }
    //endregion

    //region TOOLS 


    public void PrefInit()
    {

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();


    }




    public boolean Rate()
    {

        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(1) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .monitor();

        // Show a dialog if meets conditions
        return AppRate.showRateDialogIfMeetsConditions(this);


    }


    public void l(String key, String value )
    {
        Log.v(key, value);
    }

    public void l(String key, boolean value )
    {
        Log.v(key, value + "");
    }

    public void l(String key, int value )
    {
        Log.v(key, value + "");
    }


    public boolean isAppInstalled( String uri ) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void FacebookAnalyticInit()
    {

        FacebookSdk.sdkInitialize( getApplicationContext());
        AppEventsLogger.activateApp( getApplication());

    }

    public void AnalyticStart()
    {
        EasyTracker.getInstance(this).activityStart(this);
    }


    public void AnalyticStop()
    {
        EasyTracker.getInstance(this ).activityStop(this);  // Add this method.
    }

    public void Go(  Class<?> activity,  boolean isFinish )
    {

         startActivity(new Intent(this,  activity));
        if(isFinish) finish();


    }


    public void Go( Class<?> activity)
    {
        Go( activity, true);

    }




    public void Toast( String text , int duration    )
    {

        Toast.makeText(this, text,   duration  ).show();

    }

    public void Toast(String text   )
    {

        Toast(  text, Toast.LENGTH_SHORT);

    }


    public String RandomString(int length)
    {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random rng = new Random();


        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }


    public boolean TrueFalse()
    {

        Random r = new Random();

        return r.nextInt(2) == 1;


    }
    //endregion



}


