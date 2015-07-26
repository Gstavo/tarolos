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
import com.badlogic.gdx.math.Vector3;

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

        this.tiledMap = new TmxMapLoader().load("betamap.tmx"); // width=1196 p = 9.34375 units

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
            player.draw(batch, camW , camH);

            //font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, 0);
        } catch( NullPointerException e ){
            e.printStackTrace();
        }

        batch.end();


        // RESET INPUT KEY, KEYNOTPRESSED = 0
        myInputProcessor.destroy();

       // System.out.println("World width:" + worldWidth + "World Height:" + worldHeight);

    }

    /**
     *  Actualizes the user data to camera
     */
    private void handleInput() {

        switch (myInputProcessor.getKeyPressed()) {

            case (MyInputProcessor.TOUCH_DOWN):
/*
                Vector2 vscreenTouch = myInputProcessor.vconvertWorld(unitScale);

                Vector2 worldVector = vscreenTouch.add( (new Vector2(Gdx.graphics.getWidth()/2f * unitScale , Gdx.graphics.getHeight()/2f * unitScale)));
                player.updateMotion(worldVector);
*/
                //Proportion to camera added* (Gdx.graphics.getHeight()/Gdx.graphics.getWidth())



            // TOUCH_DRAGGED
            case (MyInputProcessor.TOUCH_DRAGGED ): {
                // Gdx Width 1196


                //Vector2 vscreenTouch = myInputProcessor.vconvertWorld(unitScale); // assuming coordinates are centered on cam.viewpowerW / 2f

   //             Vector2 vscreenTouch = myInputProcessor.vconvertWorld(unitScale).sub(cam.viewportWidth/2f , cam.viewportHeight/2f);
//                myInputProcessor.setLastTouch(myInputProcessor.getLastTouch().sub(Gdx.graphics.getWidth()/2f , Gdx.graphics.getHeight()/2f ));
   //             Vector2 vscreenTouch = myInputProcessor.vconvertWorld(camScale);


                                            // Screen possition             // Center in the midle

                System.out.println("Cam width:" + camW + "Cam height:" + camH );

                Vector2 vworldPosition = myInputProcessor.getLastTouch();

                // player 5.0 : 3.2107022
// lasttouc -419 :-299 -> scl "-26.1875:-18.6875 ->
                //Converte para world scale   // centrra no jogador
                vworldPosition.scl(unitScale);

           //     vworldPosition.add(player.getPosition());


                //Vector2 worldVector = vscreenTouch.add( player.getPosition() );
                Vector2 movement = player.updateMotion(vworldPosition);

                cam.translate(movement);
                //cam.position.set(player.getPosition().x, player.getPosition().y, 0);
                cam.update();

                out.println("Cam Position " + cam.position.x + " : " + cam.position.y);

                break;
            }
            //TOUCH_UP
            case ( MyInputProcessor.TOUCH_UP):{
                // Player Stops Moving
                break;
            }
            default:
                break;
        }
/*

        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, 100/cam.viewportWidth);

        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, worldWidth - effectiveViewportWidth / 2f);
        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, worldHeight - effectiveViewportHeight / 2f);

        cam.update();
*/

       // cam.position.x = MathUtils.clamp(cam.position.x, camW / 2f, 32 - camW / 2f);
       // cam.position.y = MathUtils.clamp(cam.position.y, camH / 2f, 32 - camH / 2f);

        cam.position.x = MathUtils.clamp(cam.position.x, 0 , 32);
        cam.position.y = MathUtils.clamp(cam.position.y, 0 , 32);



         /*
            Keeps the camera within the bounds of the world

            Explanation:

            We need to make sure the camera's zoom does not grow or shrink to values that
            would invert our world, or show too much of our world. To do this, we can calculate
            the effectiveViewportWidth and effectiveViewportHeight, which are just
            the viewportWidth/height * zoom (this gives us what we can see in the world given
            the current zoom). We can then clamp the value of the camera's zoom to values we
            require. 0.1f to prevent being too zoomed in. 100/cam.viewportWidth to prevent
            us being able to see more than the world's entire width.

            The last two lines are responsible for making sure we can/ �t translate out of the
            world boundaries. < 0, or more than 100 in either Axis

            https://github.com/libgdx/libgdx/wiki/Orthographic-camera

         */

    }

    /**
     * More resize strategies at https://github.com/libgdx/libgdx/wiki/Viewports
     */
    /*
    The following resize strategy will ensure that you will always see
    10 units in the x axis no matter what pixel-width your device has.
     */

    public Vector2 convert(MyInputProcessor myInputProcessor, float unitScale){

        Vector2 lastTouch = myInputProcessor.getLastTouch();

        return new Vector2( lastTouch.x * unitScale, lastTouch.y * unitScale);

    }

    @Override
    public void resize(int width,int height) {
        cam.viewportWidth = camW; // 10 units
        cam.viewportHeight = camH;
        cam.update();
    }



/*
    //The following resize strategy will show less/more of the world depending on the resolution
    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = width/32f;  //We will see width/32f units!
        cam.viewportHeight = cam.viewportWidth * height/width;
        cam.update();
    }
*/
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
