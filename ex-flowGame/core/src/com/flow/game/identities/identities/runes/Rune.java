package com.flow.game.identities.identities.runes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.player.Player;
import com.badlogic.gdx.graphics.Color;


/**
 * Created by Gustavo on 11/08/2015.
 */
public abstract class Rune {

    protected Vector2 position;

    protected Sprite sprite;

    protected boolean activated;
    protected float effectDuration;
    protected Vector2 runeSize;

    private Vector2 radius;
    private Sound pickupSound;

    protected void loadRune(Vector2 p){
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("audio/runepickup.wav"));

        activated = true;
        runeSize = new Vector2(1.5f,1.5f);
        radius = new Vector2(runeSize).scl(0.5f);
        position = new Vector2(p).sub( radius);
        sprite.setSize(runeSize.x, runeSize.y);
        sprite.setPosition(position.x, position.y);

    }

    public void draw(SpriteBatch batch){
        if(activated || this instanceof SpeedRune ) sprite.draw(batch);
    }

    public boolean pointColision(Vector2 p){
        Vector2 max = new Vector2(position).add(radius);
        Vector2 min = new Vector2(position).sub(radius);

        return ( p.x > min.x  && p.x < max.x && p.y > min.y  && p.y < max.y );
    }

    public boolean centerCollision(Player p){
        float r = p.getRadius();
        Vector2 c = new Vector2(p.getPosition());
        c.sub(position);
        return ( c.x * c.x  +  c.y * c.y < r*r );
    }

    // Override this function and call it to add extra effects
    public void playerEffect(Player p) {
        p.addRune(this);

        long id = pickupSound.play(0.5f);
    //    pickupSound.setPitch(id,2f);
    }

    protected void removeEffect(Player p){
    }

    public void updateEffect(Player p,float delta){

        effectDuration-=delta;
        if(effectDuration < 0) {
            p.removeActivatedRune(this);
            removeEffect(p);
        }
    }

    public void setActivated(boolean b){ activated = b; }

    public boolean getActivated(){ return activated; }

    public void dispose(){
        pickupSound.dispose();
    }


}
