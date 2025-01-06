# ScumDemo_11-12-2024
[Use the 'ScumMain.java' class to run the program]
--------------------------------------------------

This repository was created to show a Demo/WIP Version of my project 'Scum', and to allow others to look at its code.

This project 'Scum' is a Text-Based Recreation of the card game with the same name. It is coded in Java.

The [Implemented] Rules of Scum are:
------------------------------------

1 : Cards are Ranked with '2' as the lowest value, and 'Ace' as the highest value.

2 : You must play a card with a higher value than the card on the top of the stack. (ex: a '3' cannot be played if a 'King' is on top of the stack.)

3 : You must pass if you cannot play any card in the current round.

4 : If a player passes in the current round, they cannot play another card until the next round.

5 : The round ends either when the last player plays an 'Ace' card, |OR| every player passes.

6 : The last player to play a card or pass starts the next round.

7 : The first player to get rid of all their cards is the 'Emporer' of the game (1st Place / Winner).

8 : The last player to get rid of all their cards is the 'Scum' of the game (Last Place / Loser).
		


Inspiration:
------------

I started this project after Millersville University Professor, Dr. Chad Hogg, introduced me to the game.

I wanted to practice my Java coding skills by creating a project that I am passioniate about and a project that would properly display my coding skills.


Why I Chose Java:
-----------------

I chose Java to code this program in because of several Java features:
----------------------------------------------------------------------

This program uses Scanner objects, Random objects, Arrays, ArrayLists, Stacks, and the data types string and int.

I like how Java handles Scanners, ArrayLists, & Strings in particular. I have found them very crucial to the project.

Java is an Object-Oriented Programming Language. Therefore, each class can be represented as an object. For example, I can represent each Card in the game as its own object.

  Scanners are used to scan user input, which are registered as strings and ints. 
  -------------------------------------------------------------------------------

Strings can be handled very well via scanners as well as strings' various methods provided by Java's libraries.

  Random Objects are used for various tasks.
  ------------------------------------------
  
They are used to randomly select which cards are added to a player's hand from the game's card deck.

They are used to randomly assign a type/rank for each card [ex: Jack].

They are used to randomly assign names to each AI Opponent, so that each AI Opponent can be uniquely identified.

  Arrays
  ------

All the possible card types/ranks and suits are stored in their own arrays.

Player Rotation [determines which players are still in the game] uses an Array of Boolean values.

  ArrayLists
  ----------

The game's deck of cards is represented by an ArrayList of Cards. Each player's hand is represented as their own smaller ArrayList of Cards.

Each AI Opponent is stored in an ArrayList of AI.

ArrayLists are more flexible than Arrays. For example, their size can change when elements are added/removed.

  Stacks
  ------

A stack is used to represent a real stack of cards. Each player can play different cards dependent on what is 'on top' of this stack.
