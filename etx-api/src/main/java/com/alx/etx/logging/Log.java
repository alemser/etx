package com.alx.etx.logging;

/**
 * Log message with the current logger.
 * TODO: add proper logging.
 */
public class Log {
	
	private String name;
	
	public Log(String name) {
		this.name = name;
	}
	
	public static Log newLogger(String name) {
		return new Log(name);
	}
	
	public void debug(String debug) {
		syso(debug);
	}
	
	public void info(String info) {
		syso(info);
	}
	
	public void err(String err) {
		err(err);
	}
	
	public void err(String err, Throwable t) {
		syso(err);
	}
	
	void syso(String s) {
		System.out.println("["+name+"] " + s);
	}
}
