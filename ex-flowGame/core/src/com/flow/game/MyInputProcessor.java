package com.flow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

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

        if(keyPressed == NOT_PRESSED) {
            lastTouch = (new Vector2(screenX, Gdx.graphics.getHeight() - screenY)).sub(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);

            keyPressed = TOUCH_DOWN;
            return true;
        }
        else return false;
        }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if(keyPressed == NOT_PRESSED) {

        lastTouch = (new Vector2(screenX, Gdx.graphics.getHeight() - screenY)).sub(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

        keyPressed = TOUCH_DRAGGED;
        return true;
        }
        else return false;

    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {

        /*
        if(keyPressed == NOT_PRESSED ) keyPressed = TOUCH_UP;
        else return false;

        return true;*/

        keyPressed = TOUCH_UP;
        return true;
    }

    public void destroy(){

            this.lastTouch = Vector2.Zero;
            this.keyPressed = NOT_PRESSED;

    }

    public int getKeyPressed(){return keyPressed;}

    public Vector2 getLastTouch(){
        return lastTouch;
    }

    public void setKeyPressed(int keyPressed){this.keyPressed = keyPressed;}

    public void setLastTouch(Vector2 lastTouch){
        this.lastTouch = lastTouch;
    }




}
