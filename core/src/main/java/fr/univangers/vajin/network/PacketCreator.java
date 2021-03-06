package fr.univangers.vajin.network;

import java.io.IOException;
import java.net.DatagramPacket;

public interface PacketCreator {

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

    DatagramPacket getPacket() throws IOException;

    void setTransmiter(Transmiter transmiter);

    void sendPlayerAction(int action);

    void sendPlayerReady();

    void acknowledgePacket(int idReceived);



}
