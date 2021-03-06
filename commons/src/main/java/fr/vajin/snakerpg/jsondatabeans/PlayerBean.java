package fr.vajin.snakerpg.jsondatabeans;

public class PlayerBean {

    private int localId;
    private String alias;
    private int snakeEntityId;


    public PlayerBean(){

    }

    public PlayerBean(int localId, String alias, int snakeEntityId) {
        this.localId = localId;
        this.alias = alias;
        this.snakeEntityId = snakeEntityId;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getSnakeEntityId() {
        return snakeEntityId;
    }

    public void setSnakeEntityId(int snakeEntityId) {
        this.snakeEntityId = snakeEntityId;
    }
}
