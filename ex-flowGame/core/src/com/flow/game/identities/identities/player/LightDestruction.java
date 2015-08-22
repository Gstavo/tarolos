package com.flow.game.identities.identities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.WorldCell;
import com.flow.game.identities.identities.WorldMap;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Gustavo on 09/08/2015.
 */
public class LightDestruction {

    public float extraRadius;

    private Vector2 center;

    private float destructionRadius;

    private float nonDestructionRadius;

    private float destructionRate;

    // Energy spendings
    private float consumptionRate;

    private Sound cellDeadSound;

    private Sound destructionSound;
    private boolean active;

    public float getDestructionRate(){ return destructionRate; }

    public void setPosition(Vector2 p){ center = p;}

    public void updatePlayerRadius(float playerRadius){
        nonDestructionRadius = playerRadius;
        extraRadius = 0.8f*playerRadius;
        destructionRadius = extraRadius + nonDestructionRadius;
    }

    public void setDestructionRate(float r){ destructionRate = r; }

    public LightDestruction(Vector2 position, float playerRadius){
        center = position;
        updatePlayerRadius(playerRadius);
        destructionRate = 1; // hp/s
        consumptionRate = 2.5f * destructionRate; // 3/4 At average speed , energy output = -1 with 4 damage

        destructionSound = Gdx.audio.newSound( Gdx.files.internal("audio/near_wall.wav") );
        cellDeadSound = Gdx.audio.newSound( Gdx.files.internal("audio/destroy_wall.wav") );
    }

    // returns dtotal damage to targets
    public float destroyTargets(WorldMap worldMap){

        float delta = Gdx.graphics.getDeltaTime();
        float tolaDamage = 0f;
        Collection<WorldCell> cells = worldMap.getCells( playerCollisionPoints() );

        for( WorldCell worldCell : cells )  {
            float potentialDamage = destructionRate * delta;
            if( worldCell.getCollision() == true ) {
                tolaDamage +=worldCell.damage(potentialDamage, worldMap.getDestructionTile());
                if(worldCell.getHitPoints() <= 0) {
                    cellDeadSound.play(0.8f);
                }
            }
            }

        if(tolaDamage > 0) active = true; else active = false;
        return tolaDamage;
    }

    public void updateSound(){
  //      if(active) //TODO
    }

    public float getConsumption(float damage){
        return damage * consumptionRate;
    }

    private Collection<Vector2> playerCollisionPoints() {

            int circleCheck = 16;
            int deepCheck = 10;

            float alfaDelta = (float)(2 * Math.PI )/ circleCheck;
            float destructionDelta = (destructionRadius - nonDestructionRadius) / deepCheck;

            ArrayList<Vector2> destructionPoints = new ArrayList<Vector2>();

            for(float l = destructionRadius ;l > nonDestructionRadius; l-=destructionDelta)
                for (float alfa = 0; alfa <= 2 * Math.PI; alfa += alfaDelta) {

                    float x = (float) Math.cos(alfa) * l;
                    float y = (float) Math.sin(alfa) * l;
                    Vector2 point = new Vector2(center).add(x,y);
                    destructionPoints.add(point);
                }
            return destructionPoints;
    }
}
