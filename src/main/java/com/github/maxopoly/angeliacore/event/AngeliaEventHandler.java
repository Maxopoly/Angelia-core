package com.github.maxopoly.angeliacore.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to mark methods, which want to listen for specific
 * events
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AngeliaEventHandler {
	/**
	 * @return Whether this listener should automatically be carried over to new
	 *         connection instances upon reconnecting
	 */
	boolean autoTransfer() default false;
}
