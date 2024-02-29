package org.danielegiulianini.puzzled.backend.gateway;

import org.danielegiulianini.puzzled.commons.constants.ServicesInfo;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        new PuzzleApiGateway(ServicesInfo.API_GATEWAY_PORT);		//Vertx.vertx().deployVerticle(new PuzzleApiGateway(ServicesInfo.API_GATEWAY_PORT));

        Thread.sleep(1000);
    }

}
