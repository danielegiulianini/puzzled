package org.danielegiulianini.backend.domain;

import io.vertx.core.json.Json;

public class AbstractJsonable implements Jsonable {

    public String toJsonString() {
    	return Json.encodePrettily(this);
    }

}
