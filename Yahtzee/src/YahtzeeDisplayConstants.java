
/** 
 * File: YahtzeeDisplayConstants.java
 * ----------------------------------
 * This file declares several constants that are shared by the
 * different modules in the YahtzeeDisplay class.
 * 
 */

import java.awt.Color;
import acm.graphics.GPoint;

public interface YahtzeeDisplayConstants {

	/** Dark green color. */	
	public static final Color DARK_GREEN = new Color(0, 100, 0);
	
	/** Dark red color. */
	public static final Color DARK_RED = new Color(128, 0, 0);
	
	/** Vertical margin */	
	public static final double V_MARGIN = 30;
	
	/** Horizontal margin */
	public static final double H_MARGIN = 25;
	
	/** Width of the "Learn To Play" button. */	
	public static final double LEARN_BUTTON_WIDTH = 150;
	
	/** Height of the "Learn To Play" button. */
	public static final double LEARN_BUTTON_HEIGHT = 20;
	
	/** Width of the "About" button. */	
	public static final double ABOUT_BUTTON_WIDTH = 100;
	
	/** Height of the "About" button. */
	public static final double ABOUT_BUTTON_HEIGHT = 20;
	
	/** Width of the "Roll Dice" button. */	
	public static final double ROLL_BUTTON_WIDTH = 110;
	
	/** Height of the "Roll Dice" button. */
	public static final double ROLL_BUTTON_HEIGHT = 20;
	
	/** Dimension of a die */	
	public static final double DIE_WIDTH = 60;
	
	/** Space between two dice */
	public static final double SPACE_B_DICE = 10;
	
	/** Width of the first column(Category column) of the scoresheet */	
	public static final double CATEGORY_COLUMN_WIDTH = 250;
	
	/** Width of the player column of the scoresheet */
	public static final double PLAYER_COLUMN_WIDTH = 85;
	
	/** Location of the message */	
	public static final GPoint MESSAGE_LOC = new GPoint((2 * H_MARGIN) + ROLL_BUTTON_WIDTH, 
			                                              V_MARGIN + ROLL_BUTTON_HEIGHT);
	
}
