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
 * $Log: ButtonWidget.java,v $
 * Revision 1.1  2004/07/26 21:40:27  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import java.io.IOException;
import net.sf.jalita.ui.forms.BasicForm;
import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.io.TerminalIOInterface;
import java.util.Vector;



/**
 * A Button which could recive the focus and its label is sourrounded by "[" and "]"
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.1 $
 */
public class ButtonWidget extends BasicWidget {

    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** Text on the Button */
    private String textLabel;

    /** Registered Listeners */
    private Vector listener = new Vector(1, 1);



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a new ButtonWidget-Object */
    public ButtonWidget(BasicForm owner, String text, int posLine, int posColumn, int width) {
        super(owner, true);

        setWidth(width);
        setPositionLine(posLine);
        setPositionColumn(posColumn);

        setText(text);
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** Draws the button */
    public void paint() throws IOException {
        if (isFocused()) {
            getIO().drawLine(getPositionLine(), getPositionColumn()+1, TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth()-2);
            getIO().writeInverseText("[", getPositionLine(), getPositionColumn());
            getIO().writeInverseText("]", getPositionLine(), getPositionColumn() + getWidth() - 1);
        }
        else {
            getIO().drawLine(getPositionLine(), getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
        }

        String textToDraw = getText();
        if (getText().length() > (getWidth() - 2)) {
            textToDraw = getText().substring(0, getWidth() - 2);
        }

        int startPos = (getWidth() - textToDraw.length()) / 2;
        getIO().writeInverseText(textToDraw, getPositionLine(), getPositionColumn() + startPos);
    }



    /** Sets the text on the button */
    public void setText(String text) {
        if (text.length() > (getWidth() - 2)) {
            textLabel = text.substring(0, getWidth() - 2);
        }
        else {
            textLabel = text;
        }

        setCursor(getPositionLine(), getPositionColumn());

        setDirty(true);
    }



    /** Returns the text on the button */
    public String getText() {
        return textLabel;
    }



    /** On reciving the focus, mark dirty to redraw (e.g. the cursor) */
    public void focusEntered() {
        setDirty(true);
    }



    /** On losing the focus, mark dirty to redraw (e.g. the cursor) */
    public void focusLeft() {
        setDirty(true);
    }



    /** Retuns the current label */
    public String toString() {
        return getText();
    }



    /** Informs all listeners */
    public void fireActionPerformed(TerminalEvent e) {
        for (int i = 0; i < listener.size(); i++) {
            ((ButtonListener)listener.elementAt(i)).actionPerformed(e);
        }
    }



    /** Registers a listener */
    public void addButtonListener(ButtonListener l) {
        listener.add(l);
    }



    /** Removes a listener */
    public void removeButtonListener(ButtonListener l) {
        listener.remove(l);
    }



    //--------------------------------------------------------------------------
    // override abstract methods of BasicWidget
    //--------------------------------------------------------------------------

    /** Process Barcode */
    public void processBarcodeReceived(TerminalEvent e) {
        // nothing to do here
    }



    /** Process Key Event */
    public void processKeyPressed(TerminalEvent e) {
        if (e.getKey() == TerminalEvent.KEY_ENTER) {
            fireActionPerformed(e);
        }
    }

}
