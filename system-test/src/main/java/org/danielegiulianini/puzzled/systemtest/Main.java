package org.danielegiulianini.puzzled.systemtest;

import io.vertx.core.Vertx;
import org.danielegiulianini.puzzled.backend.gamelogicservice.GameLogicService;
import org.danielegiulianini.puzzled.backend.gateway.PuzzleApiGateway;
import org.danielegiulianini.puzzled.backend.managementservice.PuzzlesManagementService;
import org.danielegiulianini.puzzled.commons.constants.ServicesInfo;
import org.danielegiulianini.puzzled.frontend.domain.ClientGameLogicManager;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        //service-side:
        Vertx.vertx().deployVerticle(new PuzzlesManagementService(ServicesInfo.PUZZLES_MANAGEMENT_SERVICE_PORT));

        Thread.sleep(1000);

        Vertx.vertx().deployVerticle(new GameLogicService(ServicesInfo.GAME_LOGIC_SERVICE_PORT));

        Thread.sleep(1000);

        new PuzzleApiGateway(ServicesInfo.API_GATEWAY_PORT);

        Thread.sleep(1000);

        //service-consumer-side:
        ClientGameLogicManager man1 = new ClientGameLogicManager();
        ClientGameLogicManager man2 = new ClientGameLogicManager();
    }
}