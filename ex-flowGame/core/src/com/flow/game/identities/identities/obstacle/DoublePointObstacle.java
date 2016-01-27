package com.flow.game.identities.identities.obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Gustavo on 09/08/2015.
 */
public class DoublePointObstacle extends Obstacle {

    private Vector2 endPoint;
    private float speed;

    // True = Moving to endPoint
    private boolean positiveDirection;

    public DoublePointObstacle(Vector2 startPoint,Vector2 endPoint) {
        super(startPoint);
        this.endPoint = new Vector2(endPoint);
        positiveDirection = true;
        this.speed = 2;
    }

    public void updateMotion(){
          float delta = Gdx.graphics.getDeltaTime();
          Vector2 direction = new Vector2();
          Vector2 velocity = new Vector2();

          if(positiveDirection) {
              direction.set(endPoint).sub(currentPosition).nor();
          } else
                direction.set(startPoint).sub(currentPosition).nor();

          velocity.set(direction).scl(speed);
          this.currentPosition.add(velocity.scl(delta));
    }

    public void loadSprite(){}

    public void draw(SpriteBatch batch){}

}
