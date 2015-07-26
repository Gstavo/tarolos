package com.flow.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Gustavo on 18/07/2015.
 */
public class Player {

    private static final float RADIUS = 1;
    private static final float EXTRARADIUS = 2;

    private int speed = 3; // unidades / s

    private Vector2 position;

    private TiledMap tiledMap;

    TextureRegion textureRegion;
/*
    TextureMapObject playerObject;
    private Sprite wispMax;
    private Sprite wispMin;

    */
    private Sprite nucleo;

    private Sprite wisp;

    private float radius;
    private float extraRadius; // Quantidade extra de raio disponivel para controlar o wisp
    private final float maxNucleoRadius = 1.5f;
    private final float minNucleoRadius = 0.5f;

    private float nucleoRadius;


    private float unitScale = 1/16f;

    private boolean moved;

    Texture wispMaxTexture;

    Texture wispCurrent;

    Texture wispNucleo;

    public void draw(SpriteBatch batch, float camWidth, float camHeight)
    {

        wisp.setCenter(position.x, position.y);
        nucleo.setCenter(position.x, position.y);


        wisp.draw(batch);




        if(cycleTime < period ) {

                // int intensityRange = maxIntensity - minIntensity;
                cycleTime += Gdx.graphics.getDeltaTime();
                float time = period - cycleTime;

                //  float speed = (float)intensityRange / time;

                float speed = (float) (maxNucleoRadius - nucleoRadius) / time;

                if(!growing) speed = (float) (nucleoRadius - minNucleoRadius) / time;

                float f = 1 / Gdx.graphics.getDeltaTime();

                float deltaX;

                if (growing) deltaX = speed / f;
                else deltaX = -speed / f;

                nucleoRadius += deltaX;

        }
        else{
            if(growing) nucleoRadius = maxNucleoRadius; else nucleoRadius = minNucleoRadius;
            growing = !growing;
            cycleTime = 0;

        }

        nucleo.setSize(nucleoRadius,nucleoRadius);

        nucleo.draw(batch);


    }

    private int minIntensity;
    private int maxIntensity;
    private int ticks;
    private int maxTicks = 20;
    private boolean growing;
    private float cycleTime;
    private float period;

    public Player(TiledMap tMap, Vector2 position){
        moved = true;
        this.position = position;
        this.tiledMap = tMap;

        Float delta = Gdx.graphics.getDeltaTime();

        growing = false;
        cycleTime = 0;
        period = 1;

        nucleoRadius = 1.5f;

        radius = RADIUS;

        extraRadius = RADIUS + EXTRARADIUS;

        wispMaxTexture = new Texture("player/wisp_max.png");

        wispNucleo = new Texture("player/nucleo.png");

        nucleo = new Sprite(wispNucleo);

        wisp = new Sprite(wispMaxTexture,512,512);

        wisp.setSize(2,2);


     //   int mapWidth = tiledMap.getProperties().get("width",Integer.class)/2;
      //  int mapHeight = tiledMap.getProperties().get("height",Integer.class)/2;

        // 64, old values converted to 32
 //      TiledMapTileLayer tileLayer = new TiledMapTileLayer(mapWidth,mapHeight,64,64);

     //   TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();


        /**
         * RECOVER NEXT
         */

        /*

        textureRegion = new TextureRegion(wispMaxTexture,2,2);

    playerObject = new TextureMapObject(textureRegion);

        playerObject.setOriginX(position.x);
        playerObject.setOriginY(position.y);

         */

//        cell.setTile(new StaticTiledMapTile(textureRegion));

        // tile position, 4,10 equals 8,20 map coordinates
  //      tileLayer.setCell(4, 10, cell);

//        tiledMap.getLayers().add(tileLayer);


        //http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/maps/objects/TextureMapObject.html


        MapLayer playerLayer = new MapLayer();

   //     playerLayer.getObjects().add(playerObject);

     //   tiledMap.getLayers().add(playerLayer);

        // changing intensity http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/maps/MapLayer.html

        // CircleMapObject playerObject = (CircleMapObject)playerLayer.getObjects().get(0);



    }

    public Vector2 getPosition(){ return position;}



    public Vector2 updateMotion(Vector2 lastTouch)
    {

     //   float rad = position.angleRad(lastTouch);

   //     System.out.println(rad + " rad");

     //   if(position.y > lastTouch.y) rad +=Math.PI;

       // System.out.println("Position (" + position.x + " ," + position.y + ") Touch (" + lastTouch.x + " , " + lastTouch.y + " ) Angle rad" + rad);

       // rad += Math.PI / 2; // Set degrees to correct heading, shouldn't have to do this

        Vector2 direction = new Vector2();
        Vector2 velocity = new Vector2();
        Vector2 movement = new Vector2();



        System.out.println("Position (" + position.x + " ," + position.y + ") Touch (" + lastTouch.x + " , " + lastTouch.y + " )");

        direction.set(lastTouch);
        //direction.set(MathUtils.cos(rad), MathUtils.sin(rad)).nor();
        velocity.set(direction).scl(speed);
        movement.set(velocity).scl(Gdx.graphics.getDeltaTime());

      //  position.set(position.x + movement.x,position.y + movement.y);

         position.add(movement);

        System.out.println("Result " + position.x + " : " + position.y);

        return movement;
/*
        playerObject.setOriginX(position.x);
        playerObject.setOriginY(position.y);
*/
        //setLocation(position.x, position.y);

    }


}
