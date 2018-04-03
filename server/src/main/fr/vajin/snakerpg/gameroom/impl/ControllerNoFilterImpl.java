package fr.vajin.snakerpg.gameroom.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import fr.univangers.vajin.IO.TileMapReader;
import fr.univangers.vajin.engine.EngineBuilder;
import fr.univangers.vajin.engine.GameEngine;
import fr.univangers.vajin.engine.WrongPlayersNumberException;
import fr.univangers.vajin.engine.entities.snake.SimpleSnake;
import fr.vajin.snakerpg.database.entities.GameModeEntity;
import fr.vajin.snakerpg.database.entities.GameParticipationEntity;
import fr.vajin.snakerpg.database.entities.UserEntity;
import fr.vajin.snakerpg.gameroom.Controller;
import fr.vajin.snakerpg.gameroom.PlayerHandler;

import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;

public class ControllerNoFilterImpl implements Controller{

    private GameModeEntity gameMode;
    private GameEngine gameEngine;
    private Collection<PlayerHandler> playerHandlers;
    private int lastId;
    private String map;
    private BiMap<Integer, String> idAddressMap;

    public ControllerNoFilterImpl(GameModeEntity gameMode, String map){
        this.gameMode = gameMode;
        this.gameEngine = null;
        this.playerHandlers = new ArrayList<>();
        this.lastId = 0;
        this.map = map;
        this.idAddressMap = HashBiMap.create();
    }

    @Override
    public int getCurrentPlayerCount() {
        return playerHandlers.size();
    }

    @Override
    public int getGameRoomSize() {
        return gameMode.getMaxPlayer();
    }

    @Override
    public GameEngine getCurrentEngine() {
        return this.gameEngine;
    }

    @Override
    public void addPlayerWaitingForConnection(UserEntity userEntity) {
        //pass
    }

    @Override
    public void addPlayerHandler(PlayerHandler playerHandler) {
        this.playerHandlers.add(playerHandler);
    }

    @Override
    public UserEntity acceptConnection(int userId, byte[] token, InetAddress inetAddress, int port) {

        userId = ++this.lastId;
        SecureRandom random = new SecureRandom();
        token = new byte[4];
        random.nextBytes(token);

        String adrStr = inetAddress.toString() + ":" + port;

        if(idAddressMap.inverse().containsKey(adrStr)){
            return null;
        }

        idAddressMap.put(userId, adrStr);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setAlias("user"+String.valueOf(userId));
        userEntity.setAccountName("account"+String.valueOf(userId));
        userEntity.setToken(token);

        return userEntity;
    }

    @Override
    public GameModeEntity currentGameMode() {
        return gameMode;
    }

    @Override
    public Collection<PlayerHandler> getPlayerHandlers() {
        return this.playerHandlers;
    }

    @Override
    public Collection<GameParticipationEntity> getLastGameResults() {
        return null;
    }

    @Override
    public String getMapName() {
        return this.map;
    }

    @Override
    public void startGame() {

        TileMapReader reader = TileMapReader.newTileMapReader(map);
        EngineBuilder gameEngineBuilder = new EngineBuilder(reader.getField(), gameMode.getId());

        for (PlayerHandler playerHandler : playerHandlers){

            int id = playerHandler.getUserId();
            gameEngineBuilder.addSnake(id, new SimpleSnake());

        }
        try {
            this.gameEngine = gameEngineBuilder.build();
        } catch (WrongPlayersNumberException e) {
            e.printStackTrace();
        }
    }
}
