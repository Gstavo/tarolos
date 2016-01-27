package com.flow.game.android;


		import android.content.Intent;
		import android.os.Bundle;

		import com.badlogic.gdx.backends.android.AndroidApplication;
		import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
		import com.flow.game.FlowGame;

public class AndroidLauncher extends AndroidApplication {

	FlowGame game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
	// saves battery
		config.useAccelerometer = false;
		config.useCompass = false;

		initialize(game = new FlowGame(), config);
//ODO HANDLER
		if( game.isGameOver() ) {
			Intent iOver = new Intent(AndroidLauncher.this,GameOver.class);
			startActivity(iOver);
		}
		/*
		Intent intent = new Intent(this, com.flow.game.android.WispV10.class);
		startActivity(intent);
*/
	}
/*
	@Override
	public void onActivityResult(int g, int, Intent){

	}
*/
}
