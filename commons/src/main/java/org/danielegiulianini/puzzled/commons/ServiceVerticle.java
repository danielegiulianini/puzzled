package org.danielegiulianini.puzzled.commons;


public abstract class ServiceVerticle extends LoggingVerticle {
	protected int localPort;

	public ServiceVerticle(int localPort) {
		this.localPort = localPort;
	}
	
	@Override public void start() {
		this.setup();
	}
	
	protected abstract void setup();
}
