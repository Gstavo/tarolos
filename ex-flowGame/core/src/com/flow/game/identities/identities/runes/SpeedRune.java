package com.flow.game.identities.identities.runes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.player.Player;


/**
 * Created by Gustavo on 11/08/2015.
 */
public class SpeedRune extends Rune{

    private float speedBonus;

    public SpeedRune(Vector2 p){
        super.sprite = new Sprite( new Texture("runes/speedrune_on.png") );
      //  Color color = new Color(Color.RED);
    //    sprite.setColor(color);

        loadRune(p);

        effectDuration = 4;
        speedBonus = 2;
    }

    public void playerEffect(Player p){

        if(activated) {
            p.setExtraTimeSpeed(effectDuration);
            p.setExtraSpeed(speedBonus);
            activated = false;

            sprite = new Sprite( new Texture("runes/speedrune_off.png") );
            sprite.setSize(runeSize.x,runeSize.y);
            sprite.setPosition(position.x,position.y);
        }
    }

    @Override
    public void removeEffect(Player p) {
        p.setExtraTimeSpeed(0);
        p.setExtraSpeed(0);
    }

}
