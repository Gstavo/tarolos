package com.flow.game.identities.identities.player;

import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.WorldMap;

/**
 * Created by Gustavo on 12/08/2015.
 */
public class BlastExplosion {

    private WorldMap worldMap;

    private boolean activated;

    private Vector2 camDimensions;

    public BlastExplosion(WorldMap worldMap,Vector2 camDimensions){
        this.worldMap = worldMap;
        this.camDimensions = new Vector2(camDimensions);
    }

    public boolean getActivated(){ return activated; }
    public void setActivated(boolean b){ activated = b; }

    public void explode(Player player){
        Vector2 p = new Vector2(player.getPosition());
        Vector2 l = new Vector2(camDimensions).scl(0.5f);
        Vector2 min = new Vector2(p).sub(l);
        Vector2 max = new Vector2(p).add(l);

        for(float w = min.x ; w < max.x ; w++)
            for(float h = min.y; h < max.y;h++){
                worldMap.getWorldCell(new Vector2(w,h)).killCell();
                //TODO REMOVE COLISION TILES
            }

    }
}
