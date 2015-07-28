package com.flow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.abs;

/**
 * Created by Gustavo on 18/07/2015.
 */
public class Player {

    private static final float RADIUS = 1;
    private static final float EXTRARADIUS = 2;

    private float speed = 3; // unidades / s

    private float extraSpeed;
    private float extraTimeSpeed;

    private Vector2 position;
    private Vector2 initialPosition;
    private Vector2 accumulatedMovement;
    private Vector2 direction;

    private boolean moving;


    private TiledMap tiledMap;

    TextureRegion textureRegion;
/*
    TextureMapObject playerObject;
    private Sprite wispMax;
    private Sprite wispMin;

    */
    private Sprite nucleo;

    private Sprite wisp;

    private float radius;
    private float extraRadius; // Quantidade extra de raio disponivel para controlar o wisp
    private final float maxNucleoRadius = 1.5f;
    private final float minNucleoRadius = 0.5f;

    private float nucleoRadius;


    private float unitScale = 1/16f;

    private boolean moved;

    Texture wispMaxTexture;

    Texture wispCurrent;

    Texture wispNucleo;

    public void draw(SpriteBatch batch)
    {

        wisp.setCenter(position.x, position.y);
        nucleo.setCenter(position.x, position.y);

        wisp.draw(batch);

        if(cycleTime < period ) {

                // int intensityRange = maxIntensity - minIntensity;
                cycleTime += Gdx.graphics.getDeltaTime();
                float time = period - cycleTime;

                //  float speed = (float)intensityRange / time;

                float speed = (float) (maxNucleoRadius - nucleoRadius) / time;

                if(!growing) speed = (float) (nucleoRadius - minNucleoRadius) / time;

                float f = 1 / Gdx.graphics.getDeltaTime();

                float deltaX;

                if (growing) deltaX = speed / f;
                else deltaX = -speed / f;

                nucleoRadius += deltaX;

        }
        else{
            if(growing) nucleoRadius = maxNucleoRadius; else nucleoRadius = minNucleoRadius;
            growing = !growing;
            cycleTime = 0;

        }

        nucleo.setSize(nucleoRadius,nucleoRadius);

        nucleo.draw(batch);


    }

    private int minIntensity;
    private int maxIntensity;
    private int ticks;
    private int maxTicks = 20;
    private boolean growing;
    private float cycleTime;
    private float period;

    public Player(TiledMap tMap, Vector2 position){
        this.moving = false;
        this.initialPosition = new Vector2(position);
        this.position = new Vector2(position);
        this.moved = false;

        extraSpeed = 0;
        extraTimeSpeed = 0;

        this.accumulatedMovement = new Vector2(Vector2.Zero);
        this.tiledMap = tMap;

        this.direction = new Vector2(Vector2.Zero);

        Float delta = Gdx.graphics.getDeltaTime();

        growing = false;
        cycleTime = 0;
        period = 1;

        nucleoRadius = 1.5f;

        radius = RADIUS;

        extraRadius = RADIUS + EXTRARADIUS;

        wispMaxTexture = new Texture("player/wisp_max.png");

        wispNucleo = new Texture("player/nucleo.png");

        nucleo = new Sprite(wispNucleo);

        wisp = new Sprite(wispMaxTexture,512,512);

        wisp.setSize(2,2);

    }

    public void playerDied(){
        this.position.set(initialPosition);
    }

    public boolean getMoving(){return moving;}

    public void setMoving(boolean moving){ this.moving = moving;}

    public Vector2 getDirection(){ return direction;}

    public Vector2 getPosition(){ return position;}

    public float getRadius(){ return radius;}

    public void updateDirection(Vector2 lastTouch) {
        this.direction.set(lastTouch).nor();
    }

    public void updateMotion() throws NoMovementException
    {
        if(direction.equals(Vector2.Zero)) throw new NoMovementException();

            else {

            this.moving = true;

            Vector2 velocity = new Vector2();
            Vector2 movement = new Vector2();

            velocity.set(direction).scl(speed + extraSpeed);
            movement.set(velocity).scl(Gdx.graphics.getDeltaTime());

            position.add(movement);

        }
    }

    public void updateSpeed(){

        if( extraSpeed > 0) {

            extraTimeSpeed-=Gdx.graphics.getDeltaTime();

            if(extraTimeSpeed <= 0) {
                extraTimeSpeed = 0; extraSpeed = 0;
            }
        }



    }

    public void speedRequest(Vector2 touch){

        /* circle eq (x - c1)^2 + (y - c2)^2 <= Radius */

        touch.sub(position);

        if( Math.sqrt(touch.x) + Math.sqrt(touch.y) <= this.radius)
        {
            this.extraSpeed = 2;
            this.extraTimeSpeed = 3;
        }

    }


}
