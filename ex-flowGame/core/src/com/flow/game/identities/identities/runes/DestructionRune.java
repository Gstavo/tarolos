package com.flow.game.identities.identities.runes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.player.Player;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by Gustavo on 12/08/2015.
 */

public class DestructionRune extends Rune {

    private float initialRate;
    private float bonusRate;

    public DestructionRune(Vector2 p){
        super.sprite = new Sprite( new Texture("runes/destructionrune.png") );
        Color color = new Color(Color.BLACK);
        sprite.setColor(color);
        loadRune(p);

        effectDuration = 90;
        bonusRate = 3;
    }

    public void playerEffect(Player p){

        if(activated) {

            initialRate = p.getLightDestruction().getDestructionRate();

            p.getLightDestruction().setDestructionRate(initialRate * bonusRate);
            super.playerEffect(p);
            activated = false;
        }
    }

    @Override
    public void removeEffect(Player p) {
        p.getLightDestruction().setDestructionRate(initialRate);
    }

}
