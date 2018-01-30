package fr.univangers.vajin.gamemodel;

import java.util.List;

public abstract class AbstractGameEngine implements GameEngine {

    List<GameEngineObserver> observerList;

    @Override
    public void addGameEngineObserver(GameEngineObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void removeGameEngineObserver(GameEngineObserver observer) {
        observerList.remove(observer);
    }

    protected void notifyOfNewEntity(Entity entity) {
        for (GameEngineObserver obs : observerList) {
            obs.notifyNewEntity(entity);
        }
    }

    protected void notifyOfRemovedEntity(Entity entity) {
        for (GameEngineObserver obs : observerList) {
            obs.notifyRemovedEntity(entity);
        }
    }

    protected void notifyOfGameEnd() {
        for (GameEngineObserver obs : observerList) {
            obs.notifyGameEnd();
        }
    }
}
