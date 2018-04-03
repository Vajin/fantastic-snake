package fr.vajin.snakerpg.network.test;

import fr.univangers.vajin.network.impl.GamePacketHandler;
import fr.vajin.snakerpg.utilities.CustomByteArrayOutputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.management.Notification;
import java.nio.ByteBuffer;

public class GamePacketHandlerTest {

    byte[] getTestByteArrayUpdateEntity() {
        CustomByteArrayOutputStream stream = new CustomByteArrayOutputStream();


        Assertions.assertAll(() -> {
            stream.writeInt(1); //Id entity
            stream.writeInt(0); //Id tile
            stream.writeInt(0); //Pos X
            stream.writeInt(0); //Pos Y

            String ressourceKey = "ressourceKey";
            byte[] ressourceKeyBytes = ressourceKey.getBytes();
            stream.writeInt(ressourceKeyBytes.length);
            stream.write(ressourceKeyBytes);

            stream.writeInt(1); //Id Tile
            stream.writeInt(1); //Pos X
            stream.writeInt(0); //Pos Y
            stream.writeInt(ressourceKeyBytes.length);
            stream.write(ressourceKeyBytes);

            stream.writeInt(-1); //Fin entity 1

            stream.writeInt(-1); //Fin update

        });

        return stream.toByteArray();
    }

    @Test
    void testUpdateEntity() {
        GamePacketHandler gamePacketHandler = new GamePacketHandler();

        byte[] bytes = getTestByteArrayUpdateEntity();

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        NotificationCounter notificationCounter = new NotificationCounter();

        gamePacketHandler.getDistantEngine().addGameEngineObserver(notificationCounter);

        gamePacketHandler.getDistantEngine().beginChange();

        Assertions.assertTrue(gamePacketHandler.updateEntity(byteBuffer));

        Assertions.assertFalse(gamePacketHandler.updateEntity(byteBuffer));

        gamePacketHandler.getDistantEngine().endChange();

        Assertions.assertEquals(1, notificationCounter.getNewEntityNotificationCount());
    }
}
