import java.util.ArrayList;

public class DFSState {
    public Game game;
    public ArrayList<Move> moves;

    public DFSState(Game game) {
        this.game = game;
        this.moves = new ArrayList<>();
    }

    public DFSState(Game game, ArrayList<Move> moves) {
        this.game = game;
        this.moves = moves;
    }
}
