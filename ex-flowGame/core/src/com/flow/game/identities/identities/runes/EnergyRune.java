package com.flow.game.identities.identities.runes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.player.Player;

/**
 * Created by Gustavo on 12/08/2015.
 */
public class EnergyRune extends Rune{

    private float regenerationRateBonus;

    public EnergyRune(Vector2 p){
        super.sprite = new Sprite( new Texture("runes/energyrune.png") );
        Color color = new Color(Color.ORANGE);
        sprite.setColor(color);

        loadRune(p);

        effectDuration = 0;
        regenerationRateBonus = 2f;
    }

    public void playerEffect(Player p){

        if(activated) {
            p.getMotor().setRegenerationBonusRate(regenerationRateBonus);
            super.playerEffect(p);
            activated = false;
        }
    }


}
