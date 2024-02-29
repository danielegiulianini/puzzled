package org.danielegiulianini.puzzled.backend.managementservice;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import org.danielegiulianini.puzzled.commons.Puzzle;
import org.danielegiulianini.puzzled.commons.domain.PuzzleFactory;
import org.danielegiulianini.puzzled.commons.JsonUtils;
import org.danielegiulianini.puzzled.commons.ServiceVerticle;

import org.danielegiulianini.puzzled.commons.constants.Constants;

/* 
 * This is the REST API service that stores unsolved puzzles and provides CRUD 
 * operations to manage and manipulate them.
 */
public class PuzzlesManagementService extends ServiceVerticle {
	private PuzzlesStorage ps;

	public PuzzlesManagementService(int localPort) {
		super(localPort);
		this.ps = new PuzzlesStorage();
	}

	@Override
	protected void setup() {
		setupRoutes();

		vertx.executeBlocking(
				p -> loadData(p), 
				res -> log("ready at port: " + localPort));

	}

	//simple script that creates a puzzle
	private void loadData(Promise<Object> f) {
		log("inserting some data...");

		final int n = 3;
		final int m = 5;

		PuzzleFactory factory = new PuzzleFactory();
		log("puzzle factory created");

		Puzzle puzzle = factory.createPuzzle(n, m);
		log("created puzzle is "+ puzzle);

		ps.insertPuzzle(puzzle);
		f.complete();
	}

	private void setupRoutes() {
		Router router = Router.router(vertx);

		final Set<String> allowHeaders = new HashSet<>();
		allowHeaders.add("x-requested-with");
		allowHeaders.add("Access-Control-Allow-Origin");
		allowHeaders.add("origin");
		allowHeaders.add("Content-Type");
		allowHeaders.add("accept");

		final Set<HttpMethod> allowMethods = new HashSet<>();
		allowMethods.add(HttpMethod.GET);
		allowMethods.add(HttpMethod.POST);
		allowMethods.add(HttpMethod.PUT);
		allowMethods.add(HttpMethod.DELETE);

		router.route().handler(CorsHandler.create("*").allowedHeaders(allowHeaders).allowedMethods(allowMethods));

		router.get("/api/puzzles").handler(this::handleGetAllPuzzles);
		router.get("/api/puzzles/:puzzleId").handler(this::handleGetPuzzleById);
		router.post("/api/puzzles").handler(this::handlePostPuzzle);
		router.put("/api/puzzles").handler(this::handleUpdatePuzzle);
		router.delete("/api/puzzles/:puzzleId").handler(this::handleDeletePuzzle);

		
		getVertx()
		.createHttpServer()
		.requestHandler(router)			
		.listen(localPort);
	}

	private <T> void handleGetJson(final RoutingContext routingContext, Optional<T> obj) {
		if (obj.isPresent()) {
			HttpServerResponse response = routingContext.response();
			response.putHeader(Constants.HTTP.HeaderElement.CONTENT_TYPE, Constants.HTTP.HeaderElement.ContentType.APPLICATION_JSON).end(Json.encodePrettily(obj));
		} else {
			routingContext.response()
			.setStatusCode(Constants.HTTP.ResponseCode.NOT_FOUND)
			.end();
		}
	}

	private <T> void handleGetJson(final RoutingContext routingContext, Set<T> obj) {
		routingContext.response()
		.setStatusCode(Constants.HTTP.ResponseCode.OK)
		.putHeader(Constants.HTTP.HeaderElement.CONTENT_TYPE, Constants.HTTP.HeaderElement.ContentType.APPLICATION_JSON)
		.end(Json.encodePrettily(obj));
	}

	private void handleGetAllPuzzles(final RoutingContext routingContext) {
		log("received request for all puzzles");
		handleGetJson(routingContext, ps.getAllPuzzles());
	}

	private void handleGetPuzzleById(final RoutingContext routingContext) {
		String puzzleId = routingContext.request().getParam("puzzleId");
		handleGetJson(routingContext, ps.getPuzzleById(puzzleId));		
	}

	private void handlePostPuzzle(final RoutingContext routingContext) {
		final Optional<Puzzle> puzzle = JsonUtils.decodeJson(
				routingContext.getBodyAsString(), 
				Puzzle.class);

		if (puzzle.isPresent()) {
			ps.insertPuzzle(puzzle.get());
			routingContext.response()
			.setStatusCode(Constants.HTTP.ResponseCode.CREATED)
			.putHeader(Constants.HTTP.HeaderElement.CONTENT_TYPE, Constants.HTTP.HeaderElement.ContentType.APPLICATION_JSON)
			.end(puzzle.get().toJsonString());	
		} else {
			routingContext.response().setStatusCode(Constants.HTTP.ResponseCode.BAD_REQUEST)
			.end();
		}
	}

	private void handleUpdatePuzzle(final RoutingContext routingContext) {
		final String id = routingContext.request().getParam("puzzleId");

		final Optional<Puzzle> puzzleOpt = ps.getAllPuzzles().stream().filter(u -> u.getId().contentEquals(id)).findFirst();

		if (puzzleOpt.isPresent()) {
			final Optional<Puzzle> newPuzzle = JsonUtils.decodeJson(routingContext.getBodyAsString(), Puzzle.class);

			if (newPuzzle.isPresent()) {

				puzzleOpt.get().setAssigned(newPuzzle.get().isAssigned());

				routingContext.response()
				.putHeader(Constants.HTTP.HeaderElement.CONTENT_TYPE, Constants.HTTP.HeaderElement.ContentType.APPLICATION_JSON)
				.setStatusCode(Constants.HTTP.ResponseCode.OK).end(puzzleOpt.get().toString());
			} else {
				routingContext.response()
				.setStatusCode(Constants.HTTP.ResponseCode.BAD_REQUEST)
				.end();
			}
		} else {
			routingContext.response()
			.setStatusCode(Constants.HTTP.ResponseCode.NOT_FOUND)
			.end();
		}
	}
	
	private void handleDeletePuzzle(final RoutingContext routingContext) {
		final String id = routingContext.request().getParam("id");
		
		final Optional<Puzzle> user = ps.getAllPuzzles().stream().filter(u -> u.getId().contentEquals(id)).findFirst();

		if (user.isPresent()) {
			ps.removePuzzle(id);
			
			routingContext.response()
				.setStatusCode(Constants.HTTP.ResponseCode.OK)
				.end();
			
		} else {
			routingContext.response()
				.setStatusCode(Constants.HTTP.ResponseCode.NOT_FOUND)
				.end();
		}
	}
}
