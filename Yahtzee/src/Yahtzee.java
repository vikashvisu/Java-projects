
/**
 * File: Yahtzee.java
 * ------------------
 * This program will play the Yahtzee game.
 * 
 */


import java.util.Arrays;
import acm.io.*;
import acm.program.*;
import acm.util.*;


/** 
 * @author Vikash Kumar(BTE Roll no. - 317356)
 *
 * @version 1.0
 * 
 * @since 13-01-2015
 */
@SuppressWarnings("serial")
public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	
	/** 
	 * The game starts here. It asks about number of players and their names.
	 * creates setup for the game and calls "playGame" method.
	 */
	public void run() {
		IODialog dialog;
		while(true) {
			dialog = getDialog();
			nPlayers = dialog.readInt("Enter number of players(not greater than " + MAX_PLAYERS + "):");
			if (nPlayers <= MAX_PLAYERS) break;
		}
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}
	
	
	/**
	 * This method plays a complete game.
	 */
	private void playGame() {
		scorecard = new int[N_CATEGORIES][nPlayers];
		selectedCategories = new boolean[N_SCORING_CATEGORIES][nPlayers];
		int roundNo = 1;
		while(roundNo <= N_ROUNDS) {
			int player = 1;
			while (player <= nPlayers) {
				firstRoll(player);
				nextTwoRolls(player); 
				int category = selectCategory(player);
				int score = 0;
				if (diceMatchesCategory(category))
					score = getScore(category);
				setScores(player, category, score);
				player++;
			}
			roundNo++; 
		}
		
		giveBonus();
		display.printMessage("Winner: " + getWinner());
	}
	
	
	/** 
	 * This method rolls the dice for the first time.
	 * 
	 * @param 	player 		An integer representing the player.    
	 */	
	private void firstRoll(int player) {
		display.printMessage(playerNames[player-1] + ", Roll the dice!");
		display.waitForPlayerToClickRoll(player);
		for (int i = 0; i < N_DICE; i++) {
			dice[i] = rgen.nextInt(ONES, SIXES);
		}
		display.displayDice(dice);
	}
	
	
	/** 
	 * This method gives the two chances after the first roll.
	 * During each chance it asks to select the dice, the player
	 * wants to reroll and rolls them after clicking on the 
	 * "Roll Dice" button.
	 * 
	 * @param 	player 		An integer representing the player.
	 */
	private void nextTwoRolls(int player) {
		for (int i = 0; i < N_CHANCES; i++) {
			display.printMessage(playerNames[player-1] + ", select the dice and reroll.");
			display.waitForPlayerToSelectDice();
			for (int j = 0; j < N_DICE; j++) {
				if (display.isDieSelected(j))
					dice[j] = rgen.nextInt(ONES, SIXES);
			}
			display.displayDice(dice);
		}
	}
	
	
	/**
	 * This method asks to select a category and returns it.
	 * 
	 * @param 	player 	An integer representing the player.
	 * @return 	int  
	 */	
	private int selectCategory(int player) {
		display.printMessage(playerNames[player-1] + ", select a category.");
		
		while (true) {
			int category = display.waitForPlayerToSelectCategory();
			int index = 0;
			
			if (category < UPPER_SCORE)
				index = 1;
			else 
				index = 3;
			
			if (!selectedCategories[category-index][player-1]) {
				selectedCategories[category-index][player-1] = true;
				return category;
			} else {
				display.printMessage(playerNames[player-1] + ", that category has already been selected. "
															+ "please choose another one.");
			}
		}
	}
	
	
	/**
	 * This method returns true if the selected category matches
	 * the dice configuration otherwise returns false.
	 * 
	 * @param 	category 	A static constant integer representing the selected category.
	 * @return boolean
	 */	
	private boolean diceMatchesCategory(int category) {
		
		countOfFaces = countOccurences();
		
		boolean selection = true;
		
		if (category == THREE_OF_A_KIND)
			selection = isThreeOfAKind();
		else if (category == FOUR_OF_A_KIND)
			selection = isFourOfAKind();
		else if (category == FULL_HOUSE)
			selection = isFullHouse();
		else if (category == SMALL_STRAIGHT)
			selection = isSmallStraight();
		else if (category == LARGE_STRAIGHT)
			selection = isLargeStraight();
		else if (category == YAHTZEE)
			selection = isYahtzee(); 
		
	    return selection;
	}
	
	
	/** 
	 * This method returns an array of integers
	 * which stores the occurence of each face. 
	 * for example: if the dice has values -  2, 4, 4, 5, 3
	 * then the returned array will have values - 0, 1, 1, 2, 1, 0
	 * It means that 1 occurs 0 time, 2 occurs 1 time,
	 * 3 occurs 1 time, 4 occurs 2 times, 5 occurs 1 time and
	 * 6 occurs 0 time.
	 * 
	 * @return int[]     
	 */
	private int[] countOccurences() {
		int[] countsOfFaces = new int[N_FACES];
		for (int i = 0; i < N_DICE; i++) {
			countsOfFaces[dice[i] - 1]++;
		}
		return countsOfFaces;
	}
	
	
	/** 
	 * This method returns true if the dice configuration is
	 * three of a kind otherwise returns false.
	 * 
	 * @return boolean
	 */
	private boolean isThreeOfAKind() {
	   for (int i = 0; i < N_FACES; i++) {
			if (countOfFaces[i] >= 3)
				return true;
	   }
	   return false;
	}	
	 
	
	/** 
	 * This method returns true if the dice configuration is
	 * four of a kind otherwise returns false.
	 * 
	 * @return boolean
	 */
	private boolean isFourOfAKind() {
	   for (int i = 0; i < N_FACES; i++) {
			if (countOfFaces[i] >= 4)
				return true;
	   }
	   return false;
	}
		
	
	/** 
	 * This method returns true if the dice configuration is
	 * full house otherwise returns false.
	 * 
	 * @return boolean
	 */
	private boolean isFullHouse() {
		if (countOfFacesContains(2) && countOfFacesContains(3)) {
			return true;
		}
		else return false;
	}
	
	
	/** 
	 * This method returns true if "countOfFaces" contains the number
	 * "num" otherwise returns false.
	 * 
	 * @param 	num 	An int, number between 1 and 6(both including).
	 * @return boolean
	 */
	private boolean countOfFacesContains(int num) {
		for (int i = 0; i < N_FACES; i++) {
			if (countOfFaces[i] == num)
				return true;
		}
		return false;
	}
	
	
	/** 
	 * This method returns true if dice configuration is
	 * is small straight otherwise returns false.
	 * 			 
	 * @return boolean
	 */
	private boolean isSmallStraight() {
		if (diceContains("1234"))
			return true;
		else if (diceContains("2345"))
			return true;
		else if (diceContains("3456"))
			return true;
		else return false;
	}
	
	
	/** 
	 * This method returns true if "dice" contains the integer "num"
	 * i.e., the value of "num" has occurred atleast once after rolling 
	 * the dice otherwise returns false.
	 * 
	 * @param 	num 	An int, number between 1 and 6(both including).
	 * @return boolean
	 */
	private boolean diceContains(int num) {
		for (int i = 0; i < N_DICE; i++) {
			if (dice[i] == num) 
				return true;
		}
		return false;		
	}
	
	
	/** 
	 * This method returns true if "dice" contains all the numbers in the
	 * string "nums" i.e., all the numbers in the "sequence" has occurred
	 * atleast once after rolling the dice otherwise returns false.
	 * 
	 * @param 	nums 	A string of numeric letters representing the numbers on the dice.	 
	 * @return boolean
	 */
	private boolean diceContains(String nums) {
		int length = nums.length();
		for (int i = 0; i < length; i++) {
			if (diceContains(Character.getNumericValue(nums.charAt(i))))
				continue;
			else return false;
		}
		return true;
	}
	
	
	/** 
	 * This method returns true if dice configuration is 
	 * a large straight otherwise returns false.
	 * 			 
	 * @return boolean
	 */
	private boolean isLargeStraight() {
		if (diceContains("12345"))
			return true;
		else if (diceContains("23456"))
			return true;
		else return false;
	}

	
	/** 
	 * This method returns true if dice configuration is 
	 * yahtzee otherwise returns false.
	 * 			 
	 * @return 	boolean
	 */
	private boolean isYahtzee() {
		if (countOfFacesContains(5))
			return true;
		else return false;
	}

	
	/** 
	 * This method returns the score of the selected category.
	 * 
	 * @param 	category 	An integer representing the selected category.              
	 * @return 	int 		The score of the selected category. 
	 */	
	private int getScore(int category) {
		
		int score = 0;
		
		if (category < UPPER_SCORE) {
			score = getScoreForUpperCategories(category);
		} else if (category == THREE_OF_A_KIND ||
				   category == FOUR_OF_A_KIND ||
				   category == CHANCE) {
			score = addAllNumbers();
		} else if (category == FULL_HOUSE) {
			score = FULL_HOUSE_SCORE;
		} else if (category == SMALL_STRAIGHT) {
			score = SMALL_STRAIGHT_SCORE;
		} else if (category == LARGE_STRAIGHT) {
			score = LARGE_STRAIGHT_SCORE;
		} else if (category == YAHTZEE) {
			score = YAHTZEE_SCORE;
		}
		
		return score;
	}
	
	
	/** 
	 * This method returns the score for upper categories.
	 * If the selected categroy is "TWOS" then the score 
	 * is sum of all the twos in the dice.
	 * 
	 * @param 	category 	An integer representing the selected category.
	 * @return 	int	  		The sum of all the category numbers. 
	 */
	private int getScoreForUpperCategories(int category) {
		int score = 0;
		for (int i = 0; i < N_DICE; i++) {
			if (dice[i] == category)
				score += dice[i];
		}
		return score;
	}
	

	/** 
	 * This method returns the score by adding all
	 * the numbers on the dice.
	 * 
	 * @return 	int 	The sum of all the numbers those have come up on the dice.
	 */	
	private int addAllNumbers() {
		int score = 0;
		for (int i = 0; i < N_DICE; i++)
			score += dice[i];
		return score;
	}
	

	/** 
	 * This method sets the scores in the "scorecard" array updates the scores in the 
	 * scorecard on the UI.
	 * 
	 * @param	category	A static constant representing a category.
	 * @param	player 		An int representing the player.
	 * @param 	score		An int, the score to be filled.     
	 */	
	private void setScores(int player, int category, int score) {
		
		scorecard[category-1][player-1] = score;
		display.updateScorecard(category, player, score);
		
		if (category < UPPER_SCORE) {
			scorecard[UPPER_SCORE-1][player-1] += score;
			display.updateScorecard(UPPER_SCORE, player, scorecard[UPPER_SCORE-1][player-1]);
		} else {
			scorecard[LOWER_SCORE-1][player-1] += score;
			display.updateScorecard(LOWER_SCORE, player, scorecard[LOWER_SCORE-1][player-1]);
		}
		
		scorecard[TOTAL-1][player-1] = scorecard[UPPER_SCORE-1][player-1] +
                					   scorecard[LOWER_SCORE-1][player-1];
		display.updateScorecard(TOTAL, player, scorecard[TOTAL-1][player-1]);

	}
	
	
	/** 
	 * This method gives the bonus to the players who got more than the
	 * required upper score and adds the bonus to their total scores.     
	 */	
	private void giveBonus() {
		
		for (int player = 1; player <= nPlayers; player++) {
			if (scorecard[UPPER_SCORE - 1][player - 1] >= MIN_UPPER_SCORE_TO_GET_UPPER_BONUS) {
				scorecard[UPPER_BONUS - 1][player - 1] = UPPER_BONUS_SCORE;
				display.updateScorecard(UPPER_BONUS, player, UPPER_BONUS_SCORE);
				
				scorecard[TOTAL - 1][player - 1] += scorecard[UPPER_BONUS - 1][player - 1];
				display.updateScorecard(TOTAL, player, scorecard[TOTAL - 1][player - 1]);
			}
		}
	}
	
	
	/** 
	 * This method returns the names of the winners.
	 * 
	 * @return String	Name of the winner of the game.
	 */	
	private String getWinner() {
		int[] finalScores = scorecard[TOTAL-1].clone();
		Arrays.sort(finalScores);
		int winningScore = finalScores[nPlayers-1];
		
		String winners = "";
		for (int player = 0; player < nPlayers; player++) {
			if (scorecard[TOTAL-1][player] == winningScore)
				winners = winners + " " + playerNames[player];
		}
		return winners;
	}
	
	
	/* Private instance variables */
		
	/** An integer to store the number of players */
	private int nPlayers;
		
	/** An array of strings to store the names of all the players */	
	private String[] playerNames;
		
	/** An object which has all the methods to manage the UI */
	private YahtzeeDisplay display;
		
	/** A Random generator to store random numbers in the "dice" array during Roll */
	private RandomGenerator rgen = new RandomGenerator();
		
	/** An array to represent the dice */
	private int[] dice = new int[N_DICE];
		
	/** A 2D array to store the scores */
	private int[][] scorecard;
		
	/** A 2D array to keep track of selected categories */
	private boolean[][] selectedCategories;
		
	/** This array stores the number of occurences of each face after roll */
	private int[] countOfFaces = new int[N_FACES];
}



