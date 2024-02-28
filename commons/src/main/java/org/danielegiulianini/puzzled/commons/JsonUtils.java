package org.danielegiulianini.puzzled.commons;

import java.util.Collection;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import io.vertx.core.json.Json;

public class JsonUtils {

	public static <T> Optional<T> decodeJson(String json, Class<T> clazz) {
		try {
			return Optional.of(Json.decodeValue(json, clazz));	//return new ObjectMapper().readValue(rooms, List.class);
		} catch (Exception e) {
			return Optional.empty();
		}
	}
	
	//method for decoding collections
	public static <T1> Optional<T1> decodeJson(String json, Class<? extends Collection> collectionType, Class collectionContentType) {
		try {
			CollectionType typeReference =
				    TypeFactory.defaultInstance()
				    .constructCollectionType(collectionType, collectionContentType);
			return Optional.of(new ObjectMapper().readValue(json, typeReference)); //return new ObjectMapper().readValue(rooms, List.class);
		} catch (Exception e) {
			return Optional.empty();
		}
	}


}
