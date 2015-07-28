package com.flow.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


import javax.swing.text.Position;

import static java.lang.System.*;

/**
 * Created by Gustavo on 17/07/2015.
 *

  Method	Description
 lookAt(float x, float y, float z)
 Recalculates the direction of the camera to look at the point
 defined by the coordinates on all axes. - The z axis is ignored for 2D

 translate(float x, float y, float z)
 Moves the camera by the given amount on each axis. - Note that z is ignored for the OrthographicCamera
 rotate(float angle, float axisX, float axisY, float axisZ)	Rotates the direction and up vector of this camera
 by the given angle around the given axis. The direction and up vector will not be orthogonalized.
 The angle is persisted so the camera will be rotated by angle relative to its previous rotation.

 update()
 Recalculates the projection and view matrix of the camera and the frustum planes

 */

// http://www.gamefromscratch.com/post/2014/05/01/LibGDX-Tutorial-11-Tiled-Maps-Part-2-Adding-a-character-sprite.aspx
public class Camera implements ApplicationListener {

    static final int WORLD_WIDTH = 100;
    static final int WORLD_HEIGHT = 100;

    private Player player;

    private MyInputProcessor myInputProcessor;

    private ColisionDetection colisionDetection;

    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera cam; // controls user view
    private SpriteBatch batch; // renders world
    private Vector2 lastPosition;

  //  private TiledMapRenderer tiledMapRenderer;
    private TiledMap tiledMap;
    private float worldWidth; // world unit
    private float worldHeight; // world unit


    private BitmapFont font;// contains world data to draw the view
    private float rotationSpeed;

    private Vector2 camScale; //  10 / Gdx.graphics.getWidth()
    private float unitScale; // 1 / 16 -> 16 pixels = 1 unit world

    private float camW;
    private float camH;

    @Override
    public void create(){return;}


    public void create(Player player, MyInputProcessor myInputProcessor){
        this.rotationSpeed = 0.5f;

        this.myInputProcessor = myInputProcessor;

        this.unitScale = 1 / 128f; // 1 unit == 2^7 128 pixels

        this.tiledMap = new TmxMapLoader().load("black.tmx"); // width=1196 p = 9.34375 units

        this.colisionDetection = new ColisionDetection(tiledMap);

       // this.worldWidth = ((TiledMapTileLayer )tiledMap.getLayers().get("World")).getWidth() * unitScale * 50;

        //this.worldHeight = ((TiledMapTileLayer )tiledMap.getLayers().get("World")).getHeight() * unitScale * 50;


        /* Android camera view of the world*/

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        this.camW = w * unitScale;
        this.camH = h * unitScale;

        // width = 30 , height = 30 * ratio, fixing ecra dimentions
        cam = new OrthographicCamera(camW ,camH);

        // Extra half camera view in order to fill the void on position (0,0) of camera view
/*
        cam = new OrthographicCamera();

    //    camScale = new Vector2(10f / Gdx.graphics.getWidth(), 10f / Gdx.graphics.getHeight() ) ;
//Sets this camera to an orthographic projection, centered at (viewportWidth/2, viewportHeight/2), with the y-axis pointing up or down.

        cam.setToOrtho(false,1/camScale.x, 1/camScale.y );
*/
       // cam.viewportWidth = 10f; // 10 units
       // cam.viewportHeight = cam.viewportWidth * ( h / w);

        Vector2 p0 = new Vector2(cam.viewportWidth / 2f, cam.viewportHeight / 2f);

        cam.position.set(p0,0);
        cam.update();

        this.renderer = new OrthogonalTiledMapRenderer(tiledMap, 1/16f);

        this.player = new Player(tiledMap,p0);

      //  this.player = new Player(tiledMap , new Vector2(cam.viewportWidth / 2f , cam.viewportHeight/2f));

        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        handleInput(); // Updates camera position by user input
        cam.update();

        batch.setProjectionMatrix(cam.combined);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(cam);
        renderer.render();

        batch.begin();

        try {
            // draw rectangle x y -> bottom left corner
            player.draw(batch);

            font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), camW/2 - 2, camH/2 - 2);
        } catch( NullPointerException e ){
            e.printStackTrace();
        }

        batch.end();

        if (colisionDetection.ColisionDetection(player)) {
           player.playerDied();
        }

        // RESET INPUT KEY, KEYNOTPRESSED = 0 , lastToouch = zero
        myInputProcessor.destroy();

    }

    /**
     *  Actualizes the user data to camera
     */
    private void handleInput() {

        int key = myInputProcessor.getKeyPressed();

        switch (key) {

            case (MyInputProcessor.TOUCH_DOWN):

            case (MyInputProcessor.TOUCH_DRAGGED ): {

                                            // Screen possition             // Center in the midle

                Vector2 vworldDirection = myInputProcessor.getLastTouch();

                vworldDirection.scl(unitScale);

                player.updateDirection(vworldDirection);

                try {
                    player.updateMotion();
                    cam.position.set(player.getPosition().x, player.getPosition().y, 0);
                    cam.update();
                }catch(NoMovementException e){}

                break;
            }
            case ( MyInputProcessor.TOUCH_UP): {
                player.updateDirection(Vector2.Zero);
                player.setMoving(false);
            }
            default:
                try {
                    player.updateMotion();
                    cam.position.set(player.getPosition().x, player.getPosition().y, 0);
                    cam.update();
                }
                catch (NoMovementException e) {}
                break;
        }

        cam.position.x = MathUtils.clamp(cam.position.x, 0 , 32);
        cam.position.y = MathUtils.clamp(cam.position.y, 0 , 32);

    }

    @Override
    public void resize(int width,int height) {
        cam.viewportWidth = camW; // 10 units
        cam.viewportHeight = camH;
        cam.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
