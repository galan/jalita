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
 * Revision:      $Revision: 1.5 $
 * Checked in by: $Author: ilgian $
 * Last modified: $Date: 2008/12/02 13:12:23 $
 * 
 * $Log: FormAutomationSet.java,v $
 * Revision 1.5  2008/12/02 13:12:23  ilgian
 * Fixed bug with wait screen
 * Added extra CRLF to display wait message
 *
 * Revision 1.4  2008/10/09 15:20:51  ilgian
 * Added getSession method
 *
 * Revision 1.3  2005/05/23 18:10:20  danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 *
 * Revision 1.2  2004/12/05 17:53:50  danielgalan
 * Refactored this damn beepError thing
 *
 * Revision 1.1  2004/07/26 21:40:29  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.ui.automation;

import java.io.IOException;
import java.util.Hashtable;
import org.apache.log4j.Logger;

import net.sf.jalita.server.Session;
import net.sf.jalita.ui.forms.BasicForm;
import net.sf.jalita.io.TerminalIOInterface;
import net.sf.jalita.ui.FormManager;
import net.sf.jalita.ui.forms.WaitScreenForm;
import net.sf.jalita.ui.forms.ErrorForm;
import net.sf.jalita.ui.forms.OptionForm;
import net.sf.jalita.util.Configuration;



/**
 * This abstract class represents the parent for all classes, that define flows for forms.   
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.5 $
 */
public abstract class FormAutomationSet {

    //--------------------------------------------------------------------------
    // constants
    //--------------------------------------------------------------------------

    /** This state lets the FormManager remove the current FormAutomationSet from the stack */
    public final static int STATE_FINISHED = -2;

    /** This state shows the current WaitScreenForm */
    public final static int STATE_WAIT = -3;

    /** This state shows the current ErrorForm */
    public final static int STATE_ERROR = -4;

    /** This state shows the current dialog */
    public final static int STATE_DIALOG = -5;


    /** Carriage Return - LineFeed */
    protected final static String CRLF = "\r\n";



    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    /** log4j reference */
    public final static Logger log = Logger.getLogger(Configuration.class);



    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** This is the initial state, and should be set in initAutomationSet() with setInitState(int state) */
    public int STATE_INIT = -1;

    /** Initial automaton state */
    private int state = STATE_INIT;

    /** Maps the states to forms. Key:(int)State / Value:(Form)Form */
    protected Hashtable stateToForm = new Hashtable();

    /** automaton owner */
    protected FormManager owner;

    /** Current Wait-Form, which should be shown */
    private WaitScreenForm wait;

    /** Form (State) which was shown before the WaitScreen */
    private int stateBeforeWaitScreenForm;

    /** Tells us if the Wait-Forms is visible */
    private boolean waitScreenActive = false;




    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a FormAutomationSet object */
    public FormAutomationSet() {
        log.debug("Creating instance of FormAutomationSet");

        /** TODO set object on sessionlevel later; now on FormAutomationSet-Level */
        wait = new WaitScreenForm(this, "");
        addForm(STATE_WAIT, wait);

        initAutomationSet();
    }



    //--------------------------------------------------------------------------
    // private & protected methods
    //--------------------------------------------------------------------------

    /**
     * Initialise the following in your implementatopm of the Automaton:
     * <ul>
     *     <li> 1. Create the Form-objects
     *     <li> 2.1. Register the created Form-Objects thru addForm(..)
     *     <li> 2.2. Maybe register SubAutomatons thru AddFormAutomation(..)
     *     <li> 3. Set the initial state
     * </ul>
     */
    protected abstract void initAutomationSet();



    /**
     * Modifies the automaton state, and sets the registered Form with this state
     * to the current.
     */
    protected final void setState(int state) {
        log.debug("call form left on state " + this.state);
        getCurrentForm().formLeft();

        if (state == STATE_FINISHED) {
            owner.detachFormAutomationSet();
            return;
        }

        Object obj = stateToForm.get(new Integer(state));

        // Different BasicForm
        if (obj instanceof BasicForm) {
            this.state = state;
            
            log.debug("call form entered on state " + state);
            if (waitScreenActive) waitScreenActive = false;
            BasicForm bf = (BasicForm)stateToForm.get(new Integer(state));
            bf.formEntered();
            bf.markFormDirty();
        }

        // push FormAutomationSet
        else if (obj instanceof Class) {
            try {
                // reset saved Form
                if (waitScreenActive) {
                    this.state = stateBeforeWaitScreenForm;
                    waitScreenActive = false;
                }
                Class classAutomation = (Class)obj;
                FormAutomationSet set = (FormAutomationSet)classAutomation.newInstance();
                owner.attachFormAutomationSet(set);
                set.getCurrentForm().formEntered();
                set.getCurrentForm().markFormDirty();
            }
            catch (Exception ex) {
                /** TODO default formautomationset... */
                log.error(ex);
            }
        }

    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** 
     * This is the entry point for the controller, you have to implement a
     * dispatcher, which evaluates the action and delegates to the corresponding
     * business object. Later you have to response in a way like setState(..),
     * showErrorForm(..), etc.. See examples.
     */
    public abstract void doAction(int action);



    /** Override this Method on potential clean up */
    public abstract void finish();



    /** Returns the current Form from the automaton */
    public BasicForm getCurrentForm() {
        Object obj = stateToForm.get(new Integer(state));
        if ((obj != null) && (obj instanceof BasicForm)) {
            return (BasicForm)obj;
        }

        /** TODO Standarderror Form */
        return null;
    }



    /** Sets the initial state */
    public void setInitState(int state) {
        this.state = state;
        STATE_INIT = state;
    }



    /** Returns the current State */
    public int getState() {
        return state;
    }



    /** Override to return the name of the current FormAutomationSet */
    public String toString() {
    	/** TODO via reflection*/
        return "FormAutomationSet";
    }



    /** Sets the FormManager, to which this automaton is registered */
    public void setOwner(FormManager owner) {
        this.owner = owner;
    }



    /** Returns the FormManager, where this automaton is registered */
    public FormManager getOwner() {
        return owner;
    }



    /** Returns the terminal's IO-Interface */
    public TerminalIOInterface getIO() {
        return owner.getIO();
    }



    /** Registers a Form to this automaton */
    public void addForm(int state, BasicForm form) {
        Integer intState = new Integer(state);

        if (stateToForm.contains(intState)) {
            stateToForm.remove(intState);
        }

        stateToForm.put(intState, form);
    }



    /** Registers a FormAutomationSet to this automaton */
    public void addFormAutomation(int state, Class automation) {
        Integer intState = new Integer(state);

        if (stateToForm.contains(intState)) {
            stateToForm.remove(intState);
        }

        stateToForm.put(intState, automation);
    }



    /** Shows the annoying wait screen with a tranquilising message */
    public void showWaitScreen(String text) {
        try {
            // Save Form (state) before showing Warte-Form
            if (!(getCurrentForm() instanceof WaitScreenForm)) {
                stateBeforeWaitScreenForm = getState();
                waitScreenActive = true;
            }
            wait.setMessage(text);
            setState(STATE_WAIT);

            getCurrentForm().paint(true);
        }
        catch (Exception ex) {
            // ignore, will be catched later, action should run till the end
            // if action is still running, the WaitScreenForm will be still shown
        }
    }



    /** Convenice method, which shows a ErrorForm with a bothering message */
    public void showErrorForm(String message, FormAutomationSet owner, int exitState) {
        showErrorForm(message, owner, exitState, null);
    }



    /** Convenice method, which shows a ErrorForm and makes logger output */
    public void showErrorForm(String message, FormAutomationSet owner, int exitState, Exception ex) {
        ErrorForm errorForm = new ErrorForm(message, owner, exitState);
        addForm(STATE_ERROR, errorForm);
        setState(STATE_ERROR);
        if (ex != null) {
            log.error(ex);
        }
        try {
            getIO().beepError();
        }
        catch (IOException ioex) {
            log.error("Beep could not be send", ioex);
        }
    }



    /** Convenice method, which shows a simple message ans makes logger output */
    public void showMessageForm(String headline, String message, FormAutomationSet owner, int exitState) {
        ErrorForm errorForm = new ErrorForm(headline, message, owner, exitState);
        addForm(STATE_ERROR, errorForm);
        setState(STATE_ERROR);
    }



    /** Exits an ErrorForm, used internally */
    public void exitErrorState(int exitState) {
    	/** TODO scope? */
        if (getState() == STATE_ERROR) {
            setState(exitState);
        }
    }



    /** Shows a DialogForm */
    public void showOptionForm(FormAutomationSet owner, String message,
                               int okLabel, int abortLabel, int okAction, int abortAction) {
        OptionForm dialogForm = new OptionForm(owner, message, okLabel, abortLabel, okAction, abortAction);
        addForm(STATE_DIALOG, dialogForm);
        setState(STATE_DIALOG);
    }
    
    /** Returns the Session */
    public Session getSession(){
    	return getOwner().getSession();
    }

}
