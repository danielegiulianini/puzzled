package org.danielegiulianini.backend.domain;

import org.danielegiulianini.puzzled.commons.Puzzle;
import org.danielegiulianini.puzzled.commons.PuzzleTile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import static org.danielegiulianini.puzzled.commons.LoggingUtils.log;


public class PuzzleFactory {

	private InputStream imageAsInputStream = PuzzleFactory.class.getResourceAsStream("/bletchley-park-mansion.jpg");

	public Puzzle createPuzzle(int rows, int columns) {
		Set<PuzzleTile> tiles = createTiles(rows, columns);
		return new Puzzle(tiles, columns, rows);
	}

	private Set<PuzzleTile> createTiles(int rows, int columns) {
		final BufferedImage image;
		Set<PuzzleTile> tiles = new HashSet<>();
		try {
			image = ImageIO.read(imageAsInputStream);
		} catch (IOException ex) {
			log("problems with image loading...");
			return null;
		}

		final int imageWidth = image.getWidth(null);
		final int imageHeight = image.getHeight(null);

		int position = 0;

		final List<Integer> randomPositions = new ArrayList<>();
		IntStream.range(0, rows*columns).forEach(item -> { randomPositions.add(item); }); 
		Collections.shuffle(randomPositions);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {

				BufferedImage imagePortion = image.getSubimage(
						j * imageWidth / columns, 
						i * imageHeight / rows, 
						(imageWidth / columns), 
						imageHeight / rows);

				PuzzleTile pt = new PuzzleTile(String.valueOf(position), imagePortion, position, randomPositions.get(position));
				tiles.add(pt);
				position++;
			}
		}
		return tiles;
	}

}


