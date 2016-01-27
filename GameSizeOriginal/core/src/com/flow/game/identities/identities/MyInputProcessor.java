package com.flow.game.identities.identities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Gustavo on 17/07/2015.
 */
public class MyInputProcessor extends InputAdapter {

    public static final int NOT_PRESSED = 0;
    public static final int TOUCH_DOWN = 1;
    public static final int TOUCH_DRAGGED = 2;
    public static final int TOUCH_UP = 3;



    private int keyPressed = NOT_PRESSED;

    private Vector2 lastTouch;

    private ArrayList<Vector2> touchDownList = new ArrayList<Vector2>();

    private ArrayList<Vector2> touchDraggedList = new ArrayList<Vector2>();

    /*
        Screen coordinates are in pixel and origin is in the upper left corner

                AUto correct y = Gdx.graphics.getHeight() - screenY
         */

/*
                Camera screen

                               w
                --------------->
                |
                |
                |
                |
             h \/

*/

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

  //
            Vector2 touch = (new Vector2(screenX, Gdx.graphics.getHeight() - screenY)).sub(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);

            touchDownList.add(touch);

        if(keyPressed == NOT_PRESSED) {
            keyPressed = TOUCH_DOWN; this.lastTouch = touch;
        }

        return true;
      }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        Vector2 touch = (new Vector2(screenX, Gdx.graphics.getHeight() - screenY)).sub(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

        touchDraggedList.add(touch);

        // Priority to last touchDragged
        if(keyPressed == NOT_PRESSED || keyPressed == TOUCH_DOWN) {
            keyPressed = TOUCH_DRAGGED; this.lastTouch = touch;
            return true;
        }
        else return false;

    }

    // Touch up beats every input in priority
    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {

        keyPressed = TOUCH_UP;

        return true;
    }

    public void destroy(){

            this.lastTouch = Vector2.Zero;
            this.keyPressed = NOT_PRESSED;
            touchDownList = new ArrayList<Vector2>();
            touchDraggedList = new ArrayList<Vector2>();

    }

    public int getKeyPressed(){return keyPressed;}

    public Vector2 getLastTouch(){
        return lastTouch;
    }

    public Collection<Vector2> getTouchDownList(){ return this.touchDownList;}

    public void setKeyPressed(int keyPressed){this.keyPressed = keyPressed;}

    public void setLastTouch(Vector2 lastTouch){
        this.lastTouch = lastTouch;
    }




}
