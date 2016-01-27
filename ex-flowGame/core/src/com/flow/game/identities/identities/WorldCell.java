package com.flow.game.identities.identities;

import com.badlogic.gdx.maps.tiled.TiledMapTile;

import java.io.Serializable;

/**
 * Created by Gustavo on 10/08/2015.
 */
public class WorldCell implements Serializable {

    private int type;
    private float hitPoints;
    // To add texture

    public WorldCell(int t){
        this.type = t;
        this.hitPoints = 2;
    }

    public WorldCell(WorldCell worldCell){
/*
        this.tiledCell = new TiledMapTileLayer.Cell();
        tiledCell.setTile( worldCell.getTiledCell().getTile() );
*/
        this.type = worldCell.getType();
        this.hitPoints = worldCell.getHitPoints();
    }

    public int getType(){ return type; }
    public boolean getCollision() {return type==1 ;}
  //  public TiledMapTileLayer.Cell getTiledCell() {return tiledCell;}
    public float getHitPoints() {return hitPoints;}

    public float damage(float hp,TiledMapTile tile){
        float damage = hp;
        this.hitPoints-=hp;
        if(hitPoints <= 0) {
    //        tiledCell.setTile(tile);
            killCell();
            damage-=hitPoints; // removes damage on dead
        }
        return damage;
    }

    public void killCell(){
        type = WorldMap.PATH_TILE;
        hitPoints = 0;
    }

/*
    public boolean equals(Object o){
        if(this == o) return true;
        boolean r = false;
        if( o!=null && o.getClass() == this.getClass()){
            WorldCell cell = (WorldCell)o;
            r = (collision == cell.getCollision()) && ( tiledCell.equals(cell.getTiledCell()) && hitPoints == cell.getHitPoints() );
        }
        return r;
    }
*/
}
