package com.flow.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.Camera;
import com.flow.game.identities.identities.GameInterface;
import com.flow.game.identities.identities.WorldMap;
import com.flow.game.identities.identities.MyInputProcessor;
import com.flow.game.identities.identities.World;
import com.flow.game.identities.identities.obstacle.Obstacle;
import com.flow.game.identities.identities.player.Player;


import java.util.ArrayList;
import java.util.Iterator;

public class FlowGame extends ApplicationAdapter {

	private static String MAP_NAME = "alpha";

	public static int WORLD_HEIGHT = 512;
	public static int WORLD_WIDTH = 512;

	public static float UNITSCALE;


	private boolean started = false;
	private TiledMap tiledMap;
	private World world;

	AssetManager assetManager;

	private Camera cam;

	private Player player;
	private GameInterface gameInterface;

	private ArrayList<Obstacle> obstacles;

	private SpriteBatch batch;
	private BitmapFont font;

	private MyInputProcessor myInputProcessor;

	private Sound wispDeadSound;
	private Music wispMusic;

	private WorldMap worldMap;




	@Override
	public void create() {
		//TODO maploader initialization
		worldMap.loadMap(MAP_NAME , assetManager);
		if(!worldMap.isLoaded()){
		}
		initGame();
	}

	public void initGame(){
		this.started = true;
		assetManager = new AssetManager();
		this.batch = new SpriteBatch();
		this.font = new BitmapFont();
			tiledMap = null;

		wispMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/wisp_loop.wav"));
		wispMusic.setLooping(true);
		wispMusic.setVolume(0.4f);
		wispMusic.play();


		this.wispDeadSound = Gdx.audio.newSound( Gdx.files.internal("audio/wisp_dead.wav") );

		//Loading map information

		this.obstacles = new ArrayList<Obstacle>();
		world = worldMap.getWorld();

		this.player = new Player(tiledMap, world);
		world.setPlayer(player);

		myInputProcessor = new MyInputProcessor();
		// input events triggers camera render
		Gdx.input.setInputProcessor(myInputProcessor);
		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);

		cam = new Camera();
		cam.create(batch, player, myInputProcessor, tiledMap);

		player.setCamDimensions(cam.getCamDim());
		player.initAbilities(world);

		gameInterface = new GameInterface(player.getPosition() , cam.getCamDim().x , cam.getCamDim().y);

	}

	private boolean gameOver = false;
	public boolean isGameOver(){ return gameOver;}

	private void playerDied(){
		wispDeadSound.play();
		gameOver = true;
		System.exit(1);

		//TODO HANDLER -> Android Game Over
		/*
		Intent intent = new Intent(GameOver.this, AndroidLauncher.class);
		GameOver.this.startActivity(intent);
*/
		/*
		this.player = new Player(tiledMap,this.startPlace,world);
		player.setCamDimensions(cam.getCamDim());
		player.initAbilities(world);
		world.setPlayer(player);
		*/
	}


	@Override
	public void render(){
		handleInput();
		myInputProcessor.destroy();

		cam.render();
		worldMap.update();

		drawBatch();

		if ( world.playerCollision() ) {
			playerDied();
		}
		else {

			player.updateRuneStatus(Gdx.graphics.getDeltaTime());
			player.updateAbilities(world);
			player.update();
		}

	}

	@Override
	public void dispose() {
		Gdx.gl20.glDisable(GL20.GL_TEXTURE_2D);
		batch.dispose();
		wispDeadSound.dispose();
		wispMusic.dispose();
	}

	private void drawBatch(){
		batch.begin();

			worldMap.draw(batch,player.getPosition());

			player.draw(batch);

	//		gameInterface.draw(batch);

			world.draw(batch);

		//	font.draw(batch, "Player: " + player.getPosition().x + " : " + player.getPosition().y, 5 , 2 );

		batch.end();
	}

	private void handleInput() {

		int key = myInputProcessor.getKeyPressed();

		Iterator<Vector2> it = myInputProcessor.getTouchDownList().iterator();
		boolean speedRequested = false;
		while (it.hasNext() && !speedRequested ){
			Vector2 position = new Vector2(it.next());
			if( player.speedRequest(position.scl(1/16f)) ) speedRequested = true;
		}

	//	gameInterface.update();
	//	gameInterface.handleInput(myInputProcessor.getTouchDownList(), unitScale, player);

		player.updateSpeed();

		switch (key) {

			case (MyInputProcessor.TOUCH_DOWN):

			case (MyInputProcessor.TOUCH_DRAGGED ): {
				// Screen position             // Center in the middle
				Vector2 vworldDirection = myInputProcessor.getLastTouch();
				vworldDirection.scl(1/16f);

				player.updateDirection(vworldDirection);
				player.setMoving(true);
				player.updateMotion();

				break;
			}
			case ( MyInputProcessor.TOUCH_UP): {
				player.stopMovement();
				player.updateMotion();
				break;
			}
			default:
					player.updateMotion();
				break;
		}
		cam.setPosition(player.getPosition());
		cam.adjust(WORLD_WIDTH);

	}
}

