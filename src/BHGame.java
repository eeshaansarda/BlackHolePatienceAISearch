import java.util.ArrayList;

// For game over, store an integer
// int nonEmptyPiles = numPiles();
// play() {
//     if(pileSize(pile == 0)) nonEmptyPiles--;
// }
// gameOver() {
//     return nonEmptyPiles == 0;
// }

public class BHGame extends BHLayout implements Game {

    public BHGame(BHLayout layout) {
        super(layout);
    }

    public BHGame(BHGame old) {
        this.holecard = old.holeCard();
        this.numranks = old.numRanks();
        this.numsuits = old.numSuits();
        this.numpiles = old.numPiles();
        this.layout = old.copyLayout();
    }

    public Game copy() {
        return new BHGame(this);
    }

    public boolean play(int pile, int card) {
        int topCard = topCard(pile);
        if(topCard == card && isAdjacent(card, holeCard())) {
            holecard = topCard;
            layout.get(pile).remove(pileSize(pile) - 1);
            return true;
        }
        return false;
    }

    public boolean gameOver() {
        for(ArrayList<Integer> pile: layout) {
            if(pile.size() != 0) return false;
        }
        return true;
    }

    public ArrayList<Move> possibleMoves() {
        ArrayList<Move> possiblemoves = new ArrayList<Move>();

        for(int i = 0; i < numPiles(); i++) {
            int topcard = topCard(i);
            if(isAdjacent(topcard, holeCard())) {
                possiblemoves.add(new Move(i, topcard));
            }
        }

        return possiblemoves;
    }

}
