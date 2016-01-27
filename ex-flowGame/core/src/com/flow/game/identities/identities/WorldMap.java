package com.flow.game.identities.identities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.flow.game.FlowGame;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Gustavo on 29/08/2015.
 */
public class WorldMap implements Serializable {

    public static int COLLISION_TILE = 1;
    public static int PATH_TILE = 0;

    public static ArrayList<TextureRegion> tiles;

    public static float VIEWPORT_WIDTH = 10;
    public static float VIEWPORT_HEIGHT = 10 * (Gdx.graphics.getHeight() / Gdx.graphics.getWidth());
    public static float HALF_VIEWPORT_WIDTH = VIEWPORT_WIDTH/2;
    public static float HALF_VIEWPORT_HEIGHT = VIEWPORT_HEIGHT/2f;

    private boolean loaded;

    private String mapPath;
    private String mapLoadPath;

    private int nChunks;
    private int chunkSize;
    private int chunkLoadingSize =3; // has chunkLoadingSize^2 in memory
    private Chunk[][] chunkMap;

    private World world;
    private float worldSize;
    private float worldToMap;

    class ChunkID{
        private int W;
        private int H;
        public ChunkID(int w,int h){ W = w;H = h;}
    }

    public ChunkID positionToChunkID(Vector2 p){
        Vector2 id = new Vector2(p).scl(1/chunkSize);
        return new ChunkID((int)id.x,(int)id.y);
    }

    private TiledMap tiledMap;
    private Vector2 viewTilesSize;

    private float layerWidth;
    private float tileSize;
    private float mapSize;
    private float unitScale;

    /*
    Map [0..chunkSize * nChunks] -> [0..nChunks]
                            f = x/chunkSize
 */
    public WorldCell getCell(int w,int h){
        int x = w/chunkSize, y = h/chunkSize;
        return chunkMap[x][y].getCell(w - x * chunkSize, h - y * chunkSize);
    }

    public void loadMap(String mapName,AssetManager assetManager){

        mapPath = "maps/" + mapName;

        mapLoadPath = "maps/" + mapName + "_load/";
        //TO CHANGE assetManager initialization to flowGame

        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()) );

        assetManager.load(mapPath, TiledMap.class);
        assetManager.finishLoading();
        tiledMap = assetManager.get(mapPath + ".tmx",TiledMap.class);

        createWorldMap();
        world.setMap(this);

        loaded = true;
    }

    public World getWorld(){ return world; }

    public boolean isLoaded(){ return loaded; }

    public void createWorldMap() {
        TiledMapTileSets tileSets = tiledMap.getTileSets();
        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer collision = (TiledMapTileLayer)layers.get("COLLISION");

        layerWidth = collision.getWidth();
        tileSize = collision.getTileWidth();
        unitScale = 1/16f;
        FlowGame.UNITSCALE = unitScale;
        // current 512
        worldSize = tileSize * layerWidth * unitScale;
        worldToMap =  layerWidth /worldSize ; // correct ??

        viewTilesSize = new Vector2( 1/tileSize,1/tileSize ).scl(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        ArrayList<Vector2> startpo = new ArrayList<Vector2>();
        for( MapObject o : layers.get("PLAYER").getObjects() ) {
            MapProperties start = o.getProperties();
            Vector2 p = new Vector2((Float) start.get("x"), (Float) start.get("y")).scl(unitScale);
            startpo.add(p);
        }

        ArrayList<Vector2> endpo = new ArrayList<Vector2>();
        for(MapObject o : layers.get("END").getObjects()){
            MapProperties end = o.getProperties();
            Vector2 p = new Vector2((Float) end.get("x"), (Float) end.get("y")).scl(unitScale);
            endpo.add(p);
        }

        // Para diminuir o tamanho do tile visivel vai ser multiplicado por 16 o tamanho do mapar, reduzindo 16x
        // tile size 8; layer Width 1024 -> World Width 32 // tileset tile width 128 width = 8 * 16
        // tile size 8; layer Width 64 -> World Width 32
        // 32/ (8 * 64) = 1/16(unitscale

        world = new World(layers,tileSets,startpo,endpo,null,unitScale,worldSize);

        // Tiles textures setup

        //Order Matters!!
        tiles = new ArrayList<TextureRegion>();
        tiles.add(new TextureRegion(
                tileSets.getTileSet("path_block").iterator().next().getTextureRegion()));
        tiles.add(new TextureRegion(
                tileSets.getTileSet("collision_block").iterator().next().getTextureRegion()));

        divideInChunks();
        Collection<ChunkID> chunksID = calculateChunksID(world.getStartPlace());
        loadChunks(chunksID);
    }

    public void update(){
        Collection<ChunkID> chunksID = calculateChunksID(world.getStartPlace());
        loadChunks(chunksID);
    }

    public void divideInChunks(){
        mapSize = (int)(layerWidth * tileSize);

        chunkSize = 512;
        nChunks = (int)mapSize / chunkSize;

        MapLayers layers = tiledMap.getLayers();
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer)layers.get("COLLISION");
        TiledMapTileLayer pathLayer = (TiledMapTileLayer)layers.get("PATH");
        float layerHeight = layerWidth;

        WorldCell[][] totalMap = new WorldCell[(int)layerWidth][(int)layerHeight];

        chunkMap = new Chunk[nChunks][nChunks];

        int w,h=0;

        for( w = chunkSize; w < layerWidth; w+=chunkSize)
            for ( h = chunkSize; h < layerHeight; h+=chunkSize)
                chunkMap[w][h] = new Chunk(totalMap, new Vector2(w,h) ,
                                        w - chunkSize ,h - chunkSize,chunkSize,chunkSize,
                                        collisionLayer,pathLayer);

        saveChunksIntoFiles(chunkMap,w,h);
    }

    public void saveChunksIntoFiles(Chunk[][] chunks,int w,int h){
        for(int j = 0; j < w; j++)
            for(int i = 0; i < h; i++){
                chunks[j][i].save(mapLoadPath);
            }
    }




    public Collection<ChunkID> calculateChunksID(Vector2 center){
        ArrayList<ChunkID> ids = new ArrayList<ChunkID>();
        ChunkID first = positionToChunkID( new Vector2(center).sub(VIEWPORT_WIDTH/2f,VIEWPORT_HEIGHT/2f) );

        // Adding center first because order implies priority
        if( chunkMap[first.W][first.H] != null ) ids.add(first);

        for(int j = first.W - 1, n = 0; n < chunkLoadingSize; n++,j++)
            for(int i = first.H -1,c = 0; c < chunkLoadingSize; i++,c++)
                if( chunkMap[j][i]!=null && !(first.W ==j && first.H==i) ) ids.add( new ChunkID(j,i) );

        //TODO CHECKO FOR CHUNKMAP LIMITS
        // removes trash in line 3, after 0 and 1 lines  are loaded
        int j = first.W - 2; int max = first.H+2;
        for(int i = first.H - 2 ;  i <= max ; i++)
            chunkMap[j][i]=null;

        int min = first.H-2;
        j = first.W + 2;
        for(int i = first.H - 2 ;  i >= min; i++)
            chunkMap[j][i]=null;

        int i = first.H -2;
        max = first.W +2;
        for( j = first.W - 2 ;  j <= max ; j++)
            chunkMap[j][i]=null;

        min = first.W-2;
        i = first.H + 2;
        for(j = first.W - 2 ;  j >= min ; j++)
            chunkMap[j][i]=null;

        return ids;
    }

    public void loadChunks( Collection<ChunkID> chunksId ) {
            //   FileHandle file = Gdx.files.local(filename);
        for(ChunkID id : chunksId) {
            String filename = "chunk" + id.W + id.H + ".by";
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
                this.chunkMap[id.W][id.H] = new Chunk((Chunk) ois.readObject());
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Called if center changes or tiles are destroyed


    public void draw(SpriteBatch batch,Vector2 center){
        int minW = (int)(center.x - HALF_VIEWPORT_WIDTH),maxW = (int)(center.x + HALF_VIEWPORT_WIDTH);
        int minH = (int)(center.y - HALF_VIEWPORT_HEIGHT),maxH = (int)(center.y + HALF_VIEWPORT_HEIGHT);

        minW*= worldToMap;  maxW*=worldToMap;
        minH*= worldToMap;  maxH*=worldToMap;

        for(int w = minW ; w < maxW; w++)
            for(int h = minH; h < maxH; h++) {
                WorldCell cell = getCell(w, h);
                TextureRegion texture = tiles.get(cell.getType());
                batch.draw(texture,w,h);
            }

    }


}
