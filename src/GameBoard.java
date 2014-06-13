package src;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.eg.edu.guc.loa.engine.Board;
import src.eg.edu.guc.loa.engine.BoardCell;
import src.eg.edu.guc.loa.engine.Point;

public class GameBoard extends JPanel {

	Board gameBoard;

	TheCheckers[][] GameCheckers;

	boolean end = false;

	int ck = 1;

	Point P1;

	boolean over = false;

	MouseListener l = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {

			if (over) {
				return;
			}

			if (ck == 0) {
				ck = 1;
			} else {
				ck = 0;
			}

			Point enteredCell = null;

			for (int i = 0; i != 8; i++) {
				for (int j = 0; j != 8; j++) {

					if (e.getSource() == GameCheckers[i][j]) {
						enteredCell = new Point(i, j);
					}

				}
			}

			if (ck == 1) {

				System.out.println(enteredCell.getX() + " "
						+ enteredCell.getY());

				if (gameBoard.move(P1, enteredCell)) {

					int turn = gameBoard.getTurn();

					GameCheckers[enteredCell.getX()][enteredCell.getY()]
							.setColorNumber(turn);

					GameCheckers[P1.getX()][P1.getY()].setColorNumber(0);

					repaint();
				}

			} else {
				P1 = enteredCell;
			}

			if (enteredCell != null) {

				ArrayList<Point> PossiblePlaces = gameBoard
						.getPossibleMoves(enteredCell);

				for (int i = 0; i != PossiblePlaces.size(); i++) {
					GameCheckers[PossiblePlaces.get(i).getX()][PossiblePlaces
							.get(i).getY()].setBackground(Color.BLUE);
				}
			}

			if (enteredCell != null) {
				System.out.println(enteredCell.getX() + " "
						+ enteredCell.getY());
			}

			if (gameBoard.isGameOver()) {
				over = true;

				JFrame GameOver = new JFrame();

				GameOver.setVisible(true);

				GameOver.setLayout(new BorderLayout());

				GameOver.setSize(200, 200);

				JPanel OverPanel = new JPanel();

				JLabel Over = new JLabel("The Game Is Over Player"
						+ gameBoard.getWinner() + " Is The Winner");

				GameOver.add(Over);

				repaint();
			}

			if (GameCheckers[enteredCell.getX()][enteredCell.getY()].ColorNumber == 0) {

				P1 = null;

				ck = 1;

				return;
			}

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			for (int i = 0; i != 8; i++) {
				for (int j = 0; j != 8; j++) {

					if ((i + j) % 2 == 0) {
						GameCheckers[j][i].setBackground(Color.darkGray);
					} else {
						GameCheckers[j][i].setBackground(Color.gray);
					}

				}
			}

		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	};

	public GameBoard() {

		gameBoard = new Board();

		BoardCell[][] gameCells = gameBoard.getBoard();

		GameCheckers = new TheCheckers[8][8];

		this.setLayout(new GridLayout(8, 8));

		for (int i = 0; i != 8; i++) {
			for (int j = 0; j != 8; j++) {

				TheCheckers cell = new TheCheckers(gameCells[j][i].getPlayer());

				GameCheckers[j][i] = cell;

				if ((i + j) % 2 == 0) {
					GameCheckers[j][i].setBackground(Color.darkGray);
				} else {
					GameCheckers[j][i].setBackground(Color.gray);
				}

				cell.addMouseListener(l);

				this.add(cell);
			}
		}
	}

}
