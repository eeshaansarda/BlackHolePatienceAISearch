import java.util.ArrayList;

public interface Game {
    public boolean play(int pile, int card);
    public boolean gameOver();
    public ArrayList<Move> possibleMoves();
    public Game copy();
}
