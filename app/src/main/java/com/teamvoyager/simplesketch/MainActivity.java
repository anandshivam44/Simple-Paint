package com.teamvoyager.simplesketch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardedAd;


public class MainActivity extends AppCompatActivity implements SketchFragment.TriggerEvent {


    private RewardedAd rewardedAd;
    //    RewardedAdLoadCallback adLoadCallback;
    private InterstitialAd mInterstitialAd;

    AdListener adListener=new AdListener() {
        @Override
        public void onAdLoaded() {
//                mNextLevelButton.setEnabled(true);
            goToNextLevel();
            Intent intent = new Intent(MainActivity.this, SaveSketch.class);
            startActivity(intent);

        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
//                mNextLevelButton.setEnabled(true);

        }

        @Override
        public void onAdClosed() {
            // Proceed to the next level.
            goToNextLevel();
            Intent intent = new Intent(MainActivity.this, SaveSketch.class);
            startActivity(intent);
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SketchFragment()).commit();
        mInterstitialAd = new InterstitialAd(this);
        //test  app id         ca-app-pub-3940256099942544~3347511713
        //test ad id           ca-app-pub-3940256099942544/1033173712
        //final ad id      ca-app-pub-5098396899135570/4761505037
        mInterstitialAd.setAdUnitId("ca-app-pub-5098396899135570/4761505037");
        mInterstitialAd.setAdListener(adListener);
        loadInterstitial();
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocas) {
        super.onWindowFocusChanged(hasFocas);
        View decorView = getWindow().getDecorView();
        if(hasFocas) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    @Override
    public void showAds() {
        showInterstitial();
    }
//    private InterstitialAd newInterstitialAd() {
//        InterstitialAd interstitialAd = new InterstitialAd(this);
//        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
//        interstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
////                mNextLevelButton.setEnabled(true);
//                goToNextLevel();
//                Intent intent = new Intent(MainActivity.this, SaveSketch.class);
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
////                mNextLevelButton.setEnabled(true);
//                Intent intent = new Intent(MainActivity.this, SaveSketch.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Proceed to the next level.
//                goToNextLevel();
//                Intent intent = new Intent(MainActivity.this, SaveSketch.class);
//                startActivity(intent);
//            }
//        });
//        return interstitialAd;
//    }

    private void showInterstitial() {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Intent intent = new Intent(MainActivity.this, SaveSketch.class);
            startActivity(intent);
            goToNextLevel();
        }
    }

    private void loadInterstitial() {

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void goToNextLevel() {

//        mInterstitialAd = newInterstitialAd();
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        loadInterstitial();
    }




}