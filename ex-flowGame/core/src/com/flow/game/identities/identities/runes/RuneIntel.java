package com.flow.game.identities.identities.runes;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Gustavo on 23/08/2015.
 */
public class RuneIntel{
    private String runeName;
    private Vector2 position;

    public RuneIntel(String n,Vector2 p){
        runeName =n; position = p;
    }
    public String getName(){ return runeName; }
    public Vector2 getPosition(){ return position; }
}