/***********************************************************************
 * 
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * 
 *********************************************************************** 
 *
 * Author:   	  Daniel "tentacle" Galán y Martins
 * Creation date: 02.05.2003
 *  
 * Revision:      $Revision: 1.3 $
 * Checked in by: $Author: ilgian $
 * Last modified: $Date: 2009/02/23 13:42:06 $
 * 
 * $Log: TerminalIOInterface.java,v $
 * Revision 1.3  2009/02/23 13:42:06  ilgian
 * Added getPort() method
 *
 * Revision 1.2  2004/12/05 17:53:49  danielgalan
 * Refactored this damn beepError thing
 *
 * Revision 1.1  2004/07/26 21:40:27  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.io;

import java.io.IOException;
import java.net.InetAddress;



/**
 * Basic functionality for I/O
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.3 $
 */
public interface TerminalIOInterface {

    //--------------------------------------------------------------------------
    // constants
    //--------------------------------------------------------------------------

    /** represents the color Black */
    public final static int COLOR_BLACK = 1;

    /** represents the color red */
    public final static int COLOR_RED = 2;

    /** represents the color green */
    public final static int COLOR_GREEN = 3;

    /** represents the color yellow */
    public final static int COLOR_YELLOW = 4;

    /** represents the color blue */
    public final static int COLOR_BLUE = 5;

    /** represents the color magenta */
    public final static int COLOR_MAGENTA = 6;

    /** represents the color cyan */
    public final static int COLOR_CYAN = 7;

    /** represents the color white */
    public final static int COLOR_WHITE = 8;


    /** direction of the cursor on relative movement: up */
    public final static int CURSOR_DIRECTION_UP = 1;

    /** direction of the cursor on relative movement: down */
    public final static int CURSOR_DIRECTION_DOWN = 2;

    /** direction of the cursor on relative movement: right */
    public final static int CURSOR_DIRECTION_RIGHT = 3;

    /** direction of the cursor on relative movement: left */
    public final static int CURSOR_DIRECTION_LEFT = 4;


    /** on line operations: horizontal alignment */
    public final static int ORIENTATION_HORIZONTAL = 1;

    /** on line operations: vertical alignment */
    public final static int ORIENTATION_VERTIKAL = 2;



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** absolut cursor movement on a terminal display */
    public void cursorMoveAbsolut(int line, int column) throws IOException;



    /** relative cursor movement on a terminal display */
    public void cursorMoveRelative(int direction, int steps) throws IOException;


    /** writes <code>text</code> to the current cursor position */
    public void writeText(String text) throws IOException;


    /** writes <code>text</code> to the specific position */
    public void writeText(String text, int posLine, int posColumn) throws IOException;


    /** writes <code>text</code> invers to the current cursor position */
    public void writeInverseText(String text) throws IOException;



    /** writes <code>text</code> invers to the specific position */
    public void writeInverseText(String text, int posLine, int posColumn) throws IOException;



    /** sets the bold text style */
    public void setBold(boolean bold) throws IOException;



    /** sets the underline text style */
    public void setUnderlined(boolean underlined) throws IOException;



    /** sets the blink text style */
    public void setBlink(boolean blink) throws IOException;



    /** sets the invers text style */
    public void setReverse(boolean reverse) throws IOException;



    /** sets the foreground color according to it's constants */
    public void setForeground(int color) throws IOException;



    /** sets the background color according to it's constants */
    public void setBackground(int color) throws IOException;



    /** creates manually line break */
    public void newLine() throws IOException;



    /** clears the screen */
    public void clearScreen() throws IOException;



    /** draws a line */
    public void drawLine(int line, int column, int orientation, int length) throws IOException;



    /** draws an not filled rectangle */
    public void drawRectangle(int fromLine, int fromColumn, int toLine, int toColumn) throws IOException;



    /** clears a line */
    public void clearLine(int line, int column, int orientation, int length) throws IOException;



    /** writes content of the writer buffer */
    public void flush() throws IOException;



    /** closes the IO */
    public void close() throws IOException;



    /** Returns the internet-adress associated with the terminal */
    public InetAddress getInetAdress();


    /** Returns the port associated with the terminal */
    public int getPort();


    /** reads the next event from the reader */
    public TerminalEvent readNextEvent() throws IOException;



    /** Makes an errortone */
    public void beepError() throws IOException;



    /** @todo public void enableLED(int led) throws IOException; */
    /** @todo public void disableLEDs() throws IOException; */

    /** @todo public static int getWidth(); */
    /** @todo public static int getHeight(); */
}
