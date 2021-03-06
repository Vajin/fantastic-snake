package fr.univangers.vajin.network.impl;

import com.google.gson.Gson;
import fr.univangers.vajin.network.NetworkController;
import fr.univangers.vajin.network.PacketCreator;
import fr.univangers.vajin.network.PacketHandler;
import fr.univangers.vajin.screens.LobbyScreen;
import fr.vajin.snakerpg.jsondatabeans.LobbyBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public class LobbyPacketHandler implements PacketHandler {

    private static int BUFFER_START_POS = 16;
    private NetworkController networkController;

    private static final Logger logger = LogManager.getLogger(PacketHandler.class);

    public LobbyPacketHandler(NetworkController networkController) {
        this.networkController = networkController;
    }

    @Override
    public void handlePacket(DatagramPacket packet) {

        ByteBuffer buffer = ByteBuffer.wrap(packet.getData());

        buffer.position(BUFFER_START_POS);

        int type = buffer.getInt();

        if(type!= PacketCreator.GAMEROOM_DESC){
            throw new IllegalArgumentException("Error packet type");
        }

        int jsonSize = buffer.getInt();

        byte [] jsonData = new byte[jsonSize];

        buffer.get(jsonData);

        Gson gson = new Gson();

        String jsonString = new String(jsonData);

        logger.debug("LobbyBean JSON received\n" + jsonString);

        LobbyBean lobbyBean = gson.fromJson(jsonString,LobbyBean.class);
        this.networkController.setLobbyBean(lobbyBean);

    }
}
