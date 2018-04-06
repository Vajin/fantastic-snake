package fr.univangers.vajin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import fr.univangers.vajin.SnakeRPG;
import fr.univangers.vajin.network.NetworkController;
import fr.vajin.snakerpg.jsondatabeans.LobbyBean;
import fr.vajin.snakerpg.jsondatabeans.PlayerBean;

import javax.xml.crypto.Data;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Iterator;


public class HostLobbyScreen extends LobbyScreen {

    public HostLobbyScreen(SnakeRPG parent, NetworkController networkController) {
        super(parent, networkController);
    }

    @Override
    public void show() {

        super.show();

        Skin skin = this.getParent().getUISkin();

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Table optionTable = new Table();

        TextButton exitLobby = new TextButton("Exit lobby", skin);
        TextButton ready = new TextButton("Ready", skin);


        this.getStage().setDebugAll(true);

        SelectBox<String> mapSelectBox = new SelectBox<String>(skin);
        mapSelectBox.setItems("simple_map.tmx", "sample_map.tmx");

        mainTable.row().pad(0, 0, 0, 10);

        optionTable.add(new Label("Map", skin)).fillX().uniformX();
        optionTable.add(mapSelectBox).fillX().uniformX();
        optionTable.row().pad(10, 0, 0, 10);
        optionTable.add(new Label("Random option", skin));

        mainTable.add(optionTable);
        mainTable.add(this.getPlayerTable());
        mainTable.row();
        mainTable.add(exitLobby).fillX().uniformX();
        mainTable.add(ready).fillX().uniformX();

        exitLobby.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getParent().changeScreen(SnakeRPG.MENU_SCREEN);
            }
        });

        this.getStage().addActor(mainTable);

        Gdx.input.setInputProcessor(getStage());
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
}
