package com.github.maxopoly.angeliacore.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark methods, which want to listen for specific
 * events
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AngeliaEventHandler {
	/**
	 * @return Whether this listener should automatically be carried over to new
	 *         connection instances upon reconnecting
	 */
	boolean autoTransfer() default true;
}
