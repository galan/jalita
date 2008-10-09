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
 * Last modified: $Date: 2008/10/09 12:11:05 $
 * 
 * $Log: FormManager.java,v $
 * Revision 1.3  2008/10/09 12:11:05  ilgian
 * Added getSession method
 *
 * Revision 1.2  2005/05/23 18:10:20  danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 *
 * Revision 1.1  2004/07/26 21:40:29  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.ui;

import net.sf.jalita.io.TerminalEventListener;
import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.io.TerminalIOInterface;
import java.io.IOException;
import java.util.Stack;

import org.apache.log4j.Logger;

import net.sf.jalita.ui.automation.FormAutomationSet;
import net.sf.jalita.server.Session;
import net.sf.jalita.server.SessionObject;
import net.sf.jalita.server.SessionManager;
import net.sf.jalita.server.GlobalObject;
import net.sf.jalita.ui.forms.*;
import net.sf.jalita.util.Configuration;



/**
 * Manages all Forms associated with a Session, as well as a Stack with
 * FormAutomationSets.
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.3 $
 */
public class FormManager implements TerminalEventListener {

    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    /** log4j reference */
    public final static Logger log = Logger.getLogger(Configuration.class);

    /** Jalita configuration-properties */
    private static Configuration config = Configuration.getConfiguration();

    /** SessionManager */
    private SessionManager sessionManager = SessionManager.getSessionManager();



    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** Stack with FormAutomationSet objects */
    private Stack formAutomationStack = new Stack();

    /** Session from this FormManager */
    private Session owner;



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a FormManager object */
    public FormManager(Session owner) {
        log.debug("Creating instance of FormManager");
        this.owner = owner;

        try {
            formAutomationStack.push(config.getSessionInitFormAutomation().newInstance());
            getCurrentAutomationSet().setOwner(this);
        }
        catch (Exception ex) {
            log.error(ex);
        }

        try {
            ioChanged();
        }
        catch (IOException ex) {
            log.error(ex);
        }

    }



    //--------------------------------------------------------------------------
    // private & protected methods
    //--------------------------------------------------------------------------

    /** Redraws all marked widgets in the Form */
    protected void repaintCurrentForm() throws IOException {
        repaintCurrentForm(false);
    }



    /** Draws all marked Widgets im Form neu, if <code>fullRedraw</code> all Widgets in any case */
    protected void repaintCurrentForm(boolean fullRedraw) throws IOException {
        log.debug("Redraw " + (fullRedraw ? "all" : "dirty") + " widgets in current Form");
        getCurrentAutomationSet().getCurrentForm().paint(fullRedraw);
    }



    /** Returns the current running FormAutomationSet */
    protected FormAutomationSet getCurrentAutomationSet() {
        return (FormAutomationSet)formAutomationStack.peek();
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** If IO has changed, the current Form from the current FormAutomationSet will be redrawn */
    public void ioChanged() throws IOException {
        repaintCurrentForm(true);
    }



    /** Returns the TerminalIOInterface, which represents the connection to the terminal */
    public TerminalIOInterface getIO() {
        return owner.getIO();
    }



    /** Places a FormAutomationSet on the Stack, which will become the currentFormAutomationSet */
    public void attachFormAutomationSet(FormAutomationSet set) {
        set.setOwner(this);
        formAutomationStack.push(set);
    }



    /**
     * Removes the current FormAutomationSet from the Stack and passes the control to the following.
     * The initial Form will allways persist on the stack */
    public void detachFormAutomationSet() {
        // never get an empty Stackn -> java.util.EmptyStackException
        if (formAutomationStack.size() > 1) {
            formAutomationStack.pop();
            BasicForm bf = getCurrentAutomationSet().getCurrentForm();
            bf.formEntered();
            bf.markFormDirty();
        }
        else {
            log.warn("Internal Error (intercepted): Attempt to remove last FormAutomationSet");
        }
    }



    /** Returns the Session object */
    public SessionObject getSessionObject() {
        return owner.getSessionObject();
    }



    /** Sets the Session object */
    public void setSessionObject(SessionObject obj) {
        owner.setSessionObject(obj);
    }



    /** Returns the global object */
    public GlobalObject getGlobalObject() {
        return sessionManager.getGlobalObject();
    }



    /** Sets the global object */
    public void setGlobalObject(GlobalObject obj) {
        sessionManager.setGlobalObject(obj);
    }



    /** 
     * Will be called on Session-closing (not on broken Sessions), to
     * clean up FormAutomationSets.
     */
    public void finish() {
        while (formAutomationStack.size() > 0) {
            FormAutomationSet fas = (FormAutomationSet)formAutomationStack.pop();
            fas.finish();
        }
    }



    //--------------------------------------------------------------------------
    // implementation of the interface TerminalKeyListener
    //--------------------------------------------------------------------------

    /** Delegates the Barcode-Event to the current Form */
    public void barcodeReceived(TerminalEvent e) {
        log.debug("Barcode received in FormManager -> Barcode [" + e.getBarcode() + "]");

        getCurrentAutomationSet().getCurrentForm().barcodeReceived(e);

        try {
            repaintCurrentForm();
        }
        catch (IOException ex) {
            owner.handleIOException(ex);
        }
    }



    /** Delegates the Key-Event to the current Form */
    public void keyPressed(TerminalEvent e) {
        if (e.isPrintable()) {
            log.debug("Keypress received in FormManager -> Key (printable): [" + e.getKeyAsString() + "]");
        }
        else if (e.isFunctionKey()) {
            log.debug("Keypress received in FormManager -> FunktionKey: [" + java.lang.Math.abs(e.getKey()) + "]");
        }
        else {
            log.debug("Keypress received in FormManager -> Key (non-printable): [" + e.getKey() + "]");
        }

        getCurrentAutomationSet().getCurrentForm().keyPressed(e);

        try {
            repaintCurrentForm();
        }
        catch (IOException ex) {
            owner.handleIOException(ex);
        }

    }



	public Session getSession() {
		return owner;
	}

}
