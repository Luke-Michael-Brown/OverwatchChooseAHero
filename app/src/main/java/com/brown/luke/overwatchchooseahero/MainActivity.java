package com.brown.luke.overwatchchooseahero;

import com.brown.luke.overwatchchooseahero.OWRecommend.Hero;
import com.brown.luke.overwatchchooseahero.OWRecommend.Recommender;
import com.brown.luke.overwatchchooseahero.OWRecommend.HeroDB;
import com.brown.luke.overwatchchooseahero.OWRecommend.OnHeroesChangedListener;
import com.brown.luke.overwatchchooseahero.OWRecommend.Stage;
import com.brown.luke.overwatchchooseahero.UI.CanvasView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    // Constants
    //----------

    private final float DISABLED_ALPHA = 0.5f;
    private final String TUTORIAL_SAVE_KEY = "TUTORIAL_SAVE_KEY";


    // Fields
    //-------

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedPrefEditor;

    private String subMap;
    private String stage;
    private CanvasView view;
    private boolean isDefaultOrder = true;

    private Button runButton;
    private ImageButton resetButton;
    private ImageButton trashButton;

    private Button nextButton;
    private TextView tutorialText;
    private byte currentTutorialIndex;


    // Main method
    //-------------

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HeroDB.load(getResources());
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

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();

        subMap = "Attack";
        stage = "Hanamura";
        subMap = null;
        view = (CanvasView) findViewById(R.id.canvas_layout);
        if (view != null) {
            view.setListener(listener);
            view.setEnabled(false);
        }

        runButton = (Button) findViewById(R.id.start_btn);

        resetButton = (ImageButton) findViewById(R.id.reset_btn);
        trashButton = (ImageButton) findViewById(R.id.trash_btn);

        nextButton = (Button) findViewById(R.id.next_btn);
        tutorialText = (TextView) findViewById(R.id.tutorial_text);
        currentTutorialIndex = 0;
        tutorialText.setText(getResources().getStringArray(R.array.tutorialMessages)[currentTutorialIndex]);

        disableButton(runButton);
        disableButton(resetButton);
        disableButton(trashButton);

        // Set custom fonts
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/BigNoodleTooOblique.ttf");
        if(nextButton != null) {
            nextButton.setTypeface(tf);
        }

        if(tutorialText != null) {
            tf = Typeface.createFromAsset(getAssets(), "fonts/BigNoodleToo.ttf");
            tutorialText.setTypeface(tf);
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
                    subMap = parent.getItemAtPosition(position).toString();
                    if(!isDefaultOrder) {
                        enableButton(runButton);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        final Spinner mapSpinner = (Spinner) findViewById(R.id.map_spinner);
        if(mapSpinner != null) {
            setUpSpinner(mapSpinner, getResources().getStringArray(R.array.maps));
            mapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    stage = parent.getItemAtPosition(position).toString();

                    ArrayList<String> mapsTemp = HeroDB.getStageNameMap().get(stage).getSubMaps();
                    String[] subMaps;
                    subMaps = new String[mapsTemp.size()];
                    subMaps = mapsTemp.toArray(subMaps);
                    subMap = subMaps[0];

                    setUpSpinner(stateSpinner, subMaps);
                    if(!isDefaultOrder) {
                        enableButton(runButton);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        // Add button pressed states
        addButtonPressedState(runButton);
        addButtonPressedState(resetButton);
        addButtonPressedState(trashButton);

        if (sharedPref.getBoolean(TUTORIAL_SAVE_KEY, false)) {
            finishTutorial();
        }
    }


    // Button methods
    //----------------

    public void run(final View btn) {
        final ArrayList<String> rankedHeroes = Recommender.run(view.getAllyTeam(), view.getEnemyTeam(), stage, subMap);
        if(rankedHeroes != null) {
            view.updateOrder(rankedHeroes, false);
        }

        isDefaultOrder = false;
        disableButton(runButton);
        enableButton(resetButton);
        enableButton(trashButton);
    }

    public void reset(final View btn) {
        view.updateOrder(HeroDB.getDefaultOrder(), true);

        isDefaultOrder = true;
        enableButton(runButton);
        disableButton(resetButton);
    }


    public void trash(final View btn) {
        reset(view);
        view.refresh();

        isDefaultOrder = true;
        disableButton(runButton);
        disableButton(resetButton);
        disableButton(trashButton);
    }

    public void nextTutorial(final View btn) {
        final String[] tutorialMessages = getResources().getStringArray(R.array.tutorialMessages);
        if (currentTutorialIndex == tutorialMessages.length - 1) {
            sharedPrefEditor.putBoolean(TUTORIAL_SAVE_KEY, true);
            finishTutorial();
        } else {
            tutorialText.setText(tutorialMessages[currentTutorialIndex++]);
            if (currentTutorialIndex == tutorialMessages.length - 1) {
                nextButton.setText(getText(R.string.finish));
            }
        }
    }


    // Helpers
    //--------

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

    private void enableButton(final View button) {
        button.setEnabled(true);
        button.setAlpha(1);

        if (button == runButton) {
            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/BigNoodleTooOblique.ttf");
            ((Button) button).setTypeface(tf);
        }
    }

    private void disableButton(final View button) {
        button.setEnabled(false);
        button.setAlpha(DISABLED_ALPHA);

        if (button == runButton) {
            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/BigNoodleToo.ttf");
            ((Button) button).setTypeface(tf);
        }
    }

    private void finishTutorial() {
        findViewById(R.id.tutorial_background).setVisibility(View.GONE);
        view.setEnabled(true);
    }
}
