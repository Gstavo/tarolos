package com.flow.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.identities.identities.Camera;
import com.flow.game.identities.identities.GameInterface;
import com.flow.game.identities.identities.MyInputProcessor;
import com.flow.game.identities.identities.WorldMap;
import com.flow.game.identities.identities.obstacle.Obstacle;
import com.flow.game.identities.identities.player.Player;
import com.flow.game.identities.identities.player.NoMovementException;
import com.flow.game.identities.identities.runes.DestructionRune;
import com.flow.game.identities.identities.runes.EnergyRune;
import com.flow.game.identities.identities.runes.Rune;
import com.flow.game.identities.identities.runes.SizeRune;
import com.flow.game.identities.identities.runes.SpeedRune;
import com.flow.game.identities.identities.runes.TeleportationRune;


import java.util.ArrayList;
import java.util.Iterator;

public class FlowGame extends ApplicationAdapter {

	public static int WORLD_HEIGHT = 32;
	public static int WORLD_WIDTH = 32;

	public static float UNITSCALE;

	private TiledMap tiledMap;
	private WorldMap worldMap;

	private Vector2 startPlace;
	private Vector2 endPlace;

	private Camera cam;

	private Player player;
	private boolean gameOver = false;
	private GameInterface gameInterface;

	private ArrayList<Obstacle> obstacles;

	private SpriteBatch batch;
	private BitmapFont font;

	private MyInputProcessor myInputProcessor;

	private Sound wispDeadSound;

	private Music wispMusic;

	private float unitScale; // TiledMap to WORLD


	@Override
	public void create(){

		this.unitScale = 1/16f;
		UNITSCALE = unitScale;

		this.batch = new SpriteBatch();
		this.font = new BitmapFont();

		wispMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/wisp_loop.wav"));
		wispMusic.setLooping(true);
		wispMusic.setVolume(0.4f);
		wispMusic.play();


		this.wispDeadSound = Gdx.audio.newSound( Gdx.files.internal("audio/wisp_dead.wav") );

		//Loading map information

		this.obstacles = new ArrayList<Obstacle>();
		loadMap();

		this.player = new Player(tiledMap,this.startPlace,worldMap);
		worldMap.setPlayer(player);

		myInputProcessor = new MyInputProcessor();
		// input events triggers camera render
		Gdx.input.setInputProcessor(myInputProcessor);
		Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);

		cam = new Camera();
		cam.create(batch, player, tiledMap);
		player.setCamDimensions(cam.getCamDim());
		player.initAbilities(worldMap);

		gameInterface = new GameInterface(player.getPosition() , cam.getCamDim().x , cam.getCamDim().y);

	}

	/**
	 *  Loads tiledMap and all information of com.identities and other positions
	 */
	private void loadMap() {

		this.tiledMap = new TmxMapLoader().load("map3.tmx");

		TiledMapTile destructionTile = tiledMap.getTileSets().getTileSet("tilea4mackeditFBU").getTile(555);;

		MapLayers layers = tiledMap.getLayers();

		MapProperties start = layers.get("START").getObjects().get(0).getProperties();
		this.startPlace = new Vector2((Float) start.get("x"), (Float) start.get("y"));
		this.startPlace.scl(unitScale);

		ArrayList<Rune> runes = new ArrayList<Rune>();
		MapObjects mapRunes = layers.get("RUNES").getObjects();

		Vector2 tpPosition = null , tpDestiny = null;
		for(MapObject o : mapRunes){
			MapProperties p = o.getProperties();

			String name = o.getName();
			Vector2 pR = new Vector2( (Float)p.get("x"),(Float)p.get("y") ).scl(unitScale);

			if(name.equals("SPEEDRUNE") ) runes.add( new SpeedRune(pR) );
			else if(name.equals("SMALL_SIZERUNE")) runes.add(new SizeRune(pR,false));
			else if(name.equals("BIG_SIZERUNE"))  runes.add(new SizeRune(pR,true));
			else if(name.equals("DESTRUCTIONRUNE")) runes.add(new DestructionRune(pR));
			else if(name.equals("TELEPORTATIONRUNE_0")) tpPosition = new Vector2(pR);
			else if(name.equals("TELEPORTATIONRUNE_1")) tpDestiny = new Vector2(pR);
			else if(name.equals("ENERGYRUNE")) runes.add(new EnergyRune(pR));

		}
		// betamap3 currently 16 runes

		if( tpPosition!=null && tpDestiny!=null )
			runes.add( new TeleportationRune(tpPosition,tpDestiny) );

		// Para diminuir o tamanho do tile visivel vai ser multiplicado por 16 o tamanho do mapar, reduzindo 16x
		// tile size 8; layer Width 1024 -> World Width 32
		// tile size 8; layer Width 1024 -> World Width 32
		worldMap = new WorldMap(layers,obstacles,runes,destructionTile,WORLD_WIDTH,WORLD_HEIGHT);
/*
		this.com.identities = new ArrayList<Obstacle>();
		for(int i = 0; i < 10 ; i++){
			Random random = new Random();
			Obstacle o = new Obstacle(new Vector2(random.nextFloat()%32,random.nextFloat()%32));
			this.com.identities.add(o);
		}
*/
	}

	public boolean isGameOver(){ return  gameOver;}

	private void playerDied(){
		gameOver = true;
		wispDeadSound.play();
		try {
			wait(2000);
		}catch (Exception E){}
		System.exit(1);

		//TODO HANDLER -> Android Game Over
		/*
		Intent intent = new Intent(GameOver.this, AndroidLauncher.class);
		GameOver.this.startActivity(intent);
*/
		/*
		this.player = new Player(tiledMap,this.startPlace,worldMap);
		player.setCamDimensions(cam.getCamDim());
		player.initAbilities(worldMap);
		worldMap.setPlayer(player);
		*/
	}

	@Override
	public void render(){

		handleInput();
		myInputProcessor.destroy();


		cam.render();

		if ( worldMap.playerCollision() ) {
			playerDied();
		}
		else {

			player.updateRuneStatus(Gdx.graphics.getDeltaTime());
			player.updateAbilities(worldMap);
			player.update();
		}

		drawBatch();



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

	//	try {
			// draw rectangle x y -> bottom left corner
			player.draw(batch);

	//		gameInterface.draw(batch);

			worldMap.draw(batch);

		//	font.draw(batch, "Player: " + player.getPosition().x + " : " + player.getPosition().y, 5 , 2 );
	//	} catch( NullPointerException e ){
	//		e.printStackTrace();
	//	}

		batch.end();
	}

	private void handleInput() {

		int key = myInputProcessor.getKeyPressed();

		Iterator<Vector2> it = myInputProcessor.getTouchDownList().iterator();
		boolean speedRequested = false;
		while (it.hasNext() && !speedRequested ){
			Vector2 position = new Vector2(it.next());
			if( player.speedRequest(position.scl(unitScale)) ) speedRequested = true;
		}

	//	gameInterface.update();
	//	gameInterface.handleInput(myInputProcessor.getTouchDownList(), unitScale, player);

		player.updateSpeed();

		switch (key) {

			case (MyInputProcessor.TOUCH_DOWN):

			case (MyInputProcessor.TOUCH_DRAGGED ): {
				// Screen position             // Center in the middle
				Vector2 vworldDirection = myInputProcessor.getLastTouch();
				vworldDirection.scl(unitScale);

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

