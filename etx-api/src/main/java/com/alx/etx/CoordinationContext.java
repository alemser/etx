package com.alx.etx;

import java.util.HashMap;
import java.util.Map;

import com.alx.etx.logging.Log;

/**
 * The coordination context for controlling the coordinations.
 * 
 * @author alemser
 */
public class CoordinationContext {
	
	static ThreadLocal<CoordinationContext> CONTEXT = new ThreadLocal<CoordinationContext>();
	
	static Log log = Log.newLogger(CoordinationContext.class.getName());
	
	private Map<String, Object> properties;
	private Coordination coordination;
	
	private CoordinationContext() {
		this.coordination = new Coordination();
		this.properties = new HashMap<String, Object>();
	}
	
	public static CoordinationContext getCurrent() {
		return getCurrent(true);
	}
	
	/**
	 * Returns the current coontext.
	 * @param createsIfNotExists if true, creates one if there is no current context.
	 * @return the current context or null if createsIfNotExists is false and there is no current context.
	 */
	public static CoordinationContext getCurrent(boolean createsIfNotExists) {
		CoordinationContext ctx = CONTEXT.get();
		if( ctx == null && createsIfNotExists ) {
			log.debug("Creating the coordination context");
			ctx = new CoordinationContext();
		}
		
		return ctx;
	}
	
	public Coordination getCoordination() {
		return coordination;
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}
}
