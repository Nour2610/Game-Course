package src.eg.edu.guc.loa.engine;

import java.util.ArrayList;

public class Board implements BoardInterface {

	public static final int SIZE = 8;
	private BoardCell[][] board;
	private int turn;

	public Board() {
		board = new BoardCell[SIZE][SIZE];
		turn = BoardCell.PLAYER_1_PIECE;
		initBoard();
	}

	public BoardCell[][] getBoard() {
		return board;
	}

	private void initBoard() {

		// Initialize every board cell to a player 1 piece, player 2 piece or an
		// empty piece
		for (int rowCounter = 0; rowCounter < SIZE; rowCounter++) {
			for (int colCounter = 0; colCounter < SIZE; colCounter++) {
				if ((colCounter == 0 || colCounter == SIZE - 1)
						&& rowCounter != 0 && rowCounter != SIZE - 1) {
					board[rowCounter][colCounter] = new BoardCell(new Point(
							colCounter, rowCounter), BoardCell.PLAYER_2_PIECE);

				} else if ((rowCounter == 0 || rowCounter == SIZE - 1)
						&& colCounter != 0 && colCounter != SIZE - 1) {
					board[rowCounter][colCounter] = new BoardCell(new Point(
							colCounter, rowCounter), BoardCell.PLAYER_1_PIECE);

				} else {
					board[rowCounter][colCounter] = new BoardCell(new Point(
							colCounter, rowCounter), BoardCell.EMPTY);
				}
			}
		}
	}

	public int getTurn() {
		return turn;
	}

	private int getNextTurn() {
		// If turn is 1 returns 2 , if turn is 2 will return 1
		return 3 - turn;
	}

	public BoardCell getPiece(Point p) {
		return board[p.getY()][p.getX()];
	}

	public int getColor(Point p) {
		if (board[p.getY()][p.getX()].isEmpty()) {
			return BoardCell.EMPTY;
		}
		return board[p.getY()][p.getX()].getPlayer();
	}

	public ArrayList<Point> getPossibleMoves(Point start) {
		ArrayList<Point> result = new ArrayList<Point>();
		int colStart = start.getX();
		int rowStart = start.getY();
		int numOfPieces = 0;

		// col. up
		numOfPieces = getNumPiecesInCol(colStart);
		if (checkMove(rowStart, colStart, rowStart - numOfPieces, colStart)) {
			result.add(new Point(colStart, rowStart - numOfPieces));
		}
		// col. down
		if (checkMove(rowStart, colStart, rowStart + numOfPieces, colStart)) {
			result.add(new Point(colStart, rowStart + numOfPieces));
		}
		// row right
		numOfPieces = getNumPiecesInRow(rowStart);
		if (checkMove(rowStart, colStart, rowStart, colStart + numOfPieces)) {
			result.add(new Point(colStart + numOfPieces, rowStart));
		}
		// row left
		if (checkMove(rowStart, colStart, rowStart, colStart - numOfPieces)) {
			result.add(new Point(colStart - numOfPieces, rowStart));
		}
		// diag. north-east
		numOfPieces = getNumPiecesInDiagonal(rowStart, colStart, rowStart + 1,
				colStart - 1);
		if (checkMove(rowStart, colStart, rowStart + numOfPieces, colStart
				- numOfPieces)) {
			result.add(new Point(colStart - numOfPieces, rowStart + numOfPieces));
		}
		// diag. south-east
		numOfPieces = getNumPiecesInDiagonal(rowStart, colStart, rowStart + 1,
				colStart + 1);
		if (checkMove(rowStart, colStart, rowStart + numOfPieces, colStart
				+ numOfPieces)) {
			result.add(new Point(colStart + numOfPieces, rowStart + numOfPieces));
		}
		// diag. south-west
		numOfPieces = getNumPiecesInDiagonal(rowStart, colStart, rowStart - 1,
				colStart + 1);
		if (checkMove(rowStart, colStart, rowStart - numOfPieces, colStart
				+ numOfPieces)) {
			result.add(new Point(colStart + numOfPieces, rowStart - numOfPieces));
		}
		// diag. north-west
		numOfPieces = getNumPiecesInDiagonal(rowStart, colStart, rowStart - 1,
				colStart - 1);
		if (checkMove(rowStart, colStart, rowStart - numOfPieces, colStart
				- numOfPieces)) {
			result.add(new Point(colStart - numOfPieces, rowStart - numOfPieces));
		}
		return result;
	}

	public boolean move(Point start, Point end) {
		int startRow = start.getY();
		int startCol = start.getX();
		int endRow = end.getY();
		int endCol = end.getX();

		if (checkMove(startRow, startCol, endRow, endCol)) {
			board[endRow][endCol].setPlayer(board[startRow][startCol]
					.getPlayer());
			board[startRow][startCol].setPlayer(BoardCell.EMPTY);
			if (!isGameOver()) {
				turn = getNextTurn();
			}

			return true;
		}
		return false;
	}

	private boolean checkMove(int startRow, int startCol, int endRow, int endCol) {

		// Check point is not on boundaries
		if (endRow < 0 || endRow > SIZE - 1 || endCol < 0 || endCol > SIZE - 1) {
			return false;
		}

		// Not valid move if start is empty or is not a current player's turn
		// Or end end is not empty or contains current player's piece already.

		if (board[startRow][startCol].isEmpty()
				|| board[startRow][startCol].getPlayer() != turn
				|| (!board[endRow][endCol].isEmpty() && board[endRow][endCol]
						.getPlayer() == turn)) {
			return false;
		}
		// moving vertically
		if (startCol == endCol) {
			return checkVerticalMove(startRow, startCol, endRow);
		}
		// moving horizontally
		if (startRow == endRow) {
			return checkHorizontalMove(startRow, startCol, endCol);
		}
		// moving diagonally
		if (Math.abs(startRow - endRow) == Math.abs(startCol - endCol)) {
			return checkDiagonalMove(startRow, startCol, endRow, endCol);
		}

		// Not a valid move direction
		return false;

	}

	private boolean checkVerticalMove(int startRow, int startCol, int endRow) {
		// Move must be according to number of pieces in the line of action
		if (Math.abs(endRow - startRow) != getNumPiecesInCol(startCol)) {
			return false;
		}
		// Set path to start from North to South
		int pathStart = Math.min(startRow, endRow);
		int pathEnd = Math.max(startRow, endRow);

		// Check along path that there are not pieces from the opponent
		for (int row = pathStart + 1; row < pathEnd; row++) {
			if (getColor(new Point(startCol, row)) == (getNextTurn())) {
				return false;
			}
		}

		return true;
	}

	private boolean checkHorizontalMove(int startRow, int startCol, int endCol) {
		// Move must be according to number of pieces in the line of action
		if (Math.abs(endCol - startCol) != getNumPiecesInRow(startRow)) {
			return false;
		}

		int pathStart = Math.min(startCol, endCol);
		int pathEnd = Math.max(startCol, endCol);

		// Check that there are no oponent's pieces on the way
		for (int colCounter = pathStart + 1; colCounter < pathEnd; colCounter++) {
			if (getColor(new Point(colCounter, startRow)) == (getNextTurn())) {
				return false;
			}
		}

		return true;
	}

	private boolean checkDiagonalMove(int startRow, int startCol, int endRow,
			int endCol) {
		// Move must be according to number of pieces in the line of action
		if (Math.abs(endRow - startRow) != getNumPiecesInDiagonal(startRow,
				startCol, endRow, endCol)) {
			return false;
		}

		// Check movement in which Horizontal Direction
		boolean isRight = startCol < endCol;
		// Check movement in which vertical Direction
		boolean down = startRow < endRow;

		// Initialize the value for the row and col counters to start point.
		int rowCounter = startRow;
		int colCounter = startCol;
		while (rowCounter != endRow) {
			// Not a valid move if an oponent's piece is on the way
			if (!board[rowCounter][colCounter].isEmpty()
					&& board[rowCounter][colCounter].getPlayer() != turn) {
				return false;
			}
			// Update counter for vertical direction
			if (down) {
				rowCounter++;
			} else {
				rowCounter--;
			}

			// Update counter for horizontal direction
			if (isRight) {
				colCounter++;
			} else {
				colCounter--;
			}
		}
		// No pieces opponent pieces were found on the way
		return true;
	}

	private int getNumPiecesInRow(int row) {
		int count = 0;

		for (int colCounter = 0; colCounter < SIZE; colCounter++) {
			if (!board[row][colCounter].isEmpty()) {
				count++;
			}
		}
		return count;
	}

	private int getNumPiecesInCol(int col) {
		int count = 0;
		for (int rowCounter = 0; rowCounter < SIZE; rowCounter++) {
			if (!board[rowCounter][col].isEmpty()) {
				count++;
			}
		}
		return count;
	}

	private int getNumPiecesInDiagonal(int startRow, int startCol, int endRow,
			int endCol) {
		int count = 0;
		// Check all positions on board and count only those that are on the
		// diagonal and not empty
		for (int rowCounter = 0; rowCounter < SIZE; rowCounter++) {
			for (int colCounter = 0; colCounter < SIZE; colCounter++) {
				// Position lies on the diagonal i.e. satisfies the line
				// equation
				// and position not empty
				if (Math.abs(endRow - rowCounter) == Math.abs(endCol
						- colCounter)
						&& Math.abs(rowCounter - startRow) == Math
								.abs(colCounter - startCol)
						&& !board[rowCounter][colCounter].isEmpty()) {
					count++;
				}
			}
		}
		return count;
	}

	private int getNumOfPiecesForPlayer(int player) {
		int count = 0;
		for (int rowCounter = 0; rowCounter <= SIZE - 1; rowCounter++) {
			for (int colCounter = 0; colCounter <= SIZE - 1; colCounter++) {
				if (!board[rowCounter][colCounter].isEmpty()
						&& board[rowCounter][colCounter].getPlayer() == player) {
					count++;
				}
			}
		}
		return count;
	}

	public boolean isGameOver() {
		if (isGameOver(BoardCell.PLAYER_1_PIECE)
				|| isGameOver(BoardCell.PLAYER_2_PIECE)) {
			return true;
		}
		return false;
	}

	public boolean isGameOver(int player) {
		Point playerPoint = getPlayerPoint(player);
		boolean[][] checked = new boolean[SIZE][SIZE];
		int connectedPieces = getConnectedPieces(playerPoint.getY(),
				playerPoint.getX(), player, checked);
		if (connectedPieces == getNumOfPiecesForPlayer(player)) {
			return true;
		}

		return false;
	}

	private Point getPlayerPoint(int player) {
		for (int rowCounter = 0; rowCounter <= SIZE - 1; rowCounter++) {
			for (int colCounter = 0; colCounter <= SIZE - 1; colCounter++) {
				if (!board[rowCounter][colCounter].isEmpty()
						&& board[rowCounter][colCounter].getPlayer() == player) {
					return board[rowCounter][colCounter].getLocation();
				}
			}
		}
		return null;
	}

	private int getConnectedPieces(int row, int col, int player,
			boolean[][] checked) {
		// This position row col does not contain a connected piece if
		if (row < 0 || row > SIZE - 1 // Outside board boundaries
				|| col < 0 || col > SIZE - 1 // Outside board boundaries
				|| board[row][col].isEmpty() // No Piece at the given position
				|| board[row][col].getPlayer() != player // Piece at postion
															// belongs to
															// opponent
				|| checked[row][col]) { // Position was previously checked
			return 0;
		}
		checked[row][col] = true;
		// Check adjacent Positions (recursive call)
		return 1 + getConnectedPieces(row - 1, col, player, checked)
				+ getConnectedPieces(row - 1, col + 1, player, checked)
				+ getConnectedPieces(row, col + 1, player, checked)
				+ getConnectedPieces(row + 1, col + 1, player, checked)
				+ getConnectedPieces(row + 1, col, player, checked)
				+ getConnectedPieces(row + 1, col - 1, player, checked)
				+ getConnectedPieces(row, col - 1, player, checked)
				+ getConnectedPieces(row - 1, col - 1, player, checked);
	}

	public int getWinner() {
		// Check if current player is winner
		if (isGameOver(turn)) {
			return turn;
		}
		// Check if opponent is winner.
		int nextTurn = getNextTurn();
		if (isGameOver(nextTurn)) {
			return nextTurn;
		}

		// No winner yet.
		return 0;

	}

	public void printBoard() {
		System.out
				.println("_____________________________________________________________________");
		for (int rowCounter = 0; rowCounter < board.length; rowCounter++) {
			for (int colCounter = 0; colCounter < board[rowCounter].length; colCounter++) {
				if (getColor(new Point(colCounter, rowCounter)) == BoardCell.EMPTY) {
					System.out.print("\t" + "-");
				} else {
					System.out.print("\t"
							+ getColor(new Point(colCounter, rowCounter)));
				}
			}
			System.out.println();
		}
		System.out
				.println("_____________________________________________________________________");
	}
}
