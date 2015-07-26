package com.flow.game.android;


		import android.os.Bundle;

		import com.badlogic.gdx.backends.android.AndroidApplication;
		import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
		import com.flow.game.FlowGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
	// saves battery
		config.useAccelerometer = false;
		config.useCompass = false;

		initialize(new FlowGame(), config);

		/*
		Intent intent = new Intent(this, com.flow.game.android.MenuActivity.class);
		startActivity(intent);
*/
	}

}
