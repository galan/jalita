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
 * Creation date: 15.05.2003
 *  
 * Revision:      $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2005/05/23 18:10:19 $
 * 
 * $Log: WaitScreenForm.java,v $
 * Revision 1.2  2005/05/23 18:10:19  danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 *
 * Revision 1.1  2004/07/26 21:40:28  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.ui.forms;

import net.sf.jalita.ui.widgets.*;
import net.sf.jalita.ui.automation.FormAutomationSet;
import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.io.TerminalIOInterface;
import net.sf.jalita.ui.widgets.LineWidget;



/**
 * Waitscreen to let the user doze a while..
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.2 $
 */
public class WaitScreenForm extends BasicForm {

    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** Text with "Please wait.." */
    private LabelWidget labelText;
    /** Waittext */
    private LabelWidget labelMessage;

    /** Inverse Line */
    private LineWidget line1;
    /** Inverse Line */
    private LineWidget line2;
    /** Inverse Line */
    private LineWidget line3;



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a new WaitScreenForm-Object */
    public WaitScreenForm(FormAutomationSet owner, String message) {
        super(owner, "");
        setMessage(message);
        log.debug("Creating instance of WaitScreenForm");
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    public void initWidgets() {
        // insert Widgets
        /** @todo 20 durch getIO().getWidth() austauschen, derzeit */
        line1 = new LineWidget(this, 2, 1, TerminalIOInterface.ORIENTATION_HORIZONTAL, 20);
        line2 = new LineWidget(this, 3, 1, TerminalIOInterface.ORIENTATION_HORIZONTAL, 20);
        line3 = new LineWidget(this, 4, 1, TerminalIOInterface.ORIENTATION_HORIZONTAL, 20);

        labelText = new LabelWidget(this, "Please wait", 3, 5);
        labelText.setInverse(true);

        labelMessage = new LabelWidget(this, "...", 6, 1);


        // add widgets to Form
        addWidget(line1);
        addWidget(line2);
        addWidget(line3);
        addWidget(labelText);
        addWidget(labelMessage);
    }



    /** Sets waitmessage */
    public void setMessage(String message) {
        if (message != null) {
            labelMessage.setText(message);
        }
        else {
            labelMessage.setText("...");
        }
    }



    /** Sets the header */
    public void setHeader(String message) {
        if (message != null) {
            labelText.setText(message);
        }
        else {
            labelText.setText("...");
        }
    }



    //--------------------------------------------------------------------------
    // override abstract methods of BasicForm
    //--------------------------------------------------------------------------

    public void processBarcodeReceived(TerminalEvent e) {
        // do nothing!
    }



    public void processKeyPressed(TerminalEvent e) {
        // do nothing!
    }



    public void formLeft() {
        // do nothing
    }



    public void formEntered() {
        // do nothing
    }

}
