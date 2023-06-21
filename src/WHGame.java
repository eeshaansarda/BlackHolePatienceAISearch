import java.util.ArrayList;

// Implement possibleMoves and dfs together, so that don't have to store and then iterate over it again

public class WHGame extends BHLayout implements Game {
    private int wormhole;

    public WHGame(BHLayout layout) {
        super(layout);
        this.wormhole = -1;
    }

    public WHGame(WHGame old) {
        this.holecard = old.holeCard();
        this.numranks = old.numRanks();
        this.numsuits = old.numSuits();
        this.numpiles = old.numPiles();
        this.layout = old.copyLayout();
        this.wormhole = old.wormhole();
    }

    public int wormhole() {
        return wormhole;
    }

    public Game copy() {
        return new WHGame(this);
    }

    @Override
    public int topCard(int pile) {
        if(pile == -1) return wormhole;
        return super.topCard(pile);
    }

    public boolean gameOver() {
        for(ArrayList<Integer> pile: layout) {
            if(pile.size() != 0) return false;
        }
        return wormhole == -1;
    }

    // top card returns -1, and we check top card == - card
    // if card is 1, then you shit yourself
    public boolean play(int pile, int card) {
        int topCard = topCard(pile);
        // works without isAdjacent here TODO
        // can create a test case for that
        if (topCard == card && pile == -1 && wormhole != -1 && isAdjacent(topCard, holeCard())) {
            holecard = topCard;
            wormhole = -1;
            return true;
        } else if(topCard == -card && wormhole == -1 && pile != -1 && pileSize(pile) != 0) {
            wormhole = topCard;
            layout.get(pile).remove(pileSize(pile) - 1);
            return true;
        } else if(topCard == card && isAdjacent(card, holeCard())) {
            holecard = topCard;
            layout.get(pile).remove(pileSize(pile) - 1);
            return true;
        }
        return false;
    }

    public ArrayList<Move> possibleMoves() {
        ArrayList<Move> possiblemoves = new ArrayList<Move>();

        if(isAdjacent(wormhole, holeCard())) {
            possiblemoves.add(new Move(-1, wormhole));
        }

        for(int i = 0; i < numPiles(); i++) {
            int topcard = topCard(i);
            if(isAdjacent(topcard, holeCard())) {
                possiblemoves.add(new Move(i, topcard));
            }
            if(wormhole == -1 && topcard != -1) {
                possiblemoves.add(new Move(i, -topcard));
            }
        }

        return possiblemoves;
    }

}
