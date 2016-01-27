package com.flow.game.identities.identities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.player.Player;

import java.util.Collection;

/**
 * Created by Gustavo on 12/08/2015.
 */
public class GameInterface {

    private Vector2 center;
    private float camWidth;
    private float camHeight;

    private Sprite blastButton;
    private Vector2 b_blastP;
    private float b_blastSize;

    public void setCenter(Vector2 p){ center = p; }
    public Vector2 getCenter(){ return center; }

    public GameInterface(Vector2 p, float camW, float camH){
        center = p;
        camWidth = camW;
        camHeight = camH;

        blastButton = new Sprite( new Texture("gameinterface/blastbutton.png") );

        b_blastSize = camW / 10;
        blastButton.setSize(b_blastSize, b_blastSize);

        Vector2 b_blastP = new Vector2(center).add(camW*0.40f,camH*0.40f );
        blastButton.setCenter(b_blastP.x,b_blastP.y);

    }

    public void handleInput(Collection<Vector2> input,float unitScale,Player player){

        for(Vector2 e : input){
            Vector2 p = new Vector2(e).scl(unitScale);
            if( squarePointIntersection(b_blastP, b_blastSize, p) ) player.blastDemand();
        }
    }

    public boolean squarePointIntersection(Vector2 s,float l,Vector2 p){

        Vector2 max = new Vector2(s).add(0.5f*l,0.5f*l);
        Vector2 min = new Vector2(s).add(-0.5f*l,-0.5f*l);

        return ( p.x > min.x && p.x < max.x && p.y > min.y && p.y < max.y );

    }

    public void update(){
        Vector2 blastP = new Vector2(center).add(camWidth*0.40f,camHeight*0.40f );
        blastButton.setCenter(blastP.x,blastP.y);
    }

    public void draw(SpriteBatch batch){
        update();
        blastButton.draw(batch);
    }

}
