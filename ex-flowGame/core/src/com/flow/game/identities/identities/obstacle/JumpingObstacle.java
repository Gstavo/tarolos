package com.flow.game.identities.identities.obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.WorldMap;

import java.util.Collection;
import java.util.Random;

/**
 * Created by Gustavo on 15/08/2015.
 */
public class JumpingObstacle extends Obstacle{

    private Vector2 direction;
    private float speed;

    public JumpingObstacle(Vector2 initialPosition){
        super(initialPosition);
        Random random = new Random();
        direction = new Vector2(random.nextFloat(),random.nextFloat()).nor();

    }

    public void updateMotion(WorldMap worldMap){
        Collection<Vector2> cPoints = collisionPoints();
        if( worldMap.worldCellCollision( cPoints ) ){
            directionChange();
        }

        float delta = Gdx.graphics.getDeltaTime();

        Vector2 velocity = new Vector2(direction).scl(speed);
        this.currentPosition.add(velocity.scl(delta));
    }

    private void directionChange(){
        direction.rotate(45);
    }

    public void loadSprite(){}

    public Collection<Vector2> collisionPoints() {return null;}

}
