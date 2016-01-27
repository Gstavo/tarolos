package com.flow.game.identities.identities;

import com.flow.game.identities.identities.player.Player;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;



import static java.lang.Thread.sleep;

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

    private OrthographicCamera cam; // controls user view

    private float unitScale; // 1 / 16 -> 16 pixels = 1 unit world

    private float camW;
    private float camH;

    public Vector2 getCamDim(){ return new Vector2(camW,camH); }

    public void sclCam(float f){ camW*=f;camH*=f;
    cam.update();}

    public OrthographicCamera getCam(){ return cam; }

    @Override
    public void create(){return;}

    private SpriteBatch batch;

    public void create(SpriteBatch batch, Player player, MyInputProcessor myInputProcessor,TiledMap tiledMap){

        this.batch= batch;

        this.unitScale = 1 / 16f; // 1 unit == 2^7 128 pixels

        /* Android camera view of the world*/

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();


        this.camW = 30; // w * camScale;
        this.camH = 30 *(h/w);// h * camScale;

        cam = new OrthographicCamera(camW ,camH);

        cam.position.set(player.getPosition(), 0);
        cam.update();

    }

    @Override
    public void render() {
        // setProjectionMatrix before calling render

        cam.update();
        batch.setProjectionMatrix(cam.combined);
        //TODO WorldMap Render
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



    }

    public void setProjectionMatrix(SpriteBatch batch){
        cam.update();
        batch.setProjectionMatrix(cam.combined);
    }

    /**
     *  Actualizes the user data to camera
     */


    public void setPosition(Vector2 p){
        cam.position.set(p.x , p.y,0);
        cam.update();
    }


    /*
        Sets the camera within the limits of the World
     */
    public void adjust(int max){
        cam.position.x = MathUtils.clamp(cam.position.x, 0 , max);
        cam.position.y = MathUtils.clamp(cam.position.y, 0 , max);
        cam.update();
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
    }

}
