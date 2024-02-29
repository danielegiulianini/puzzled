package org.danielegiulianini.backend.constants;

public class Constants {

	public class HTTP{
		
		public class ResponseCode {
			public static final int OK = 200;
			public static final int CREATED = 201;
			public static final int NOT_FOUND = 404;
			public static final int BAD_REQUEST = 400;
		}
		
		public class HeaderElement {
			public static final String CONTENT_TYPE = "content-type";
			
			public class ContentType {
				public static final String APPLICATION_JSON = "application/json; charset=utf-8";
			}
		}
	}
}
