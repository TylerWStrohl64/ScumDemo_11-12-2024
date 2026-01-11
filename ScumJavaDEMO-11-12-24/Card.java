//import java.util.Random;

//A "Deck" will be an ArrayList of Cards.
public class Card {

	//Variable Declarations:
	//----------------------
	
	private String type;
	private String suit;
	private int rank;
	
	public final String [] types = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
	public final String [] suits = {"Clubs", "Spades", "Hearts", "Diamonds"};
	//ranks correspond to type. [ex: "Ace" -> 14].
	public final int [] ranks = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
	
	//Default Card:
	public Card () {
		
		type = types[0];
		suit = suits[0];
		rank = ranks[0];
	}
	
	
	//*************************************************************************************************************
	
	//Set the type, suit, and rank for each card.
	//See "makeCardDeck()" function in ScumMain.
	public void assign(String type, String suit, int rank) {
		
		this.type = type;
		this.suit = suit;
		this.rank = rank;
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
