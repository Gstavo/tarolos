package com.flow.game.identities.identities.obstacle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Gustavo on 09/08/2015.
 */
public abstract class Obstacle {

    protected Vector2 startPoint;
    protected Vector2 currentPosition;
    protected Sprite sprite;

    public Obstacle(){}

    public Obstacle(Vector2 initialPosition){
        this.startPoint = new Vector2(initialPosition);
        currentPosition = new Vector2(initialPosition);
        loadSprite();
    }

    public abstract void loadSprite();

    // to be abstract
    public boolean pointColision(Vector2 p){return false;}

    public void draw(SpriteBatch batch){ sprite.draw(batch); }

    //public abstract void up-dateMotion();


}
