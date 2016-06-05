package com.brown.luke.overwatchchooseahero;

import com.brown.luke.overwatchchooseahero.ChooseAHero.ChooseAHero;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    // Field
    private String state;
    private ChooseAHeroView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.hide();
        }

        state = "ATTACK";
        view = (ChooseAHeroView) findViewById(R.id.canvas_layout);

        // Set background
        Random rand = new Random();
        View bg = findViewById(R.id.app_bg);
        if(bg != null) {
            switch (rand.nextInt(4) + 1) {
                case 1:
                    bg.setBackgroundResource(R.drawable.bg_1);
                    break;
                case 2:
                    bg.setBackgroundResource(R.drawable.bg_2);
                    break;
                case 3:
                    bg.setBackgroundResource(R.drawable.bg_3);
                    break;
                case 4:
                    bg.setBackgroundResource(R.drawable.bg_4);
                    break;
            }
        }


        // Set custom fonts
        TextView title = (TextView) findViewById(R.id.app_title);
        if(title != null) {
            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Futura.ttf");
            title.setTypeface(tf);
        }


        Button runButton = (Button) findViewById(R.id.start_btn);
        if(runButton != null) {
            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/BigNoodleTooOblique.ttf");
            runButton.setTypeface(tf);
        }


        // Load an ad into the AdMob banner view.
        final AdView adView = (AdView) findViewById(R.id.adView);
        if(adView != null) {
            AdRequest adRequest = new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build();
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    adView.setVisibility(View.VISIBLE);
                }
            });
            adView.loadAd(adRequest);
        }
    }

    public void run(View btn) {
        final ArrayList<String> rankedHeroes = ChooseAHero.run(view.getAllyTeam(), view.getEnemyTeam(), state);
        if(rankedHeroes != null) {
            view.updateOrder(rankedHeroes);
        }
    }

    public void reset(View btn) {
        final ArrayList<String> defaultOrder = new ArrayList<String>(Arrays.asList("genji", "mccree", "pharah",
                "reaper", "soldier76", "tracer",
                "bastion", "hanzo", "junkrat",
                "mei", "torbjorn", "widowmaker",
                "dva", "reinhardt", "roadhog",
                "winston", "zarya", "lucio",
                "mercy", "symmetra", "zenyatta"));
        view.updateOrder(defaultOrder);
    }

    public void changeState(View btn) {
        switch (state) {
            case "ATTACK":
                state = "DEFEND";
                break;
            case "DEFEND":
                state = "KOH";
                break;
            case "KOH":
                state = "ATTACK";
                break;
        }

        ((Button) btn).setText(state);
    }

}
