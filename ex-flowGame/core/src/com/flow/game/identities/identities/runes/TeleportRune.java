package com.flow.game.identities.identities.runes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.player.Player;

/**
 * Created by Gustavo on 12/08/2015.
 */
public class TeleportRune extends Rune {

    private Vector2 destiny;

    public TeleportRune(Vector2 p, Vector2 d) {
        destiny = new Vector2(d);
        super.sprite = new Sprite( new Texture("runes/teleportationrune.png") );
        Color color = new Color(Color.BLUE);
        sprite.setColor(color);

        loadRune(p);

        effectDuration = 0;
    }

    @Override
    public void playerEffect(Player p) {

        if(activated) {
            p.setPosition(destiny);
            super.playerEffect(p);
            activated = false;
        }
    }

    @Override
    public void removeEffect(Player p) {}
}
