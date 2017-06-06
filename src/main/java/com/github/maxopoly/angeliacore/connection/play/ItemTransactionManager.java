package com.github.maxopoly.angeliacore.connection.play;

import java.util.Map;
import java.util.TreeMap;

/**
 * When moving items around in any kind of inventory, the client has to request a transaction, which is then confirmed
 * or denied by the server. Each transaction has a short identifying it, this short is initially determined by the
 * client. We just use a counter, like the vanilla client does. This class allows actual use cases of item transactions
 * to check whether a transaction was successfull or not, as the state will be updated here. Once a final (success or
 * denied) state has been looked up once, it will be removed from the tracking to avoid keeping track of the data thats
 * no longer needed
 *
 */
public class ItemTransactionManager {

	/**
	 * Possible states of a transaction. Initially the state will always be PENDING and then switch to ACCEPTED or DENIED
	 * as final state
	 *
	 */
	public enum State {
		PENDING, ACCEPTED, DENIED;
	}

	private short counter;
	private Map<Short, State> pendingTransactions;

	public ItemTransactionManager() {
		this.pendingTransactions = new TreeMap<Short, ItemTransactionManager.State>();
		this.counter = 0;
	}

	/**
	 * Gets an id for a new transaction and sets it's status to PENDING
	 * 
	 * @return Id of the action
	 */
	public synchronized short getActionTicket() {
		short id = counter++;
		pendingTransactions.put(id, State.PENDING);
		return id;
	}

	/**
	 * Updates the state of the transaction with the given id. This should only be used to change a set once to it's
	 * finale state (accepted or denied)
	 * 
	 * @param id
	 *          ID of the transaction
	 * @param status
	 *          Result of the transaction
	 */
	public synchronized void setState(short id, State status) {
		if (status != State.ACCEPTED && status != State.DENIED) {
			throw new IllegalArgumentException("State must be ACCEPTED or DENIED");
		}
		State currentState = pendingTransactions.get(id);
		if (currentState != State.PENDING) {
			throw new IllegalAccessError("State can't be changed if it's not PENDING");
		}
		pendingTransactions.put(id, status);
	}

	/**
	 * Retrieves the state of the action with the given id and removes the action from the tracking if it is in a final
	 * state (accepted or denied)
	 * 
	 * @param id
	 *          ID of the transaction
	 * @return State of the transaction
	 */
	public synchronized State getState(short id) {
		State state = pendingTransactions.get(id);
		if (state == null || state == State.PENDING) {
			return state;
		}
		// state is accepted or denied, so we cleanup, because the status was requested
		pendingTransactions.remove(id);
		return state;
	}

}
