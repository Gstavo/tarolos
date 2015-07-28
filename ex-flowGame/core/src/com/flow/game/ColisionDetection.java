package com.flow.game;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import javax.swing.text.Position;

/**
 * Created by Gustavo on 27/07/2015.
 */
public class ColisionDetection {

    private static String COLISION_ID = "Colision";

    //  black tile id 286

    private TiledMapTileLayer colisionLayer;

    private TiledMapTileLayer l0, l1;

    public ColisionDetection(TiledMap map) {


        this.colisionLayer = (TiledMapTileLayer)map.getLayers().get(COLISION_ID);

    }

    public boolean ColisionDetection(Player player) {

        if(!player.getMoving()) return false;

        boolean colision = false;

        Vector2 playerPosition = player.getPosition();
        Vector2 direction = player.getDirection();

        Vector2 possibleColision = new Vector2(direction.scl(player.getRadius())).add(playerPosition);


        ArrayList<TiledMapTileLayer.Cell> colisionCells = new ArrayList<TiledMapTileLayer.Cell>();

        colisionCells.add(this.colisionLayer.getCell( (int) possibleColision.x,(int) possibleColision.y ));

        float playerRadius = player.getRadius();

        /* Check 4 points of wisp perimeter */

        Vector2 sidePosition = new Vector2(playerRadius,0).add(playerPosition);
        colisionCells.add(this.colisionLayer.getCell((int) sidePosition.x,(int) sidePosition.y));

        sidePosition = new Vector2(-playerRadius,0).add(playerPosition);
        colisionCells.add(this.colisionLayer.getCell((int) sidePosition.x,(int) sidePosition.y));

        sidePosition = new Vector2(0,playerRadius).add(playerPosition);
        colisionCells.add(this.colisionLayer.getCell((int) sidePosition.x,(int) sidePosition.y));

        sidePosition = new Vector2(0,-playerRadius).add(playerPosition);
        colisionCells.add(this.colisionLayer.getCell((int) sidePosition.x,(int) sidePosition.y));

        for(TiledMapTileLayer.Cell cell : colisionCells)
            if(cell != null) colision = true;

        return colision;

    }

}
