package fr.univangers.vajin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import fr.univangers.vajin.SnakeRPG;
import fr.univangers.vajin.engine.EngineBuilder;
import fr.univangers.vajin.engine.GameEngine;
import fr.univangers.vajin.engine.WrongPlayersNumberException;
import fr.univangers.vajin.engine.entities.snake.SimpleSnake;
import fr.univangers.vajin.io.TileMapReader;
import fr.univangers.vajin.network.DistantEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameLoadingScreen implements Screen, InputProcessor {

    private final static Logger logger = LogManager.getLogger(GameLoadingScreen.class);

    private final Texture background;
    private String gameAtlas = "snake.atlas";

    private SnakeRPG parent;

    private SpriteBatch batch;

    private Stage stage;
    private Label loadingInfoLabel;
    private TextButton startButton;
    private boolean localGame = true;
    private boolean started = false;

    public void setLocalGame(boolean localGame) {
        this.localGame = localGame;
    }

    int currentLoadingStage;

    private String mapFileName;


    public GameLoadingScreen(SnakeRPG parent) {
        this.parent = parent;
        this.stage = new Stage(new ScreenViewport());
        this.stage.setDebugAll(true);

        this.batch = new SpriteBatch();

        parent.getAssetManager().queueLoadingScreenAssets();
        parent.getAssetManager().getManager().finishLoading();

        this.background = parent.getAssetManager().getManager().get("background/menu1280x720.jpg", Texture.class);


        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {
        this.started = false;
        this.currentLoadingStage = 0;

        this.loadingInfoLabel = new Label("Starting loading....", parent.getUISkin());

        Table table = new Table();
        table.setFillParent(true);

        stage.addActor(table);


        this.startButton = new TextButton("Start !", parent.getUISkin());
        startButton.setDisabled(true);
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startGame();
            }
        });


        table.add(this.loadingInfoLabel).fillX().uniformX();
        table.row().pad(10, 0, 0, 0);
        table.add(this.startButton);


        Gdx.input.setInputProcessor(stage);

    }


    public void setMapFileName(String mapFileName) {
        this.mapFileName = mapFileName;
    }


    private static final int FONT = 1;        // loading fonts
    private static final int PARTY = FONT + 1;        // loading particle effects
    private static final int SOUND = PARTY + 1;        // loading sounds
    private static final int MUSIC = SOUND + 1;        // loading music
    private static final int IMAGE = MUSIC + 1;
    private static final int MAP = IMAGE + 1;
    private static final int FINAL = MAP + 1;
    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.batch.begin();
        this.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.batch.end();


        if (parent.getAssetManager().getManager().update()) {
            currentLoadingStage++;
            switch (currentLoadingStage) {
                case FONT:
                    logger.debug("Loading fonts");
                    loadingInfoLabel.setText("Loading fonts");
                    parent.getAssetManager().queueAddFonts(); // first load done, now start fonts
                    break;
                case PARTY:
                    logger.debug("Loading particules");
                    loadingInfoLabel.setText("Loading particules");
                    parent.getAssetManager().queueAddParticleEffects(); // fonts are done now do party effects
                    break;
                case SOUND:
                    logger.debug("Loading sounds");
                    loadingInfoLabel.setText("Loading sounds...");
                    parent.getAssetManager().queueAddSounds();
                    break;
                case MUSIC:
                    logger.debug("Loading music");
                    loadingInfoLabel.setText("Loading music...");
                    parent.getAssetManager().queueAddMusic();
                    break;
                case IMAGE:
                    logger.debug("Loading images");
                    loadingInfoLabel.setText("Loading images");
                    parent.getAssetManager().queueAddGameImages();
                    break;
                case MAP:
                    logger.debug("Loading map");
                    loadingInfoLabel.setText("Loading map...");
                    parent.getAssetManager().setMapToLoad(mapFileName);
                    parent.getAssetManager().queueLoadingTileMap();
                    break;
                case FINAL:
                    logger.debug("Loading finished");
                    loadingInfoLabel.setText("Finished");
                    if (startButton.isDisabled()) {
                        startButton.setDisabled(false);
                    }
                    if (!localGame && !started) {
                        startGame();
                    }
                    break;
            }
        }


        stage.act();
        stage.draw();

    }

    private void startGame() {
        System.out.println("StartGame");
        if (currentLoadingStage >= FINAL) {
            this.started = true;

            if (localGame) {
                TileMapReader reader = TileMapReader.newTileMapReader(mapFileName);

                EngineBuilder classicEngineBuilder = new EngineBuilder(reader.getField(), 1);


                classicEngineBuilder.addSnake(0, new SimpleSnake());
                classicEngineBuilder.addSnake(1, new SimpleSnake());
                //   classicEngineBuilder.addSnake(2, new SimpleSnake());

                GameEngine classicEngine = null;
                try {
                    classicEngine = classicEngineBuilder.build();
                } catch (WrongPlayersNumberException e) {
                    e.printStackTrace();
                }
                parent.setScreen(new LocalGameScreen(parent, reader, parent.getAssetManager().getManager(), classicEngine, mapFileName));
            } else {
                DistantEngine engine = new DistantEngine();
                parent.getDistantGameScreen().setMap(mapFileName);
                parent.changeScreen(SnakeRPG.DISTANT_GAME_SCREEN);
            }

/*
        try {
            int idProtocol = 0x685fa053;
            InetAddress receiverInetAddress = InetAddress.getLocalHost();
            int receiverPort = 6969;


            DatagramSocket datagramSocket = new DatagramSocket(6970);
            PlayerPacketCreator playerPacketCreator = new PlayerPacketCreatorImpl(idProtocol);
            playerPacketCreator.setEngine(classicEngine);
            PlayerTransmiter transmiter = new PlayerTransmiter(datagramSocket, playerPacketCreator, idProtocol, 2f, receiverInetAddress, receiverPort);
            transmiter.start();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
*/
        } else {
            System.out.println("Not done loading !");
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        startGame();
            return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
