package com.brown.luke.overwatchchooseahero;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;


public class ChooseAHeroView extends View {
     // Fields
    private final ArrayList<SqEntity> heroOrder;
    private final ArrayList<HexEntity> allyTeam;
    private final ArrayList<HexEntity> enemyTeam;

    private final ArrayList<Entity> entities;
    private final ArrayList<HexEntity> hexEntities;

    private int currentIndex = -1;
    private float pokeX;
    private float pokeY;
    private final Paint paint;
    private final int FPS = 60;

    public ChooseAHeroView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Entity.setRes(getResources());
        paint = new Paint();

        heroOrder = new ArrayList<SqEntity>();
        heroOrder.add(new SqEntity("genji"));
        heroOrder.add(new SqEntity("mccree"));
        heroOrder.add(new SqEntity("pharah"));
        heroOrder.add(new SqEntity("reaper"));
        heroOrder.add(new SqEntity("soldier76"));
        heroOrder.add(new SqEntity("tracer"));
        heroOrder.add(new SqEntity("bastion"));
        heroOrder.add(new SqEntity("hanzo"));
        heroOrder.add(new SqEntity("junkrat"));
        heroOrder.add(new SqEntity("mei"));
        heroOrder.add(new SqEntity("torbjorn"));
        heroOrder.add(new SqEntity("widowmaker"));
        heroOrder.add(new SqEntity("dva"));
        heroOrder.add(new SqEntity("reinhardt"));
        heroOrder.add(new SqEntity("roadhog"));
        heroOrder.add(new SqEntity("winston"));
        heroOrder.add(new SqEntity("zarya"));
        heroOrder.add(new SqEntity("lucio"));
        heroOrder.add(new SqEntity("mercy"));
        heroOrder.add(new SqEntity("symmetra"));
        heroOrder.add(new SqEntity("zenyatta"));

        allyTeam = new ArrayList<HexEntity>();
        allyTeam.add(new HexEntity("empty"));
        allyTeam.add(new HexEntity("empty"));
        allyTeam.add(new HexEntity("empty"));
        allyTeam.add(new HexEntity("empty"));
        allyTeam.add(new HexEntity("empty"));

        enemyTeam = new ArrayList<HexEntity>();
        enemyTeam.add(new HexEntity("empty"));
        enemyTeam.add(new HexEntity("empty"));
        enemyTeam.add(new HexEntity("empty"));
        enemyTeam.add(new HexEntity("empty"));
        enemyTeam.add(new HexEntity("empty"));
        enemyTeam.add(new HexEntity("empty"));

        entities = new ArrayList<Entity>();
        hexEntities = new ArrayList<HexEntity>();
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

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if(heroOrder.get(0).getPos() == null) {
            setInitialPositions();
        }

        paint.setColor(Color.BLACK);
        for(Entity entity : entities) {
            entity.update(1000 / FPS);

            PointF pos = entity.getPos();
            final float x = pos.x;
            final float y = pos.y;
            final Bitmap bm = entity.getBitmap();
            final float w = bm.getHeight();
            final float h = bm.getHeight();

            if(entity.isHover()) {
                paint.setAlpha(167);
            }

            canvas.drawBitmap(bm, x, y, paint);
            paint.setAlpha(255);
        }

        if(currentIndex >= 0) {
            final Entity entity = heroOrder.get(currentIndex);
            final Bitmap bitmap = entity.getBitmap();
            canvas.drawBitmap(bitmap, pokeX - bitmap.getWidth() / 2, pokeY - bitmap.getHeight() / 2, paint);
        }

        this.postInvalidateDelayed(1000 / FPS);
    }

    private void setInitialPositions() {
        int ENTITY_SIZE = heroOrder.get(0).getBitmap().getWidth();
        int NUMBER_OF_ROWS = 5;

        int index = 0;
        int y = (this.getHeight() - ENTITY_SIZE * NUMBER_OF_ROWS) / 2;
        for(int i = 0; i < NUMBER_OF_ROWS; ++i) {
            int NUM_OF_HEROES = (i == 0 || i == NUMBER_OF_ROWS - 1) ? 3 : 5;
            int x = (this.getWidth() - NUM_OF_HEROES * ENTITY_SIZE) / 2;
            for(int j = 0; j < NUM_OF_HEROES; ++j) {
                heroOrder.get(index).setPos(new PointF(x, y));
                x += ENTITY_SIZE;
                ++index;
            }
            y += ENTITY_SIZE;
        }

        int ENTITY_WIDTH = allyTeam.get(0).getBitmap().getWidth();
        int ENTITY_HEIGHT = allyTeam.get(0).getBitmap().getHeight();

        int SPACING = 40;
        int START_X =  (getWidth() - (ENTITY_WIDTH * 3 + SPACING * 2)) / 2;
        for(int i = 1; i < allyTeam.size() - 1; ++i) {
            final HexEntity ally = allyTeam.get(i);
            final PointF pos = new PointF(START_X + (i - 1) * (ENTITY_WIDTH + SPACING), getHeight() - ENTITY_WIDTH - SPACING);
            ally.setPos(pos);
            addBorder("ally", ally);
        }
        allyTeam.get(0).setPos(new PointF(SPACING, getHeight() - 3 * ENTITY_HEIGHT / 2));
        addBorder("ally", allyTeam.get(0));
        allyTeam.get(allyTeam.size() - 1).setPos(new PointF(getWidth() - ENTITY_WIDTH - SPACING, getHeight() - 3 * ENTITY_HEIGHT / 2));
        addBorder("ally", allyTeam.get(allyTeam.size() - 1));

        SPACING = 25;
        START_X =  (getWidth() - (ENTITY_WIDTH * 4 + SPACING * 3)) / 2;
        for(int i = 1; i < enemyTeam.size() - 1; ++i) {
            final HexEntity enemy = enemyTeam.get(i);
            final PointF pos = new PointF(START_X + (i - 1) * (ENTITY_WIDTH + SPACING), SPACING);
            enemy.setPos(pos);
            addBorder("enemy", enemy);
        }
        enemyTeam.get(0).setPos(new PointF(SPACING, SPACING + 3 * ENTITY_HEIGHT / 4));
        addBorder("enemy", enemyTeam.get(0));
        enemyTeam.get(enemyTeam.size() - 1).setPos(new PointF(getWidth() - ENTITY_WIDTH - SPACING, SPACING + 3 * ENTITY_HEIGHT / 4));
        addBorder("enemy", enemyTeam.get(enemyTeam.size() - 1));
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event){
       if(isAnimating()) {
           return true;
       }

        final PointF poke = new PointF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for(int i = 0; i < heroOrder.size(); ++i) {
                    final Entity entity = heroOrder.get(i);

                    if(entity.contains(poke)) {
                        pokeX = event.getX();
                        pokeY = event.getY();
                        currentIndex = i;
                    }
                }
                return true;

            case MotionEvent.ACTION_UP:
                for(Entity entity : hexEntities) {
                    if(entity.contains(poke)) {
                        if(currentIndex == -1) {
                            entity.setImage("empty");
                        } else {
                            Entity current = this.heroOrder.get(currentIndex);
                            entity.setImage(current.getName());
                        }
                        break;
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

    private void addBorder(final String team, final HexEntity hex) {
        final Bitmap bitmap = hex.getBitmap();
        final HexEntity border = new HexEntity(team);
        final Bitmap borderBitmap = border.getBitmap();

        border.setPos(new PointF(hex.getPos().x - (borderBitmap.getWidth() - bitmap.getWidth()) / 2,
                                 hex.getPos().y - (borderBitmap.getHeight() - bitmap.getHeight()) / 2 + 1));
        entities.add(border);
    }

    public void updateOrder(final ArrayList<String> heroOrderStrings) {
        if(!isAnimating()) {
            ArrayList<SqEntity> newHeroOrder = new ArrayList<SqEntity>(heroOrder);

            for(int i = 0; i < heroOrder.size(); ++i) {
                SqEntity entity = heroOrder.get(i);
                int index = heroOrderStrings.indexOf(entity.getName());
                if (i != index) {
                    entity.setAnimators(heroOrder.get(index).getPos());
                    newHeroOrder.set(index, entity);
                }
            }

            for(int i = 0; i < heroOrder.size(); ++i) {
                heroOrder.set(i, newHeroOrder.get(i));
            }
        }
    }

    public ArrayList<String> getAllyTeam() {
        final ArrayList<String> allyTeamStrings = new ArrayList<String>();
        for(Entity entity : allyTeam) {
            String name = entity.getName();
            if(!name.equals("empty")) {
                allyTeamStrings.add(entity.getName());
            }
        }
        return allyTeamStrings;
    }

    public ArrayList<String> getEnemyTeam() {
        final ArrayList<String> enemyTeamStrings = new ArrayList<String>();
        for(Entity entity : enemyTeam) {
            String name = entity.getName();
            if(!name.equals("empty")) {
                enemyTeamStrings.add(entity.getName());
            }
        }
        return enemyTeamStrings;
    }

    public boolean isAnimating() {
        for(Entity entity : heroOrder) {
            if(entity.isAnimating()) {
                return true;
            }
        }

        return false;
    }

}
