package com.flow.game.android;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MenuActivity extends Activity {

    //private String assets[] = {"metabot","rj","rks","sawny","tgod"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

/*
        String assets[] = {"metabot","rj","rks","sawny","tgod"};
    int i;

        for(i=0 ; i < 5 ; i++) {
            loadAsset(assets[i]);
        }

        setContentView(R.layout.activity_menu);
*/

        //Set up buttons

        Button buttonSinglePlayer = (Button) findViewById(R.id.buttonSinglePlayer);
        Button buttonStats = (Button) findViewById(R.id.buttonStats);

        buttonSinglePlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startGame();

            }
        });

        buttonStats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startStats();

            }
        });

       // MediaPlayer menu_theme_sound;
       // menu_theme_sound = MediaPlayer.create(this, R.raw.sound_87_5);
       // menu_theme_sound.start();


/*
        menu_theme_sound.prepareAsync();

        menu_theme_sound.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                   public void onPrepared(MediaPlayer mp) {

                                                       mp.start();

                                                   }
                                               }
        );
*/
    }

    public void startGame() {
        Intent intent = new Intent(MenuActivity.this, AndroidLauncher.class);
        MenuActivity.this.startActivity(intent);
    }


    public void startStats() {
        Intent intent = new Intent(MenuActivity.this, com.flow.game.android.StatsActivity.class);
        MenuActivity.this.startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Setup assets */
/*
    private void loadAsset(String name) {
        ImageView i = null;
        StringBuilder sb = new StringBuilder();
        sb.append(name + ".jpg");

        if(name.equals("sawny")) i = (ImageView) findViewById(R.id.sawny);
        else if(name.equals("rks")) i = (ImageView) findViewById(R.id.rks);
        else if(name.equals("tgod")) i = (ImageView) findViewById(R.id.tgod);
        else if(name.equals("metabot")) i = (ImageView) findViewById(R.id.metabot);
        else if(name.equals("rj")) i = (ImageView) findViewById(R.id.rj);

        try
        {
            // get input stream
            InputStream ims = getAssets().open(sb.toString());
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            i.setImageDrawable(d);
            i.setAdjustViewBounds(true);
        }
        catch(IOException ex)
        {
            return;
        }
    }
*/
/*
    private void loadAsset2(String name) {

        StringBuilder sb = new StringBuilder();
        sb.append(name + ".jpg");

        Bitmap bm;
        try {
            bm = getBitmapFromAsset(sb.toString());
        }catch(IOException e) { bm = null;}

            ImageView i = null;

        if(name.equals("sawny")) i = (ImageView) findViewById(R.id.sawny);
        if(name.equals("rks")) i = (ImageView) findViewById(R.id.rks);
        if(name.equals("tgod")) i = (ImageView) findViewById(R.id.tgod);
        if(name.equals("metabot")) i = (ImageView) findViewById(R.id.metabot);
        if(name.equals("rj")) i = (ImageView) findViewById(R.id.rj);

        if(i!=null) i.setImageBitmap(bm);

    }
    private Bitmap getBitmapFromAsset(String strName) throws IOException
    {
        AssetManager assetManager = getAssets();
        InputStream istr = assetManager.open(strName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }
    */
}
