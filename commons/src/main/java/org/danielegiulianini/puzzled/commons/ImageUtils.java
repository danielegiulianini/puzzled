package org.danielegiulianini.puzzled.commons;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {
	
	public static byte[] convertBufferedImageToByteArrayOrReturnNullOnError(BufferedImage originalImage) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( originalImage, "jpg", baos );
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch(IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static BufferedImage convertFromByteArraytoBufferedImage(byte[] pixelArray, int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		byte[] array = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(pixelArray, 0, array, 0, array.length);
		return image;
	}
}
