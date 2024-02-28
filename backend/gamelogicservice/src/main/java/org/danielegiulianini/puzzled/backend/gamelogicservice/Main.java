package org.danielegiulianini.puzzled.backend.gamelogicservice;

import io.vertx.core.Vertx;
import org.danielegiulianini.backend.constants.ServicesInfo;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Vertx.vertx().deployVerticle(new GameLogicService(ServicesInfo.GAME_LOGIC_SERVICE_PORT));
        Thread.sleep(1000);
    }
}