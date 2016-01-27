package com.flow.game.identities.identities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.FlowGame;

/**
 * Created by Gustavo on 21/08/2015.
 */
   public class ProgressBar {
        private Sprite platform;
        private Sprite bar;
        private float progress;

        private Vector2 position;
        private Vector2 size;
        private float capacity;
        // Do a simple adaptation, PowerX powery weight of the current device resolution, the mainstream 800 * 480


        public void draw (SpriteBatch batch) {

            platform.setCenter(position.x, position.y);
           // platform.setSize(size.x,size.y);
            platform.draw(batch);

            //bar.setCenter(position.x, position.y);
            bar.setSize((int) ((size.x -0.2f) * (progress / 100f)), 0.3f);
            bar.setPosition(position.x - size.x,position.y);
            bar.draw(batch);

        }


        public ProgressBar (Player p, float capacity) {
            updatePositon(p);
            progress = capacity;
            this.capacity = capacity;

            platform = new Sprite(new Texture (Gdx.files.internal ("player/energybar_plat.png")));
            platform.setSize(size.x,size.y);
         //   platform.rotate90(true);


            bar = new Sprite(new Texture(Gdx.files.internal ("player/energybar.png")));
            bar.setSize(size.x,size.y*0.8f);

        }

    public static int ENERGYBAR_HEIGHT = 45;

        public void updatePositon(Player p){
            float radius = p.getTextureRadius();

            position = new Vector2(p.getPosition());
            size = new Vector2(1.5f*radius, 0.4f);
            position.sub( 0, radius  );
        }
        public void setProgress (float p) {
            this.progress = p;
        }
        public void dispose () {
           // platform.dispose ();
           // bar.dispose ();
        }
    }


