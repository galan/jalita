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
 * Creation date: 20.05.2003
 *  
 * Revision:      $Revision: 1.1 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2004/07/26 21:40:28 $
 * 
 * $Log: ErrorForm.java,v $
 * Revision 1.1  2004/07/26 21:40:28  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.ui.forms;

import org.apache.log4j.Logger;
import net.sf.jalita.application.Configuration;
import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.io.TerminalIOInterface;
import net.sf.jalita.ui.automation.FormAutomationSet;
import net.sf.jalita.ui.widgets.LabelWidget;
import net.sf.jalita.ui.widgets.LineWidget;
import net.sf.jalita.ui.widgets.KeyLabelWidget;
import net.sf.jalita.ui.widgets.ButtonListener;



/**
 * Template for new error forms
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.1 $
 */
public class ErrorForm extends BasicForm {

    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    /** log4j reference */
    public final static Logger log = Logger.getLogger(Configuration.class);

    /** Jalita configuration-properties */
    private static Configuration config = Configuration.getConfiguration();



    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** Text with "E R R O R !" */
    private LabelWidget labelText;
    /** Errormessage */
    private LabelWidget labelMessage;

    /** Inverse Line */
    private LineWidget line1;
    /** Inverse Line */
    private LineWidget line2;
    /** Inverse Line */
    private LineWidget line3;

    /** Button to close Form */
    private KeyLabelWidget keySchliessen;

    /** Action triggerd on exitl */
    private int exitAction;
    /** State to be set on exit */
    private int exitState;



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a new ErrorForm-Object, which executes exitAction from the owner on exit */
    public ErrorForm(FormAutomationSet owner, String message, int exitAction) {
        this(owner, "     E R R O R !", message, exitAction);
    }



    /** Creates a new ErrorForm-Object, which executes exitAction from the owner on exit */
    public ErrorForm(FormAutomationSet owner, String headline, String message, int exitAction) {
        super(owner, "");
        setMessage(message);
        setExitAction(exitAction);
        labelText = new LabelWidget(this, headline, 3, 1);
        labelText.setInverse(true);
        addWidget(labelText);
        addWidget(new LabelWidget(this, "", 17, 2));
        log.debug("Creating instance of ErrorForm");
    }



    /** Creates a new ErrorForm-Object, which switches to exitState from the owner on exit */
    public ErrorForm(String headline, String message, FormAutomationSet owner, int exitState) {
        super(owner, "");
        setMessage(message);
        setExitState(exitState);
        labelText = new LabelWidget(this, headline, 3, 1);
        labelText.setInverse(true);
        addWidget(labelText);
        addWidget(new LabelWidget(this, "", 17, 2));
        log.debug("Creating instance of ErrorForm");
    }



    /** Creates a new ErrorForm-Object, which switches to exitState from the owner on exit */
    public ErrorForm(String message, FormAutomationSet owner, int exitState) {
        this("     E R R O R !", message, owner, exitState);
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    public void initWidgets() {
        // insertt widgets
        /** @todo 20 durch getIO().getWidth() austauschen, derzeit */
        line1 = new LineWidget(this, 2, 1, TerminalIOInterface.ORIENTATION_HORIZONTAL, 20);
        line2 = new LineWidget(this, 3, 1, TerminalIOInterface.ORIENTATION_HORIZONTAL, 20);
        line3 = new LineWidget(this, 4, 1, TerminalIOInterface.ORIENTATION_HORIZONTAL, 20);

        keySchliessen = new KeyLabelWidget(this, "Close", TerminalEvent.KEY_ENTER, 17, 2, 18);

        labelMessage = new LabelWidget(this, "...", 6, 1);


        // eventhandler
        keySchliessen.addButtonListener(new ButtonListener() {
            public void actionPerformed(TerminalEvent e) {
                keySchliessenActionPerfomed(e);
            }
        });


        // add widgets to Form
        addWidget(line1);
        addWidget(line2);
        addWidget(line3);
        addWidget(labelMessage);
        addWidget(keySchliessen);
    }



    /** Triggeres Event on closing the ErrorForm */
    public void keySchliessenActionPerfomed(TerminalEvent e) {
        owner.doAction(exitAction);
        owner.exitErrorState(exitState);
    }



    /** Sets the Errormessage */
    public void setMessage(String message) {
        if (message != null) {
            labelMessage.setText(message);
        }
        else {
            labelMessage.setText("...");
        }
    }



    /** Sets the Header */
    public void setHeader(String message) {
        if (message != null) {
            labelText.setText(message);
        }
        else {
            labelText.setText("...");
        }
    }



    /** Returns the Action, which will be executed when the ErrorForm will be closed */
    public int getExitAction() {
        return exitAction;
    }



    /** Sets the Action, which will be executed when the ErrorForm will be closed */
    public void setExitAction(int exitAction) {
        this.exitAction = exitAction;
    }



    /** Returns the State, which will be set when the ErrorForm will be closed */
    public int getExitState() {
        return exitState;
    }



    /** Sets the State, which will be set when the ErrorForm will be closed */
    public void setExitState(int exitState) {
        this.exitState = exitState;
    }



    //--------------------------------------------------------------------------
    // override abstract methods of BasicForm
    //--------------------------------------------------------------------------

    /** Using a Scanner, the ErrorForm should also be closed when a Scan is done */
    public void processBarcodeReceived(TerminalEvent e) {
        keySchliessenActionPerfomed(e);
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
