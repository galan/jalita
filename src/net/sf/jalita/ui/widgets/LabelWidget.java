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
 * Creation date: 06.05.2003
 *  
 * Revision:      $Revision: 1.1 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2004/07/26 21:40:27 $
 * 
 * $Log: LabelWidget.java,v $
 * Revision 1.1  2004/07/26 21:40:27  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import java.io.IOException;
import net.sf.jalita.ui.forms.BasicForm;
import net.sf.jalita.io.TerminalEvent;



/**
 * A simple label
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.1 $
 */
public class LabelWidget extends BasicWidget {

    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** Text on the widget */
    private String textLabel = "";

    /** Saves the last length of the label, to remove charatars that don't fit after setText() */
    private int lastLength = 0;



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a new LabelWidget */
    public LabelWidget(BasicForm owner, String text, int posLine, int posColumn) {
        super(owner, false);

        setPositionLine(posLine);
        setPositionColumn(posColumn);

        setText(text);
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** Draws the widget */
    public void paint() throws IOException {
        if (inverse) {
            getIO().setReverse(true);
        }

        if (underlined) {
            getIO().setUnderlined(true);
        }

        String txt = getText();
        getIO().writeText(txt, getPositionLine(), getPositionColumn());

        if (lastLength > txt.length()) {
            StringBuffer clearText = new StringBuffer();
            for (int i = 0; i < (lastLength - txt.length()); i++) {
                clearText.append(" ");
            }
            getIO().writeText(clearText.toString());
        }

        if (underlined) {
            getIO().setUnderlined(false);
        }

        if (inverse) {
            getIO().setReverse(false);
        }
    }



    /** Sets the text */
    public void setText(String text) {
        lastLength = textLabel.length();

        setWidth(text.length());

        textLabel = text;
        setCursor(getPositionLine(), getPositionColumn());

        setDirty(true);
    }



    /** Returns the text */
    public String getText() {
        return textLabel;
    }



    /** Called when widget recives the focus */
    public void focusEntered() {
        // do nothing
    }



    /** Called when widget lost its focus */
    public void focusLeft() {
        // do nothing
    }



    //--------------------------------------------------------------------------
    // override abstract methods of BasicWidget
    //--------------------------------------------------------------------------

    /** Processes barcodes */
    public void processBarcodeReceived(TerminalEvent e) {
        // nothing to do here
    }



    /** Processes key events */
    public void processKeyPressed(TerminalEvent e) {
        // nothing to do here
    }

}
