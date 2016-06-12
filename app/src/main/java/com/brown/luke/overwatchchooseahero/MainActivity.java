package com.brown.luke.overwatchchooseahero;

import com.brown.luke.overwatchchooseahero.ChooseAHero.ChooseAHero;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
    private boolean isDefaultOrder = true;

    private Button runButton;
    private ImageButton resetButton;
    private ImageButton trashButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.hide();
        }

        final OnHeroesChangedListener listener = new OnHeroesChangedListener() {
            @Override
            public void heroAdded() {
                enableButton(runButton);
                enableButton(trashButton);
            }

            @Override
            public void heroRemoved() {
                enableButton(runButton);
            }

            @Override
            public void allHeroesRemoved() {
                disableButton(runButton);
                if(isDefaultOrder) {
                    disableButton(trashButton);
                }
            }
        };

        state = "Attack";
        view = (ChooseAHeroView) findViewById(R.id.canvas_layout);
        if (view != null) {
            view.setListener(listener);
        }

        runButton = (Button) findViewById(R.id.start_btn);
        resetButton = (ImageButton) findViewById(R.id.reset_btn);
        trashButton = (ImageButton) findViewById(R.id.trash_btn);

        disableButton(runButton);
        disableButton(resetButton);
        disableButton(trashButton);

        // Set custom fonts
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
        if(stateSpinner != null) {
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
        }

        final Spinner mapSpinner = (Spinner) findViewById(R.id.map_spinner);
        if(mapSpinner != null) {
            setUpSpinner(mapSpinner, getResources().getStringArray(R.array.maps));
        }

        // Add button pressed states
        addButtonPressedState(runButton);
        addButtonPressedState(resetButton);
        addButtonPressedState(trashButton);
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
                View v = super.getDropDownView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Futura.ttf");
                ((TextView) v).setTypeface(externalFont);
                //parent.setBackgroundColor(getResources().getColor(R.color.darkBlue));
                return v;
            }
        };
        spinner.setAdapter(adapter);
    }

    private void addButtonPressedState(final View button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setScaleX(0.8f);
                        v.setScaleY(0.8f);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.setScaleX(1);
                        v.setScaleY(1);
                        break;
                }
                return false;
            }
        });
    }

    public void enableButton(View button) {
        button.setEnabled(true);
        button.setAlpha(1);
    }

    public void disableButton(View button) {
        button.setEnabled(false);
        button.setAlpha(0.5f);
    }

    public void run(View btn) {
        final ArrayList<String> rankedHeroes = ChooseAHero.run(view.getAllyTeam(), view.getEnemyTeam(), state);
        if(rankedHeroes != null) {
            view.updateOrder(rankedHeroes, false);
        }

        isDefaultOrder = false;
        disableButton(runButton);
        enableButton(resetButton);
        enableButton(trashButton);
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

        isDefaultOrder = true;
        enableButton(runButton);
        disableButton(resetButton);
    }


    public void trash(View btn) {
        reset(view);
        view.refresh();

        isDefaultOrder = true;
        disableButton(runButton);
        disableButton(resetButton);
        disableButton(trashButton);
    }
}
