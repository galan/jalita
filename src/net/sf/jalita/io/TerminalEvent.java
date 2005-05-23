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
 * Creation date: 04.05.2003
 *  
 * Revision:      $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2005/05/23 18:10:20 $
 * 
 * $Log: TerminalEvent.java,v $
 * Revision 1.2  2005/05/23 18:10:20  danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 *
 * Revision 1.1  2004/07/26 21:40:28  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.io;

import java.util.EventObject;
import org.apache.log4j.Logger;

import net.sf.jalita.util.Configuration;



/**
 * Action describing Event, that is triggered by an user interaction on the terminal.
 * Such an event could be a key is pressed or an barcode has been scanned.
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.2 $
 */
public class TerminalEvent extends EventObject {

    //--------------------------------------------------------------------------
    // constants
    //--------------------------------------------------------------------------

    /** represents key F1 */
    public final static int KEY_F01 =  -1;

    /** represents key F2 */
    public final static int KEY_F02 =  -2;

    /** represents key F3 */
    public final static int KEY_F03 =  -3;

    /** represents key F4 */
    public final static int KEY_F04 =  -4;

    /** represents key F5 */
    public final static int KEY_F05 =  -5;

    /** represents key F6 */
    public final static int KEY_F06 =  -6;

    /** represents key F7 */
    public final static int KEY_F07 =  -7;

    /** represents key F8 */
    public final static int KEY_F08 =  -8;

    /** represents key F9 */
    public final static int KEY_F09 =  -9;

    /** represents key F10 */
    public final static int KEY_F10 = -10;

    /** represents key F11 */
    public final static int KEY_F11 = -11;

    /** represents key F12 */
    public final static int KEY_F12 = -12;


    /** represents key Enter */
    public final static int KEY_ENTER = -100;

    /** represents key Tabulator */
    public final static int KEY_TAB = -101;

    /** represents key-komination Shift+Tabulator */
    public final static int KEY_TAB_BACK = -102;

    /** represents key Pause (usually only on pc-keyboard present) */
    public final static int KEY_PAUSE = -103;

    /** represents key Backspace */
    public final static int KEY_BACKSPACE = -104;


    /** represents key Cursor Up */
    public final static int KEY_UP = -200;

    /** represents key Cursor Down */
    public final static int KEY_DOWN = -201;

    /** represents key Cursor Right */
    public final static int KEY_RIGHT = -202;

    /** represents key Cursor Left */
    public final static int KEY_LEFT = -203;


    /** represents key Insert */
    public final static int KEY_PASTE = -300;

    /** represents key Delete */
    public final static int KEY_DEL = -301;

    /** represents key Home (Pos1) */
    public final static int KEY_HOME = -302;

    /** represents key End */
    public final static int KEY_END = -303;

    /** represents key Page Up */
    public final static int KEY_PAGE_UP = -304;

    /** represents key Page Down */
    public final static int KEY_PAGE_DOWN = -305;


    /** Representiert eine unbekannte und undefineirte Taste */
    public final static int KEY_UNDEFINED = -10000;



    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    /** log4j reference */
    public final static Logger log = Logger.getLogger(Configuration.class);



    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** flag indicates if event represents barcode */
    private boolean barcode = false;

    /** data of barcode (filled only if condition <code>barcode == true</code> */
    private String barcodeString;

    /** Code for a pressed key */
    private int key = KEY_UNDEFINED;

    /** indicates if this event allready is evaluated */
    private boolean consumed = false;



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates an TerminalEvent object as barcode-representation */
    public TerminalEvent(Object source, String barcode) {
        super(source);
        log.debug("Creating barcode-instance of TerminalEvent");
        this.barcode = true;
        barcodeString = barcode;
    }



    /** Creates an TerminalEvent object as key-representation */
    public TerminalEvent(Object source, int key) {
        super(source);
        log.debug("Creating key-instance of TerminalEvent");
        this.key = key;
    }



    /** Creates an TerminalEvent object as key-representation (mostly for printable characters) */
    public TerminalEvent(Object source, char key) {
        super(source);
        log.debug("Creating char-instance of TerminalEvent");
        this.key = key;
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** Tells us, if this event represents an barcode */
    public boolean isBarcode() {
        return barcode;
    }



    /** Returns the barcode (only if this event is a barcode, elsewise an empty String) */
    public String getBarcode() {
        if (barcodeString != null) {
            return barcodeString;
        }

        return "";
    }



    /** Returns the code of an pressed key (only of no barcode) */
    public int getKey() {
        return key;
    }



    /** Gibt bei druckbaren Zeichen dieses als String zurück */
    public String getKeyAsString() {
        if (!isPrintable()) {
            return "";
        }
        return String.valueOf((char)key);
    }



    /** Returns the key charater if it is printable, elsewise an space character */
    public char getKeyAsChar() {
        if (!isPrintable()) {
            return ' ';
        }
        return (char)key;
    }



    /** Tells us, if this character is printable */
    public boolean isPrintable() {
        return key > 0;
    }



    /** Tells us, if this event represents a functionkey (Fnn) */
    public boolean isFunctionKey() {
        return (key <= KEY_F01) && (key >= KEY_F12);
    }



    /** Returns if the event is allready evaluated */
    public boolean isConsumed() {
        return consumed;
    }



    /** Evaluated (Consumes) the event. */
    public void consume() {
        consumed = true;
    }

}
