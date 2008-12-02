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
 * Creation date: 27.03.2003
 *  
 * Revision:      $Revision: 1.4 $
 * Checked in by: $Author: ilgian $
 * Last modified: $Date: 2008/12/02 13:17:01 $
 * 
 * $Log: Session.java,v $
 * Revision 1.4  2008/12/02 13:17:01  ilgian
 * Added constructor with Session argument
 *
 * Revision 1.3  2008/10/09 10:23:00  ilgian
 * Added support for session attributes
 *
 * Revision 1.2  2005/05/23 18:10:20  danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 *
 * Revision 1.1  2004/07/26 21:40:28  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import net.sf.jalita.io.*;
import net.sf.jalita.ui.FormManager;
import net.sf.jalita.util.Configuration;



/**
 * Represents a session to a terminal
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.4 $
 */

public class Session implements Runnable {

    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    /** log4j reference */
    public final static Logger log = Logger.getLogger(Configuration.class);

    /** Jalita configuration-properties */
    private static Configuration config = Configuration.getConfiguration();

    private Map attributes = new HashMap();

    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** Access to an terminal */
    private TerminalIOInterface io;

    /** Associated global SessionManager */
    private SessionManager sessionManager = SessionManager.getSessionManager();

    /** FormManager, which administrates an stack of FormAutomationSets and other elements */
    private FormManager formManager;

    /** true if session is closed normally */
    private boolean finished = false;

    /** list of TerminalEvents objects */
    private Vector eventListener = new Vector(1, 1);

    /** time index of last event happend. used to catch timeouts */
    private long lastActivity;

    /** the user defined object, associated with this session */
    private SessionObject sessionObject = null;



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a new Session object */
    public Session(Socket socket) {
        log.debug("Creating instance of Session");
        bindSocket(socket);
        formManager = new FormManager(this);
        addTerminalEventListener(formManager);
        lastActivity = System.currentTimeMillis();
    }

    public Session(Session session){
    	
    }

    //--------------------------------------------------------------------------
    // private & protected methods
    //--------------------------------------------------------------------------

    /** Inform listeners about recived barcode */
    private void fireBarcodeEvent(TerminalEvent event) {
        for (int i = 0; i < eventListener.size(); i++) {
            ((TerminalEventListener)eventListener.elementAt(i)).barcodeReceived(event);
        }
    }



    /** Inform listeners about recived key */
    private void fireKeyEvent(TerminalEvent event) {
        for (int i = 0; i < eventListener.size(); i++) {
            ((TerminalEventListener)eventListener.elementAt(i)).keyPressed(event);
        }
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /**
     * Binds a socket to this Session, create therewith a new TerminalIO and informs
     * the FormManager. Furthermore an existing but eventually not closed Session,
     * thru e.g. time out, will be closed (with the sameIP).
     */
    public void bindSocket(Socket socket) {
        lastActivity = System.currentTimeMillis();

        if (io != null) {
            sessionManager.closeBrokenSession(this);
        }

        try {
            /** TODO negotiate terminal type here later. maybe better in the Dispatcher transmitted thru via int consts? */
            io = new VT100TerminalIO(socket);

            // redraw
            try {
               // io.clearScreen();
            }
            catch (Exception ex) {
                // ignore..
            }

            // inform formManager
            if (formManager != null) {
                formManager.ioChanged();
            }
        }
        catch (IOException ex) {
            handleIOException(ex);
        }
    }



    /** Starts the Session as seperate Thread */
    public void startSession() {
        lastActivity = System.currentTimeMillis();

        Thread sessionThread = new Thread(this);
        sessionThread.start();
    }



    /** Returns the current IO as String, or as "broken IO" if Connection is interrupted */
    public String toString() {
        if (io != null) {
            return io.toString();
        }

        return "brocken IO";
    }



    /** Returns the current IO from the terminal */
    public TerminalIOInterface getIO() {
        return io;
    }



    /**
     * Shuts the Session down, informs the FormManager, which in turn informs the finite automatons.
     * At the end the session object will be finished
     */
    public void finish() {
        finished = true;

        try {
            io.close();
        }
        catch (IOException ex) {
        	// ignore, because lokalIO.readNextEvent() in run() throws an exception
        }

        formManager.finish();

        if (sessionObject != null) {
            sessionObject.finish();
        }
    }



    /** Tells if the Session is allready finished */
    public boolean isFinished() {
        return finished;
    }



    /** Calculates if there is an timeOut of this Session */
    public boolean isTimeout() {
        return lastActivity < System.currentTimeMillis() - config.getSessionTimeOut();
    }



    /** Registers a TerminalEventListener, which can respond to barcodes and keys */
    public void addTerminalEventListener(TerminalEventListener listener) {
        eventListener.add(listener);
    }



    /** Removes a TerminalEventListener */
    public void removeTerminalEventListener(TerminalEventListener listener) {
        eventListener.remove(listener);
    }



    /** Responds commonly to an IOException during an existing connection, to remove this */
    public void handleIOException(Exception ex) {
        sessionManager.registerBrokenSession(this.io);
        log.debug(ex);
    }



    /** Sets the sesionObject, which is only in this session visible */
    public void setSessionObject(SessionObject obj) {
        sessionObject = obj;
    }



    /** Returns the SessionObject */
    public SessionObject getSessionObject() {
        return sessionObject;
    }



    //--------------------------------------------------------------------------
    // implementation of the interface Runnable
    //--------------------------------------------------------------------------

    /** This runs the Session as parallel Thread */
    public void run() {
        TerminalIOInterface lokalIO = this.io;
        log.info("Connection created and SessionThread started on Node " + lokalIO);

        boolean broken = false;
        try {
            // endless loop, could only be interrupted by finish() or a TimeOut
            while(true) {
                lastActivity = System.currentTimeMillis();

                TerminalEvent event = lokalIO.readNextEvent();

                if (event == null) {
                    // could (should) never happen
                    throw new IOException("TerminalEvent was null!");
                }

                if (event.isBarcode()) {
                    fireBarcodeEvent(event);
                }
                else {
                    fireKeyEvent(event);
                }
            }
        }
        catch (IOException ex) {
        	broken = true;
        }
        
        if (!isTimeout() && !finished) {
        	if (broken) {
                log.error("Connection lost on Node " + lokalIO);
                sessionManager.registerBrokenSession(lokalIO);
        	}
        	else {
                log.info("SessionThread finished on Node " + lokalIO);
                sessionManager.registerClosedSession(this);
        	}
        }

        log.info("Session-Thread closed on Node " + lokalIO);
    }

    
    public void setAttribute(String name, Object value){
    	attributes.put(name.toLowerCase().trim(), value);
    }
    
    public Object getAttribute(String name){
    	return attributes.get(name.toLowerCase().trim());
    }
}
