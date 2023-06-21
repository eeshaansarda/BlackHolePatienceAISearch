import java.util.ArrayList;
import java.util.Random ;
import java.util.Iterator ;

// Starter by Ian Gent, Sep 2021
// This class is provided to save you writing some of the basic parts of the code
// Also to provide a uniform layout generator

// You may freely edit this code if you wish, e.g. adding methods to it. 
// Obviously we are aware the starting point is provided so there is no need to explicitly credit us
// Please clearly mark any new code that you have added/changed to make finding new bits easier for us

public class BHLayout {

    protected int holecard;
    protected int numranks;
    protected int numsuits;
    protected int numpiles;

    protected ArrayList<ArrayList<Integer>> layout;

    public int holeCard() { 
        return holecard;
    }
    public int numRanks() { 
        return numranks;
    }
    public int numSuits() { 
        return numsuits;
    }
    public int numPiles() { 
        return numpiles;
    }
    public int cardsInDeck() { 
        return numranks * numsuits; 
    }
    public int pileSize(int i) { 
        return layout.get(i).size();
    }

    // TODO use this??
    private boolean validCard(int card) {
        return card > 0 && card <= cardsInDeck();
    }

    public int cardAt(int pile, int position) { 
        if(pile >= numPiles() || position >= pileSize(pile)) { 
            return -1; }
        else {
            return layout.get(pile).get(position);
        }
    }
    public int topCard(int pile) { 
        if(pile >= numPiles() || pileSize(pile)<1) { 
            return -1; }
        else {
            return layout.get(pile).get(pileSize(pile)-1);
        }
    }

    protected boolean isAdjacent(int cardOne, int cardTwo) {
        if(cardOne == -1 || cardTwo == -1) return false;

        int oneMod = cardOne % numRanks(), twoMod = cardTwo % numRanks();
        return (oneMod - 1 == twoMod) || (oneMod + 1 == twoMod) ||
            (oneMod == 0 && twoMod == numRanks() - 1) || (oneMod == numRanks() - 1 && twoMod == 0);
    }

    protected ArrayList<ArrayList<Integer>> copyLayout() {
         ArrayList<ArrayList<Integer>> copy = new  ArrayList<ArrayList<Integer>>(0);
         for(int i=0;i<numPiles();i++) { 
             ArrayList<Integer> newpile = new ArrayList<Integer>(0);
             for(int j=0;j<pileSize(i);j++){
                 newpile.add(j,cardAt(i,j));
             }
             copy.add(newpile);
         }
         return copy; 
    }

    // deal out all cards except Ace of Spades (1)
    //
    public void randomise (int seed)   { 
        randomise(seed,cardsInDeck()-1);
    }

    public void randomise (int seed, int numInLayout)   { 
        int maxindex = cardsInDeck()-1;

        int[] cards = new int[maxindex] ;
        for (int i = 0; i < maxindex; i++) {
            cards[i] = i+2 ;
        	}
        // Completely shuffle the deck 
        //
        // Choose the sequence uniformly at random
        // NB random.nextInt(k) gives a value in range 0..k-1

        Random random = new Random(seed) ;

        for (int i = 0; i < maxindex-1; i++) {
            int temp = cards[i] ;
            int index = i+random.nextInt(maxindex-i) ;
            cards[i] = cards[index] ;
            cards[index] = temp ;
        	}

        // Now empty the piles
        //
        for (int i = 0; i < numPiles() ; i++) { 
            layout.get(i).clear();
        	}

        // Now put the right number of cards on the layout
        // For safety don't allow more cards than are available 

        int remaining=Math.min(numInLayout,maxindex);

        int nextpile = 0; 

        for (int i = 0; i < remaining ; i++) { 
            layout.get(nextpile).add(cards[i]);
            nextpile = (nextpile+1) % numPiles();
        	}

        holecard = 1; 
		}

		public void print() {
			System.out.println(numRanks() + " " + numSuits() + " " + numPiles());
			System.out.println(holeCard());
       	 	for (int i=0; i < numPiles() ; i++) { 
       	     	for(int j=0; j < pileSize(i) ; j++) {
                	System.out.print(layout.get(i).get(j)+" ");
            	}
            	System.out.println("-1");
        	}
		}


    // Helper function for constructors 
    //
    private void createLayout() { 
        this.layout = new ArrayList<ArrayList<Integer>>(numpiles);
        for(int i=0; i < numpiles; i++) { 
            layout.add(i,new ArrayList<Integer>(0));
        }
    }

    //
    //// Variety of Constructors
    //
	

    // create empty problem for standard deck with ace of spades in hole 
    
    public BHLayout() {
        this(13,4,17,1) ; 
    }
    //
    // create empty problem ace of spades in hole 
   public BHLayout(Integer ranks, Integer suits, Integer piles) {
        this(ranks,suits,piles,1);
	}

    // create empty layout with given parameters 
    //
   public BHLayout(Integer ranks, Integer suits, Integer piles, Integer hole) {
        this.holecard = hole ; // Ace of spades
        this.numranks = ranks;
        this.numpiles = piles;
        this.numsuits = suits;
        createLayout();
	}


    // Create layout from sequence of integers 

    public BHLayout(ArrayList<Integer> integers) { 
	Iterator<Integer> reader = integers.iterator();
        int r = (reader.hasNext() ? reader.next() : 0);
        int s = (reader.hasNext() ? reader.next() : 0);
        int p = (reader.hasNext() ? reader.next() : 0);
        int h = (reader.hasNext() ? reader.next() : 0);
        this.numranks = r;
        this.numsuits = s;
        this.numpiles = p;
        createLayout();
        this.holecard = h;
        int nextpile = 0;
        while( reader.hasNext() && (nextpile < p) ) { 
            int card = reader.next();
            if (card == -1) { 
                nextpile++;
            }
            else { 
                layout.get(nextpile).add(card);
            }
        }
    }

    // Copy Constructor
    //
    
    public BHLayout(BHLayout old) { 
        this.holecard = old.holeCard();
        this.numranks = old.numRanks();
        this.numsuits = old.numSuits();
        this.numpiles = old.numPiles();
        this.layout = old.copyLayout();
    }

}




