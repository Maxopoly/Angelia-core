package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.ItemTransactionManager;
import com.github.maxopoly.angeliacore.connection.play.packets.out.ClickWindowPacket;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;
import com.github.maxopoly.angeliacore.model.item.ItemStack;
import java.io.IOException;

/**
 * Executes a single inventory click. The click can either be accepted or denied by the server and this action will only
 * finish once a status confirmation has been received by the server. Note that this only sends the packets for the
 * action, but does not update the client side model, which has to be done by whatever is using this class
 *
 */
public class ClickInventory extends AbstractAction {

	private byte windowID;
	private short slot;
	private byte button;
	private int mode;
	private ItemStack clickedSlot;
	private boolean packetSent;
	private boolean success;
	private boolean done;
	private short clickID;

	public ClickInventory(ServerConnection connection, byte windowID, short slot, byte button, int mode,
			ItemStack clickedSlot) {
		super(connection);
		this.windowID = windowID;
		this.slot = slot;
		this.button = button;
		this.mode = mode;
		this.done = false;
		this.clickedSlot = clickedSlot;
	}

	@Override
	public void execute() {
		if (done) {
			return;
		}
		ItemTransactionManager transManager = connection.getItemTransActionManager();
		if (!packetSent) {
			// click packet wasnt sent yet, so let's do that

			// first we check whether the inv even exists and only progress if it does
			Inventory inv = connection.getPlayerStatus().getInventory(windowID);
			if (inv == null) {
				done = true;
				success = false;
			}
			try {
				this.clickID = transManager.getActionTicket();
				ClickWindowPacket pickUpPacket = new ClickWindowPacket(windowID, slot, button, clickID, mode, clickedSlot);
				connection.sendPacket(pickUpPacket);
				packetSent = true;
			} catch (IOException e) {
				connection.getLogger().error("Failed to send inv click packet", e);
			}
		} else {
			// click packet was sent and we are awaiting confirmation
			ItemTransactionManager.State state = transManager.getState(clickID);
			switch (state) {
				case PENDING:
					// keep waiting
					return;
				case DENIED:
					success = false;
					done = true;
					break;
				case ACCEPTED:
					success = true;
					done = true;
					break;
			}
		}
	}

	/**
	 * An item transaction may be denied by the server. This method can be used afterwards to determine whether everything
	 * worked. Note that this will always return false while the transaction is unfinished.
	 * 
	 * @return True only if the transaction was successfull, false otherwise
	 */
	public boolean wasSuccessfull() {
		return success;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.INVENTORY, ActionLock.HOTBAR_SLOT };
	}

}
