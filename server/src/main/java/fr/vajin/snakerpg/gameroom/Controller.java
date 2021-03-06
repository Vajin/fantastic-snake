package fr.vajin.snakerpg.gameroom;

import fr.univangers.vajin.engine.GameEngine;
import fr.vajin.snakerpg.database.entities.GameModeEntity;
import fr.vajin.snakerpg.database.entities.UserEntity;
import fr.vajin.snakerpg.jsondatabeans.GameEndBean;

import java.net.InetAddress;
import java.util.Collection;

public interface Controller {

    int getCurrentPlayerCount();

    /**
     * @return max number of player that can be simultaneousy connected
     */
    int getGameRoomSize();

    GameEngine getCurrentEngine();

    void addPlayerWaitingForConnection(UserEntity userEntity);
//
    void addPlayerHandler(PlayerHandler playerHandler);
//
    void removePlayer(PlayerHandler playerHandler);

    UserEntity acceptConnection(int userId, byte[] token, String alias, InetAddress inetAddress, int port);

    GameModeEntity currentGameMode();

    Collection<PlayerHandler> getPlayerHandlers();

    GameEndBean getLastGameResults();

    String getMapName();

    void startGame();

    void endGame();

    void setPlayerReady(int idPlayer);


}
