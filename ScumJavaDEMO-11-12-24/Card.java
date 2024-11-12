import java.util.Random;

//A "Deck" will be an ArrayList of Cards.
public class Card {

	//Variable Declarations
	private String type;
	private String suit;
	private int rank;
	
	private final String [] types = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
	private final String [] suits = {"Clubs", "Spades", "Hearts", "Diamonds"};
	private final int [] ranks = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
	
	//Default Card
	public Card () {
		
		type = types[0];
		suit = suits[0];
		rank = ranks[0];
	}
	
	//Preassigned Values Card
	public Card (String type, String suit, int rank) {
		
		this.type = type;
		this.suit = suit;
		this.rank = rank;
	}
	
	//*************************************************************************************************************
	
	//Randomize Card Values
	public void assign() {
		
		Random randTR = new Random();
		int trIndex = randTR.nextInt(ranks.length);
		type = types[trIndex];
		rank = ranks[trIndex];
		
		Random randSuit = new Random();
		int stIndex = randSuit.nextInt(suits.length);
		suit = suits[stIndex];
	}
	
	public String getType() {
		
		return type;
	}
	
	public String getSuit() {
		
		return suit;
	}
	
	public int getRank() {
		
		return rank;
	}
	
	public String toString() {

		return "\t" + getType() + " of " + getSuit();
	}
}
