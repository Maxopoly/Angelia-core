package com.github.maxopoly.angeliacore.event;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Logger;

public class EventBroadcaster {

	private Map<Class<? extends AngeliaEvent>, List<MethodListenerTuple>> listenerMapping;
	private Logger logger;

	public EventBroadcaster(Logger logger) {
		this.listenerMapping = new HashMap<Class<? extends AngeliaEvent>, List<MethodListenerTuple>>();
		this.logger = logger;
	}

	@SuppressWarnings("unchecked")
	public synchronized void registerListener(AngeliaListener listener) {
		for (Method method : listener.getClass().getMethods()) {
			if (!Modifier.isPublic(method.getModifiers())) {
				// method should be public for any outside use case
				continue;
			}
			if (!method.getReturnType().equals(Void.TYPE)) {
				// method should not return anything
				continue;
			}
			boolean eventHandlerAnnotationFound = false;
			boolean autoTransferListener = false;
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation instanceof AngeliaEventHandler) {
					eventHandlerAnnotationFound = true;
					autoTransferListener = ((AngeliaEventHandler) annotation).autoTransfer();
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
			Class<?> eventClass = method.getParameterTypes()[0];
			if (!AngeliaEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
				// only parameter is not a subtype of the event class
				continue;
			}
			// we found a valid listener method at this point
			internalRegister((Class<? extends AngeliaEvent>) eventClass, method, autoTransferListener, listener);
		}
	}

	private void internalRegister(Class<? extends AngeliaEvent> eventClass, Method method, boolean autoTransfer,
			AngeliaListener listener) {
		List<MethodListenerTuple> existingListeners = listenerMapping.get(eventClass);
		if (existingListeners == null) {
			existingListeners = new LinkedList<EventBroadcaster.MethodListenerTuple>();
			listenerMapping.put(eventClass, existingListeners);
		}
		method.setAccessible(true);
		existingListeners.add(new MethodListenerTuple(method, listener, autoTransfer));
	}

	public synchronized void broadcast(AngeliaEvent e) {
		List<MethodListenerTuple> listeners = listenerMapping.get(e.getClass());
		if (listeners == null) {
			return;
		}
		for (MethodListenerTuple tuple : listeners) {
			try {
				tuple.method.invoke(tuple.listener, e);
			} catch (Exception ex) {
				// catching just any kind of exception isnt nice behavior, but this is where
				// code outside of the core will run
				// and we dont want the exceptions of that code to mess with the core
				logger.error("Executing listener in class " + tuple.listener.getClass() + " threw exception ", ex);
			}
		}
	}

	public synchronized void unregisterListener(AngeliaListener lis) {
		for (Entry<Class<? extends AngeliaEvent>, List<MethodListenerTuple>> entry : listenerMapping.entrySet()) {
			List<MethodListenerTuple> currList = entry.getValue();
			for (int i = 0; i < currList.size(); i++) {
				MethodListenerTuple curr = currList.get(i);
				if (curr.listener == lis) {
					currList.remove(curr);
					i--;
				}
			}
		}
	}

	public synchronized void transferListeners(EventBroadcaster newInstance) {
		for (Entry<Class<? extends AngeliaEvent>, List<MethodListenerTuple>> entry : listenerMapping.entrySet()) {
			List<MethodListenerTuple> currList = entry.getValue();
			for (MethodListenerTuple curr : currList) {
				if (curr.autoTransfer) {
					internalRegister(entry.getKey(), curr.method, true, curr.listener);
				}
			}
		}
	}

	private class MethodListenerTuple {
		private Method method;
		private AngeliaListener listener;
		private boolean autoTransfer;

		private MethodListenerTuple(Method m, AngeliaListener listener, boolean autoTransfer) {
			this.method = m;
			this.listener = listener;
			this.autoTransfer = autoTransfer;
		}
	}
}
