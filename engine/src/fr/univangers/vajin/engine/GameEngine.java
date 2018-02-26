package fr.univangers.vajin.engine;

import fr.univangers.vajin.engine.entities.Entity;
import fr.univangers.vajin.engine.field.Field;
import fr.univangers.vajin.engine.utilities.Position;

import java.util.List;

public interface GameEngine {

    List<Entity> getEntityList();

    Field getField();

    void computeTick();

    void sendInput(int player, int input);

    void addGameEngineObserver(GameEngineObserver observer);

    void removeGameEngineObserver(GameEngineObserver observer);

    int getPlayerScore(int player);

    boolean isGameOver();

    boolean doesAnEntityCoverPosition(Position position);
}