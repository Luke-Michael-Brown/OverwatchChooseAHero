package com.brown.luke.overwatchchooseahero.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.brown.luke.overwatchchooseahero.MainActivity;
import com.brown.luke.overwatchchooseahero.OWRecommend.HeroDB;
import com.brown.luke.overwatchchooseahero.OWRecommend.OnHeroesChangedListener;

import java.util.ArrayList;


public class CanvasView extends View {
    // Constants
    //----------

    private final byte FPS = 30;
    private final byte TEAM_SIZE = 6;
    private final byte NUMBER_OF_ROWS = 4;
    private final byte[] NUMBER_OF_COLUMNS = {6, 6, 5, 5};


    // Fields
    //--------

    private final ArrayList<SqEntity> heroOrder = new ArrayList<>();
    private final ArrayList<HexEntity> allyTeam = new ArrayList<>();
    private final ArrayList<HexEntity> enemyTeam = new ArrayList<>();

    private final ArrayList<Entity> entities =  new ArrayList<>();
    private final ArrayList<HexEntity> hexEntities = new ArrayList<>();

    private OnHeroesChangedListener listener;
    private final Paint paint;

    private byte numberOfHeroesSelected = 0;
    private byte currentIndex = -1;
    private float pokeX = -1;
    private float pokeY = -1;


    // Constructor
    //------------

    public CanvasView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();

        for(String name : HeroDB.getDefaultOrder()) {
            heroOrder.add(new SqEntity(name));
        }

        for(byte i = 0; i < TEAM_SIZE - 1; ++i) {
            allyTeam.add(new HexEntity("empty"));
        }

        for(byte i = 0; i < TEAM_SIZE; ++i) {
            enemyTeam.add(new HexEntity("empty"));
        }

        for(Entity entity : heroOrder) {
            entities.add(entity);
        }
        for(HexEntity entity : allyTeam) {
            entities.add(entity);
            hexEntities.add(entity);
        }
        for(HexEntity entity : enemyTeam) {
            entities.add(entity);
            hexEntities.add(entity);
        }

        // start the animation:
        this.postInvalidate();
    }


    // Main methods
    //--------------

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if(heroOrder.get(0).getPos() == null) {
            setInitialPositions();
        }

        paint.setColor(Color.BLACK);
        for(Entity entity : entities) {
            PointF pos = entity.getPos();
            if(pos != null) {
                final float x = pos.x;
                final float y = pos.y;

                entity.update(1000 / FPS);

                final Bitmap bm = entity.getBitmap();
                if (entity.isHover()) {
                    paint.setAlpha(167);
                }

                canvas.drawBitmap(bm, x, y, paint);
                paint.setAlpha(255);
            }
        }

        if(currentIndex >= 0) {
            final Entity entity = heroOrder.get(currentIndex);
            final Bitmap bitmap = entity.getBitmap();
            canvas.drawBitmap(bitmap, pokeX - bitmap.getWidth() / 2, pokeY - bitmap.getHeight() / 2, paint);
        }

        this.postInvalidateDelayed(1000 / FPS);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event){
       if(isAnimating()) {
           return true;
       }

        final PointF poke = new PointF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for(byte i = 0; i < heroOrder.size(); ++i) {
                    final Entity entity = heroOrder.get(i);

                    if(entity.contains(poke)) {
                        pokeX = event.getX();
                        pokeY = event.getY();
                        currentIndex = i;
                        return true;
                    }
                }
                return true;

            case MotionEvent.ACTION_UP:
                for(Entity entity : hexEntities) {
                    if(entity.contains(poke)) {
                        if(currentIndex == -1) {
                            entity.setImage("empty");
                            --numberOfHeroesSelected;
                            listener.heroRemoved();
                            if(numberOfHeroesSelected == 0) {
                                listener.allHeroesRemoved();
                            }
                        } else {
                            Entity current = this.heroOrder.get(currentIndex);
                            entity.setImage(current.getName());
                            listener.heroAdded();
                            ++numberOfHeroesSelected;
                        }
                        currentIndex = -1;
                        return true;
                    }
                }

                if (currentIndex != -1) {
                    if(pokeY <= this.getWidth() / 4) {
                        for(HexEntity enemy : enemyTeam) {
                            if (enemy.isEmpty()) {
                                Entity current = this.heroOrder.get(currentIndex);
                                enemy.setImage(current.getName());
                                listener.heroAdded();
                                ++numberOfHeroesSelected;
                                currentIndex = -1;
                                return true;
                            }
                        }
                    }
                    if(pokeY >= 3 * this.getWidth() / 4) {
                        for(HexEntity ally : allyTeam) {
                            if (ally.isEmpty()) {
                                Entity current = this.heroOrder.get(currentIndex);
                                ally.setImage(current.getName());
                                listener.heroAdded();
                                ++numberOfHeroesSelected;
                                currentIndex = -1;
                                return true;
                            }
                        }
                    }
                }


            case MotionEvent.ACTION_CANCEL:
                currentIndex = -1;
                return true;

            case MotionEvent.ACTION_MOVE:
                pokeX = poke.x;
                pokeY = poke.y;
                for(HexEntity hexEntity : hexEntities) {
                    if(hexEntity.contains(poke) && currentIndex != -1) {
                        hexEntity.setHoverBitmap(heroOrder.get(currentIndex).getName());
                    } else {
                        hexEntity.removeHoverBitmap();
                    }
                }
                return true;

        }

        return false;
    }


    // Setters
    //---------

    public void setListener(final OnHeroesChangedListener listener) {
        this.listener = listener;
    }

    private void setInitialPositions() {
        final int HEX_WIDTH = allyTeam.get(0).getBitmap().getWidth();
        final int HEX_HEIGHT = allyTeam.get(0).getBitmap().getHeight();

        // Ally team
        float X_SPACING = 40 * MainActivity.getScreenSize().x / 768;
        float Y_SPACING = 10 * MainActivity.getScreenSize().x / 768;
        final float allyBarHeight = HEX_HEIGHT + 4 * Y_SPACING;
        float x = (getWidth() - (HEX_WIDTH * 3 + X_SPACING * 2)) / 2;
        float y = getHeight() - HEX_HEIGHT - Y_SPACING;
        for(byte i = 1; i < allyTeam.size() - 1; ++i) {
            final HexEntity ally = allyTeam.get(i);
            final PointF pos = new PointF(x + (i - 1) * (HEX_WIDTH + X_SPACING), y);
            ally.setPos(pos);
            addBorder("ally", ally);
        }
        allyTeam.get(0).setPos(new PointF(X_SPACING, getHeight() - allyBarHeight));
        addBorder("ally", allyTeam.get(0));
        allyTeam.get(allyTeam.size() - 1).setPos(new PointF(getWidth() - HEX_WIDTH - Y_SPACING, getHeight() - allyBarHeight));
        addBorder("ally", allyTeam.get(allyTeam.size() - 1));

        // Enemy team
        X_SPACING = 25 * MainActivity.getScreenSize().x / 768;
        final float enemyBarHeight = HEX_HEIGHT + 11 * Y_SPACING;
        x = (getWidth() - (HEX_WIDTH * 4 + X_SPACING * 3)) / 2;
        y = Y_SPACING;
        for(byte i = 1; i < enemyTeam.size() - 1; ++i) {
            final HexEntity enemy = enemyTeam.get(i);
            final PointF pos = new PointF(x + (i - 1) * (HEX_WIDTH + X_SPACING), y);
            enemy.setPos(pos);
            addBorder("enemy", enemy);
        }
        enemyTeam.get(0).setPos(new PointF(X_SPACING, enemyBarHeight - HEX_HEIGHT));
        addBorder("enemy", enemyTeam.get(0));
        enemyTeam.get(enemyTeam.size() - 1).setPos(new PointF(getWidth() - HEX_WIDTH - X_SPACING, enemyBarHeight - HEX_HEIGHT));
        addBorder("enemy", enemyTeam.get(enemyTeam.size() - 1));

        // Portraits
        final int SQ_SIZE = heroOrder.get(0).getBitmap().getWidth();
        byte index = 0;
        y = (this.getHeight() - SQ_SIZE * NUMBER_OF_ROWS - allyBarHeight - enemyBarHeight) / 2 + enemyBarHeight;
        for(byte i = 0; i < NUMBER_OF_ROWS; ++i) {
            byte NUM_OF_HEROES = NUMBER_OF_COLUMNS[i];
            x = (this.getWidth() - NUM_OF_HEROES * SQ_SIZE) / 2;
            for(byte j = 0; j < NUM_OF_HEROES; ++j) {
                heroOrder.get(index).setPos(new PointF(x, y));
                x += SQ_SIZE;
                ++index;
            }
            y += SQ_SIZE;
        }
    }

    // Public methods
    //----------------

    public void updateOrder(final ArrayList<String> heroOrderStrings, final boolean reset) {
        if(!isAnimating()) {
            ArrayList<SqEntity> newHeroOrder = new ArrayList<>(heroOrder);

            for(byte i = 0; i < heroOrder.size(); ++i) {
                SqEntity entity = heroOrder.get(i);
                byte index = (byte) heroOrderStrings.indexOf(entity.getName());
                if (i != index) {
                    if(!reset) {
                        entity.setAnimators(heroOrder.get(index).getPos());
                    }

                    newHeroOrder.set(index, entity);
                }
            }

            for(byte i = 0; i < heroOrder.size(); ++i) {
                heroOrder.set(i, newHeroOrder.get(i));
            }

            if(reset) {
                setInitialPositions();
            }
        }
    }

    public void refresh() {
        for(HexEntity entity : hexEntities) {
            entity.setImage("empty");
        }
        numberOfHeroesSelected = 0;
    }

    public ArrayList<String> getAllyTeam() {
        final ArrayList<String> allyTeamStrings = new ArrayList<>();
        for(Entity entity : allyTeam) {
            String name = entity.getName();
            if(!name.equals("empty")) {
                allyTeamStrings.add(entity.getName());
            }
        }
        return allyTeamStrings;
    }

    public ArrayList<String> getEnemyTeam() {
        final ArrayList<String> enemyTeamStrings = new ArrayList<>();
        for(Entity entity : enemyTeam) {
            String name = entity.getName();
            if(!name.equals("empty")) {
                enemyTeamStrings.add(entity.getName());
            }
        }
        return enemyTeamStrings;
    }


    // Helpers
    //----------

    private boolean isAnimating() {
        for(Entity entity : heroOrder) {
            if(entity.isAnimating()) {
                return true;
            }
        }

        return false;
    }

    private void addBorder(final String team, final HexEntity hex) {
        final Bitmap bitmap = hex.getBitmap();
        final HexEntity border = new HexEntity(team);
        final Bitmap borderBitmap = border.getBitmap();

        border.setPos(new PointF(hex.getPos().x - (borderBitmap.getWidth() - bitmap.getWidth()) / 2,
                hex.getPos().y - (borderBitmap.getHeight() - bitmap.getHeight()) / 2 + 1));
        entities.add(border);
    }
}
