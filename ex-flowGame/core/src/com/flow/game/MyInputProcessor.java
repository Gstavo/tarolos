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
//    private Vector2 delta;

    /*
        Screen coordinates are in pixel and origin is in the upper left corner

                AUto correct y = Gdx.graphics.getHeight() - screenY
         */

    //  cam.viewportWidth
    public Vector2 vconvertWorld(float unitScale){

            return new Vector2(lastTouch.x * unitScale, lastTouch.y * unitScale);

    }

    public Vector2 vconvertWorld(Vector2 camScale){

        return lastTouch.scl(camScale);

    }

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
        Vector2 newTouch = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
        // delta will now hold the difference between the last and the current touch positions
        // delta.x > 0 means the touch moved to the right, delta.x < 0 means a move to the left
  //      delta = newTouch.cpy().sub(lastTouch);
        lastTouch = newTouch.sub(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);

        keyPressed = TOUCH_DRAGGED;
        return true;
        }
        else return false;

    }

    public int getKeyPressed(){return keyPressed;}

    public Vector2 getLastTouch(){
        return lastTouch;
    }
/*
    public Vector2 getDelta(){
        return delta;
    }
*/
    public void setKeyPressed(int keyPressed){this.keyPressed = keyPressed;}

    public void setLastTouch(Vector2 lastTouch){
        this.lastTouch = lastTouch;
    }
/*
    public void setDelta(Vector2 delta){
        this.delta = delta;
    }
*/

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {

        if(keyPressed == NOT_PRESSED) keyPressed = TOUCH_UP;
            else return false;

        return true;
    }

    public void destroy(){
        lastTouch = Vector2.Zero;
        keyPressed = NOT_PRESSED;
    }

}
