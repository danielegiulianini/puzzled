package org.danielegiulianini.puzzled.commons;

import java.awt.image.BufferedImage;

import io.vertx.core.json.Json;

/*
 * Class that models an image to be sent over the network (byte array (base64) encoded).
 *  */
public class CustomImage {

	private int width, height;
	private byte[] imagePixels;

	//for jackson de-serialization
	public CustomImage() {}
	
	public CustomImage(BufferedImage image) {
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.imagePixels = ImageUtils.convertBufferedImageToByteArrayOrReturnNullOnError(image);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public byte[] getImagePixels() {
		return imagePixels;
	}

	public String toJsonString() {
		//byte[] is serialized by Jackson library with base64 encoding
		return Json.encodePrettily(this);
	}

}
