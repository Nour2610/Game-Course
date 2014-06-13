package src;
import java.awt.LayoutManager;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;

import src.eg.edu.guc.loa.engine.Board;
import src.eg.edu.guc.loa.engine.BoardCell;

public class TheCheckers extends JPanel {

	int ColorNumber;

	public TheCheckers(int x) {
		ColorNumber = x;
		//setLayout(new FlowLayout()); // why?
		//setSize(5, 25);
		//setBackground(Color.BLACK);
		//setVisible(true);
	}

	public void setColorNumber(int x) {
		ColorNumber = x;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Ellipse2D elip = new Ellipse2D.Double(2.5, 12.5, 40, 40);

		if (ColorNumber == 1) {
			g.setColor(Color.PINK);
		}
		if (ColorNumber == 2) {
			g.setColor(Color.WHITE);
		}
		if (ColorNumber == 0) {
			g.setColor(getBackground());
		}
		((Graphics2D) g).fill(elip);
	}

	public static void main(String[] args) {

		TheCheckers[][] white = new TheCheckers[8][8];

		JFrame LOA = new JFrame();

		LOA.setSize(500, 500);

		LOA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		LOA.setVisible(true);

		JPanel loaPanel = new JPanel();

		loaPanel.setLayout(new GridLayout(8, 8));

		LOA.add(new GameBoard());
	}
}