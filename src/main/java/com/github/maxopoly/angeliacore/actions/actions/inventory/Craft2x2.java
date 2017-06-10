package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.crafting.CraftingRecipe;
import com.github.maxopoly.angeliacore.crafting.CraftingRecipe2x2;
import com.github.maxopoly.angeliacore.model.ItemStack;
import com.github.maxopoly.angeliacore.model.inventory.DummyInventory;
import com.github.maxopoly.angeliacore.model.inventory.PlayerInventory;

public class Craft2x2 extends InventoryAction {

	private CraftingRecipe recipe;
	private byte windowID;
	private int amount;

	private boolean done;
	private int slotBeingHandled;
	private int movementsLeftForCurrent;
	private MoveItemAmount moveAction;

	public Craft2x2(ServerConnection connection, CraftingRecipe2x2 recipe, int amount) {
		super(connection);
		this.recipe = recipe;
		this.windowID = 0;
		this.amount = amount;
		this.done = false;
		this.slotBeingHandled = 0;
	}

	@Override
	public void execute() {
		PlayerInventory inv = (PlayerInventory) connection.getPlayerStatus().getInventory((byte) 0);
		if (inv == null) {
			this.done = true;
			this.successfull = false;
			return;
		}
		if (recipe.amountAvailable(inv) < amount) {
			// we could just craft as many here as we can, but I like this behavior better as it means the execution either
			// completly works or it doesnt
			this.done = true;
			this.successfull = false;
			return;
		}
		ItemStack is = recipe.getIngredient(slotBeingHandled);
		if (is.isEmpty()) {
			if (++slotBeingHandled >= recipe.getSize()) {
				successfull = true;
				done = true;
				return;
			} else {
				// more slots left to go, so we just repeat execution, dont need to waste a tick on an empty slot
				execute();
				return;
			}
		}
		if (moveAction == null) {
			movementsLeftForCurrent = amount;
			// search for a storage slot which has the item we want and start moving from there in the crafting slot
			resetMoveAction(inv);
			moveAction.execute();
			return;
		}
		if (!moveAction.isDone()) {
			moveAction.execute();
			return;
		}
		if (!moveAction.wasSuccessfull()) {
			resetMoveAction(inv);
			// retry
			execute();
			return;
		}
		if (moveAction.wasSuccessfull()) {
			movementsLeftForCurrent -= moveAction.getAmountMoved();
		}
	}

	private void resetMoveAction(PlayerInventory inv) {
		ItemStack is = recipe.getIngredient(slotBeingHandled);
		DummyInventory storage = inv.getPlayerStorage();
		int slot = storage.findSlot(is);
		if (slot == -1) {
			// this shouldnt really happen as we checked beforehand that enough items are around, but oh well
			connection.getLogger().warn("Crafting action ran out of materials half way through?");
			done = true;
			successfull = false;
			return;
		}
		ItemStack foundStack = storage.getSlot(slot);
		this.moveAction = new MoveItemAmount(connection, windowID, inv.translateStorageSlotToTotal(slot),
				inv.translateCraftingSlotToTotal(slotBeingHandled), Math.min(foundStack.getAmount(), movementsLeftForCurrent));
	}

	@Override
	public boolean isDone() {
		return done;
	}

}
