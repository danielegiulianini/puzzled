package org.danielegiulianini.puzzled.frontend.domain;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pcd.ass03.puzzle.mysol.commons.Position;

/*
 * Class responsible for displaying other-users pointers during puzzle solving.
 * Constraint: container's layout manager must be different from FlowLayout to 
 * correctly show the image.
 * */
public class PointersDrawer extends JPanel {
	private static final long serialVersionUID = 1L;

	String imagePath = "src/main/java/pcd/ass03/puzzle/mysol/client/domain/pointer_small.png";;//"src/main/java/pcd/ass03/puzzle/concentrated/bletchley-park-mansion.jpg";
	private Map<String, Position> pointersPositionsByUserId;	//Map<String, Position> pointersImagesByUserId;

	private static Image pointerImage;
	private static final int DEFAULT_POINTER_IMAGE_WIDTH = 100;
	private static final int DEFAULT_POINTER_IMAGE_HEIGHT = 100;

	//constructor code run on main thread
	public PointersDrawer(Container container) {
		SwingUtilities.invokeLater(() -> {
			container.add(this);
			setOpaque(false);
		});

		pointerImage = loadImage();
		this.pointersPositionsByUserId = new HashMap<>();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		this.pointersPositionsByUserId.values().forEach(p -> {
			g2.drawImage(
					pointerImage,
					(int) p.getX(), 
					(int) p.getY(),
					DEFAULT_POINTER_IMAGE_WIDTH,
					DEFAULT_POINTER_IMAGE_HEIGHT,
					null);
		});

		revalidate();
	}

	//prefer replacing the position instead of updating it
	public void updatePositionOf(String userId, Position p) {
		this.pointersPositionsByUserId.put(userId, p);
		repaint();
	}

	public void deleteUser(String userId) {
		this.pointersPositionsByUserId.remove(userId);
		repaint();
	}

	private Image loadImage() {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(imagePath));
			System.out.println("image loaded");
		} catch (IOException ex) {
			ex.printStackTrace();		
		}

		return image;
	}
}
