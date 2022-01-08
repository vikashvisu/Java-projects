
/** 
 * File: YahtzeeDisplay.java
 * -------------------------
 * This file creates and manages the UI of the game.
 */


import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import acm.graphics.*;
import acm.io.IODialog;


public class YahtzeeDisplay implements YahtzeeConstants, YahtzeeDisplayConstants {
	
	/** 
	 * Constructs the UI.
	 * 
	 * @param 	canvas 			A GCanvas, the default canvas on the window of the program.
	 * @param 	playerNames 	An array of strings, the names of the players.
	 */
	public YahtzeeDisplay(GCanvas canvas, String[] playerNames) {
		yCanvas = canvas;
		yCanvas.setBackground(DARK_GREEN);
		addAboutButton();
		addLearnToPlayButton();
		addRollButton();
		addQDice();
		addScorecard(playerNames);
		addMouseListeners();
		currentThread = Thread.currentThread();
	}
	
	
	/** 
	 * This method adds the "About" button on the UI.
	 */
	private void addAboutButton() {
		aboutButton = getWhiteRoundRect(ABOUT_BUTTON_WIDTH, ABOUT_BUTTON_HEIGHT);
		yCanvas.add(aboutButton, (yCanvas.getWidth() - ABOUT_BUTTON_WIDTH), 0);
		
		GLabel label = new GLabel("About");
		label.setFont("Arial-Bold-15");
		label.setColor(Color.BLUE);
		addLabel(label, aboutButton);		
	}
	
	
	/** 
	 * This method adds a label on an object.
	 * 
	 * @param 	label		A GLabel.
	 * @param 	obj			A GObject to get labeled.
	 */
	private void addLabel(GLabel label, GObject obj) {
		double xLoc = obj.getX() + ((obj.getWidth() - label.getWidth()) / 2);	
		double yLoc = obj.getY() + (obj.getHeight() * 0.85);
		yCanvas.add(label, xLoc, yLoc);
	}
	
	
	/** 
	 * This method creates a white round rectangle with black border(default) 
	 * and returns it.
	 * 
	 * @param	width		A double, Width of the rectangle.
	 * @param 	height		A double, Height of the rectangle.
	 * @return 	GRoundRect	A round rectangle with dimension width*height.
	 */
	private GRoundRect getWhiteRoundRect(double width, double height) {
		GRoundRect roundRect = new GRoundRect(width, height);
		roundRect.setFillColor(Color.WHITE);
		roundRect.setFilled(true); 
		return roundRect;
	}
	
	
	/** 
	 * This method adds the "Learn To Play" button on the UI.
	 */
	private void addLearnToPlayButton() {
		learnToPlayButton = getWhiteRoundRect(LEARN_BUTTON_WIDTH, LEARN_BUTTON_HEIGHT);
		yCanvas.add(learnToPlayButton, (aboutButton.getX() - LEARN_BUTTON_WIDTH), 0);
		
		GLabel label = new GLabel("Learn To Play");
		label.setFont("Arial-Bold-15");
		label.setColor(Color.BLUE);
		addLabel(label, learnToPlayButton);		
	}
	
	
	/** 
	 * This method adds the "Roll Dice" button on the UI.
	 */
	private void addRollButton() {
		rollButton = getWhiteRoundRect(ROLL_BUTTON_WIDTH, ROLL_BUTTON_HEIGHT);
		yCanvas.add(rollButton, H_MARGIN, V_MARGIN);
		
		GLabel label = new GLabel("Roll Dice");
		label.setFont("Arial-Bold-15");
		label.setColor(Color.BLUE);
		addLabel(label, rollButton);		
	}
	
	
	/** 
	 * This method adds question mark labeled dice on the UI.
	 */
	private void addQDice() {
		
		double xLoc = 2 * H_MARGIN;
		double yLoc = (2 * V_MARGIN) + ROLL_BUTTON_HEIGHT;
		for (int i = 0; i < N_DICE; i++) {
			dice[i] = getWhiteRoundRect(DIE_WIDTH, DIE_WIDTH);
			yCanvas.add(dice[i], xLoc, yLoc);
			yLoc += DIE_WIDTH + SPACE_B_DICE;
			
			GLabel qMark = new GLabel("?");
			qMark.setColor(Color.BLUE);
			qMark.setFont("Lucida Sans-50");
			addLabel(qMark, dice[i]);
		}
	}
	
	
	/** 
	 * This method adds scorecard on the UI.
	 * 
	 * @param 	playerNames 	An array of strings, the names of the players. 
	 */
	private void addScorecard(String[] playerNames) {
		
		int nPlayers = playerNames.length;
		scorecard = new GRect[N_CATEGORIES + 1][nPlayers + 1];
		double rowHeight = ((dice[4].getY() + DIE_WIDTH) - dice[0].getY()) / (N_CATEGORIES + 1);
		
		addCategoryColumn(rowHeight);
		addPlayerColumns(rowHeight, nPlayers);
		addPlayerNames(playerNames);

		colorTitleRow();
		colorRowsLabeledWithScoreTrackers();
		colorDivider();
	}
	
	
	/** 
	 * This method adds the first column(category column) of the scorecard. 
	 * 
	 * @param rowHeight		A double, the height of each row of the scorecard. 
	 */
	private void addCategoryColumn(double rowHeight) {
		
		double xLoc = (2 * H_MARGIN) + ROLL_BUTTON_WIDTH;
		double yLoc = dice[0].getY();
		
	    String[] categoryColumnLabels = {"Category", "Ones", "Twos", "Threes", "Fours", "Fives", "Sixes",
				 						 "Upper Score", "Upper Bonus(35) If Upper Score >= 63", "Three of a Kind",
				 						 "Four of a Kind", "Full House (25)", "Small Straight (30)",
				 						 "Large Straight (40)", "Yahtzee!!! (50)", "Chance", "Lower Score", "TOTAL"};
		
		for (int i = 0; i < scorecard.length; i++) {
			scorecard[i][0] = getWhiteRect(CATEGORY_COLUMN_WIDTH, rowHeight);
			yCanvas.add(scorecard[i][0], xLoc, yLoc);
			yLoc += rowHeight;
			
			GLabel label = new GLabel(categoryColumnLabels[i]);
			label.setFont("Arial-Bold-12");
			label.setColor(DARK_GREEN);
			addLabel(label, scorecard[i][0]);
		}	
	}
	
	
	/** 
	 * This method creates a white rectangle with black border(default) 
	 * and returns it.
	 * 
	 * @param	width		A double, Width of the rectangle.
	 * @param 	height		A double, Height of the rectangle.
	 * @return 	GRect		A rectangle with dimension width*height.
	 */
	private GRect getWhiteRect(double width, double height) {
		
		GRect rect = new GRect(width, height);
		rect.setFillColor(Color.WHITE);
		rect.setFilled(true); 
		return rect;
	}
	
	
	/** 
	 * This method adds all the player columns of the scorecard. 
	 * 
	 * @param rowHeight		A double, height of each row in the scorecard. 
	 * @param nPlayers 		An int, number of players.
	 */
	private void addPlayerColumns(double rowHeight, int nPlayers) {
		
		double xLoc = scorecard[0][0].getX() + CATEGORY_COLUMN_WIDTH;
		double yLoc = scorecard[0][0].getY();
		
		for (int i = 0; i < scorecard.length; i++) {
		
			for (int j = 1; j <= nPlayers; j++) {
				scorecard[i][j] = getWhiteRect(PLAYER_COLUMN_WIDTH, rowHeight);
				yCanvas.add(scorecard[i][j], xLoc, yLoc);
				xLoc += PLAYER_COLUMN_WIDTH;
			}
			
			yLoc += rowHeight;
			xLoc = scorecard[0][0].getX() + CATEGORY_COLUMN_WIDTH;
		}
	}
	
	
	/** 
	 * This method adds the names of all the players on the scorecard.
	 * 
	 * @param 	playerNames 	An array of strings, names of the players.
	 */
	private void addPlayerNames(String[] playerNames) {
		
		for (int i = 1; i <= playerNames.length; i++) {
			GLabel name = new GLabel(playerNames[i - 1]);
			name.setFont("Arial-Bold-12");
			name.setColor(DARK_RED);
			addLabel(name, scorecard[0][i]);
		}
	}
	
	
	/** 
	 * This method colors the first(title) row of the scorecard.
	 */
	private void colorTitleRow() {
		for (int i = 0; i < scorecard[0].length; i++) {
			scorecard[0][i].setFillColor(Color.ORANGE);
		}
	}
	
	
	/** 
	 * This method highlights the rows labeled with score trackers,
	 * such as upper score.
	 */
	private void colorRowsLabeledWithScoreTrackers() {
		
		for (int i = 0; i < scorecard[0].length; i++) {
			scorecard[UPPER_SCORE][i].setFillColor(Color.LIGHT_GRAY);	
		}
		
		for (int i = 0; i < scorecard[0].length; i++) {
			scorecard[UPPER_BONUS][i].setFillColor(Color.PINK);	
		}
		
		for (int i = 0; i < scorecard[0].length; i++) {
			scorecard[LOWER_SCORE][i].setFillColor(Color.LIGHT_GRAY);	
		}
		
		for (int i = 0; i < scorecard[0].length; i++) {
			scorecard[TOTAL][i].setFillColor(Color.YELLOW);	
		}
	}
	
	
	/** 
	 * This method colors the divider(line) between the category column and the other(player) columns
	 * in blue.
	 */
	private void colorDivider() {
		GLine line = new GLine(scorecard[0][1].getX(), scorecard[0][0].getY(),
							   scorecard[TOTAL][1].getX(), dice[4].getY() + DIE_WIDTH);
		line.setColor(Color.BLUE);
		yCanvas.add(line);
	}
	
	
	/** 
	 * This method adds the mouse listeners.
	 */
	private void addMouseListeners() {
		
		yCanvas.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				
				if (rollButton.contains(e.getX(), e.getY()) && waitingForPlayerToClickRoll) {
					synchronized(currentThread) {
						currentThread.notify();
					}
				}
				
				if (waitingForPlayerToSelectDice) {
					for (int i = 0; i < N_DICE; i++) {
						if (dice[i].contains(e.getX(), e.getY()) && !selectedDice[i]) {
							dice[i].setFillColor(Color.BLUE);
							selectedDice[i] = true;
						} else if (dice[i].contains(e.getX(), e.getY()) && selectedDice[i]) {
							dice[i].setFillColor(Color.WHITE);
							selectedDice[i] = false;
						}
					}
				}
				
				if (waitingForPlayerToSelectCategory) {
					for (int i = ONES; i <= CHANCE; i++) {
						if (scorecard[i][0].contains(e.getX(), e.getY()) && i != UPPER_SCORE && i != UPPER_BONUS) {
							selectedCategory = i;
							synchronized(currentThread) {
								currentThread.notify();
							}
						}
					}
				}
				
				if (learnToPlayButton.contains(e.getX(), e.getY())) {
					try {
						Desktop.getDesktop().browse(new URI("http://www.yahtzee-game.com/#rules"));
					} catch (IOException e1) {}
					  catch (URISyntaxException e2) {}	
				}
				
				if (aboutButton.contains(e.getX(), e.getY())) {
					IODialog aboutDialog = new IODialog();
					aboutDialog.println("Yahtzee\nVersion  1.0\nDeveloper  Vikash Kumar(317356)\n" +
										"Guidance  Miss Vinita Gupta\n ");
				}
			}
			
			public void mouseEntered(MouseEvent e) { }
			public void mouseExited(MouseEvent e) { }
			public void mousePressed(MouseEvent e) { }
			public void mouseReleased(MouseEvent e) { }
			
		});	
	}
	
	
	/** 
	 * This method prints a message on the UI.
	 * 
	 * @param 	message 	A string, the message to get printed.
	 */
	public void printMessage(String message) {
		
		GLabel label = new GLabel(message);
		label.setColor(Color.WHITE);
		label.setFont("Lucida Sans-Bold-12");
		
		GRect messageBoard = getWhiteRect(yCanvas.getWidth() - (H_MARGIN + ROLL_BUTTON_WIDTH), 2 * label.getHeight());
		messageBoard.setColor(DARK_GREEN);
		messageBoard.setFillColor(DARK_GREEN);
		messageBoard.setFilled(true);
		yCanvas.add(messageBoard, H_MARGIN + ROLL_BUTTON_WIDTH, V_MARGIN);
		
		yCanvas.add(label, MESSAGE_LOC);
	}
		
	/**
	 * This method pauses the program and waits for the "Roll Dice" button to get clicked.
	 * The program is resumed, when the "Roll Dice" button is clicked. 
	 * 
	 * @param player 	An int representing the player
	 */
	public void waitForPlayerToClickRoll(int player) {
		addQDice();
		highlightPlayer(player);
		waitingForPlayerToClickRoll = true;
		synchronized(currentThread) {
			try {
				currentThread.wait();
			} catch(InterruptedException e) { }
		}
		waitingForPlayerToClickRoll = false;
	}
	
	
	/** 
	 * This method highlights the player whose turn is on.
	 * 
	 * @param	player 	An int representing the player.
	 */
	public void highlightPlayer(int player) {
		if (player == 1) {
			scorecard[0][scorecard[0].length - 1].setFillColor(Color.ORANGE);
			scorecard[0][player].setFillColor(Color.MAGENTA);
		} else {
			scorecard[0][player - 1].setFillColor(Color.ORANGE);
			scorecard[0][player].setFillColor(Color.MAGENTA);
		}
	}
	
	
	/** 
	 * This method displays the numbers on the dice after roll.
	 * 
	 * @param 	intDice 		An array of integers, numbers to get shown on the dice.	
	 */
	public void displayDice(int[] intDice) {
		
		double xLoc = 2 * H_MARGIN;
		double yLoc = (2 * V_MARGIN) + ROLL_BUTTON_HEIGHT;
		for (int i = 0; i < N_DICE; i++) {
			dice[i] = getWhiteRoundRect(DIE_WIDTH, DIE_WIDTH);
			yCanvas.add(dice[i], xLoc, yLoc);
			yLoc += DIE_WIDTH + SPACE_B_DICE;
			
			GLabel num = new GLabel(Integer.toString(intDice[i]));
			num.setColor(DARK_RED);
			num.setFont("Lucida Sans-50");
			addLabel(num, dice[i]);
		}
	}
	
	
	/**
	 * This method pauses the program and waits for the dice to get selected.
	 * The program is resumed, when the "Roll Dice" button is clicked.
	 */
	public void waitForPlayerToSelectDice() {
		unselectDice();
		waitingForPlayerToSelectDice = true;
		waitingForPlayerToClickRoll = true;
		synchronized(currentThread) {
			try {
				currentThread.wait();
			} catch(InterruptedException e) { }
		}
		waitingForPlayerToSelectDice = false;
		waitingForPlayerToClickRoll = false;
	}
	
	
	/**
	 * This method sets the select property of each die to false.
	 */
	private void unselectDice() {
		for (int i = 0; i < N_DICE; i++) {
			selectedDice[i] = false;
		}
	}
	
	
	/** 
	 * This method returns the select property of a die.
	 * 
	 * @param	diceNum		An int, the index of a die.
	 * @return 	boolean		Select property of the die.
	 */
	public boolean isDieSelected(int diceNum) {
		return selectedDice[diceNum];
	}
	

	/** 
	 * This method returns the selected category.
	 * 
	 * @return 	int		A static constant representing the selected category. 
	 */
	public int waitForPlayerToSelectCategory() {
		waitingForPlayerToSelectCategory = true;
		synchronized(currentThread) {
			try {
				currentThread.wait();
			} catch(InterruptedException e) { }
		}
		
		waitingForPlayerToSelectCategory = false;
		return selectedCategory;
	}
	
	
	/** 
	 * This method fills the scores in the scorecard.
	 * 
	 * @param	category	A static constant representing a category.
	 * @param	player 		An int representing the player.
	 * @param 	score		An int, the score to be filled.
	 */
	public void updateScorecard(int category, int player, int score) {
				
		scorecard[category][player].sendToFront();
		
		GLabel scoreLabel = new GLabel(Integer.toString(score));
		scoreLabel.setFont("Arial-Bold-12");
		scoreLabel.setColor(Color.BLUE);
		addLabel(scoreLabel, scorecard[category][player]);	
	}
	
	
	/* private instance variables */
	
	/** It stores a reference to the canvas on the window of the program. */
	private GCanvas yCanvas;
	
	/**	It stores a reference to the "Learn To Play" button. */
	private GRoundRect learnToPlayButton;
	
	/**	It stores a reference to the "About" button. */
	private GRoundRect aboutButton;
	
	/**	It stores a reference to the "Roll Dice" button. */
	private GRoundRect rollButton;
	
	/** It stores a reference to the graphical dice. */
	private GRoundRect[] dice = new GRoundRect[N_DICE];
	
	/** It stores a reference to the graphical scorecard. */
	private GRect[][] scorecard;
	
	/** It stores a reference to the currently executing thread object. */
	private Thread currentThread;
	
	/** It stores a state of the program. */
	private boolean waitingForPlayerToClickRoll = false;
	
	/** It stores a state of the program. */
	private boolean waitingForPlayerToSelectDice = false;
	
	/** It stores a state of the program. */
	private boolean waitingForPlayerToSelectCategory = false;
	
	/** 
	 * It stores the select properties of the dice. For example: If the second element of 
	 * "selectedDice" array is true then the second die is selected.
	 */
	private boolean[] selectedDice = new boolean[N_DICE];
	
	/** It stores the static constant representing the selected category. */
	private int selectedCategory; 

	
}




