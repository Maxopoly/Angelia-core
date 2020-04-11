package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.crafting.CraftingRecipe;
import com.github.maxopoly.angeliacore.model.crafting.CraftingRecipe2x2;
import com.github.maxopoly.angeliacore.model.inventory.DummyInventory;
import com.github.maxopoly.angeliacore.model.inventory.PlayerInventory;
import com.github.maxopoly.angeliacore.model.item.ItemStack;
import com.github.maxopoly.angeliacore.model.item.Material;

public class Craft2x2 extends InventoryAction {

	private CraftingRecipe recipe;
	private byte windowID;
	private int amount;

	private boolean done;
	private int slotBeingHandled;
	private int movementsLeftForCurrent;
	private MoveItemAmount moveAction;
	private MoveItem takeResult;

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
		System.out.println("Ticking");
		PlayerInventory inv = (PlayerInventory) connection.getPlayerStatus().getInventory((byte) 0);
		if (inv == null) {
			this.done = true;
			this.successful = false;
			return;
		}
		if (takeResult != null) {
			// in the process of taking the result
			if (takeResult.isDone()) {
				System.out.println("Done");
				if (takeResult.wasSuccessful()) {
					// worked
					successful = true;
					done = true;
				} else {
					// try again
					System.out.println("FAILED, retry");
					setupResultTake();
					takeResult.execute();
				}
				return;
			}
			System.out.println("Still trying");
			takeResult.execute();
			return;
		}
		System.out.println(movementsLeftForCurrent);
		if (moveAction == null) {
			if (recipe.amountAvailable(inv.compress()) < amount
					|| -1 == inv.getPlayerStorage().findSlotByType(new ItemStack(Material.EMPTY_SLOT))) {
				// do initial checks
				this.done = true;
				this.successful = false;
				return;
			}
		}
		ItemStack is = recipe.getIngredient(slotBeingHandled);
		if (is.isEmpty()) {
			System.out.println("empty");
			if (++slotBeingHandled >= recipe.getSize()) {
				setupResultTake();
				takeResult.execute();
				return;
			} else {
				// more slots left to go, so we just repeat execution, dont need to waste a tick
				// on an empty slot
				execute();
				return;
			}
		}
		if (moveAction == null) {
			// open player inv
			new OpenPlayerInventory(connection).execute();
			movementsLeftForCurrent = amount;
			System.out.println("Setting up for maximum");
			// search for a storage slot which has the item we want and start moving from
			// there in the crafting slot
			resetMoveAction(inv);
			moveAction.execute();
			return;
		}
		if (!moveAction.isDone()) {
			moveAction.execute();
			return;
		}
		if (!moveAction.wasSuccessful()) {
			resetMoveAction(inv);
			// retry
			if (!isDone()) {
				execute();
			}
			return;
		}
		movementsLeftForCurrent -= moveAction.getAmountMoved();
		if (movementsLeftForCurrent == 0) {
			slotBeingHandled++;
		}
		if (slotBeingHandled == recipe.getSize()) {
			setupResultTake();
			takeResult.execute();
			return;
		}
		resetMoveAction(inv);
	}

	@Override
	public boolean isDone() {
		return done;
	}

	private void resetMoveAction(PlayerInventory inv) {
		ItemStack is = recipe.getIngredient(slotBeingHandled);
		DummyInventory storage = inv.getPlayerStorage();
		int slot = storage.findSlot(is);
		if (slot == -1) {
			// this shouldnt really happen as we checked beforehand that enough items are
			// around, but oh well
			connection.getLogger().warn("Crafting action ran out of materials half way through?");
			done = true;
			successful = false;
			return;
		}
		ItemStack foundStack = storage.getSlot(slot);
		this.moveAction = new MoveItemAmount(connection, windowID, inv.translateStorageSlotToTotal(slot),
				inv.translateCraftingSlotToTotal(slotBeingHandled),
				Math.min(foundStack.getAmount(), movementsLeftForCurrent));
	}

	private void setupResultTake() {
		PlayerInventory inv = connection.getPlayerStatus().getPlayerInventory();
		inv.updateSlot(inv.getCraftingResultID(), recipe.getResult());
		// move result to an empty slot
		this.takeResult = new MoveItem(connection, windowID, inv.getCraftingResultID(), inv.translateStorageSlotToTotal(
				inv.getPlayerStorage().findSlotByType(new ItemStack(Material.EMPTY_SLOT))));
	}

}
