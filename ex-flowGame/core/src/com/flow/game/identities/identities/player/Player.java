package com.flow.game.identities.identities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.World;
import com.flow.game.identities.identities.runes.Rune;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by Gustavo on 18/07/2015.
 */
public class Player {

    private static final float EXTRARADIUS = 2;

    public static float playerBaseSpeed = 8f;

    private float playerSpeed;
    private float speed = 2; // unidades / s
    private float extraSpeed;
    private float extraTimeSpeed;
    private boolean moving;
    private float noMovementTime;
    private float delta;

    private Vector2 position;
    private Vector2 initialPosition;
    private Vector2 direction;
    private Vector2 camDimensions;


    /**
     *  r 0.5 -> 10 camW
     *      r*x -> 30 camW
     *      x = 1.5f
     *
     *      textureradius =
     */
    private float camResize = 1.5f;
    private Sprite wisp;
    public static final float RADIUS = 0.5f;
    private float textureRadius = 0.8f*camResize;
    private float radius;
    private final float initMaxNucleoRadius = 0.8f * camResize;
    private final float initMinNucleoRadius = 0.2f * camResize;

    private Sprite nucleo;
    private float nucleoRadius;
    private float maxNucleoRadius;
    private float minNucleoRadius;

    Texture wispMaxTexture;
    Texture wispCurrent;
    Texture wispNucleo;

    // Abilites

    LightDestruction lightDestruction;
    BlastExplosion blastExplosion;

    // Motor

    EnergyMotor motor;

    // Animation
    private boolean growing;
    private float cycleTime;
    private float period;

    //Runes
    private ArrayList<Rune> activeRunes;
/*
    public void soundLaunch(){
        wispSound.loop();
    }
*/
    public Player(TiledMap tMap,World world){
        this.moving = false;

        this.position = world.getStartPlace();
        this.initialPosition = new Vector2(position);

        // Old speed 2 -> 32
        // new speed s -> 512 s =32
        playerSpeed = speed = playerBaseSpeed;
        extraSpeed = 0;
        extraTimeSpeed = 0;

        this.direction = new Vector2(Vector2.Zero);
        this.activeRunes = new ArrayList<Rune>();

        growing = false;
        cycleTime = 0;
        period = 1;

        nucleoRadius =initMaxNucleoRadius;

        radius = RADIUS;

        wispMaxTexture = new Texture("player/wisp_max.png");

        wispNucleo = new Texture("player/nucleo.png");

        nucleo = new Sprite(wispNucleo);
        maxNucleoRadius = initMaxNucleoRadius;
        minNucleoRadius = initMinNucleoRadius;

        wisp = new Sprite(wispMaxTexture);
        wisp.setSize(2*textureRadius,2*textureRadius);

        this.motor = new EnergyMotor(this);

    }
/*
    public void dispose(){
        wispSound.dispose();;
    }
*/
    public void resetNucleo() {
        maxNucleoRadius = initMaxNucleoRadius;
        minNucleoRadius = initMinNucleoRadius;
        textureRadius = maxNucleoRadius;
        radius = RADIUS;
        resetNucleoCycle();
    }

    public void resetNucleoCycle() {
        growing = false;
        cycleTime = 0;
        period = 1;
        nucleoRadius = maxNucleoRadius;
    }


    public void updateNucleoSize(float ratio){
        maxNucleoRadius*=ratio;
        minNucleoRadius*=ratio;
        resetNucleoCycle();
    }

    public void updateRadius(float r){
        float ratio = r/radius;
        radius = r;
        textureRadius = (0.8f / 0.5f) * r;
        updateNucleoSize(ratio);
    }
    public void draw(SpriteBatch batch)
    {
        wisp.setCenter(position.x, position.y);
        nucleo.setCenter(position.x, position.y);

        wisp.setSize(2 * textureRadius, 2 * textureRadius);
        wisp.draw(batch);

        motor.draw(batch,this);

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

        if(nucleoRadius > 0) nucleo.setSize(2*nucleoRadius,2*nucleoRadius);
            else nucleo.setSize(0.1f,0.1f);

        nucleo.draw(batch);
    }

    public void playerDied(){
        position = new Vector2(initialPosition);
    }

    public void setCamDimensions(Vector2 camDimensions) { this.camDimensions = camDimensions; }
    public void setPosition(Vector2 p) {position = new Vector2(p);}

    public boolean getMoving(){return moving;}

    public void setMoving(boolean moving){ this.moving = moving;}

    public Vector2 getDirection(){ return direction;}

    public Vector2 getPosition(){ return position;}

    public float getRadius(){ return radius;}

    public float getTextureRadius(){return textureRadius;}

    public void setRadius(float r){ radius = r;}

    public EnergyMotor getMotor(){ return motor; }

    public void updateDirection(Vector2 lastTouch) {
        this.direction.set(lastTouch).nor();
    }
    private float deaceleration;

    public void stopMovement(){
        noMovementTime = 0f;
        moving = false;

        playerSpeed = (speed + extraSpeed) * (motor.getEnergy()/100f);
        deaceleration = (0 - playerSpeed) / 0.3f;
    }

    public void updateMotion()
    {
        delta = Gdx.graphics.getDeltaTime();

        Vector2 velocity = new Vector2();
        Vector2 movement = new Vector2();

        playerSpeed = speed + extraSpeed;
        playerSpeed*=motor.getEnergy()/100f;

        if(moving == false) {

            noMovementTime += (delta > 0.3f ) ? 0.1f : delta;
            //v = a*t + vi
            playerSpeed = deaceleration * noMovementTime + playerSpeed;

            playerSpeed = ( playerSpeed < 0) ? 0 : playerSpeed;

        } else {

            motor.movementConsumption(delta,speed * (motor.getEnergy()/100f) );

        }
        velocity.set(direction).scl(playerSpeed);
        movement.set(velocity).scl(delta);
        position.add(movement);

    }


    public void setNoMovementTime(float t){ noMovementTime = t;}
    public void setExtraSpeed(float speed){ this.extraSpeed = speed; }
    public void setExtraTimeSpeed(float time){ this.extraTimeSpeed = time; }

    public void updateSpeed(){

        if( extraSpeed > 0) {

            extraTimeSpeed-=Gdx.graphics.getDeltaTime();
            if(extraTimeSpeed <= 0) {
                extraTimeSpeed= 0; extraSpeed = 0;
            }
        }
    }


    public boolean speedRequest(Vector2 touch2){

        /* circle eq (x - c1)^2 + (y - c2)^2 <= Radius */

        boolean r = false;
        Vector2 touch = new Vector2(touch2);

        if( Math.pow(touch.x,2) + Math.pow(touch.y,2) <= this.textureRadius * textureRadius )
        {
            this.extraSpeed = 4; // used 4
            this.extraTimeSpeed = 3;
            r = true;
        }
        return r;
    }

    public void update(){
        motor.updateMotor(delta,0f);
    }


    public LightDestruction getLightDestruction(){ return lightDestruction; }

    public void initAbilities(World world){
        this.lightDestruction = new LightDestruction( position , radius);
        this.blastExplosion = new BlastExplosion(world,camDimensions);
    }

    public void updateAbilities(World world){

        updateLightDestruction(world);

    }

    public void blastDemand(){ blastExplosion.explode(this);}

    public void updateLightDestruction(World world){
        //needed cuz tp
        lightDestruction.setPosition(position);
        //needed cuz sizeRune
        lightDestruction.updatePlayerRadius(radius);
        float damage = world.playerLightCollision(lightDestruction);
        float consumption = lightDestruction.getConsumption(damage);
        motor.subEnergy(consumption);
    }

    public void addRune(Rune r){ activeRunes.add(r);}

    public void removeActivatedRune(Rune r){ activeRunes.remove(r); }

    public void updateRuneStatus(float delta){

        if(!activeRunes.isEmpty()) {
            for (Rune r : new HashSet<Rune>(activeRunes)) {
                r.updateEffect(this, delta);
            }

        }

    }

    public int countRuneEffect(Rune r){

        int c = 0;
        for(Rune runeActivated : activeRunes)
            if( runeActivated.getClass().equals(r.getClass()) ) c++;
        return c;
    }
}
