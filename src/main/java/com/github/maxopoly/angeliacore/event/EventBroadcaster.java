package com.github.maxopoly.angeliacore.event;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class EventBroadcaster {

	private Map<Class<? extends AngeliaEvent>, List<MethodListenerTuple>> listenerMapping;
	private Logger logger;

	public EventBroadcaster(Logger logger) {
		this.listenerMapping = new HashMap<Class<? extends AngeliaEvent>, List<MethodListenerTuple>>();
		this.logger = logger;
	}

	public void registerListener(AngeliaListener listener) {
		for (Method method : listener.getClass().getMethods()) {
			if (!method.isAccessible()) {
				// method should be accessible, so public for any outside use case
				continue;
			}
			if (!method.getReturnType().equals(Void.TYPE)) {
				// method should not return anything
				continue;
			}
			boolean eventHandlerAnnotationFound = false;
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation instanceof AngeliaEventHandler) {
					eventHandlerAnnotationFound = true;
					break;
				}
			}
			if (!eventHandlerAnnotationFound) {
				// method is missing the annotation, we dont care about it
				continue;
			}
			if (method.getParameterCount() != 1) {
				// parameter should only be the event object
				continue;
			}
			Class eventClass = method.getParameterTypes()[0];
			if (!AngeliaEvent.class.isAssignableFrom(eventClass)) {
				// only parameter is not a subtype of the event class
				continue;
			}
			// we found a valid listener method at this point
			List<MethodListenerTuple> existingListeners = listenerMapping.get(eventClass);
			if (existingListeners == null) {
				existingListeners = new LinkedList<EventBroadcaster.MethodListenerTuple>();
				listenerMapping.put(eventClass, existingListeners);
			}
			existingListeners.add(new MethodListenerTuple(method, listener));
		}
	}

	public void broadcast(AngeliaEvent e) {
		List<MethodListenerTuple> listeners = listenerMapping.get(e.getClass());
		if (listeners == null) {
			return;
		}
		for (MethodListenerTuple tuple : listeners) {
			try {
				tuple.method.invoke(tuple.listener, e);
			} catch (Exception ex) {
				// catching just any kind of exception isnt nice behavior, but this is where code outside of the core will run
				// and we dont want the exceptions of that code to mess with the core
				logger.error("Executing listener in class " + tuple.listener.getClass() + " threw exception ", ex);
			}
		}
	}

	private class MethodListenerTuple {
		private Method method;
		private AngeliaListener listener;

		private MethodListenerTuple(Method m, AngeliaListener listener) {
			this.method = m;
			this.listener = listener;
		}
	}
}
