package com.github.maxopoly.angeliacore.gamelogic.physics;

import java.io.IOException;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionPacket;
import com.github.maxopoly.angeliacore.event.events.angelia.ClientPhysicsEvent;
import com.github.maxopoly.angeliacore.libs.yaml.config.GlobalConfig;
import com.github.maxopoly.angeliacore.model.ThePlayer;
import com.github.maxopoly.angeliacore.model.block.states.BlockState;
import com.github.maxopoly.angeliacore.model.entity.AABB;
import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.Vector;

/**
 * Heavy inspiration for this class has been taken from mineflayers physics
 * implementation
 * https://github.com/PrismarineJS/mineflayer/blob/master/lib/plugins/physics.js
 *
 */
public class PlayerPhysicsManager {

	private double delta;
	private double gravity;
	private double terminalVelocity;
	private double walkingSpeed;
	private double sprintingMultiplier;
	private double playerHeight;
	private double drag;

	private ThePlayer player;
	private ServerConnection connection;
	private Vector queuedMovement;

	public PlayerPhysicsManager(ServerConnection connection, ThePlayer player) {
		this.player = player;
		this.connection = connection;
		GlobalConfig config = connection.getConfig();
		this.delta = config.getPhysicsDelta();
		this.gravity = config.getGravity();
		this.terminalVelocity = config.getTerminalVelocity();
		this.walkingSpeed = config.getPhysicsWalkingSpeed();
		this.sprintingMultiplier = config.getPhysicsSprintingMultiplier();
		this.playerHeight = config.getPhysicsPlayerHeight();
		this.drag = config.getPhysicsDrag();
	}

	/**
	 * A single acceleration value may be queued, which is added to the players
	 * current velocity upon the next tick. After each physics tick this value is
	 * set to null This value is in m/s^2. Calls to this method will overwrite any
	 * preexisting value
	 * 
	 * @param acceleration New acceleration value to apply to player on next physics
	 *                     tick
	 */
	public void queueAcceleration(Vector acceleration) {
		this.queuedMovement = acceleration;
	}

	/**
	 * A single acceleration value may be queued, which is added to the players
	 * current velocity upon the next tick. After each physics tick this value is
	 * set to null This value is in m/s^2. Values passed through this method will be
	 * added to any preexisting acceleration
	 * 
	 * @param acceleration New acceleration value to apply to player on next physics
	 *                     tick
	 */
	public void addAcceleration(Vector acceleration) {
		if (this.queuedMovement == null) {
			this.queuedMovement = acceleration;
		} else {
			this.queuedMovement.add(acceleration);
		}
	}

	/**
	 * @return Acceleration applied to the player on next physics tick in m/s^2
	 */
	public Vector getQueuedAcceleration() {
		return queuedMovement;
	}

	/**
	 * Makes a physic tick for the player and sends appropriate packets out
	 */
	public void updateState() {
		tickPhysics();
		try {
			Location loc = player.getLocation();
			connection.sendPacket(new PlayerPositionPacket(loc.getX(), loc.getY(), loc.getZ(), player.isOnGround()));
		} catch (IOException e) {
			connection.getLogger().error("Failed to update location", e);
		}
	}

	/**
	 * Does a physic tick for the player. Applies gravity and queued movement while
	 * taking terminal velocities and friction into account to emulate normal
	 * minecraft physics
	 */
	private void tickPhysics() {
		if (player.getHealth() <= 0) {
			// don't move when dead
			return;
		}
		double tickInverse = 1.0 / connection.getTicksPerSecond();
		// in m/s
		Vector velocity = player.getVelocity();
		Vector startingVelocity = velocity;
		boolean onGroundBefore = player.isOnGround();
		// convert velocity to m/tick
		velocity.multiply(tickInverse);
		Vector acceleration = new Vector();

		Location playerLoc = player.getLocation();

		// converting gravity from meter/s^2 to meter/tick^2 required dividing by
		// tick/second twice
		double meterPerTickGravity = gravity * tickInverse * tickInverse;
		boolean onGround;

		// apply gravity
		onGround = standingOnBlock(playerLoc);

		if (onGround) {
			// remove y component if facing downwards, effectively stopping an ongoing fall
			if (velocity.getY() < 0) {
				velocity = velocity.add(0, -velocity.getY(), 0);
			}
		} else {
			// accelerate downwards
			acceleration = acceleration.add(0, -meterPerTickGravity, 0);
		}

		// apply ground friction as inverse walking acceleration, even when midair
		// remove Y component
		Vector slowDownVec = new Vector(velocity.getX(), 0, velocity.getZ());
		double speedVectorLength = slowDownVec.getLength();
		slowDownVec = slowDownVec.getOpposite();
		slowDownVec = slowDownVec.normalize();
		double tickWalkingSpeed = walkingSpeed * tickInverse;
		double walkSpeedMultiplier = player.isSprinting() ? tickWalkingSpeed * sprintingMultiplier : tickWalkingSpeed;
		// prevent negative overshooting, so our friction doesn't accelerate us
		// backwards
		walkSpeedMultiplier = Math.min(speedVectorLength, walkSpeedMultiplier);
		slowDownVec = slowDownVec.multiply(walkSpeedMultiplier);
		velocity = velocity.add(slowDownVec);

		// movement generated clientside
		if (queuedMovement != null) {
			velocity = velocity.add(queuedMovement.multiply(tickInverse));
			queuedMovement = null;
		}

		// acceleration and velocity are both in m/ticks
		velocity = velocity.add(acceleration);

		// apply drag
		velocity = velocity.multiply(1.0 - drag);

		// bound terminal velocity, which is a negative number
		if (velocity.getY() < terminalVelocity) {
			velocity = new Vector(velocity.getX(), terminalVelocity, velocity.getZ());
		}

		// collision check with blocks at target location
		Location target = playerLoc.add(velocity);
		boolean wouldMoveIntoBlock = false;
		if (velocity.getLength() > delta) {
			// only do collision checks if we actually moved
			double multiplier = 1.0;
			BlockState lowerTargetBlock = target.getBlockAt(connection);
			if (lowerTargetBlock != null && lowerTargetBlock.hasCollision()) {
				// would move into block, so attempt to do a shorter step and find how far we
				// can go based on ray tracing
				AABB offsetLowerAABB = lowerTargetBlock.getBoundingBox().move(target.toBlockLocation());
				double distMultiplier = offsetLowerAABB.intersectRay(velocity, playerLoc);
				if (distMultiplier > 0) {
					multiplier = Math.min(multiplier, distMultiplier - delta);
				}
				Location updatedTargetLoc = playerLoc.add(velocity.multiply(multiplier));
				if (offsetLowerAABB.intersects(player.getBoundingBox().move(updatedTargetLoc))) {
					wouldMoveIntoBlock = true;
				} else {
					// recheck whether we're standing on a block now
					onGround = standingOnBlock(updatedTargetLoc);
				}
			}
			// only do second collision check if first one passed
			if (!wouldMoveIntoBlock) {
				Location upperTarget = target.add(0, playerHeight, 0);
				BlockState upperTargetBlock = upperTarget.getBlockAt(connection);
				if (upperTargetBlock != null && upperTargetBlock.hasCollision()) {
					AABB offsetUpperAABB = upperTargetBlock.getBoundingBox().move(upperTarget.toBlockLocation());
					double distMultiplier = offsetUpperAABB.intersectRay(velocity, playerLoc);
					if (distMultiplier > 0) {
						multiplier = Math.min(multiplier, distMultiplier - delta);
					}
					Location updatedTargetLoc = playerLoc.add(velocity.multiply(multiplier));
					if (offsetUpperAABB.intersects(player.getBoundingBox().move(updatedTargetLoc))) {
						wouldMoveIntoBlock = true;
					}
				}
			}
			if (multiplier < delta) {
				wouldMoveIntoBlock = true;
			} else {
				target = playerLoc.add(velocity.multiply(multiplier));
			}
		}
		Location result;
		if (wouldMoveIntoBlock) {
			result = playerLoc;
			// reset velocity, so it doesn't build up infinitely while running into a wall
			velocity = new Vector();
		} else {
			result = target;
		}

		// revert velocity to m/s
		velocity.multiply(1 / tickInverse);

		connection.getEventHandler().broadcast(
				new ClientPhysicsEvent(playerLoc, result, startingVelocity, velocity, onGroundBefore, onGround));

		// write out finished values
		player.updateLocation(result);
		player.setVelocity(velocity);
		player.setOnGround(onGround);
	}

	private boolean standingOnBlock(Location playerLoc) {
		double playerAABBRange = connection.getConfig().getPlayerAABBRadius();
		AABB offsetPlayerAABB = player.getBoundingBox().move(new Vector(0, -delta, 0));
		for (int i = 0; i < 4; i++) {
			// cycle through all 4 possible combinations
			double x = i < 2 ? playerAABBRange : -playerAABBRange;
			double z = i % 2 == 0 ? playerAABBRange : -playerAABBRange;
			Location offSetLoc = playerLoc.add(x, -delta, z);
			BlockState block = offSetLoc.getBlockAt(connection);
			if (block != null && block.hasCollision()
					&& block.getOffsetBoundingBox(offSetLoc).intersects(offsetPlayerAABB)) {
				return true;
			}
		}
		return false;
	}

}
