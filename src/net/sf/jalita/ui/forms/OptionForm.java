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
 * Creation date: 02.07.2003
 *  
 * Revision:      $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2005/05/23 18:10:19 $
 * 
 * $Log: OptionForm.java,v $
 * Revision 1.2  2005/05/23 18:10:19  danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 *
 * Revision 1.1  2004/07/26 21:40:28  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.ui.forms;

import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.io.TerminalIOInterface;
import net.sf.jalita.ui.automation.FormAutomationSet;
import net.sf.jalita.ui.widgets.LabelWidget;
import net.sf.jalita.ui.widgets.LineWidget;
import net.sf.jalita.ui.widgets.KeyLabelWidget;
import net.sf.jalita.ui.widgets.ButtonListener;



/**
 * Dialoge mit zwei Benutzer-Optionen
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.2 $
 */
public class OptionForm extends BasicForm {

    //--------------------------------------------------------------------------
    // constants
    //--------------------------------------------------------------------------

    public static final int OK_OK = 1;
    public static final int OK_CONTINUE = 2;

    public static final int ABORT_ABORT = 3;
    public static final int ABORT_BACK = 4;



    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** Text with " o p t i o n " */
    private LabelWidget labelText;
    /** Message */
    private LabelWidget labelMessage;

    /** Inverse Line */
    private LineWidget line1;
    /** Inverse Line */
    private LineWidget line2;
    /** Inverse Line */
    private LineWidget line3;

    /** Button zum confirm */
    private KeyLabelWidget keyOk;
    /** Button to abort/return */
    private KeyLabelWidget keyAbort;

    /** Action on pressing the confirm-button */
    private int okAction;
    /** Action on presson the abort-button */
    private int abortAction;

    /** Caption for confirm */
    protected int okLabel;
    /** Caption for abort */
    protected int abortLabel;


    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a new DialogForm-Object */
    public OptionForm(FormAutomationSet owner, String message, int okLabel, int abortLabel, int okAction, int abortAction) {
        super(owner, "");
        setMessage(message);
        setOkAction(okAction);
        setAbortAction(abortAction);
        setOkLabel(okLabel);
        setAbortLabel(abortLabel);
        log.debug("Creating instance of DialogForm");
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

        labelText = new LabelWidget(this, " O p t i o n !", 3, 3);
        labelText.setInverse(true);
        keyOk = new KeyLabelWidget(this, "Ok..", TerminalEvent.KEY_ENTER, 15, 2, 18);
        keyAbort = new KeyLabelWidget(this, "Abort..", TerminalEvent.KEY_F03, 17, 2, 18);
        labelMessage = new LabelWidget(this, "...", 6, 1);


        // eventhandler
        keyOk.addButtonListener(new ButtonListener() {
            public void actionPerformed(TerminalEvent e) {
                keyOkActionPerfomed(e);
            }
        });

        keyAbort.addButtonListener(new ButtonListener() {
            public void actionPerformed(TerminalEvent e) {
                keyAbortActionPerfomed(e);
            }
        });


        // add widgets to Form
        addWidget(line1);
        addWidget(line2);
        addWidget(line3);
        addWidget(labelText);
        addWidget(labelMessage);
        addWidget(keyOk);
        addWidget(keyAbort);
    }



    /** Action triggered when pressed Ok */
    public void keyOkActionPerfomed(TerminalEvent e) {
        owner.doAction(getOkAction());
    }



    /** Action triggered when pressed abort */
    public void keyAbortActionPerfomed(TerminalEvent e) {
        owner.doAction(getAbortAction());
    }



    /** Sets the message */
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



    /** Returns the Action, which will be executed when pressed Ok */
    public int getOkAction() {
        return okAction;
    }



    /** Setsthe Action, which will be executed when pressed Ok */
    public void setOkAction(int okAction) {
        this.okAction = okAction;
    }



    /** Returns the Action, which will be executed when pressed Abort */
    public int getAbortAction() {
        return abortAction;
    }



    /** Setss the Action, which will be executed when pressed Abort */
    public void setAbortAction(int abortAction) {
        this.abortAction = abortAction;
    }



    /** Returns the value of the Ok Label */
    public int getOkLabel() {
        return okLabel;
    }



    /** Sets the value of the Ok Button */
    public void setOkLabel(int okLabel) {
        switch (okLabel) {
            case OK_OK:
                keyOk.setText("OK");
                this.okLabel = okLabel;
                break;
            case OK_CONTINUE:
                keyOk.setText("Cont.");
                this.okLabel = okLabel;
                break;
            default:
                keyOk.setText("OK");
                this.okLabel = OK_OK;
                break;
        }
    }



    /** Returns the Value of the Abort button */
    public int getAbortLabel() {
        return abortAction;
    }



    /** Sets the Value of the Abort button */
    public void setAbortLabel(int abortLabel) {
        switch (okLabel) {
            case ABORT_ABORT:
                keyAbort.setText("Abort");
                this.abortLabel = abortLabel;
                break;
            case ABORT_BACK:
                keyAbort.setText("Back");
                this.abortLabel = abortLabel;
                break;
            default:
                keyAbort.setText("Abort");
                this.abortLabel = ABORT_ABORT;
                break;
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
