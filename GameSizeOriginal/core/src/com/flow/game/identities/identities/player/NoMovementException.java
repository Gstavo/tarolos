package com.flow.game.identities.identities.player;

/**
 * Created by Gustavo on 27/07/2015.
 */
public class NoMovementException extends Exception {

    public NoMovementException(){
        super();
    }

    public NoMovementException(String message){
        super(message);
    }
}
