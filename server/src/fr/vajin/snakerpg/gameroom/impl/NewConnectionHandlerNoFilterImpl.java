package fr.vajin.snakerpg.gameroom.impl;

import fr.univangers.vajin.engine.GameEngine;
import fr.vajin.snakerpg.gameroom.*;
import fr.vajin.snakerpg.gameroom.impl.creators.PlayerPacketCreatorImpl;
import fr.vajin.snakerpg.gameroom.impl.handlers.PlayerHandlerImpl;
import fr.vajin.snakerpg.gameroom.impl.handlers.PlayerPacketHandlerImpl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class NewConnectionHandlerNoFilterImpl implements NewConnectionHandler {


    Controller controller;
    DatagramSocket datagramSocket;
    Receiver receiver;


    @Override
    public synchronized boolean handleDatagramPacket(DatagramPacket datagramPacket) {
        if (controller.getCurrentPlayerCount() < controller.getGameRoomSize()) {

            int userId = 0;
            int token = 0;
            //TODO retreive from packet (or add argument on the method ??? Since we read it in the receiver)


            int port = datagramPacket.getPort();
            InetAddress address = datagramPacket.getAddress();

            PlayerPacketCreator playerPacketCreator = new PlayerPacketCreatorImpl(Controller.ID_PROTOCOL);
            GameEngine gameEngine = controller.getCurrentEngine();


            if (gameEngine != null) { //Only if the is a game being played
                playerPacketCreator.setEngine(gameEngine);
            }

            PlayerTransmiter playerTransmiter = new PlayerTransmiter(datagramSocket, playerPacketCreator, Controller.ID_PROTOCOL, 2f, address, port);

            PlayerPacketHandler playerPacketHandler = new PlayerPacketHandlerImpl(playerPacketCreator, playerTransmiter, controller);
            PlayerHandler playerHandler = new PlayerHandlerImpl(userId, token, playerPacketHandler, playerTransmiter, playerPacketCreator);
            playerPacketHandler.setPlayerHandler(playerHandler);

            playerTransmiter.start();

            receiver.addPlayerHandler(playerHandler);
            controller.addPlayerHandler(playerHandler);

            return true;
        } else {
            return false;
        }
    }
}
