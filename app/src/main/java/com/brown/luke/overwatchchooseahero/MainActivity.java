package com.brown.luke.overwatchchooseahero;

import com.brown.luke.overwatchchooseahero.OWRecommend.Recommender;
import com.brown.luke.overwatchchooseahero.OWRecommend.HeroDB;
import com.brown.luke.overwatchchooseahero.OWRecommend.OnHeroesChangedListener;
import com.brown.luke.overwatchchooseahero.UI.CanvasView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Constants
    //-----------

    private static final int AD_ANIMATION_DURATION = 625;
    private static final int TUTORIAL_ANIMATION_DURATION = 350;

    // Static fields
    //--------------

    private static Resources res;
    private static String packageName;
    private static Point screenSize;

    // Constants
    //----------

    private final float DISABLED_ALPHA = 0.30f;
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
        res = getResources();
        packageName = getApplicationContext().getPackageName();
        Display display = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);

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
            view.setBars(findViewById(R.id.top_bar), findViewById(R.id.bottom_bar));
            view.setListener(listener);
        }

        runButton = (Button) findViewById(R.id.start_btn);

        resetButton = (ImageButton) findViewById(R.id.reset_btn);
        trashButton = (ImageButton) findViewById(R.id.trash_btn);

        nextButton = (Button) findViewById(R.id.next_btn);
        tutorialText = (TextView) findViewById(R.id.tutorial_text);

        disableButton(runButton);
        disableButton(resetButton);
        disableButton(trashButton);

        updateFont(nextButton, "fonts/BigNoodleTooOblique.ttf");
        updateFont(tutorialText, "fonts/BigNoodleToo.ttf");
        updateFont(findViewById(R.id.skip_btn), "fonts/BigNoodleToo.ttf");
        updateFont(findViewById(R.id.version_number), "fonts/BigNoodleToo.ttf");

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
                    if(adView.getVisibility() != View.VISIBLE) {
                        final View adProgress = findViewById(R.id.ad_progress);
                        if (adProgress != null) {
                            adProgress.setVisibility(View.INVISIBLE);
                        }
                        TranslateAnimation animation = new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
                        animation.setDuration(AD_ANIMATION_DURATION);
                        adView.setAnimation(animation);
                        adView.setVisibility(View.VISIBLE);
                        adView.animate();
                    }
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

        if (!sharedPref.getBoolean(TUTORIAL_SAVE_KEY, false)) {
            startTutorial(null);
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
            finishTutorial();
        } else {
            tutorialText.setText(tutorialMessages[currentTutorialIndex++]);
            if (currentTutorialIndex == tutorialMessages.length - 1) {
                nextButton.setText(getText(R.string.finish));
            }
        }
    }

    public void skipTutorial(final View view) {
        finishTutorial();
    }

    public void startTutorial(final View view) {
        final View tutorialBackground = findViewById(R.id.tutorial_background);
        if(tutorialBackground != null) {
            AlphaAnimation animation = new AlphaAnimation(0, 1);
            animation.setDuration(TUTORIAL_ANIMATION_DURATION);
            tutorialBackground.setAnimation(animation);
            tutorialBackground.setVisibility(View.VISIBLE);
            tutorialBackground.animate();
        }
        currentTutorialIndex = 0;
        tutorialText.setText(getResources().getStringArray(R.array.tutorialMessages)[currentTutorialIndex]);
    }

    public void nop(final View view) {
    }


    // Helpers
    //--------

    private void setUpSpinner(final Spinner spinner, final String[] entries) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, entries) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                updateFont(v, "fonts/Futura.ttf");
                return v;
            }


            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                updateFont(v, "fonts/Futura.ttf");
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
            updateFont(button, "fonts/BigNoodleTooOblique.ttf");
        }
    }

    private void disableButton(final View button) {
        button.setEnabled(false);
        button.setAlpha(DISABLED_ALPHA);

        if (button == runButton) {
            updateFont(button, "fonts/BigNoodleToo.ttf");
        }
    }

    private void finishTutorial() {
        sharedPrefEditor.putBoolean(TUTORIAL_SAVE_KEY, true);
        sharedPrefEditor.commit();
        final View tutorialBackground = findViewById(R.id.tutorial_background);
        if(tutorialBackground != null) {
            AlphaAnimation animation = new AlphaAnimation(1, 0);
            animation.setDuration(TUTORIAL_ANIMATION_DURATION);
            tutorialBackground.setAnimation(animation);
            tutorialBackground.setVisibility(View.GONE);
            tutorialBackground.animate();
        }
    }

    private void updateFont(final View view, final String font) {
        Typeface tf = Typeface.createFromAsset(getAssets(), font);

        if(view != null) {
            ((TextView) view).setTypeface(tf);
        }
    }


    // Static methods
    //---------------

    public static Resources getRes() {
        return res;
    }

    public static String getPName() {
       return packageName;
    }

    public static Point getScreenSize() {
        return screenSize;
    }
}
