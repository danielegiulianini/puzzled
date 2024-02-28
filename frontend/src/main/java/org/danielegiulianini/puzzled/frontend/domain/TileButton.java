package org.danielegiulianini.puzzled.frontend.domain;

import org.danielegiulianini.puzzled.commons.PuzzleTile;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class TileButton extends JButton {
	private static final long serialVersionUID = 1L;

	public TileButton(final PuzzleTile tile) {
		super(new ImageIcon(tile.getImage().getImagePixels()));
		
		addMouseListener(new MouseAdapter() {            
            @Override
            public void mouseClicked(MouseEvent e) {
            	setBorder(BorderFactory.createLineBorder(Color.red));
            }
        });
	}
}
