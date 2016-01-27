package com.flow.game.identities.identities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.player.LightDestruction;
import com.flow.game.identities.identities.player.Player;
import com.flow.game.identities.identities.obstacle.Obstacle;
import com.flow.game.identities.identities.runes.DestructionRune;
import com.flow.game.identities.identities.runes.EnergyRune;
import com.flow.game.identities.identities.runes.Rune;
import com.flow.game.identities.identities.runes.RuneIntel;
import com.flow.game.identities.identities.runes.SizeRune;
import com.flow.game.identities.identities.runes.SpeedRune;
import com.flow.game.identities.identities.runes.TeleportRune;


import java.lang.Object;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/**
 * Created by Gustavo on 09/08/2015.
 */
public class World {

    private WorldMap map;
    private int height;
    private int width;

    private ArrayList<?extends Obstacle> obstacles;

    private float worldToLayersRatio;
    private Player player;

    private TreeSet<WorldCell> collisionCells;
    private TiledMapTile destructionTile;
    private int tileSize;

    private HashMap<String,Object> collisionObjects;
    private HashSet<Rune> runes;

    private Vector2 startPlace;
    private Vector2 endPlace;

    public void setMap(WorldMap map){ this.map = map; }
    public Vector2 getStartPlace(){ return startPlace;}
    public TiledMapTile getDestructionTile(){ return destructionTile; }

    public World(MapLayers layers, TiledMapTileSets tileSets, ArrayList<Vector2> startpo, ArrayList<Vector2> endpo,
                 ArrayList<? extends Obstacle> obstacles,
                 float unitScale, float worldSize){

        Random rnd = new Random();
        int p = rnd.nextInt( startpo.size() );
        //startPlace =new Vector2( startpo.get(p));
        startPlace = new Vector2(startpo.get(0));

        p = rnd.nextInt(endpo.size());
        endPlace = new Vector2(endpo.get(4));
        runes = new HashSet<Rune>();

        for(MapObject o :layers.get("SPEEDRUNE").getObjects() ){
            MapProperties end = o.getProperties();
            Vector2 pos = new Vector2((Float) end.get("x"), (Float) end.get("y")).scl(unitScale);

         //   if( rnd.nextFloat() < 0.85f)
                runes.add( new SpeedRune(pos) );
        }

        for(MapObject o: layers.get("ENERGYRUNE").getObjects() ){
            MapProperties end = o.getProperties();
            Vector2 pos = new Vector2((Float) end.get("x"), (Float) end.get("y")).scl(unitScale);

           // if( rnd.nextFloat() < 0.90f)
                runes.add(new EnergyRune(pos));
        }

        for(MapObject o :layers.get("SMALLSIZERUNE").getObjects() ){
            MapProperties end = o.getProperties();
            Vector2 pos = new Vector2((Float) end.get("x"), (Float) end.get("y")).scl(unitScale);

            runes.add(new SizeRune(pos,false));
        }

        for(MapObject o: layers.get("BIGSIZERUNE").getObjects() ){
            MapProperties end = o.getProperties();
            Vector2 pos = new Vector2((Float) end.get("x"), (Float) end.get("y")).scl(unitScale);

         //   if( rnd.nextFloat() < 0.70f)
                runes.add(new SizeRune(pos,true));
        }

        for(MapObject o :layers.get("DESTRUCTIONRUNE").getObjects() ){
            MapProperties end = o.getProperties();
            Vector2 pos = new Vector2((Float) end.get("x"), (Float) end.get("y")).scl(unitScale);

           // if(rnd.nextFloat() <0.90f)
                runes.add(new DestructionRune(pos));
        }

        ArrayList<Vector2> teleports = new ArrayList<Vector2>();
        for(MapObject o: layers.get("TELEPORTRUNE").getObjects() ){
            MapProperties end = o.getProperties();
            Vector2 pos = new Vector2((Float) end.get("x"), (Float) end.get("y")).scl(unitScale);
           // if(rnd.nextFloat() <0.95f)
                teleports.add(pos);
        }

        ArrayList<Vector2> mix = new ArrayList<Vector2>();
        for (int size = teleports.size(); size> 0; size-- ){
            int i = rnd.nextInt(size);
            Vector2 point = teleports.get(i);
            mix.add(point);
            teleports.remove(i);
        }
        teleports = mix;

        Vector2 tp = null;
        for(int i=0; i < teleports.size();  i++)
            if(tp == null) tp = teleports.get(i);
                else{
                runes.add(new TeleportRune(tp, teleports.get(i)) );
                tp=null;
            }



        TiledMapTileLayer collision = (TiledMapTileLayer)layers.get("COLLISION");
        TiledMapTileLayer path = (TiledMapTileLayer)layers.get("PATH");

        tileSize =(int)collision.getTileWidth();

        Iterator<TiledMapTile> tileIt = tileSets.getTileSet("path_block").iterator();
        destructionTile = tileIt.next();

        int layersWidth = collision.getWidth();
        int layersHeight = collision.getHeight();

        this.height = layersWidth;
        this.width = layersHeight;

        // To Eliminate
        worldToLayersRatio = (float)layersWidth / worldSize;

        this.obstacles = obstacles;

    }

    public void draw(SpriteBatch batch){
        for(Rune r : runes)
            r.draw(batch);
        if(obstacles!=null)
        for(Obstacle o : this.obstacles)
            o.draw(batch);
    }

    // Optimize code
    // Standard equals working -> Tiled cells dont get copied
    public Collection<WorldCell> getCells(Collection<Vector2> points){
        HashSet<WorldCell> nub = new HashSet<WorldCell>();
        for(Vector2 p : points) {
            Vector2 pl = new Vector2(p).scl(worldToLayersRatio);
            if(pl.x <= width && pl.x >= 0 && pl.y <= height && pl.y >= 0) {
                WorldCell cell = map.getCell((int)pl.x,(int)pl.y);
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

        WorldCell worldCell = map.getCell( (int)mapPosition.x ,(int)mapPosition.y );

        return worldCell;

    }

    /*
    public TiledMapTileLayer.Cell getTiledCell(Vector2 worldPosition){
        Vector2 mapPosition = worldPosition.scl(worldToLayersRatio);

        WorldCell worldCell = worldMap[(int)mapPosition.x][(int)mapPosition.y];

        return worldCell.getTiledCell();

    }
*/
    public boolean worldCellCollision(Collection<Vector2> collisionPoints){
        boolean collision = false;
        Iterator<Vector2> it = collisionPoints.iterator();
        while (it.hasNext() && !collision)
            if( getWorldCell(it.next()).getCollision() ) collision = true;
        return collision;
    }

    // returns total damage to cells affected
    public float playerLightCollision(LightDestruction lightDestruction){
        HashSet<Vector2> points = new HashSet<Vector2>();

        int maxX,minX,maxY,minY;
        Vector2 center = player.getPosition();
        float maxR = lightDestruction.getDestructionRadius();
        float minR = player.getRadius();
        Vector2 max = new Vector2(center).add(maxR,maxR);
        Vector2 min = new Vector2(center).sub(minR, minR);
        maxX = (int) max.x; maxY = (int) max.y;
        minX = (int) min.x; minY = (int) min.y;
        int alfa = (int) (tileSize * (1/worldToLayersRatio));

        int halfX =  maxX / 2;
        int halfY = maxY / 2;

        for(int h = minY ; h < halfY ; h+=alfa)
            for(int w = minX; w <halfX; w+=alfa){
                float a = w -center.x;
                float b = h - center.y;
                float aux  = a*a + b*b;
                if( aux < maxR*maxR && aux > minR*minR ) {
                    points.add(new Vector2(center.x + w, center.y + h));
                    points.add(new Vector2(center.x - w, center.y + h));
                    points.add(new Vector2(center.x + w, center.y - h));
                    points.add(new Vector2(center.x - w, center.y - h));
                }
            }

        return lightDestruction.destroyTargets(getWorldCells(points),destructionTile,this);
    }

    public Collection<WorldCell> getWorldCells(Collection<Vector2> points){
        HashSet<WorldCell> worldCells = new HashSet<WorldCell>() ;
        for (Vector2 p : points){
            WorldCell worldCell = getWorldCell(p);
            if(worldCell!= null)
                worldCells.add(worldCell);
        }
        return  worldCells;
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
