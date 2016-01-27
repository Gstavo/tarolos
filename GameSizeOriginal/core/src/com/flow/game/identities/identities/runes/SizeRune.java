package com.flow.game.identities.identities.runes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.player.Player;

/**
 * Created by Gustavo on 11/08/2015.
 */
public class SizeRune extends Rune {

    private float sizeAlteration;
    private float initialRadius;

    private boolean growth;

    public SizeRune(Vector2 p,boolean g) {

        if(g) {
            super.sprite = new Sprite( new Texture("runes/bigsizerune.png") );
            Color color = new Color(Color.GREEN);
            sprite.setColor(color);
        }
            else {
            super.sprite = new Sprite( new Texture("runes/smallsizerune.png") );
            Color color = new Color(Color.OLIVE);
            sprite.setColor(color);
        }

        loadRune(p);

        effectDuration = 6;
        growth = g;
        sizeAlteration = g ? 1.3f : 0.7f;
    }

    @Override
    public void playerEffect(Player p) {

        if(activated) {
            initialRadius = p.getRadius();
            p.updateRadius(initialRadius * sizeAlteration );
            super.playerEffect(p);
            activated = false;
        }
    }

    public void removeSpecialEffect(Player p) {
        p.updateRadius(initialRadius);
    }

}
