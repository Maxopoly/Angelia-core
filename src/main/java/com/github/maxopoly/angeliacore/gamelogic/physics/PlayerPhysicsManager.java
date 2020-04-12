package com.github.maxopoly.angeliacore.gamelogic.physics;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionPacket;
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
	private List<Vector> queuedMovement;

	public PlayerPhysicsManager(ServerConnection connection, ThePlayer player) {
		this.player = player;
		this.connection = connection;
		this.queuedMovement = new LinkedList<>();
		GlobalConfig config = connection.getConfig();
		this.delta = config.getPhysicsDelta();
		this.gravity = config.getGravity();
		this.terminalVelocity = config.getTerminalVelocity();
		this.walkingSpeed = config.getPhysicsWalkingSpeed();
		this.sprintingMultiplier = config.getPhysicsSprintingMultiplier();
		this.playerHeight = config.getPhysicsPlayerHeight();
		this.drag = config.getPhysicsDrag();
	}

	public void queueVelocity(Vector movement) {
		this.queuedMovement.add(movement);
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
		// convert velocity to m/tick
		velocity.multiply(tickInverse);
		Vector acceleration = new Vector();

		Location playerLoc = player.getLocation();
		AABB offSetPlayerAABB = player.getBoundingBox().move(playerLoc);
		AABB downwardsOffsetPlayerAABB = offSetPlayerAABB.move(new Vector(0, - delta, 0));

		// converting gravity from meter/s^2 to meter/tick^2 required dividing by
		// tick/second twice
		double meterPerTickGravity = gravity * tickInverse * tickInverse;
		boolean onGround;

		// apply gravity
		Location offSetBlockStandingOnLoc = playerLoc.add(0, -delta, 0);

		BlockState blockStandingOn = offSetBlockStandingOnLoc.getBlockAt(connection);

		if (blockStandingOn != null && blockStandingOn.getID() != 0
				&& blockStandingOn.getOffsetBoundingBox(offSetBlockStandingOnLoc).intersects(downwardsOffsetPlayerAABB)) {
			// standing on a block
			onGround = true;
		} else {
			onGround = false;
			acceleration = acceleration.add(0, -meterPerTickGravity, 0);
		}

		// movement generated clientside
		for (Vector movement : queuedMovement) {
			acceleration = acceleration.add(movement);
		}

		// acceleration and velocity are both in m/s²
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
			if (lowerTargetBlock != null && lowerTargetBlock.getID() != 0) {
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
					offSetBlockStandingOnLoc = updatedTargetLoc.add(0, -delta, 0);
					blockStandingOn = offSetBlockStandingOnLoc.getBlockAt(connection);
					if (blockStandingOn != null && blockStandingOn.getID() != 0
							&& blockStandingOn.getOffsetBoundingBox(offSetBlockStandingOnLoc)
									.intersects(player.getBoundingBox().move(updatedTargetLoc))) {
						onGround = true;
					}
				}
			}
			// only do second collision check if first one passed
			if (!wouldMoveIntoBlock) {
				Location upperTarget = target.add(0, playerHeight, 0);
				BlockState upperTargetBlock = upperTarget.getBlockAt(connection);
				if (upperTargetBlock != null && upperTargetBlock.getID() != 0) {
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

		// ground friction, we decrease velocity by walking speed
		if (onGround) {
			// remove Y component
			velocity = new Vector(velocity.getX(), 0, velocity.getZ());
			double speedVectorLength = velocity.getLength();
			Vector slowDownVec = velocity.getOpposite();
			slowDownVec = slowDownVec.normalize();
			double multiplier = player.isSprinting() ? walkingSpeed * sprintingMultiplier : walkingSpeed;
			multiplier *= tickInverse;
			// prevent negative overshooting, so our friction doesn't accelerate us
			// backwards
			multiplier = Math.min(speedVectorLength, multiplier);
			slowDownVec = slowDownVec.multiply(multiplier);
			velocity = velocity.add(slowDownVec);
		}
		// revert velocity to m/s
		velocity.multiply(1 / tickInverse);

		// write out finished values
		player.updateLocation(result);
		player.setVelocity(velocity);
		player.setOnGround(onGround);
	}

}
