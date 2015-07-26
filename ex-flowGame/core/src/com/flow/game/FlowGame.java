package com.flow.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class FlowGame extends ApplicationAdapter {

	private Camera cam;

	private Player player;

	private MyInputProcessor io;


	@Override
	public void create(){

		// PPeripheral enumeration https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/Input.java#L560

	/*
		boolean multiTouch = Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen);

		inputProcessor = new MyInputProcessor();
		Gdx.input.setInputProcessor(inputProcessor);
		Gdx.input.setInputProcessor(new InputControl());
*/

		io = new MyInputProcessor();

		// input events triggers camera render
		Gdx.input.setInputProcessor(io);

		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);


		if(Gdx.files.isLocalStorageAvailable()) {
			cam = new Camera();
			cam.create(player,io);
		}


	}

	@Override
	public void render(){
		cam.render();
	}

	@Override
	public void dispose(){
		Gdx.gl20.glDisable(GL20.GL_TEXTURE_2D);
	}
}

