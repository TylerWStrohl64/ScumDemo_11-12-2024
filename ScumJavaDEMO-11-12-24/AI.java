import java.util.ArrayList;
import java.util.Random;
public class AI {

	//Variable Declarations:
	//----------------------
	
	private String name;
	private ArrayList<Card> hand;
	private boolean outStatus;
	private boolean passStatus;
	private int hierarchy;
	
	//Constructor:
	public AI(String name) {
		
		this.name = name;
		hand = new ArrayList<Card>();
		outStatus = false;
		passStatus = false;
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
	
	public void playCard(int cardIndex) {

		System.out.println(name + " has played the card: " + hand.get(cardIndex).toString());
		hand.remove(cardIndex);
	}
	
	public String getName() {
		
		return name;
	}
	
	public ArrayList<Card> getHand() {
		
		return hand;
	}
	
	public boolean getPassedStatus() {
		
		return passStatus;
	}
	
	public void setPassedStatus(boolean AIPassStatus) {
		
		passStatus = AIPassStatus;
	}
	
	public boolean getOutStatus() {
		
		return outStatus;
	}
	
	public void setOutStatus(boolean AIOutStatus) {
		
		outStatus = AIOutStatus;
	}
	
	public void setHierarchy(int AIHierarchy) {
		
		hierarchy = AIHierarchy;
	}
	
	public int getHeirarchy() {
		
		return hierarchy;
	}
}
