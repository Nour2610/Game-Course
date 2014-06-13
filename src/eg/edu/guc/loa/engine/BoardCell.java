package src.eg.edu.guc.loa.engine;

public class BoardCell {
	public static final int EMPTY = 0;
	public static final int PLAYER_1_PIECE = 1;
	public static final int PLAYER_2_PIECE = 2;
	
	private Point location;
	private int playerPiece;

	public BoardCell(Point location, int playerPiece) {
		this.location = location;
		this.playerPiece = playerPiece;
	}
	
	public Point getLocation() {
		return location;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
	
	public int getPlayer() {
		return playerPiece;
	}
	
	public void setPlayer(int player) {
		this.playerPiece = player;
	}
	
	public boolean isEmpty() {
		return playerPiece == BoardCell.EMPTY;
	}
}
