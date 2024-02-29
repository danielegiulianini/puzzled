package org.danielegiulianini.puzzled.frontend.domain;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.danielegiulianini.puzzled.commons.Position;
import org.danielegiulianini.puzzled.commons.Puzzle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class PuzzleBoard extends JFrame implements OtherUsersEventsObserver {
	private static final long serialVersionUID = 1L;

	private ClientGameLogicManager man;
	private Puzzle puzzle;
	private PointersDrawer pointersDrawer;
	private JPanel board;

	public PuzzleBoard(ClientGameLogicManager man, Puzzle puzzle) {
		this.man = man;
		this.puzzle = puzzle;

		int rows = puzzle.getRows();
		int columns = puzzle.getCols();

		SwingUtilities.invokeLater(() -> {

			setTitle("Puzzle");

			//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosing(WindowEvent e)
				{
					man.onMyExit();
					e.getWindow().dispose();
					//System.exit(-1);
				}
			});

			setResizable(false);

			board = new JPanel();
			board.setBorder(BorderFactory.createLineBorder(Color.gray));
			board.setLayout(new GridLayout(rows, columns, 0, 0));
			getContentPane().add(board, BorderLayout.CENTER);

			MouseClicksRedirectingGlassPane glassPane = new MouseClicksRedirectingGlassPane(board);
			JRootPane rootPane = SwingUtilities.getRootPane(board);
			rootPane.setGlassPane(glassPane);

			pointersDrawer = new PointersDrawer(glassPane);

			glassPane.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					man.onMyPointerMoved(new Position(e.getX(), e.getY()));
				}
			});

			glassPane.activate(true);
			paintPuzzle(board);
		});
	}

	private void paintPuzzle(final JPanel board) {
		SwingUtilities.invokeLater(() -> {
			board.removeAll();

			puzzle.getTiles().stream().sorted().forEach(tile -> {
				final TileButton btn = new TileButton(tile);
				board.add(btn);
				btn.setBorder(BorderFactory.createLineBorder(Color.gray));

				btn.addMouseListener(new MouseAdapter() {
					@Override 
					public void mouseClicked(MouseEvent e) {
						man.onMyTileSelection(tile);
					}
				});
			});
			pack();	//setLocationRelativeTo(null);
		});
	}

	@Override
	public void onPuzzleCompleted() {
		JOptionPane.showMessageDialog(this, "Puzzle Completed!", "", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void onTilesSwap(String t1Id, String t2Id) {
		puzzle.swap(t1Id, t2Id);
		paintPuzzle(board);
	}

	@Override
	public void onCursorMoved(String userId, Position cursorPosition) {
		this.pointersDrawer.updatePositionOf(userId, cursorPosition);
	}

	public void onOtherUserExited(String userId) {
		pointersDrawer.deleteUser(userId);
	}

	public void display() {
		SwingUtilities.invokeLater(() -> this.setVisible(true));
	}

}

/*
 * 	//if events are too frequent with mouse listener use this method instead
 * private void startSendingCursorPosition(JPanel board) {
	Timer timer = new Timer(66, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Point p = MouseInfo.getPointerInfo().getLocation();
			int dx = p.x - board.getLocation().x;
			int dy = p.y - board.getLocation().y;
			p = new Point(
					dx < 0 ? 0 : dx > board.getWidth() ? board.getWidth() : dx, 
							dy < 0 ? 0 : dy> board.getWidth() ? board.getWidth() : dy);
			man.onMyPointerMoved(new Position(e.getX(), e.getY()));
		}
	});
	timer.start();
}*/
