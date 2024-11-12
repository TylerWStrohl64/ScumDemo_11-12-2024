import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
public class ScumMain {

	//Scum (Java-Edition) by Tyler Strohl. See GitHub Page for more details.
	
	//NOTE: This is a Work-In-Progress build of the project. Code Segments will be changed in later builds.
	//Current Build Created on: 11-12-2024.
	//*******************************************************************************************
	
	/*
	 * Ideas left to implement:
	 * ------------------------
	 * 
	 * Player Class
	 * AI Class
	 * 		AI method/s & decisions.
	 *
	 * Use a Stack for the current stack of cards / card pile for that round.
	 * 	keep track if a player layed down 2 of a card or 3 of a card or 1 of a card.
	 * 	compare what is on the stack with the card the player wants to play, and compare the amount of that card.
	 * 
	 * There should be a boolean value for the player and each AI that gets flipped to true when they're out
	 * 	that goes in the appropriate spot in the boolean array. game ends when all are flipped to true.
	 * 
	 * Passing & Rounds
	 * 		end round when top of stack has ace/s
	 */
	
	/*
	 * Bugs to fix:
	 * 
	 * Playing a card that doesnt exist or input is not an int.
	 * AI takes multiple turns at once / passing issues.
	 */
	
	
	//Global Variables:
	//*****************
	
	public static Player thePlayer = new Player();
	
	//empties each round.
	public static Stack<Card> cardStack = new Stack<Card>();
	
	public static ArrayList<AI> aiPlayers = new ArrayList<AI>();

	public static int playerCount;
	public static int passCount = 0;
	public static int numCardsToPlay;
	
	//*********************************************************************************************************
	//*********************************************************************************************************
	
	public static ArrayList<Card> makeCardDeck() {
		
		ArrayList<Card> deck = new ArrayList<Card>();
		
		//set to 53 to avoid an index out of bounds exception.
		for (int i = 1; i <= 53; i++) {
			Card aCard = new Card();
			aCard.assign();
			deck.add(aCard);
		}
		return deck;
	}
	
	//seperate method to prevent program from crashing on an invalid player count.
	public static int setPlayerCount(Scanner sc) {
		
		System.out.print("How many Players? (Must be between 2 - 13 Players) ");
		//int playerCount = sc.nextInt();
		playerCount = sc.nextInt();
		
		return playerCount;
	}
	
	//make method for handling input that is not an int?
	
	public static void showMenuOptions() {
		
		System.out.println("It is your turn. What would you like to do?");
		
		System.out.println("\t" + "H: Shows your Hand.");
		System.out.println("\t" + "L: Lay-Down/Play a Card.");
		System.out.println("\t" + "P: Pass.");
		System.out.println("\t" + "T: See Top of Card Stack.");
		System.out.println("Choose one of the above keys. (Pressing Neither Key will bring this prompt up again)");
		
		Scanner menScan = new Scanner(System.in);
		String menOp = menScan.next();
		menOp = menOp.toUpperCase();
		
		if (menOp.equals("H")) {
			System.out.println("Here is your Hand: ");
			showHand(thePlayer.getHand());
			}
		
		if (menOp.equals("L")) {
			
			System.out.println("Which card would you like to play?");
			//call the same hand method from above.
			showHand(thePlayer.getHand());
			
			System.out.println("(Enter the Number/Index of the card you would like to play (shown on left. ex: 0)");
			//int numCard = menScan.nextInt();
			int numCard = 0;
			
			//prevents crashing. will [not recurse] if invalid options are chosen.
			if (menScan.hasNextInt()) {
				numCard = menScan.nextInt();
				
				//below cases need to be fixed, and may have to be split between 2 methods.
				//recursive calls play a card twice.
				
				if ((numCard < 0) || (numCard >= thePlayer.getHand().size())) {
					System.out.println("Invalid Option. Enter the Number/Index of the card you would like to play (shown on left. ex: 0)");
					numCard = 0;
					//showMenuOptions();
				}
			}
			else {
				System.out.println("Please enter a Number/Index");
				//showMenuOptions();
			}
			
			//call method to play a card (its function could be to remove the card from your hand)
											//need it to check if the card is able to be played.
			Card cardToPush = thePlayer.getHand().get(numCard);
			
			//no card comparisons needed if stack is empty. program would crash on comparison.
			if (cardStack.size() == 0) {
				
				thePlayer.playCard(numCard);
				//like in the real card game, this would be on the top of the stack of cards.
				cardStack.push(cardToPush);
				//test that the stack size is correct.
				System.out.println(cardStack.size());
				
				System.out.println("\t" + "\t" + "The card: " + cardToPush.toString() + " is on the top of the stack.");
				thePlayer.setPlayedStatus(true);
			}
			else {
				
				//(size - 1) since indexes start at 0.
				Card topCard = cardStack.get(cardStack.size() - 1);
				
				//the card can only be played if: [The Value is higher than the current stack top.] && [The Num of Cards being played is same].
				if ((cardToPush.getRank()) > (topCard.getRank())) {
					
					//if this doesnt work, move it outside the if statement.
					thePlayer.playCard(numCard);
					//like in the real card game, this would be on the top of the stack of cards.
					cardStack.push(cardToPush);
					//test that the stack size is correct.
					System.out.println(cardStack.size());
					
					System.out.println("\t" + "\t" + "The card: " + cardToPush.toString() + " is on the top of the stack.");
					thePlayer.setPlayedStatus(true);
				}
				else {
					System.out.println("Your card/s cannot be played. Choose different card/s |OR| Pass.");
					showMenuOptions();
				}
			}
			
		}
		if ((menOp.equals("P")) && (thePlayer.getPassedStatus() == false)) {
			thePlayer.setPassedStatus(true);
			passCount++;
			//TEST
			//System.out.print(passCount);
			
			//need to reset some values if all players passed.
			
			System.out.println("You have passed. Simulating AI Opponent Moves:");
			//simulate ai player moves via a method.
		}
		
		if (menOp.equals("T")) {
			
			if (cardStack.size() == 0) {
				System.out.println("There are no cards on the stack.");
			}
			else {
				System.out.println(cardStack.get(cardStack.size() - 1) + " is on the top of the stack.");
			}
		}
		
		//this crashes the program. maybe create outside the method and close in main?
		//menScan.close();
		System.out.println();
	}
	
	public static void showRules() {
		
		System.out.println("These are the Rules of Scum: ");
		System.out.println("----------------------------");
		
		System.out.println("1 : The player with the '2' of 'Clubs' card starts the game." + "\n");
		System.out.println("2 : You must pass if you cannot play any cards in the current round." + "\n");
		System.out.println("3 : If a player passes in the current round, they cannot play another card until the next round." + "\n");
		System.out.println("4 : The round ends either when the last player plays an 'Ace' card, |OR| every player passes." + "\n");
		System.out.println("5 : The last player to play a card or pass starts the next round." + "\n");
		System.out.println("6 : The first player of each round determines how many cards are played at a time (ex: the first player may lay down 2 cards with the same value at one time. then every player must play 2 cards or pass." + "\n");
		System.out.println("7 : Cards are Ranked with '2' as the lowest value, and 'Ace' as the highest value." + "\n");
		System.out.println("8 : You must play card/s with a higher value than the card/s on the top of the stack. If you play multiple cards, they must all be the same value (ex: 6)" + "\n");
		System.out.println("9 : The first player to get rid of all their cards is the 'Emporer' of the game." + "\n");
		System.out.println("10: The last player to get rid of all their cards is the 'Scum' of the game." + "\n");
		System.out.println("11: At the start of a new game (not round) the 'Scum' must give the 'Emporer' their 2 highest value cards. The 'Emporer' gives the 'Scum' their 2 lowest value cards." + "\n");
		
		System.out.println("---------------------------------------------------");
	}
	
	public static void showHand(ArrayList<Card> hand) {
		
		hand = thePlayer.getHand();
		for (int i = 0; i < hand.size(); i++) {
			System.out.println(i + hand.get(i).toString());
		}
	}
	
	//determine how each AI Opponent will play the game.
	public static void aiActions(Scanner sc, AI currentOP) {
		
		//An AI cannot choose options H |OR| T
		
		
		//Determine card before anything else. This ensures we have the correct card in the hand and stack.
		Random rand = new Random();
		int cardIndex = rand.nextInt(currentOP.getHand().size());
		Card cardToPush = currentOP.getHand().get(cardIndex);
		
		//play a card if the stack is empty.
		if (cardStack.size() == 0) {
			currentOP.playCard(cardIndex);
			
			//like in the real card game, this would be on the top of the stack of cards.
			cardStack.push(cardToPush);
			//test that the stack size is correct.
			System.out.println(cardStack.size());
			
			System.out.println("\t" + "\t" + "The card: " + cardToPush.toString() + " is on the top of the stack.");
		}
		//we'll just have them all pass for now.
		else {
			
			if ((passCount < playerCount) && (currentOP.getPassedStatus() == false)) {
				currentOP.setPassedStatus(true);
				passCount++;
				//TEST
				//System.out.print(passCount);
				//need to reset some stuff if all players passed.
				
				System.out.println(currentOP.getName() + " has passed.");
			}
		}
	}
	
	//how will passing work?
	public static void newRound() {
		
		passCount = 0;
		cardStack.clear();
		
		//reset pass status for all players.
		thePlayer.setPassedStatus(false);
		for (int i = 0; i < aiPlayers.size(); i++) {
			AI currentOP = aiPlayers.get(i);
			currentOP.setPassedStatus(false);
		}
		
		//determine who starts the next round.
		
		System.out.println("A new round has begun.");
	}
	
	//Main:
	//************************************************************************************
	//************************************************************************************
	
	public static void main(String[] args) {

		//Start of Setup
		//*******************************************************************************
		
		Scanner sc = new Scanner (System.in);
		
		System.out.println("Welcome to Scum: Java Edition! This is a text-based recreation of the Scum Card-Game programmed in the Java language.");
		System.out.println("This recreation was programmed by Tyler Strohl This program you are using is a Demo/WIP Version of the project. See GitHub Page for more details." + "\n");
		System.out.println("Press [R] to Read The Rules. |OR| Press [Any Other Key] to Play.");
		
		String command = sc.next();
		command = command.toUpperCase();
		if (command.equals("R")) {
			showRules();
		}
		
		//set the number of players.
		playerCount = setPlayerCount(sc);
				if (playerCount < 2) {
					System.out.println("Player Count must be [at least] 2." + "\n");
					setPlayerCount(sc);
				}
				else if (playerCount > 13) {
					System.out.println("Player Count must be [at most] 13." + "\n");
					setPlayerCount(sc);
				}
		
		//create an AI object for every AI player needed.
		String [] aiNames = {"Tyler", "Ashton", "Hailey", "Sara", "Chayse", "Chad", "Brad", "Steve", "Kevin", "Jack", "Kyle", "Aiden", "Austin", "Dan", "Michael", "Chris", "Susan", "Jessica"};
		ArrayList<String> listAINames = new ArrayList<String>(Arrays.asList(aiNames));
		
		for (int i = 1; i <= (playerCount - 1); i++) {
			
			//Assign random name to AI Player & add it to the ArrayList of AI Players/Opponents.
			Random randName = new Random();
			int randIndex = randName.nextInt(listAINames.size());
			//String aiName = aiNames[randIndex];
			String aiName = listAINames.get(randIndex);
			AI opponent = new AI(aiName);
			aiPlayers.add(opponent);
			//wont get the same name twice.
			listAINames.remove(randIndex);
			
			//TEST
			System.out.println("AI OPPONENT: " + aiPlayers.get((i - 1)).getName());
		}
		
		//deck is 52 cards. cardCount is how many cards each player gets from this deck (should be same for all).
		ArrayList<Card> gameDeck = makeCardDeck();
		int cardCount = ((gameDeck.size()) / (playerCount));

		thePlayer.setHand(gameDeck, cardCount);
		
		//set hand for AI Players
		for (int i = 0; i < aiPlayers.size(); i++) {
			
			AI currentOP = aiPlayers.get(i);
			currentOP.setHand(gameDeck, cardCount);
		}
		
		boolean [] totalHeirarchy = new boolean [playerCount];
		ArrayList<Boolean> notOut = new ArrayList<Boolean>(Arrays.asList(new Boolean[playerCount]));
		
		
		for (int i = 1; i <= playerCount; i++) {
			notOut.add(true);
		}
		
		//ArrayList<AI> aiPlayersLeft = new ArrayList<AI>(aiPlayers);
		
		//ArrayList<Boolean> notPassed = new ArrayList<Boolean>(notOut.size());
		//increment when a player wins.
		int currentHeirarchy = 0;
		int playerHeirarchy = 0;
		
		//*******************************************************************************
		//End Of Setup.
		
		
			//outer loop runs until every player runs out of cards.
			while (totalHeirarchy[playerCount - 1] != true) {
				//inner loop runs first, and runs until human player (you) runs out of cards.
				while (thePlayer.getHand().size() != 0) {
					
					if ((thePlayer.getPassedStatus() == false) && (thePlayer.getPlayedStatus() == false)) {
						showMenuOptions();
					}
					else {
						//call other method/s so that the AI players take their turn.
						//if player pushes onto stack or passes, then next player can go. <<<-------
							//compare size of stack if player pushes onto stack.
							
							//may have to change this loop when order is determined.
							for (int i = 0; i < aiPlayers.size(); i++) {
								
								//needs to be changed.
								AI currentOP = aiPlayers.get(i);
								
								//given the size of the loop this should work. if the above statement is correct.
								if (currentOP.getHand().size() == 0) {
									
									currentOP.setOutStatus(true);
									notOut.remove(aiPlayers.indexOf(currentOP));
								}
								else {
									aiActions(sc, currentOP);
								}
								
								if (i == (aiPlayers.size() - 1)) {
									thePlayer.setPlayedStatus(false);
								}
							}
					}
						
					
					//if (passCount == notOut.size()) {
						//newRound();
					//}
					if (cardStack.size() != 0)
					{
						//(size - 1) since indexes start at 0.
						Card topCard = cardStack.get(cardStack.size() - 1);
						if ((passCount >= playerCount) || (topCard.getType().equals("Ace"))) {
							newRound();
						}
						//newRound();
					}
				}
				
				//rest of code is triggered after the player runs out of cards.
				
				//some of these should only happen once.
				thePlayer.setOutStatus(true);	
				System.out.println(thePlayer.getOutStatus());
					
				playerHeirarchy = currentHeirarchy;
				thePlayer.setHeirarchy(playerHeirarchy);
				totalHeirarchy[playerHeirarchy] = true;
				
				notOut.remove(playerHeirarchy);
				
				//*******************************************************************************
				//TESTS FOR ABOVE STATEMENTS
				System.out.println(currentHeirarchy);
				System.out.println(playerHeirarchy);
				System.out.println(totalHeirarchy[playerHeirarchy]);
				//*******************************************************************************
				
				
				//*******************************************************************************
				//FOR TESTING PURPOSES [it terminates the program after the player is out]
				totalHeirarchy[playerCount - 1] = true;
				//*******************************************************************************
		}
		
		//print results. (will need to increment heirarchy by 1 when printed)
		//if the heirarchy is (0) than that is [emporer]. if the heirarchy is (size - 1) then thats [scum].
		
		sc.close();
	}

}
