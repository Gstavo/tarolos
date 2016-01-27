package com.flow.game.identities.identities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.player.Player;
import com.flow.game.identities.identities.obstacle.Obstacle;
import com.flow.game.identities.identities.runes.Rune;


import java.lang.Object;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Gustavo on 09/08/2015.
 */
public class WorldMap {

    private WorldCell[][] worldMap; // [x][y]
    private int height;
    private int width;

    private ArrayList<?extends Obstacle> obstacles;

    private float worldToLayersRatio;
    private Player player;

    private TreeSet<WorldCell> collisionCells;
    private TiledMapTile destructionTile;

    private HashMap<String,Object> collisionObjects;
    private ArrayList<Rune> runes;

    public TiledMapTile getDestructionTile(){ return destructionTile; }

    public WorldMap(MapLayers layers,ArrayList<?extends Obstacle> obstacles,ArrayList<Rune> runes, TiledMapTile destructionTile, int worldWidth,int worldHeight){

        this.destructionTile = destructionTile;
        this.runes = runes;

        TiledMapTileLayer collision = (TiledMapTileLayer)layers.get("COLLISION");
        TiledMapTileLayer path = (TiledMapTileLayer)layers.get("PATH");

        collision.getHeight(); collision.getWidth();

        int layersWidth = collision.getWidth();
        int layersHeight = collision.getHeight();

        this.height = layersWidth;
        this.width = layersHeight;

        worldToLayersRatio = (float)layersWidth / worldWidth;

        //collision.getTileHeight(); collision.getTileWidth();

        worldMap = new WorldCell[layersWidth][layersHeight];

        for(int w = 0; w < layersWidth; w++)
            for (int h = 0; h < layersHeight; h++) {

                TiledMapTileLayer.Cell collisionCell = collision.getCell(w, h);
                if (collisionCell != null) worldMap[w][h] = new WorldCell(collisionCell, true);
                else {
                    TiledMapTileLayer.Cell pathCell = path.getCell(w, h);
                    if (pathCell != null) worldMap[w][h] = new WorldCell(pathCell, false);
                }
            }

        this.obstacles = obstacles;

    }

    public void draw(SpriteBatch batch){
        for(Rune r : runes)
            r.draw(batch);
        if(obstacles!=null)
        for(Obstacle o : this.obstacles)
            o.draw(batch);
    }

    // Standard equals working -> Tiled cells dont get copied
    public Collection<WorldCell> getCells(Collection<Vector2> points){
        HashSet<WorldCell> nub = new HashSet<WorldCell>();
        for(Vector2 p : points) {
            Vector2 pl = new Vector2(p).scl(worldToLayersRatio);
            if(pl.x <= width && pl.x >= 0 && pl.y <= height && pl.y >= 0) {
                WorldCell cell = this.worldMap[((int) pl.x)][((int) pl.y)];
                nub.add(cell);
            }
        }
        return nub;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public boolean playerCollision(){

        boolean colided = false;

      //  this.collisionCells = new TreeSet<WorldCell>();

        this.collisionObjects = new HashMap<String,Object>();

        Vector2 position = new Vector2(player.getPosition());

        for(Vector2 point : playerColisionPoints()) {

            Vector2 collisionPoint = new Vector2(point);
            Vector2 collisionCalculus = new Vector2().set(collisionPoint).sub(position);

            float playerRadius = player.getRadius();
            if (Math.pow(collisionCalculus.x, 2) + Math.pow(collisionCalculus.y, 2) <= Math.pow(playerRadius, 2))
            {
               //colilisionPoint must be in worldUnits
                WorldCell cell = getWorldCell(collisionPoint);
                if(cell.getCollision())
                    colided = true;
            }

            if(obstacles != null)
            for( Obstacle o : obstacles) {
                String name = o.getClass().getSimpleName();
                if (o.pointColision(collisionPoint) && !collisionObjects.containsKey(name))
                    collisionObjects.put(name,o);
            }

            for( Rune rune : runes)
                if(rune.getActivated() && rune.pointColision(collisionPoint)) rune.playerEffect(player);

        }

        //colided = !collisionCells.isEmpty() || !collisionObjects.isEmpty();

        return colided;
    }

    public WorldCell getWorldCell(Vector2 worldPosition){
        Vector2 mapPosition = worldPosition.scl(worldToLayersRatio);

        WorldCell worldCell = worldMap[(int)mapPosition.x][(int)mapPosition.y];

        return worldCell;

    }

    public TiledMapTileLayer.Cell getTiledCell(Vector2 worldPosition){
        Vector2 mapPosition = worldPosition.scl(worldToLayersRatio);

        WorldCell worldCell = worldMap[(int)mapPosition.x][(int)mapPosition.y];

        return worldCell.getTiledCell();

    }

    public boolean worldCellCollision(Collection<Vector2> collisionPoints){
        boolean collision = false;
        Iterator<Vector2> it = collisionPoints.iterator();
        while (it.hasNext() && !collision)
            if( getWorldCell(it.next()).getCollision() ) collision = true;
        return collision;
    }

    public Collection<Vector2> playerColisionPoints() {

        int nPositions = 16;
        float alfaDelta = (2 * (float) Math.PI / nPositions);
        float playerRadius = player.getRadius();

        ArrayList<Vector2> playerColisionPoints = new ArrayList<Vector2>();

        for (float alfa = 0; alfa <= 2 * Math.PI; alfa += alfaDelta) {

            float x = (float) Math.cos(alfa) * playerRadius;

            float y = (float) Math.sin(alfa) * playerRadius;

            Vector2 playerPosition = new Vector2(player.getPosition());

            Vector2 possibleCollision = new Vector2(playerPosition.add(x, y));

            playerColisionPoints.add(possibleCollision);

        }

        return playerColisionPoints;
    }

}
