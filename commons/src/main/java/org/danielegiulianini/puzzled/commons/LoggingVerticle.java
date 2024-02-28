package org.danielegiulianini.puzzled.commons;

import io.vertx.core.AbstractVerticle;

public class LoggingVerticle extends AbstractVerticle {
	
	//System.out is shared between verticles
	protected void log(final String msg) {
		System.out.println("[" + getClass().getSimpleName() + "] " + msg);
	}
}
