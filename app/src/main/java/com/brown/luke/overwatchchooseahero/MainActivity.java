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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

        state = "Attack";
        view = (ChooseAHeroView) findViewById(R.id.canvas_layout);

        // Set custom fonts
        Button runButton = (Button) findViewById(R.id.start_btn);
        if(runButton != null) {
            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/BigNoodleTooOblique.ttf");
            runButton.setTypeface(tf);
        }


        // Load an ad into the AdMob banner view.
        final AdView adView = (AdView) findViewById(R.id.adView);
        if(adView != null) {

            AdRequest adRequest = new AdRequest.Builder()
                    .setRequestAgent("android_studio:ad_template")
                    .addTestDevice("B355818CF4EB6EE16E798FC1DAE19E08")
                    .build();
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    adView.setVisibility(View.VISIBLE);
                }
            });
            adView.loadAd(adRequest);
        }

        final Spinner stateSpinner = (Spinner) findViewById(R.id.state_spinner);
        setUpSpinner(stateSpinner, getResources().getStringArray(R.array.states));
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner mapSpinner = (Spinner) findViewById(R.id.map_spinner);
        setUpSpinner(mapSpinner, getResources().getStringArray(R.array.maps));
    }

    private void setUpSpinner(final Spinner spinner, final String[] entries) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, entries) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Futura.ttf");
                ((TextView) v).setTypeface(externalFont);
                return v;
            }


            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Futura.ttf");
                ((TextView) v).setTypeface(externalFont);
                parent.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                return v;
            }
        };
        spinner.setAdapter(adapter);
    }


    public void run(View btn) {
        final ArrayList<String> rankedHeroes = ChooseAHero.run(view.getAllyTeam(), view.getEnemyTeam(), state);
        if(rankedHeroes != null) {
            view.updateOrder(rankedHeroes, false);
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
        view.updateOrder(defaultOrder, true);
    }

    public void trash(View btn) {
        reset(view);
        view.refresh();
    }

}
