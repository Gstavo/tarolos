package com.flow.game.identities.identities;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Gustavo on 30/08/2015.
 */

public class Chunk implements Serializable{

    private int width;
    private int height;

    private WorldCell[][] map;
    private Vector2 position;
    private String filename;

    public Chunk(WorldCell[][] totalMap,Vector2 position,
                 int j,int i,int widthF,int heightF,
                 TiledMapTileLayer collisionLayer,TiledMapTileLayer pathLayer) {

        this.position = position;
        this.width = widthF; this.height = heightF;
        filename = "chunk"+ position.x + position.y + ".b";

        widthF+=j;heightF+=i;
        for(int w = j; w < widthF; w++) {
            for (int h = i; h < heightF; h++) {
                TiledMapTileLayer.Cell collisionCell = collisionLayer.getCell(w, h);
                int type =  collisionCell != null ? WorldMap.COLLISION_TILE : WorldMap.PATH_TILE;
                totalMap[w][h] = new WorldCell(type);
            }
        }
    }

    public Chunk(Chunk chunk){
        this.width = chunk.getWidth();
        this.height = chunk.getHeight();
        setMap(chunk.getMap());

        this.position = new Vector2( chunk.getPosition() );
        this.filename = chunk.getFilename();
    }

    public int getWidth(){ return this.width;}
    public int getHeight(){ return this.height;}
    public Vector2 getPosition(){ return this.position;}
    public String getFilename(){ return this.filename;}

    public WorldCell[][] getMap(){ return this.map;}
    public WorldCell getCell(int w,int h){ return map[w][h]; }

    public void setMap(WorldCell[][] map){
        WorldCell[][] cp = new WorldCell[width][height];
        for(int j = 0; j < width; j++)
            for(int i = 0; i < height;i++)
                cp[j][i] = new WorldCell( map[j][i]);
        this.map = cp;
    }

    public void save(String mapLoadPath){

        try{
            File f = new File( mapLoadPath,filename);
            f.createNewFile();
            FileHandle file = new FileHandle(f);

            FileOutputStream fos = new FileOutputStream(file.name());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
