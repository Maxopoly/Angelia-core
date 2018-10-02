package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.EntityActionPacket;

import java.io.IOException;

public class Sneak extends AbstractAction {

    private static boolean isSneaking;

    public Sneak(ServerConnection connection, boolean sneaking) {
        super(connection);
        isSneaking = sneaking;
    }

    @Override
    public void execute() {
        int entityId = connection.getPlayerStatus().getEntity().getEntityId();
        int sneaking = isSneaking ? 0 : 1; // 0 = Start sneaking, 1 = Stop sneaking
        try {
            connection.sendPacket(new EntityActionPacket(entityId, sneaking, 0));
        }
        catch (IOException error) {
            connection.getLogger().error("Failed to sneaking packet.", error);
            return;
        }
    }

    @Override
    public boolean isDone() {
        return !isSneaking;
    }

    @Override
    public ActionLock[] getActionLocks() {
        return new ActionLock[0];
    }

}