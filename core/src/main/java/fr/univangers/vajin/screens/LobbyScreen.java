package fr.univangers.vajin.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import fr.univangers.vajin.SnakeRPG;
import fr.univangers.vajin.network.NetworkController;
import fr.vajin.snakerpg.jsondatabeans.PlayerBean;

import java.util.Iterator;

public abstract class LobbyScreen extends AbstractMenuScreen {

    protected final NetworkController networkController;
    private Table playerTable;

    @Override
    public void show() {
        super.show();
        if (playerTable == null) {
            this.playerTable = new Table();
        }
        updateTable();
    }

    public LobbyScreen(SnakeRPG parent, NetworkController networkController) {
        super(parent);
        this.networkController = networkController;
    }

    public void updateTable() {
        if (this.getApplication().getScreen() == this) {

            Skin skin = this.getApplication().getUISkin();

            playerTable.reset();

            Iterator<PlayerBean> it = this.networkController.getLobbyBean().getPlayers().iterator();
            while (it.hasNext()) {
                PlayerBean playerBean = it.next();

                String aliasLabelText = "";
                if (playerBean.getLocalId() == this.networkController.getIdPlayer()) {
                    aliasLabelText += "-> ";
                }
                aliasLabelText += playerBean.getAlias();
                Label aliasLabel = new Label(aliasLabelText, skin);
                playerTable.add(aliasLabel).fillX().colspan(3);
                if (it.hasNext()) {
                    playerTable.row().pad(10, 0, 0, 0);
                }
            }
        }
    }

    public Table getPlayerTable() {
        return playerTable;
    }

}
