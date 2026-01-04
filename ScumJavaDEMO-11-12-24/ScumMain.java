import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
public class ScumMain {

	//Scum (Java-Edition) by Tyler Strohl. See GitHub Page for more details: https://github.com/TylerWStrohl64/ScumDemo_11-12-2024
	
	//NOTE: This is a Work-In-Progress build of the project. Code Segments will be changed in later builds.
	//First   Build Created on: 11-12-2024.
	//Current Build Created on: 01-04-2026.
	//*******************************************************************************************
	
	/*
	 * Ideas left to implement:
	 * ------------------------
	 * 
	 * Player Class
	 * AI Class
	 * 		AI method/s & decisions.
	 *
	 * Card Stack
	 *  Track if a player layed down 2 of a card or 3 of a card or 1 of a card. / play multiple cards.
	 * 
	 * Player Rotation
	 *  There should be a boolean value for the player and each AI that gets flipped to true when they're out
	 * 	that goes in the appropriate spot in the boolean array. game ends when all are flipped to true.
	 * 
	 * Passing & Rounds
	 * 		Player Rotation.
	 * 
	 * Finish a Game
	 */
	
	/*
	 * Bugs to fix:
	 * ------------
	 * 
	 * Passing Issues & Rotation Issues
	 * 		program sometimes crashes when passing in different scenarios.
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
	
	//determines which players won & lost.
	public static boolean[] totalHierarchy;
	public static int currentHierarchy;
	
	//determines which player's turn it is, & how many players are left in the game.
	public static ArrayList<Boolean> playerRotation = new ArrayList<Boolean>();
	//index should correspond with index of the player (will be 0) & the indexes of ai (will be 1 to size - 1)
	public static int playerRotationIndex = 0;
	//determines which player starts a new round.
	public static int startingRotationIndex = 0;
	
	//*********************************************************************************************************
	//*********************************************************************************************************
	
	public static ArrayList<Card> makeCardDeck() {
		
		ArrayList<Card> deck = new ArrayList<Card>();
		
		//new card distribution method (accurate to real 52 card deck, instead of randomized).
		//there are 13 types of cards.
		for (int ctIndex = 0; ctIndex < 13; ctIndex++) {
			
			//4 suits:
			for (int suitIndex = 0; suitIndex < 4; suitIndex++) {
				
				Card aCard = new Card();
				
				String type = aCard.types[ctIndex];
				String suit = aCard.suits[suitIndex];
				int rank = aCard.ranks[ctIndex];
				
				aCard.assign(type, suit, rank);
				deck.add(aCard);
			}
		}
		
		//**************************************
		//TESTING:
//		System.out.println("TESTING makeCardDeck() FUNCTION");
//
//		for (int i = 0; i < 52; i++) {
//			Card currentCard = deck.get(i);
//			System.out.println(i + ":" + currentCard.toString());
//		}
		//**************************************
		return deck;
	}
	
	//separate method to prevent program from crashing on an invalid player count.
	public static int setPlayerCount(Scanner sc) {
		
		System.out.print("How many Players? (Must be between 2 - 13 Players) ");
		playerCount = sc.nextInt();
		
		if ((playerCount >= 2) && (playerCount <= 13)) {
			
			//initialize size of playerRotation here to ensure correct size.
			playerRotation = new ArrayList<Boolean>(Arrays.asList(new Boolean[playerCount]));
			
			for (int i = 1; i <= playerCount; i++) {
				playerRotation.add(true);
			}
			
			totalHierarchy = new boolean [playerCount];
		}
		
		return playerCount;
	}
	
	//***************[Most Frequently Used Method]***************
	//scanner is passed as parameter to close it in main. program crashes when scanner is declared & closed in this method.
	public static void showMenuOptions(Scanner menScan) {
		
		System.out.println("It is your turn. What would you like to do?");
		
		System.out.println("\t" + "R: Read the Rules.");
		System.out.println("\t" + "H: Shows your Hand.");
		System.out.println("\t" + "L: Lay-Down/Play a Card.");
		System.out.println("\t" + "P: Pass.");
		System.out.println("\t" + "T: See Top of Card Stack.");
		System.out.println("Choose one of the above keys. (Pressing Neither Key will bring this prompt up again)");
		
		String menOp = menScan.next();
		menOp = menOp.toUpperCase();
		
		//*************************************************************************************************************************
		//R: Read the Rules.
		if (menOp.equals("R")) {
			showRules();
		}
		//*************************************************************************************************************************
		//H: Shows your Hand.
		if (menOp.equals("H")) {
			System.out.println("Here is your Hand: ");
			showHand(thePlayer.getHand());
		}
		//*************************************************************************************************************************
		//L: Lay-Down/Play a Card.
		if (menOp.equals("L")) {
			
			//Which Card is Being Played:
			//*************************************************************************************************************************
			System.out.println("Which Card would you like to play?");
			showHand(thePlayer.getHand());
			
			System.out.println("(Enter the Number/Index of the Card you would like to play (shown on left. ex: 0)");
			//numCard is initialized at 0 before changing.
			int numCard = 0;
			
			//*************************************************************************************************************************
			//Check if Input is Valid: 		[Method is called again in main if Input is Invalid]
			
			
			//check prevents crashing.
			if (menScan.hasNextInt()) {
				numCard = menScan.nextInt();
				
				if ((numCard < 0) || (numCard >= thePlayer.getHand().size())) {
					System.out.println("Invalid Option. Enter the Number/Index of the card you would like to play (shown on left. ex: 0)");
					numCard = 0;
				}
				//*************************************************************************************************************************
				//Nested else statement is hit if Input is Valid:
				else {
					
					Card cardToPush = thePlayer.getHand().get(numCard);

					//no card comparisons needed if stack is empty. program would crash on comparison.
					if (cardStack.size() == 0) {

						thePlayer.playCard(numCard);
						startingRotationIndex = playerRotationIndex;
						playerRotationIndex++;
						
						//like in the real card game, this would be on the top of the stack of cards.
						cardStack.push(cardToPush);

						System.out.println("\n" + "\t" + "\t" + "The card: " + cardToPush.toString() + " is on the top of the stack.");
					}
					//check other rules if stack is not empty:
					else {

						//(size - 1) since indexes start at 0.
						Card topCard = cardStack.get(cardStack.size() - 1);

						//the card can only be played if: [The Value is higher than the current stack top.] && [The Num of Cards being played is same].
						if ((cardToPush.getRank()) > (topCard.getRank())) {

							thePlayer.playCard(numCard);
							startingRotationIndex = playerRotationIndex;
							playerRotationIndex++;
							
							//like in the real card game, this would be on the top of the stack of cards.
							cardStack.push(cardToPush);

							System.out.println("\n" + "\t" + "\t" + "The card: " + cardToPush.toString() + " is on the top of the stack.");
						}
						else {
							System.out.println("Your card/s cannot be played. Choose different card/s |OR| Pass.");
							showMenuOptions(menScan);
						}
					}
				}
				//*************************************************************************************************************************
			}
			//hit on invalid input:
			else {
				System.out.println("Invalid Option. Enter the Number/Index of the card you would like to play (shown on left. ex: 0)");
			}
		}
		//*************************************************************************************************************************
		//P: Pass.
		if ((menOp.equals("P")) && (thePlayer.getPassedStatus() == false)) {
			thePlayer.setPassedStatus(true);
			passCount++;
			playerRotationIndex++;
			
			System.out.println("You have passed. Simulating AI Opponent Moves:");
			//aiActions method gets called in main.
		}
		//*************************************************************************************************************************
		//T: See Top of Card Stack.
		if (menOp.equals("T")) {
			
			if (cardStack.size() == 0) {
				System.out.println("There are no cards on the stack.");
			}
			else {
				System.out.println(cardStack.get(cardStack.size() - 1) + " is on the top of the stack.");
			}
		}
		System.out.println();
	}
		//*************************************************************************************************************************
	
	public static void showRules() {
		
		System.out.println("These are the [Implemented] Rules of Scum: ");
		System.out.println("------------------------------------------");
		
		System.out.println("1 : Cards are Ranked with '2' as the lowest value, and 'Ace' as the highest value." + "\n");
		System.out.println("2 : You must play a card with a higher value than the card on the top of the stack. (ex: a '3' cannot be played if a 'King' is on top of the stack.)" + "\n");
		System.out.println("3 : You must pass if you cannot play any card in the current round." + "\n");
		System.out.println("4 : If a player passes in the current round, they cannot play another card until the next round." + "\n");
		System.out.println("5 : The round ends either when the last player plays an 'Ace' card, |OR| every player passes." + "\n");
		System.out.println("6 : The last player to play a card or pass starts the next round." + "\n");
		System.out.println("7 : The first player to get rid of all their cards is the 'Emporer' of the game (1st Place / Winner)." + "\n");
		System.out.println("8 : The last player to get rid of all their cards is the 'Scum' of the game (Last Place / Loser)." + "\n");
		
		System.out.println("\n" + "\t" + "(Read the Rules again by pressing the 'R' menu option when prompted.)");
		System.out.println("----------------------------------------------------------------------------------");
	}
	
	public static void showHand(ArrayList<Card> hand) {
		
		hand = thePlayer.getHand(); //this line may be redundant. or just dont use as param. player is static global.
		for (int i = 0; i < hand.size(); i++) {
			System.out.println(i + hand.get(i).toString());
		}
		System.out.println("---------------------------------------------------");
	}
	
	//Determine how each AI Opponent will play the game.
	//For the time being, the AI will either: [play a random card] |OR| [pass if a card is on the stack]
	public static void aiActions(Scanner sc, AI currentOP) {
		
		//fall into this if statement if the opponent is already out:
		if ((currentOP.getHand().size() == 0)) {
			
			passCount++;
		}
		//fall into this else statement if the opponent is not out:
		else {
			//Determine card before anything else. This ensures we have the correct card in the hand and stack.
			Random rand = new Random();
			int cardIndex = rand.nextInt(currentOP.getHand().size());
			Card cardToPush = currentOP.getHand().get(cardIndex);
			
			//play a card if the stack is empty.
			if (cardStack.size() == 0) {
				currentOP.playCard(cardIndex);
				startingRotationIndex = playerRotationIndex;
				
				//like in the real card game, this would be on the top of the stack of cards.
				cardStack.push(cardToPush);
				
				System.out.println("\t" + "\t" + "The card: " + cardToPush.toString() + " is on the top of the stack.");
				
				//fall into nested if statement if the card played is the opponent's last card.
				if (currentOP.getHand().size() == 0) {
					
					currentOP.setOutStatus(true);
					System.out.println(currentOP.getName() + " is out! Current Hierarchy: " + currentHierarchy);
					totalHierarchy[currentHierarchy] = true;
					currentOP.setHierarchy(currentHierarchy);
					currentHierarchy++;
					passCount++;
					
					//statement may have to change if player rotation indexes change.
					playerRotation.remove(aiPlayers.indexOf(currentOP));
				}
				
			}
			//[for now] all AI will pass if a card is already on the stack.
			else {
				
				if ((passCount < playerCount) && (currentOP.getPassedStatus() == false)) {
					currentOP.setPassedStatus(true);
					passCount++;
					
					System.out.println(currentOP.getName() + " has passed.");
				}
			}
		}
		playerRotationIndex++;
	}
	
	//called at the start of a new round (either all players pass |OR| an ACE card is played).
	public static void newRound() {
		
		passCount = 0;
		cardStack.clear();
		
		//reset pass status for all players.
		thePlayer.setPassedStatus(false);
		for (int i = 0; i < aiPlayers.size(); i++) {
			AI currentOP = aiPlayers.get(i);
			currentOP.setPassedStatus(false);
		}
		//the current index of the player rotation should determine the player who went last and start with that player.
		playerRotationIndex = startingRotationIndex;
		
		System.out.println("A new round has begun." + "\n");
	}
	
	//Main:
	//************************************************************************************
	//************************************************************************************
	
	public static void main(String[] args) {

		//Start of Setup
		//*******************************************************************************
		
		Scanner sc = new Scanner (System.in);
		
		System.out.println("Welcome to Scum: Java Edition! This is a text-based recreation of the Scum Card-Game programmed in the Java language.");
		System.out.println("This recreation was programmed by Tyler Strohl. This program you are using is a Demo/WIP Version of the project. See GitHub Page for more details." + "\n");
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
		String [] aiNames = {"Mario", "Luigi", "Wario", "Waluigi", "DK", "Yoshi", "Bowser", "Toad", "Peach", "Daisy", "King Boo", "Shy Guy", "Dry Bones", "Goomba", "Koopa Troopa", "Rosalina", "Bowser Jr.", "Hammer Bro", "Kamek"};
		ArrayList<String> listAINames = new ArrayList<String>(Arrays.asList(aiNames));
		
		for (int i = 1; i <= (playerCount - 1); i++) {
			
			//Assign random name to AI Player & add it to the ArrayList of AI Players/Opponents.
			Random randName = new Random();
			int randIndex = randName.nextInt(listAINames.size());

			String aiName = listAINames.get(randIndex);
			AI opponent = new AI(aiName);
			aiPlayers.add(opponent);
			//wont get the same name twice.
			listAINames.remove(randIndex);
			
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

		//temporarily, the player (you) will start the game.
		playerRotationIndex = 0;

		//increment when a player wins.
		currentHierarchy = 0;
		int playerHierarchy = 0;
		
		//*******************************************************************************
		//End Of Setup.
		
		
			//outer loop runs until every player runs out of cards.
			while (totalHierarchy[playerCount - 1] != true) {
				//inner loop runs first, and runs until human player (you) runs out of cards.
				while (thePlayer.getHand().size() != 0) {
					
					//Player's Turn:
					if ((playerRotationIndex == 0) && (thePlayer.getPassedStatus() == false)) {
						showMenuOptions(sc);
					}
					//AI Turn/s: [will need to copy this outside of inner loop too.] this could maybe be re-done in aiActions?
					else {
						//call aiActions Method so that the AI players take their turn.
						
							//may have to change this loop when order is determined.
							for (int i = 0; i < aiPlayers.size(); i++) {
								
								//needs to be changed.
								AI currentOP = aiPlayers.get(i);
								
								aiActions(sc, currentOP);
							}
						
							if (playerRotationIndex >= playerRotation.size()) {
								
								playerRotationIndex = 0;
							}
					}
					if (cardStack.size() != 0)
					{
						//(size - 1) since indexes start at 0.
						Card topCard = cardStack.get(cardStack.size() - 1);
						if ((passCount >= playerCount) || (topCard.getType().equals("Ace"))) {
							newRound();
						}
					}
				}
				//end of inner loop.
				
				//*******************************************************************************
				//Remaining code is triggered after the player runs out of cards:
				
				//some of these should only happen once.
				thePlayer.setOutStatus(true);	
				System.out.println(thePlayer.getOutStatus());
					
				playerHierarchy = currentHierarchy;
				thePlayer.setHierarchy(playerHierarchy);
				totalHierarchy[playerHierarchy] = true;
				
				playerRotation.remove(playerHierarchy);
				
				//*******************************************************************************
				//TESTS FOR ABOVE STATEMENTS
				System.out.println("[TESTING] Player (You) have played your last card. The game has ended.");
				System.out.println(currentHierarchy);
				System.out.println(playerHierarchy);
				System.out.println(totalHierarchy[playerHierarchy]);
				//*******************************************************************************
				
				
				//*******************************************************************************
				//FOR TESTING PURPOSES [it terminates the program after the player is out].
				totalHierarchy[playerCount - 1] = true;
				//*******************************************************************************
		}
		
		//print results.
		//if the hierarchy is (0) than that is [emperor]. if the hierarchy is (size - 1) then thats [scum].
		
		sc.close();
	}

}
