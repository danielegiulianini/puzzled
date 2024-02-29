package org.danielegiulianini.puzzled.backend.managementservice;

import io.vertx.core.Vertx;
import org.danielegiulianini.puzzled.commons.constants.ServicesInfo;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Vertx.vertx().deployVerticle(new PuzzlesManagementService(ServicesInfo.PUZZLES_MANAGEMENT_SERVICE_PORT));

        Thread.sleep(1000);
    }

}
