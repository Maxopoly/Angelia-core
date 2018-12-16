package com.github.maxopoly.angeliacore.plugin.parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParameterLoad {

	/**
	 * Is the parameter required? If set to true, an exception will be thrown if no
	 * option is given for it during plugin startup
	 * 
	 * @return Whether this option is required
	 */
	boolean isRequired() default false;

	/**
	 * The identifier used for this option when parsing it from command line. If
	 * this is not set, the variable name will be used
	 * 
	 * @return Identifier used for this option or an empty string if the variable
	 *         name should be used
	 */
	String id() default "";

	/**
	 * The path to use for this option when loading it from the plugins yaml config
	 * or an empty string if no path was specified
	 * 
	 * @return
	 */
	String configId() default "";

	/**
	 * Priorization order based on which is decided whether to use the direct input
	 * or yaml value
	 * 
	 * @return
	 */
	LoadPolicy policy() default LoadPolicy.PRIORIZE_INPUT;

}
