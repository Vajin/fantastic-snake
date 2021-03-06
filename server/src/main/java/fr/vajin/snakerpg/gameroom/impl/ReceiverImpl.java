package fr.vajin.snakerpg.gameroom.impl;

import com.google.common.collect.Maps;
import fr.vajin.snakerpg.LoggingUtilities;
import fr.vajin.snakerpg.gameroom.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReceiverImpl implements Receiver {

    private final static Logger logger = LogManager.getLogger(ReceiverImpl.class);

    private final int idProtocol;
    private Controller controller;
    private Map<Integer, PlayerHandler> playerHandlerMap;
    private NewConnectionHandler newConnectionHandler;
    private ExecutorService executorService;

    public ReceiverImpl(int idProtocol, Controller controller, NewConnectionHandler newConnectionHandler) {
        this.idProtocol = idProtocol;
        this.playerHandlerMap = Maps.newConcurrentMap();
        this.newConnectionHandler = newConnectionHandler;
        this.controller = controller;
        this.executorService = Executors.newFixedThreadPool(4);
    }

    @Override
    public void managePacket(DatagramPacket packet) {
        byte[] data = packet.getData();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        int idProtocol = buffer.getInt();

        LoggingUtilities.logPacketDebug(logger, packet, "Received packet");

        if (idProtocol == this.idProtocol) {
            int playerId = buffer.getInt();

            if (playerHandlerMap.containsKey(playerId)) {
                PlayerPacketHandler playerPacketHandler = playerHandlerMap.get(playerId).getPlayerPacketHandler();

                this.executorService.submit(() -> playerPacketHandler.handleDatagramPacket(packet));

            } else {
                this.executorService.submit(() -> newConnectionHandler.handleDatagramPacket(packet));
            }
        }else{
            logger.info("Wrong protocol id");
        }
    }

    @Override
    public void setNewConnectionHandler(NewConnectionHandler newConnectionHandler) {
        this.newConnectionHandler = newConnectionHandler;
    }

    @Override
    public void addPlayerHandler(PlayerHandler handler) {
        playerHandlerMap.put(handler.getUserId(), handler);
    }

    @Override
    public void removePlayerHandler(PlayerHandler handler) {
        playerHandlerMap.remove(handler.getUserId());
    }

    @Override
    public void run() {

    }
}
