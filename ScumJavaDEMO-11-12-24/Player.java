import java.util.ArrayList;
import java.util.Random;
public class Player {

	//Variable Declarations
	ArrayList<Card> hand;
	boolean outStatus;
	boolean passStatus;
	boolean playedStatus;
	int heirarchy;
	int numCardsToPlace;
	
	//Constructor
	public Player() {
		
		hand = new ArrayList<Card>();
		outStatus = false;
		passStatus = false;
		playedStatus = false;
	}
	
	//*************************************************************************************************************
	
	public void setHand(ArrayList<Card> deck, int cardCount) {
		
		for (int i = 1; i <= cardCount; i++) {
			//remove a random card from the deck and add it to the hand.
			Random rand = new Random();
			int randIndex = rand.nextInt(deck.size());
			hand.add(deck.get(randIndex));
			deck.remove(randIndex);
		}
	}
	
	public ArrayList<Card> getHand() {
		
		return hand;
	}
	
	public void playCard(int cardIndex) {
		
		//check if the card can be played:
		//if (ai card rank)   >  (player card rank) then card cannot be played.
		//if (numCardsPlaced) != (numCardsToPlace ) then card cannot be played.
		// ^^^ this may be done in main.

		//else play the card. removing the card before print will print the wrong card.
		System.out.println("\n" + "You have played the card: " + hand.get(cardIndex).toString());
		hand.remove(cardIndex);
	}
	
	public boolean getPlayedStatus() {
		
		return playedStatus;
	}
	
	public void setPlayedStatus(boolean playerPlayStatus) {
		
		playedStatus = playerPlayStatus;
	}
	
	public boolean getPassedStatus() {
		
		return passStatus;
	}
	
	public void setPassedStatus(boolean playerPassStatus) {
		
		passStatus = playerPassStatus;
	}
	
	public boolean getOutStatus() {
		
		return outStatus;
	}
	
	public void setOutStatus(boolean playerOutStatus) {
		
		outStatus = playerOutStatus;
	}
	
	public void setHeirarchy(int playerHeirarchy) {
		
		heirarchy = playerHeirarchy;
	}
	
	public int getHeirarchy() {
		
		return heirarchy;
	}
}
