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
 * Creation date: 30.04.2003
 *  
 * Revision:      $Revision: 1.4 $
 * Checked in by: $Author: ilgian $
 * Last modified: $Date: 2009/02/26 16:50:59 $
 * 
 * $Log: VT100Writer.java,v $
 * Revision 1.4  2009/02/26 16:50:59  ilgian
 * Added beep with beepCount parameter
 *
 * Revision 1.3  2005/05/23 18:10:20  danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 *
 * Revision 1.2  2004/12/05 17:53:49  danielgalan
 * Refactored this damn beepError thing
 *
 * Revision 1.1  2004/07/26 21:40:27  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.io;

import java.io.*;
import org.apache.log4j.Logger;

import net.sf.jalita.util.Configuration;



/**
 * VT100-compatible stream writer, almost.
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.4 $
 */
public class VT100Writer extends Writer implements VT100Constants {

    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    /** log4j reference */
    public final static Logger log = Logger.getLogger(Configuration.class);



    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** Tells us, if the output should be automatically be flushed to the writer after a newLine */
    private boolean autoFlush = true;

    /** tells us, is the stream closed */
    private boolean closed = false;

    /** The chain stream */
    private Writer out;

    /** text style bold */
    private boolean attributeBold = false;

    /** text style underlined */
    private boolean attributeUnderscore = false;

    /** text style blinking */
    private boolean attributeBlink = false;

    /** text style inverse */
    private boolean attributeReverse = false;



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a new VT100Writer */
    public VT100Writer(Writer out) {
        log.debug("Creating instance of VT100Writer");
        this.out = out;
    }



    /** Creates a new VT100Writer */
    public VT100Writer(Writer out, boolean autoFlush) {
        log.debug("Creating instance of VT100Writer");
        this.out = out;
        this.autoFlush = autoFlush;
    }



    /** Creates a new VT100Writer */
    public VT100Writer(OutputStream out) {
        log.debug("Creating instance of VT100Writer");
        this.out = new OutputStreamWriter(out);
    }



    /** Creates a new VT100Writer */
    public VT100Writer(OutputStream out, boolean autoFlush) {
        log.debug("Creating instance of VT100Writer");
        this.out = new OutputStreamWriter(out);
        this.autoFlush = autoFlush;
    }



    //--------------------------------------------------------------------------
    // private & protected methods
    //--------------------------------------------------------------------------

    /** Resets the text style */
    private void clearAttributes() throws IOException {
        out.write(CSI + ATTRIBUTE_OFF + FINAL_ATTRIBUTE);
    }



    /** Transmits current text style */
    private void sendAttributes() throws IOException {
        if (!attributeBold && !attributeUnderscore && !attributeBlink && !attributeReverse) {
            clearAttributes();
            return;
        }

        boolean previousToken = false;
        out.write(CSI);

        if (attributeBold) {
            if (previousToken) {
                out.write(DELIMITER);
            }
            out.write(ATTRIBUTE_BOLD);
            previousToken = true;
        }

        if (attributeUnderscore) {
            if (previousToken) {
                out.write(DELIMITER);
            }
            out.write(ATTRIBUTE_UNDERSCORE);
            previousToken = true;
        }

        if (attributeBlink) {
            if (previousToken) {
                out.write(DELIMITER);
            }
            out.write(ATTRIBUTE_BLINK);
            previousToken = true;
        }

        if (attributeReverse) {
            if (previousToken) {
                out.write(DELIMITER);
            }
            out.write(ATTRIBUTE_REVERSE);
            previousToken = true;
        }

        out.write(FINAL_ATTRIBUTE);
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** absolut cursor movement on a terminal display */
    public void cursorMoveAbsolut(int line, int column) throws IOException {
        out.write(CSI + line + DELIMITER + column + FINAL_CURSOR_MOVE_ABSOLUT);
    }



    /** relative cursor movement on a terminal display */
    public void cursorMoveRelative(int direction, int steps) throws IOException {
        switch (direction) {
            case TerminalIOInterface.CURSOR_DIRECTION_UP:
                out.write(CSI + steps + FINAL_CURSOR_MOVE_UP);
                break;
            case TerminalIOInterface.CURSOR_DIRECTION_DOWN:
                out.write(CSI + steps + FINAL_CURSOR_MOVE_DOWN);
                break;
            case TerminalIOInterface.CURSOR_DIRECTION_RIGHT:
                out.write(CSI + steps + FINAL_CURSOR_MOVE_RIGHT);
                break;
            case TerminalIOInterface.CURSOR_DIRECTION_LEFT:
                out.write(CSI + steps + FINAL_CURSOR_MOVE_LEFT);
                break;
            default:
                // Nicht machen
                break;
        }
    }



    /** writes <code>text</code> to the current cursor position */
    public void writeText(String text) throws IOException {
        /** @todo eventuell vorher auf sonderzeichen parsen */
        out.write(text);
    }



    /** writes <code>text</code> to the specific position */
    public void writeText(String text, int posLine, int posColumn) throws IOException {
        /** @todo eventuell vorher auf sonderzeichen parsen */
        cursorMoveAbsolut(posLine, posColumn);
        writeText(text);
    }



    /** writes <code>text</code> invers to the current cursor position */
    public void writeInverseText(String text) throws IOException {
        /** @todo eventuell vorher auf sonderzeichen parsen */

        setReverse(true);
        writeText(text);
        setReverse(false);
    }



    /** writes <code>text</code> invers to the specific position */
    public void writeInverseText(String text, int posLine, int posColumn) throws IOException {
        /** @todo eventuell vorher auf sonderzeichen parsen */

        setReverse(true);
        cursorMoveAbsolut(posLine, posColumn);
        writeText(text);
        setReverse(false);
    }



    /** sets the bold text style */
    public void setBold(boolean bold) throws IOException {
        attributeBold = bold;
        sendAttributes();
    }



    /** sets the underline text style */
    public void setUnderscore(boolean underscore) throws IOException {
        attributeUnderscore = underscore;
        sendAttributes();
    }



    /** sets the blink text style */
    public void setBlink(boolean blink) throws IOException {
        attributeBlink = blink;
        sendAttributes();
    }



    /** sets the invers text style */
    public void setReverse(boolean reverse) throws IOException {
        attributeReverse = reverse;
        sendAttributes();
    }



    /** sets the foreground color according to it's constants */
    public void setForeground(int color) throws IOException {
        switch(color) {
            case TerminalIOInterface.COLOR_BLACK:
                out.write(CSI + COLOR_BLACK_FORE + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_BLUE:
                out.write(CSI + COLOR_BLUE_FORE + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_CYAN:
                out.write(CSI + COLOR_CYAN_FORE + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_GREEN:
                out.write(CSI + COLOR_GREEN_FORE + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_MAGENTA:
                out.write(CSI + COLOR_MAGENTA_FORE + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_RED:
                out.write(CSI + COLOR_RED_FORE + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_WHITE:
                out.write(CSI + COLOR_WHITE_FORE + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_YELLOW:
                out.write(CSI + COLOR_YELLOW_FORE + FINAL_COLOR);
                break;
            default:
                out.write(CSI + COLOR_WHITE_FORE + FINAL_COLOR);
        }
    }



    /** sets the background color according to it's constants */
    public void setBackground(int color) throws IOException {
        switch(color) {
            case TerminalIOInterface.COLOR_BLACK:
                out.write(CSI + COLOR_BLACK_BACK + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_BLUE:
                out.write(CSI + COLOR_BLUE_BACK + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_CYAN:
                out.write(CSI + COLOR_CYAN_BACK + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_GREEN:
                out.write(CSI + COLOR_GREEN_BACK + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_MAGENTA:
                out.write(CSI + COLOR_MAGENTA_BACK + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_RED:
                out.write(CSI + COLOR_RED_BACK + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_WHITE:
                out.write(CSI + COLOR_WHITE_BACK + FINAL_COLOR);
                break;
            case TerminalIOInterface.COLOR_YELLOW:
                out.write(CSI + COLOR_YELLOW_BACK + FINAL_COLOR);
                break;
            default:
                out.write(CSI + COLOR_BLACK_BACK + FINAL_COLOR);
        }
    }



    /** creates manually line break */
    public void newLine() throws IOException {
        out.write(CSI + FINAL_NEW_LINE);
        if (autoFlush) {
        	flush();
        }
    }



    /** clears the screen */
    public void clearScreen() throws IOException {
        out.write(CSI + PARAM_ERASE_IN_DISPLAY_ALL + FINAL_ERASE_IN_DISPLAY);
    }



    /** draws a line */
    public void drawLine(int line, int column, int orientation, int length) throws IOException {
        setReverse(true);
        cursorMoveAbsolut(line, column);
        if (orientation == TerminalIOInterface.ORIENTATION_VERTIKAL) {
            for (int i = 0; i < length; i++) {
                writeText(" ", line+i, column);
            }
        }
        else {
            StringBuffer sb = new StringBuffer(length);
            for (int i = 0; i < length; i++) {
                sb.append(" ");
            }
            writeText(sb.toString());
        }
        setReverse(false);
    }



    /** draws an not filled rectangle */
    public void drawRectangle(int fromLine, int fromColumn, int toLine, int toColumn) throws IOException {
        drawLine(fromLine, fromColumn, TerminalIOInterface.ORIENTATION_HORIZONTAL, toColumn - fromColumn + 1);
        drawLine(fromLine, fromColumn, TerminalIOInterface.ORIENTATION_VERTIKAL, toLine - fromLine + 1);
        drawLine(toLine, fromColumn, TerminalIOInterface.ORIENTATION_HORIZONTAL, toColumn - fromColumn + 1);
        drawLine(fromLine, toColumn, TerminalIOInterface.ORIENTATION_VERTIKAL, toLine - fromLine + 1);
    }



    /** clears a line */
    public void clearLine(int line, int column, int orientation, int length) throws IOException {
        cursorMoveAbsolut(line, column);
        if (orientation == TerminalIOInterface.ORIENTATION_VERTIKAL) {
            for (int i = 0; i < length; i++) {
                writeText(" ", line+i, column);
            }
        }
        else {
            StringBuffer sb = new StringBuffer(length);
            for (int i = 0; i < length; i++) {
                sb.append(" ");
            }
            writeText(sb.toString());
        }
    }


    /** Makes an errortone */
    public void beepError() throws IOException {
    	beepError(1);
    }

    /** Makes an errortone */
    public void beepError(int number) throws IOException {
    	for(int i=0;i<number;i++)
    		writeText(BEEP_ERROR);
        flush();
    }



    /** closes the IO */
    public void close() throws IOException {
        try {
            this.flush();
        }
        catch(IOException ex) {
        }

        synchronized(lock) {
            this.closed = true;
            out.close();
        }
    }



    //--------------------------------------------------------------------------
    // overriding abstract methods of Writer
    //--------------------------------------------------------------------------

    public void write(int c) throws IOException {
        out.write(c);
    }



    public void write(char[] text) throws IOException {
        out.write(text);
    }



    public void write(char[] text, int offset, int length) throws IOException {
        out.write(text, offset, length);
    }


    /** writes content of the writer buffer */
    public void flush() throws IOException {
        synchronized (lock) {
            if (closed) {
                throw new IOException("VT100Stream closed");
            }
            out.flush();
        }
    }

}
