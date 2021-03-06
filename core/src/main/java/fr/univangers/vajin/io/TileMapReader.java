package fr.univangers.vajin.io;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import fr.univangers.vajin.engine.entities.Entity;
import fr.univangers.vajin.engine.field.Field;
import fr.univangers.vajin.engine.field.FieldUnit;
import fr.univangers.vajin.engine.field.FieldUnitEnum;
import fr.univangers.vajin.engine.field.StaticField;
import fr.univangers.vajin.engine.utilities.Matrix;
import fr.univangers.vajin.engine.utilities.StaticMatrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TileMapReader {

    private static final Logger logger = LogManager.getLogger(TileMapReader.class);

    public static final String TERRAIN_LAYER = "terrain";

    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";

    public static final String TILE_WIDTH = "tilewidth";
    public static final String TILE_HEIGHT = "tileheight";

    public static final String TILE_TYPE = "fieldUnit";

    private Field field;
    private List<Entity> objects;

    private int mapWidth;
    private int mapHeight;

    private int tileHeight;
    private int tileWidth;

    private TiledMap tiledMap;

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public static TileMapReader newTileMapReader(String mapFilePath) {


        FileHandleResolver resolver = new InternalFileHandleResolver();

        logger.debug("fdp");
        if(resolver.resolve(mapFilePath).exists()){
            logger.debug("File found");
        }else{
            logger.info("Can't find file");
        }

        logger.debug("Loading file");

        TiledMap tiledMap = new TmxMapLoader(resolver).load(mapFilePath);

        logger.debug("Map " + mapFilePath + "loaded");

        return new TileMapReader(tiledMap);
    }

    public TileMapReader(TiledMap map) {

        this.tiledMap = map;
        this.objects = new ArrayList<>();

        MapProperties properties = map.getProperties();

        this.mapWidth = properties.get(WIDTH, Integer.class);
        this.mapHeight = properties.get(HEIGHT, Integer.class);

        this.tileWidth = properties.get(TILE_WIDTH, Integer.class);
        this.tileHeight = properties.get(TILE_HEIGHT, Integer.class);

        logger.debug("mapWidth : " + mapWidth + ", mapHeight : " + mapHeight);

        Matrix<FieldUnit> fieldMatrix = new StaticMatrix<FieldUnit>(mapHeight, mapWidth);

        logger.debug("Matrix rows : " + fieldMatrix.getRowDimension());
        logger.debug("Matrix columns :" + fieldMatrix.getColumnDimension());

        TiledMapTileLayer terrainLayer = (TiledMapTileLayer) map.getLayers().get(TERRAIN_LAYER);

        if (terrainLayer == null) {
            logger.info("ERROR TILEDMAP : NO \"terrain\" layer ! Taking first layer (index 0) as terrain layer");
            terrainLayer = (TiledMapTileLayer) map.getLayers().get(0);
        }


        //CAREFUL :  iteration on HEIGHT (rows) first
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                TiledMapTileLayer.Cell cell = terrainLayer.getCell(i, j);

                TiledMapTile tile = cell.getTile();

                MapProperties tileProperties = tile.getProperties();

                String fieldUnitString = tileProperties.get(TILE_TYPE, String.class);

                //  System.out.println("Tile " + i + ", " + j + " -> " + fieldUnitString);

                if (fieldUnitString == null) {
                    fieldUnitString = "null";
                }
                switch (fieldUnitString) {
                    case "grass":
                        fieldMatrix.set(j, i, FieldUnitEnum.GRASS);
                        break;
                    case "barren_land":
                        fieldMatrix.set(j, i, FieldUnitEnum.BARRENLAND);
                        break;
                    case "water":
                        fieldMatrix.set(j, i, FieldUnitEnum.WATER);
                        break;
                    default:
                        fieldMatrix.set(j, i, FieldUnitEnum.WALL);
                        break;
                }
            }
        }
        this.field = new StaticField(fieldMatrix);

    }

    public Field getField() {
        return field;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }
}
