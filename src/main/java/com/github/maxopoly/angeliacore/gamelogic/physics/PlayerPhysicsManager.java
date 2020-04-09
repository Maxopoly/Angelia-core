package com.github.maxopoly.angeliacore.gamelogic.physics;

import java.util.List;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.ThePlayer;
import com.github.maxopoly.angeliacore.model.block.ChunkHolder;
import com.github.maxopoly.angeliacore.model.block.states.BlockState;
import com.github.maxopoly.angeliacore.model.entity.AABB;
import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.Vector;
import com.github.maxopoly.angeliacore.model.location.Velocity;

/**
 * Heavy inspiration for this class has been taken from mineflayers physics implementation
 * https://github.com/PrismarineJS/mineflayer/blob/master/lib/plugins/physics.js
 *
 */
public class PlayerPhysicsManager {
	
	private static final double DELTA = 1E-9;
	private static final double GRAVITY = 27;
	private static final double TERMINAL_VELOCITY = -20;
	private static final double WALKING_SPEED = 4.317;
	private static final double SPRINTING_MULTIPLIER = 1.3;
	
	private ThePlayer player;
	private int ticksPerSecond;
	private double tickInverse;
	private ChunkHolder chunkHolder;
	private List<Vector> queuedMovement;
	
	public PlayerPhysicsManager(ThePlayer player, int ticksPerSecond, ChunkHolder chunkHolder) {
		this.player = player;
		this.ticksPerSecond = ticksPerSecond;
		this.chunkHolder = chunkHolder;
		this.tickInverse = 1.0 / ticksPerSecond;
	}
	
	public Location getNext() {
		Vector velocity = new Vector();
		Vector acceleration = new Vector();
		AABB offSetAABB = player.getBoundingBox().move(player.getLocation().add(0, - DELTA, 0));
		Location playerLoc = player.getLocation();
		Location below = playerLoc.add(0, -1, 0);
		double y = playerLoc.getY();
		boolean onGround = false;
		
		//gravity
		if (y - Math.floor(y) > DELTA) {
			//not just standing on a block, check whats going on
			BlockState bottomHalf = chunkHolder.getBlockAt(playerLoc);
			if (offSetAABB.intersects(bottomHalf.getBoundingBox().move(playerLoc))) {
				onGround = true;
			}
			else {
				acceleration = acceleration.add(0, GRAVITY, 0);
			}
		}
		else {
			//check block below to see whether we want to fall further
			BlockState belowBlock = chunkHolder.getBlockAt(below);
			if (offSetAABB.intersects(belowBlock.getBoundingBox().move(below))) {
				onGround = true;
			}
			else {
				acceleration = acceleration.add(0, GRAVITY, 0);
			}
		}
		
		//movement generated clientside
		for(Vector movement : queuedMovement) {
			acceleration = acceleration.add(movement);
		}
		
		//acceleration and velocity are both in m/s²
		velocity = velocity.add(acceleration);
		
		//bound terminal velocity
		velocity = new Vector(velocity.getX(), Math.max(TERMINAL_VELOCITY, velocity.getY()), velocity.getZ());
		
		
		//convert from m/s² to meter/tick, so divide by the inverse of ticks per second
		Vector offSet = velocity.multiply(tickInverse);
		
		//TODO collision checks
		
		
		//ground friction, we decrease velocity by walking speed
		if(onGround) {
			Vector onGroundDeceleration = velocity.add(0, - velocity.getY()  ,0);
			onGroundDeceleration = onGroundDeceleration.getOpposite();
			onGroundDeceleration = onGroundDeceleration.normalize();
			double multiplier = player.isSprinting() ? WALKING_SPEED * SPRINTING_MULTIPLIER : WALKING_SPEED;
			onGroundDeceleration = onGroundDeceleration.multiply(multiplier);
			velocity = velocity.add(onGroundDeceleration);
		}
		
		//TODO set location
		//TODO set on ground state
		
		return null;
	}

}
