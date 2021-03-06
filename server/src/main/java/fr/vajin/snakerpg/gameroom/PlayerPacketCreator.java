package fr.vajin.snakerpg.gameroom;


import fr.vajin.snakerpg.utilities.CustomByteArrayOutputStream;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Prend les updates du moteur & controlleur, anisi que les informations d'acknoledgement
 * des packets précedement envoyé, et préparent ainsi les prochains packets à être envoyés.
 */
public interface PlayerPacketCreator{

    int RESP_JOIN_STATE = 10;
    int WAITING_FOR_GAME_STATE = 20;
    int GAME_START_STATE = 30;
    int GAME_END_STATE = 40;
    int GAME_STATE = 50;


    interface PlayerPacketCreatorState {

        DatagramPacket getNextPacket(CustomByteArrayOutputStream stream) throws IOException;

    }


    int ID_PROTOCOL = 0x685fa053;

    int JOIN = 1;
    int RESP_JOIN = 2;
    int LIFELINE = 3;
    int GAMEROOM_DESC = 4;
    int GAME_START = 5;
    int GAME = 6;
    int GAME_END = 7;
    int PLAYER_ACTION = 8;
    int PLAYER_READY = 9;


    /**
     * Permet d'obtenir le prochain paquet à envoyer.
     *
     * @return le prochain paquet à envoyer.
     */
    DatagramPacket getNextPacket() throws IOException;

    /**
     * @param idLastReceived
     */
    void acknowledgePacket(int idLastReceived);

    void setState(int state);

    Integer getState();

    void setPlayerHandler(PlayerHandler playerHandler);

    PlayerHandler getPlayerHandler();

    void startGame();

    void endGame();
}
