package com.flow.game.identities.identities.player;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Created by Gustavo on 18/08/2015.
 */
public class EnergyMotor{

    public static float INITIAL_CAPACITY = 100;
    public static float WARNING_10 = 10f;

    private float activationTime;
    private boolean disabled;

    private float initialRegenerationRate;
    private float regenerationRate;
    private float regenerationBonus;

    private float cineticEnergyConsumptionRate;

    private float energy;

    private ProgressBar energyBar;


    public EnergyMotor(Player p){
        activationTime = 0;
        disabled = false;
        regenerationRate = 5f; //  u/s
        initialRegenerationRate = regenerationRate;
        energy = INITIAL_CAPACITY;
        cineticEnergyConsumptionRate = 1.5f; // ENERGY/WORLDDISTANCE max speed 3 max  spend rate
        regenerationBonus = 0;
        energyBar = new ProgressBar(p,INITIAL_CAPACITY);
      //  energyBarLoad(p);
    }
/*
    public void energyBarLoad(Player p){
        Vector2 position = new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        float radius =      2 * p.getTextureRadius() * (1/FlowGame.UNITSCALE);
        position.sub(radius, radius);
        Vector2 size = new Vector2(2*radius,0.2f);
/*
        Pixmap pixmap = new Pixmap((int)size.x,(int)size.y, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.YELLOW);
        pixmap.fill();
*/
    /*
        Skin skin = new Skin();
        skin.add("yellow", new Texture(Gdx.files.internal("player/energybar.png")));

        TextureRegionDrawable textureBar = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("player/energybar.png"))));
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("yellow", Color.YELLOW), textureBar);
        barStyle.knobBefore = barStyle.knob;


        energyBar = new ProgressBar(0,INITIAL_CAPACITY,0.1f,false,barStyle);

        //energyBar.setPosition(position.x, position.y);
        energyBar.setPosition(position.x, position.y);
        energyBar.setSize(600, energyBar.getPrefHeight());
        energyBar.setValue(INITIAL_CAPACITY);
        //energyBar.setAnimateDuration(5);
    }
*/
    public void updateEnergyBar(){
        energy = energy > 100 ? 100 : energy;
        energy = energy < 0 ? 0 : energy;
        // clamped inside aswell
        energyBar.setProgress(energy);
    }

    // Returns energy left
    public float updateMotor(float delta,float consumptionRate){

        if(!disabled) {
            energy = ( energy < 0) ? 0 : energy;
            energy = energy + (regenerationRate - consumptionRate) * delta;
            activationTime+=delta;
        }

        if(energy > INITIAL_CAPACITY){
            if(regenerationBonus != 0)resetRegeneration();
            energy = INITIAL_CAPACITY;
        }
        updateEnergyBar();
        return energy;
    }

    public void draw(SpriteBatch batch,Player p){
        energyBar.setProgress(energy);
        energyBar.updatePositon(p);
        energyBar.draw(batch);
    }
    public float movementConsumption(float delta,float speed){
        float consumption = cineticEnergyConsumptionRate*(delta*speed);
        energy-= consumption;
        return consumption;
    }

    public void setRegenerationBonusRate(float bonus){
        regenerationRate*=bonus;
        regenerationBonus = bonus;
    }

    public void resetRegeneration(){
        regenerationRate = initialRegenerationRate;
        regenerationBonus = 0;
    }

    public void subEnergy(float d){
        energy-=d;
    }
    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public boolean getWarning10(){ return energy < WARNING_10;}

    public void dispose(){
        energyBar.dispose();
    }
}
