package com.flow.game.identities.identities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by Gustavo on 10/08/2015.
 */
public class WorldCell {

    private TiledMapTileLayer.Cell tiledCell;
    private boolean collision;
    private float hitPoints;

    public WorldCell(TiledMapTileLayer.Cell tiledCell, boolean collision){
        this.tiledCell = tiledCell;
        this.collision = collision;
        this.hitPoints = 2;
    }

    public boolean getCollision() {return collision;}
    public TiledMapTileLayer.Cell getTiledCell() {return tiledCell;}
    public float getHitPoints() {return hitPoints;}

    public float damage(float hp,TiledMapTile tile){
        float damage = hp;
        this.hitPoints-=hp;
        if(hitPoints <= 0) {
            tiledCell.setTile(tile);
            killCell();
            damage-=hitPoints; // removes damage on dead
        }
        return damage;
    }

    public void killCell(){

        collision =false;
    }


    public boolean equals(Object o){
        if(this == o) return true;
        boolean r = false;
        if( o!=null && o.getClass() == this.getClass()){
            WorldCell cell = (WorldCell)o;
            r = (collision == cell.getCollision()) && ( tiledCell.equals(cell.getTiledCell()) && hitPoints == cell.getHitPoints() );
        }
        return r;
    }

}
