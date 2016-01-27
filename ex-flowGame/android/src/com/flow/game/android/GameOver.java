package com.flow.game.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;


/**
 * Created by Gustavo on 19/08/2015.
 */
public class GameOver extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
/*
        try {
            wait(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
        Intent intent = new Intent(GameOver.this,AndroidLauncher.class);
        GameOver.this.startActivity(intent);

    }
}
