package Core;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Cards.DiscardCard;
import Cards.LightningStrikeCard;
import Cards.ShieldCard;
import Cards.StrikeCard;
import Cards.MoveCard;
import Cards.ShootCard;
import TypeListings.CardType;
import TypeListings.Direction;

public class Deck {

    // Deck() //Creates a deck of 20 cards Deck(int X) //Creates a deck of X cards

    private static Random rand = new Random();
    private static boolean seedSet = false;
    private final static long seed = System.currentTimeMillis();

    List<Card> deck = new ArrayList<Card>();
    List<Card> discard = new ArrayList<Card>();
    List<Card> drawn = new ArrayList<Card>();

    
    public int GetCardCount() {
        return deck.size();
    }
    public int GetCardCountDrawn() {
        return drawn.size();
    }
    public int GetCardCountDiscard() {
        return discard.size();
    }
    public int GetCardCountTotal() {
        return deck.size() + discard.size() + drawn.size();
    }
    
    // sends the referanced cards to the discard pile from drawn/deck, adds cards to
    // discard pile if not in either
    void Discard(Card ref) {
        boolean found = false;

        // checks if the card is being discarded from the "drawn" cards
        for (int i = 0; i < drawn.size() && found == false; i++) {
            if (drawn.get(i).GetCardID() == ref.GetCardID()) {
                found = true;
                drawn.remove(i);
                discard.add(ref);
            }
        }

        // checks if the card is being discard from the deck
        for (int i = 0; i < deck.size() && found == false; i++) {
            if (deck.get(i).GetCardID() == ref.GetCardID()) {
                found = true;
                deck.remove(i);
                discard.add(ref);
            }
        }

        // Adds if not already discarded
        // allows for extra cards to be added directly into the discard pile from
        // outside of the had
        if (!discard.contains(ref)) {
            discard.add(ref);
        }
    }

    // Discards the top X cards, If X < deck size, discards entire deck
    void Discard(int X) {
        if (deck.size() < X) {
            X = deck.size();
        }

        for (int i = 0; i < X; ++i) {
            discard.add(deck.get(0));
            deck.remove(0);
        }
    }

    Card DrawCard() {
        if (deck.size() == 0) {
            Shuffle();
        }

        // returns null if there are no cards that have not been drawn in the deck
        if (deck.size() == 0) {
            return null;
        }
        Card hold = deck.get(0);
        deck.remove(0);
        drawn.add(hold);
        return hold;
    }

    void AddCard(Card ref) {
        deck.add(ref);
    }

    // Shuffles the discard and deck together into deck
    void Shuffle() {
        while (!discard.isEmpty()) {
            deck.add(discard.get(0));
            discard.remove(0);
        }
        List<Card> hold = new ArrayList<Card>();
        hold.addAll(deck);
        deck.clear();
        int i = 0;
        while (!hold.isEmpty()) {
            i = rand.nextInt(hold.size());
            deck.add(hold.get(i));
            hold.remove(i);
        }
    }

    // creates a deck of size X
    Deck(int x) {
        if (seedSet == false) {
            rand.setSeed(seed);
            seedSet = true;
        }

        //ensures 1/10th of the deck is move up
        for(int i =0; i< x/10;++i) {
            deck.add(new MoveCard("Move Up", "Moves player up", Direction.UP, 1));
        }
        
        for (int i = 0; i < (x - (x/10)); ++i) {
            // TODO: update to be calling from resource manager
            Card hold = null;

            switch (CardType.values()[rand.nextInt(9)]) {
            case MOVE_UP:
                hold = new MoveCard("Move Up", "Moves player up", Direction.UP, 1);
                break;
            case MOVE_DOWN:
                hold = new MoveCard("Move Down", "Moves player down", Direction.DOWN, 1);
                break;
            case MOVE_LEFT:
                hold = new MoveCard("Move Left", "Moves player left", Direction.LEFT, 1);
                break;
            case MOVE_RIGHT:
                hold = new MoveCard("Move Right", "Moves player right", Direction.RIGHT, 1);
                break;
            case SHOOT:
                hold = new ShootCard("Shoots", "Fire a shot forwards");
                break;
            case STRIKE:
                hold = new StrikeCard("Strike", "Strikes a random enemy on the field");
                break;
            case SHIELD:
                hold = new ShieldCard("Shield", "Generates a shield to protect the player");
                break;
            case DISCARD:
                hold = new DiscardCard("Discard", "Discards all cards and re-draws");
                break;
            case LIGHTNINGSTRIKE:
                hold = new LightningStrikeCard("Lightning Strike", "Destorys random enemies on the field");
                break;
            default:
                hold = new MoveCard("Move Up", "Moves player up", Direction.UP, 1);
                break;
            }

            deck.add(hold);
        }
        
        Shuffle();
    }

    // creates a deck of size 20
    Deck() {
        if (seedSet == false) {
            rand.setSeed(seed);
            seedSet = true;
        }

        //ensure at least 2 move up cards
        for (int i=0; i<2;++i) {
            deck.add(new MoveCard("Move Up", "Moves player up", Direction.UP, 1));
        }
        
        for (int i = 0; i < 18; ++i) {
            // TODO: update to be calling from resource manager
            Card hold = null;

            switch (CardType.values()[rand.nextInt(9)]) {
            case MOVE_UP:
                hold = new MoveCard("Move Up", "Moves player up", Direction.UP, 1);
                break;
            case MOVE_DOWN:
                hold = new MoveCard("Move Down", "Moves player down", Direction.DOWN, 1);
                break;
            case MOVE_LEFT:
                hold = new MoveCard("Move Left", "Moves player left", Direction.LEFT, 1);
                break;
            case MOVE_RIGHT:
                hold = new MoveCard("Move Right", "Moves player right", Direction.RIGHT, 1);
                break;
            case SHOOT:
                hold = new ShootCard("Shoots", "Fire a shot forwards");
                break;
            case STRIKE:
            	hold = new StrikeCard("Strike", "Strikes a random enemy on the field");
            	break;
            case SHIELD:
            	hold = new ShieldCard("Shield", "Generates a shield to protect the player");
            	break;
            case DISCARD:
            	hold = new DiscardCard("Discard", "Discards all cards and re-draws");
            	break;
            case LIGHTNINGSTRIKE:
            	hold = new LightningStrikeCard("Lightning Strike", "Destorys random enemies on the field");
            	break;
            default:
                hold = new MoveCard("Move Up", "Moves player up", Direction.UP, 1);
                break;
            }
            deck.add(hold);
        }
        Shuffle();
    }
    
    // Access to seed to allow the user to save the seed. Can be
    // loaded at a later stage to reconstitute game/deck conditions.
    public static long getSeed()
    {
    	return seed;
    }

}
