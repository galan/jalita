/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * Author: Daniel Galán y Martins
 * Creation date: 30.04.2003
 * Revision: $Revision: 1.4 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:59 $
 * $Log: VT100Constants.java,v $
 * Revision 1.4  2009/12/02 21:53:59  danielgalan
 * naming
 *
 * Revision 1.3 2004/12/05 17:53:49 danielgalan
 * Refactored this damn beepError thing
 * Revision 1.2 2004/08/06 00:55:34 danielgalan
 * prepare release
 * Revision 1.1 2004/07/26 21:40:28 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.io;

/**
 * Constants of the VT100 specification (see http://vt100.net/docs/vt100-ug)
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.4 $
 */

public interface VT100Constants {

	//--------------------------------------------------------------------------
	// common constants
	//--------------------------------------------------------------------------

	/** Escape as int */
	public final static int ASCII_ESC = 033;

	/** Escape as String */
	public final static String ESC = "\u001b";

	/** Control Sequence Introducer */
	public final static String CSI = ESC + "[";

	/** Parameter Delimiter */
	public final static String DELIMITER = ";";

	/** Carriage Return */
	public final static int CR = 13; // '\n' == 0D

	/** Line Feed */
	public final static int LF = 10; // '\f' == 0A

	/** Control-Sequence for error tone */
	public final static String BEEP_ERROR = "" + '\07';

	//--------------------------------------------------------------------------
	// constants, that define the meaning of the Control Sequence
	//--------------------------------------------------------------------------

	public final static String PARAM_ERASE_IN_DISPLAY_ACTIVE_TO_END = "0";
	public final static String PARAM_ERASE_IN_DISPLAY_START_TO_ACTIVE = "1";
	public final static String PARAM_ERASE_IN_DISPLAY_ALL = "2";

	public final static String PARAM_ERASE_IN_LINE_ACTIVE_TO_END = "0";
	public final static String PARAM_ERASE_IN_LINE_START_TO_ACTIVE = "1";
	public final static String PARAM_ERASE_IN_LINE_ALL = "2";

	public final static String FINAL_CURSOR_MOVE_ABSOLUT = "H";
	public final static String FINAL_CURSOR_MOVE_UP = "A";
	public final static String FINAL_CURSOR_MOVE_DOWN = "B";
	public final static String FINAL_CURSOR_MOVE_RIGHT = "C";
	public final static String FINAL_CURSOR_MOVE_LEFT = "D";
	public final static String FINAL_CURSOR_MOVE_HOME = "H";

	public final static String FINAL_NEW_LINE = "E";
	public final static String FINAL_ERASE_IN_DISPLAY = "J";
	public final static String FINAL_ERASE_IN_LINE = "K";

	public final static String FINAL_COLOR = "m";

	public final static String FINAL_ATTRIBUTE = "m";

	//--------------------------------------------------------------------------
	// constants, that define character attributes on the terminal display
	//--------------------------------------------------------------------------

	public final static String ATTRIBUTE_OFF = "0";
	public final static String ATTRIBUTE_BOLD = "1";
	public final static String ATTRIBUTE_UNDERSCORE = "4";
	public final static String ATTRIBUTE_BLINK = "5";
	public final static String ATTRIBUTE_REVERSE = "7";

	//--------------------------------------------------------------------------
	// constants, that define barcode control codes
	//--------------------------------------------------------------------------

	public final static int BARCODE_START = '*'; // 42
	public final static int BARCODE_END = CR;

	//--------------------------------------------------------------------------
	// constants, that define color codes
	//--------------------------------------------------------------------------

	public final static int COLOR_BLACK_FORE = 30;
	public final static int COLOR_BLACK_BACK = 40;
	public final static int COLOR_RED_FORE = 31;
	public final static int COLOR_RED_BACK = 41;
	public final static int COLOR_GREEN_FORE = 32;
	public final static int COLOR_GREEN_BACK = 42;
	public final static int COLOR_YELLOW_FORE = 33;
	public final static int COLOR_YELLOW_BACK = 43;
	public final static int COLOR_BLUE_FORE = 34;
	public final static int COLOR_BLUE_BACK = 44;
	public final static int COLOR_MAGENTA_FORE = 35;
	public final static int COLOR_MAGENTA_BACK = 45;
	public final static int COLOR_CYAN_FORE = 36;
	public final static int COLOR_CYAN_BACK = 46;
	public final static int COLOR_WHITE_FORE = 37;
	public final static int COLOR_WHITE_BACK = 47;

	//--------------------------------------------------------------------------
	// constants, that define incoming ESC-Parameters and special keys for TerminalEvents
	//--------------------------------------------------------------------------

	public final static int ESC_PARAM_F_KEY = 79;
	public final static int ESC_PARAM_F_KEY_1 = 49;
	public final static int ESC_PARAM_F_KEY_2 = 50;
	public final static int ESC_PARAM_CURSOR = 91;
	public final static int ESC_PARAM_CURSOR_FINAL_EXTENDED = 126;
	public final static int ESC_PARAM_PIPE = 60;

	public final static int ESC_F_01 = 80;
	public final static int ESC_F_02 = 81;
	public final static int ESC_F_03 = 82;
	public final static int ESC_F_04 = 83;
	public final static int ESC_F_05 = 54;
	public final static int ESC_F_06 = 55;
	public final static int ESC_F_07 = 56;
	public final static int ESC_F_08 = 57;
	public final static int ESC_F_09 = 48;
	public final static int ESC_F_10 = 49;
	public final static int ESC_F_11 = 51;
	public final static int ESC_F_12 = 52;
	public final static int ESC_F_05_ALT = 84;
	public final static int ESC_F_06_ALT = 85;
	public final static int ESC_F_07_ALT = 86;
	public final static int ESC_F_08_ALT = 87;
	public final static int ESC_F_09_ALT = 88;
	public final static int ESC_F_10_ALT = 89;
	public final static int ESC_F_11_ALT = 90;
	public final static int ESC_F_12_ALT = 91;

	public final static int ESC_CURSOR_UP = 65;
	public final static int ESC_CURSOR_DOWN = 66;
	public final static int ESC_CURSOR_RIGHT = 67;
	public final static int ESC_CURSOR_LEFT = 68;
	public final static int ESC_CURSOR_TAB_BACK = 90;

	public final static int ESC_CURSOR_EXT_PASTE = 50;
	public final static int ESC_CURSOR_EXT_DEL = 51;
	public final static int ESC_CURSOR_EXT_HOME = 49;
	public final static int ESC_CURSOR_EXT_END = 52;
	public final static int ESC_CURSOR_EXT_PAGE_UP = 53;
	public final static int ESC_CURSOR_EXT_PAGE_DOWN = 54;

	public final static int SPECIAL_AE_BIG = 196;
	public final static int SPECIAL_AE_SMALL = 228;
	public final static int SPECIAL_OE_BIG = 214;
	public final static int SPECIAL_OE_SMALL = 246;
	public final static int SPECIAL_UE_BIG = 220;
	public final static int SPECIAL_UE_SMALL = 252;

	public final static int SPECIAL_SZ = 63;
	public final static int SPECIAL_GRAD = 176;
	public final static int SPECIAL_ROOF = 94; // Character '^'

	public final static int SPECIAL_TAB = 9;
	public final static int SPECIAL_PAUSE = 26;

	public final static int SPECIAL_BACKSPACE = 8;
	public final static int SPECIAL_DEL = 127;

}
