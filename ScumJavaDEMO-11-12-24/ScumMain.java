import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
public class ScumMain {

	//Scum (Java-Edition) by Tyler Strohl. See GitHub Page for more details: https://github.com/TylerWStrohl64/ScumDemo_11-12-2024
	
	//NOTE: This is a Work-In-Progress build of the project. Code Segments will be changed in later builds.
	//First   Build Created on: 11-12-2024.
	//Current Build Created on: 01-04-2025.
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
	 *  track if a player layed down 2 of a card or 3 of a card or 1 of a card. / play multiple cards.
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
	public static boolean[] totalHeirarchy;
	public static int currentHeirarchy;
	
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
		playerCount = sc.nextInt();
		
		//initialize size of playerRotation here to ensure correct size.
		playerRotation = new ArrayList<Boolean>(Arrays.asList(new Boolean[playerCount]));
		
		for (int i = 1; i <= playerCount; i++) {
			playerRotation.add(true);
		}
		
		//is totalHeirarchy being used?
		totalHeirarchy = new boolean [playerCount];
		
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
			//call the same hand method from above.
			showHand(thePlayer.getHand());
			
			System.out.println("(Enter the Number/Index of the Card you would like to play (shown on left. ex: 0)");
			//int numCard = menScan.nextInt();
			//numCard is initialized at 0 before changing.
			int numCard = 0;
			
			//*************************************************************************************************************************
			//Check if Input is Valid: 		[Recurse if Input is Invalid]
			
			
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
						//test that the stack size is correct:
						//System.out.println(cardStack.size());

						System.out.println("\n" + "\t" + "\t" + "The card: " + cardToPush.toString() + " is on the top of the stack.");
						thePlayer.setPlayedStatus(true);
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
							thePlayer.setPlayedStatus(true);
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
		System.out.println("---------------------------------------------------");
	}
	
	//determine how each AI Opponent will play the game.
	public static void aiActions(Scanner sc, AI currentOP) {
		
		//An AI cannot choose options H |OR| T
		
		
		
		//this may not work if stuff is removed from aiPlayers or playerRotation ???
		if ((currentOP.getHand().size() == 0)) {
			
			//passing again after this statement will result in an infinite loop or crashing.
			//System.out.println(currentOP.getName() + " is already out!");
			passCount++;
		}
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
				
				
				if (currentOP.getHand().size() == 0) {
					
					currentOP.setOutStatus(true);
					System.out.println(currentOP.getName() + " is out! Current Heirarchy: " + currentHeirarchy);
					totalHeirarchy[currentHeirarchy] = true;
					currentOP.setHeirarchy(currentHeirarchy);
					currentHeirarchy++;
					passCount++;
					
					//statement needs to be re-done. index out of bounds exception. indexes dont line up.
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
		//the current index of the player rotation should determine the player who went last and start with that player.
		playerRotationIndex = startingRotationIndex;
		
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

		//how should the current index in the playerRotation be determined?
			//how can it be used to determine the current player?
		playerRotationIndex = 0;

		//increment when a player wins.
		//int currentHeirarchy = 0;
		currentHeirarchy = 0;
		int playerHeirarchy = 0;
		
		//*******************************************************************************
		//End Of Setup.
		
		
			//outer loop runs until every player runs out of cards.
			while (totalHeirarchy[playerCount - 1] != true) {
				//inner loop runs first, and runs until human player (you) runs out of cards.
				while (thePlayer.getHand().size() != 0) {
					
					//Player's Turn:
					if ((playerRotationIndex == 0) && (thePlayer.getPassedStatus() == false)) {
						showMenuOptions(sc);
						
						//TESTING
						//System.out.println("playerRotationIndex: " + playerRotationIndex);
						//TESTING
					}
					//AI Turn/s: [will need to copy this outside of inner loop too.] this could maybe be re-done in aiActions?
					else {
						//call other method/s so that the AI players take their turn.
						//if player pushes onto stack or passes, then next player can go. <<<-------
							//compare size of stack if player pushes onto stack
						
						
							//may have to change this loop when order is determined.
							for (int i = 0; i < aiPlayers.size(); i++) {
								
								//playerRotationIndex++;
								
								//needs to be changed.
								AI currentOP = aiPlayers.get(i);
								
								aiActions(sc, currentOP);
								
								//does this if statement still have a purpose?
								if (i == (aiPlayers.size() - 1)) {
									thePlayer.setPlayedStatus(false);
								}
								
								//TESTING
								//System.out.println("playerRotationIndex: " + playerRotationIndex);
								//TESTING
								
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
				//rest of code is triggered after the player runs out of cards:
				
				//some of these should only happen once.
				thePlayer.setOutStatus(true);	
				System.out.println(thePlayer.getOutStatus());
					
				playerHeirarchy = currentHeirarchy;
				thePlayer.setHeirarchy(playerHeirarchy);
				totalHeirarchy[playerHeirarchy] = true;
				
				//what index is the player at in the player rotation?
				playerRotation.remove(playerHeirarchy);
				
				//*******************************************************************************
				//TESTS FOR ABOVE STATEMENTS
				System.out.println("[TESTING] Player (You) have played your last card. The game has ended.");
				System.out.println(currentHeirarchy);
				System.out.println(playerHeirarchy);
				System.out.println(totalHeirarchy[playerHeirarchy]);
				//*******************************************************************************
				
				
				//*******************************************************************************
				//FOR TESTING PURPOSES [it terminates the program after the player is out].
				totalHeirarchy[playerCount - 1] = true;
				//*******************************************************************************
		}
		
		//print results. (will need to increment heirarchy by 1 when printed)
		//if the heirarchy is (0) than that is [emporer]. if the heirarchy is (size - 1) then thats [scum].
		
		sc.close();
	}

}
