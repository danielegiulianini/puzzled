package org.danielegiulianini.puzzled.commons;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.danielegiulianini.puzzled.commons.ServiceVerticle;



public class RestFetcherService extends ServiceVerticle {
	
	public RestFetcherService(int localPort) {
		super(localPort);
	}

	protected WebClient client;
	
	protected void setup() {
		client = WebClient.create(vertx);
	}

	protected <T> void fetchFromRestService(int port, String host, String uri, Handler<HttpResponse<Buffer>> successHandler) {
		client
		.get(port, host, uri)
		.send(ar -> {
			if (ar.succeeded()) {
				successHandler.handle(ar.result());
			}
		});
	}
}

