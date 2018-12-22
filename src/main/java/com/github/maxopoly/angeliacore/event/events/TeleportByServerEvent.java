package com.github.maxopoly.angeliacore.event.events;

import com.github.maxopoly.angeliacore.model.location.DirectedLocation;

/**
 * Called when the player is teleported by the player. This is useful to dermine
 * when the player is stuck running against a block, as he will be teleported
 * back.
 *
 */
public class TeleportByServerEvent implements AngeliaEvent {

	private DirectedLocation previousClientsideLocation;
	private DirectedLocation serverDictatedLocation;

	public TeleportByServerEvent(DirectedLocation previousClientsideLocation, DirectedLocation serverDictatedLocation) {
		this.previousClientsideLocation = previousClientsideLocation;
		this.serverDictatedLocation = serverDictatedLocation;
	}

	/**
	 * @return Location teleported (back) to
	 */
	public DirectedLocation getLocationTeleportedTo() {
		return serverDictatedLocation;
	}

	/**
	 * @return Where the client thought it was before it got teleported
	 */
	public DirectedLocation getPreviousClientSideLocation() {
		return previousClientsideLocation;
	}

}
