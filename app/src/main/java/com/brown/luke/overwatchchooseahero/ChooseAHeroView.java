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


public class ChooseAHeroView extends View {
     // Fields
    private ArrayList<Entity> heroOrder;
    private ArrayList<Entity> allyTeam;
    private ArrayList<Entity> enemyTeam;

    private int currentIndex = -1;
    private float pokeX;
    private float pokeY;
    private Paint paint;
    private int FPS = 60;

    public ChooseAHeroView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();

        heroOrder = new ArrayList<Entity>();
        heroOrder.add(new Entity("genji", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("mcCree", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("pharah", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("reaper", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("soldier76", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("tracer", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("bastion", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("hanzo", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("junkrat", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("mei", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("torbjorn", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("widowmaker", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("dva", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("reinhardt", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("roadhog", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("winston", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("zarya", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("lucio", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("mercy", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("symmetra", EntityType.SQ, 1.5f, getResources()));
        heroOrder.add(new Entity("zenyatta", EntityType.SQ, 1.5f, getResources()));

        allyTeam = new ArrayList<Entity>();
        allyTeam.add(new Entity("empty", EntityType.HEX, 1, getResources()));
        allyTeam.add(new Entity("empty", EntityType.HEX, 1, getResources()));
        allyTeam.add(new Entity("empty", EntityType.HEX, 1, getResources()));
        allyTeam.add(new Entity("empty", EntityType.HEX, 1, getResources()));
        allyTeam.add(new Entity("empty", EntityType.HEX, 1, getResources()));

        enemyTeam = new ArrayList<Entity>();
        enemyTeam.add(new Entity("empty", EntityType.HEX, 1, getResources()));
        enemyTeam.add(new Entity("empty", EntityType.HEX, 1, getResources()));
        enemyTeam.add(new Entity("empty", EntityType.HEX, 1, getResources()));
        enemyTeam.add(new Entity("empty", EntityType.HEX, 1, getResources()));
        enemyTeam.add(new Entity("empty", EntityType.HEX, 1, getResources()));
        enemyTeam.add(new Entity("empty", EntityType.HEX, 1, getResources()));

        // start the animation:
        this.postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(heroOrder.get(0).getPos() == null) {
            resetPositions();
        }

        paint.setColor(Color.BLACK);
        for(Entity entity : heroOrder) {
            PointF pos = entity.getPos();
            final float x = pos.x;
            final float y = pos.y;
            final Bitmap bm = entity.getBitmap();
            final float w = bm.getHeight();
            final float h = bm.getHeight();

            canvas.drawBitmap(bm, x, y, paint);
        }

        //canvas.drawBitmap(allyTeam.get(1).getBitmap(), allyTeam.get(1).getPos().x, allyTeam.get(1).getPos().y, paint);
        for(Entity entity : allyTeam) {
            PointF pos = entity.getPos();
            final float x = pos.x;
            final float y = pos.y;
            final Bitmap bm = entity.getBitmap();
            final float w = bm.getHeight();
            final float h = bm.getHeight();

            canvas.drawBitmap(bm, x, y, paint);
        }

        for(Entity entity : enemyTeam) {
            PointF pos = entity.getPos();
            final float x = pos.x;
            final float y = pos.y;
            final Bitmap bm = entity.getBitmap();
            final float w = bm.getHeight();
            final float h = bm.getHeight();

            canvas.drawBitmap(bm, x, y, paint);
        }

        if(currentIndex >= 0) {
            final Entity entity = heroOrder.get(currentIndex);
            final Bitmap bitmap = entity.getBitmap();
            canvas.drawBitmap(bitmap, pokeX - bitmap.getWidth() / 2, pokeY - bitmap.getHeight() / 2, paint);
        }

        this.postInvalidateDelayed(1000 / FPS);
    }

    public void resetPositions() {
        int ENTITY_SIZE = heroOrder.get(0).getBitmap().getWidth();
        int NUMBER_OF_ROWS = 5;
        int MIN_NUMBER_OF_HERO_PER_ROW = 3;
        int MAX_NUMBER_OF_HERO_PER_ROW = 5;

        int SPACING_X = (this.getWidth() - MAX_NUMBER_OF_HERO_PER_ROW * ENTITY_SIZE) / (MAX_NUMBER_OF_HERO_PER_ROW + 1);
        int SPACING_Y = SPACING_X / 2;

        int index = 0;
        for(int i = 0; i < NUMBER_OF_ROWS; ++i) {
            final int NUM_OF_HEROES_PER_ROW = (i == 0 || i == NUMBER_OF_ROWS - 1) ? MIN_NUMBER_OF_HERO_PER_ROW : MAX_NUMBER_OF_HERO_PER_ROW;
            final int START_X = (i == 0 || i == NUMBER_OF_ROWS - 1) ?
                    (MAX_NUMBER_OF_HERO_PER_ROW - MIN_NUMBER_OF_HERO_PER_ROW - 1) * ENTITY_SIZE + (MAX_NUMBER_OF_HERO_PER_ROW - MIN_NUMBER_OF_HERO_PER_ROW) * SPACING_X :
                    SPACING_X;

            int y = (this.getHeight() - ENTITY_SIZE * NUMBER_OF_ROWS - (NUMBER_OF_ROWS - 1) * SPACING_Y) / 2 +
                    i * (ENTITY_SIZE + SPACING_Y);
            for(int j = 0 ; j < NUM_OF_HEROES_PER_ROW; ++j) {
                int x = START_X + j * (ENTITY_SIZE + SPACING_X);
                heroOrder.get(index).setPos(new PointF(x, y));
                ++index;
            }
        }

        int ENTITY_WIDTH = allyTeam.get(0).getBitmap().getWidth();
        int ENTITY_HEIGHT = allyTeam.get(0).getBitmap().getHeight();

        int SPACING = 30;
        int START_X =  (getWidth() - (ENTITY_WIDTH * 3 + SPACING * 2)) / 2;
        for(int i = 1; i < allyTeam.size() - 1; ++i) {
            PointF pos = new PointF(START_X + (i - 1) * (ENTITY_WIDTH + SPACING), getHeight() - ENTITY_WIDTH - SPACING);
            allyTeam.get(i).setPos(pos);
        }
        allyTeam.get(0).setPos(new PointF(SPACING, getHeight() - 3 * ENTITY_HEIGHT / 2));
        allyTeam.get(allyTeam.size() - 1).setPos(new PointF(getWidth() - ENTITY_WIDTH - SPACING, getHeight() - 3 * ENTITY_HEIGHT / 2));

        SPACING = 15;
        START_X =  (getWidth() - (ENTITY_WIDTH * 4 + SPACING * 3)) / 2;
        for(int i = 1; i < enemyTeam.size() - 1; ++i) {
            PointF pos = new PointF(START_X + (i - 1) * (ENTITY_WIDTH + SPACING), SPACING);
            enemyTeam.get(i).setPos(pos);
        }
        enemyTeam.get(0).setPos(new PointF(SPACING, SPACING + 3 * ENTITY_HEIGHT / 4));
        enemyTeam.get(enemyTeam.size() - 1).setPos(new PointF(getWidth() - ENTITY_WIDTH - SPACING, SPACING + 3 * ENTITY_HEIGHT / 4));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
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
                for(Entity entity : allyTeam) {
                    if(entity.contains(poke)) {
                        if(currentIndex == -1) {
                            entity.setImage("empty", 1, getResources());
                        } else {
                            Entity current = this.heroOrder.get(currentIndex);
                            entity.setImage(current.getName(), 1, getResources());
                        }
                        break;
                    }
                }
                for(Entity entity : enemyTeam) {
                    if(entity.contains(poke)) {
                        if(currentIndex == -1) {
                            entity.setImage("empty", 1, getResources());
                        } else {
                            Entity current = this.heroOrder.get(currentIndex);
                            entity.setImage(current.getName(), 1, getResources());
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
                return true;

        }

        return false;
    }

    public ArrayList<String> getAllyTeam() {
        ArrayList<String> allyTeamStrings = new ArrayList<String>();
        for(Entity entity : allyTeam) {
            String name = entity.getName();
            if(name.equals("empty")) {
                allyTeamStrings.add(entity.getName());
            }
        }
        return allyTeamStrings;
    }

    public ArrayList<String> getEnemyTeam() {
        ArrayList<String> enemyTeamStrings = new ArrayList<String>();
        for(Entity entity : enemyTeam) {
            String name = entity.getName();
            if(name.equals("empty")) {
                enemyTeamStrings.add(entity.getName());
            }
        }
        return enemyTeamStrings;
    }

}
