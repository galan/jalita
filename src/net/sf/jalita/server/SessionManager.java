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
 * Creation date: 27.04.2003
 *  
 * Revision:      $Revision: 1.4 $
 * Checked in by: $Author: ilgian $
 * Last modified: $Date: 2009/03/04 14:21:40 $
 * 
 * $Log: SessionManager.java,v $
 * Revision 1.4  2009/03/04 14:21:40  ilgian
 * Added sessions Enumeration method
 *
 * Revision 1.3  2009/02/23 13:42:57  ilgian
 * Session identified by source IP+port
 *
 * Revision 1.2  2005/05/23 18:10:20  danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 *
 * Revision 1.1  2004/07/26 21:40:28  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.server;



import java.util.Hashtable;
import java.util.Iterator;
import java.net.*;
import java.util.Vector;
import java.io.*;

import net.sf.jalita.io.TerminalIOInterface;
import net.sf.jalita.util.Configuration;

import java.util.Enumeration;
import java.util.Map.Entry;

import org.apache.log4j.Logger;



/**
 * Administrates the connected terminals
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.4 $
 */
public class SessionManager implements Runnable {

    //--------------------------------------------------------------------------
    // constants
    //--------------------------------------------------------------------------

    /** Lock-object for the cleanup */
    private static final Object cleanupLockObject = new Object();



    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    /** log4j reference */
    public final static Logger log = Logger.getLogger(Configuration.class);

    /** Jalita configuration-properties */
    private static Configuration config = Configuration.getConfiguration();

    /** Singleton-object for the SessionManagers */
    private static SessionManager sessionManager = null;

    /**
     * Locates the Session via IP.
     * Key = InetAdress-Object(IP-Adresse); Value = Session-Object
     * Consider that the Key may change on conversion to IPv6.
     */
    private static Hashtable sessions = new Hashtable();

    /** Contains TerminalIOInterface-Objects which are interrupted, but are not closed */
    private static Vector brokenSessions = new Vector();

    /** Contains Session-Objects wich are cloed, for e.g. by time-out */
    private static Vector closedSessions  = new Vector();

    /** Globale SessionObject which all Sessions can access */
    private static GlobalObject globalObject;



    //--------------------------------------------------------------------------
    // class methods
    //--------------------------------------------------------------------------

    /** Returns the Singleton */
    public static SessionManager getSessionManager() {
        if (sessionManager == null ) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }



    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** Tells us if the cleanup Thread is running in background */
    private boolean cleanupActive = true;



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a new SessionManager Object */
    private SessionManager() {
        log.debug("Creating instance of SessionManager");

        // Starts the cleanup thread in background, which manages broken and closed connections, ass well as time outs
        Thread cleanupThread = new Thread(this);
        cleanupThread.start();
    }



    //--------------------------------------------------------------------------
    // private & protected methods
    //--------------------------------------------------------------------------

    /** Validates connections from the Sessions and frees resources */
    private void cleanup() {
        synchronized (cleanupLockObject) {
            checkSessions();
            cleanupBrokenSession();
            cleanupClosedSession();
        }
    }



    /** Clearing Sessions which connections are broken */
    private void cleanupBrokenSession() {
        // to call ".size()" in the loop (processing FIFO)
        boolean brokenObjectsLeft = true;

        // cleanup loop
        while (brokenObjectsLeft) {
            synchronized(brokenSessions) {
                if (brokenSessions.size() > 0) {
                    // retrieve next connection
                    Object next = brokenSessions.firstElement();

                    if (next instanceof TerminalIOInterface) {
                        TerminalIOInterface nextIO = (TerminalIOInterface)next;
                        log.debug("Closing Socket from broken Session: " + nextIO);

                        closeTerminalIO(nextIO);
                    }

                    // remove from the brokenSession-List, if available
                    brokenSessions.removeElement(next);
                }
                else {
                    brokenObjectsLeft = false;
                }
            }
        }
    }



    /** Clearing Sessions which are closed normally */
    private void cleanupClosedSession() {
        // cleanup loop
        while (closedSessions.size() != 0) {
            // get a died one
            Session nextOne = (Session)closedSessions.firstElement();
            log.debug("Closing Socket from closed Session: " + nextOne);

            closeTerminalIO(nextOne.getIO());

            nextOne.finish();

            // remove it from the closedSession-List and list of the running Sessions
            closedSessions.removeElementAt(0);
        }
    }



    /** Validates the Sessions if they are broken (interrupted), closed or have a timeout. */
    private void checkSessions() {
        Enumeration enumSessions = sessions.elements();
        Enumeration enumKeys = sessions.keys();

        while (enumSessions.hasMoreElements()) {
            Session nextSession = (Session)enumSessions.nextElement();
            String nextKey = (String)enumKeys.nextElement();

            if (nextSession.isTimeout()) {
                log.debug("Session-Timeout for '" + nextSession + "'");
                sessions.remove(nextKey);

                if (nextSession.getIO() != null) {
                    brokenSessions.remove(nextSession.getIO());
                    registerClosedSession(nextSession);
                }
            }
        }
    }



    /** Closes IO to a terminal. */
    private void closeTerminalIO(TerminalIOInterface io) {
        try {
            io.close();
        }
        catch (IOException ex) {
            log.warn("Could not close TerminalIO: " + io);
        }
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** Adds a new Sesion. One IP is associated to one Session. */
    public void addSession(Socket socket) {
        Session newSession = null;
        String sessionID = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        
        if(config.getServerMaxSessionsPerHost() == 1){
        	sessionID = socket.getInetAddress().getHostAddress();
        }
        Session oldSession = (Session)sessions.get(sessionID);
        int sessionCount = 0;

        // New Session to not registered IP
        if (oldSession == null) {
        	for(Iterator<String> iter = sessions.keySet().iterator(); iter.hasNext(); ){
        		String tmpKey = iter.next();
        		if(tmpKey.startsWith(socket.getInetAddress().getHostAddress()+ ":")){
        			sessionCount++;
        		}
        	}
        	if(sessionCount >= config.getServerMaxSessionsPerHost()){
        		log.debug("IP '" + sessionID + "' exceeds maximum connections --> drop");
        		try {
					socket.close();
				} catch (IOException e) {
					//ignore
				}
				return;
        	} else {
	            log.debug("IP '" + sessionID + "' is not registered -> new Session");
	            newSession = new Session(socket);
	            sessions.put(sessionID, newSession);
        	}
        }

        // IP exists, but is closed -> close and create new Session
        else if (oldSession.isFinished()) {
            log.debug("IP '" + sessionID + "' exists, but is closed -> closing and new Session");
            registerClosedSession(oldSession);
            newSession = new Session(socket);
            sessions.put(sessionID, newSession);
        }

        // IP exists, will be reused
        else {
            log.debug("IP '" + sessionID + "' exists -> reusing");
            oldSession.bindSocket(socket);
            newSession = oldSession;
        }

        // Start the new Session (as Thread)
        newSession.startSession();
    }


    public Enumeration sessions() {
		return sessions.elements();
	}

    /**
     * Closes the broken Session immediately. In difference to registerBrokenSession
     * the IO of the Session will not be put in a Queue before closing, this happens here
     * immediatly.
     */
    public void closeBrokenSession(Session session) {
        synchronized(brokenSessions) {
            closeTerminalIO(session.getIO());

            // remove it from the brokenSession-List, if available
            brokenSessions.removeElement(session.getIO());
        }
    }



    /** Placed an interrupted Connection in the broken Session List */
    public void registerBrokenSession(TerminalIOInterface brokenIO) {
        if (!brokenSessions.contains(brokenIO)) {
            brokenSessions.addElement(brokenIO);
        }
    }


    /** Placed a closed Session in the closed Session List, and removes it from the running Session List */
    public void registerClosedSession(Session session) {
        sessions.remove(session.getIO().getInetAdress() + ":" + session.getIO().getPort());
        if (!closedSessions.contains(session)) {
            closedSessions.addElement(session);
        }
    }



    /** Shuts all Sessions down */
    public void shutdown() {
        // no automatic cleanup here anymore
        cleanupActive = false;

        // inform sessions (-> IO will be closed, as well as finish() in the SessionObject)
        Enumeration enumSessions = sessions.elements();
        while (enumSessions.hasMoreElements()) {
            Session selectedSession = (Session)enumSessions.nextElement();
            selectedSession.finish();
        }

        // finish global object
        if (globalObject != null) {
            globalObject.finish();
        }

        // execute cleanup last time
        cleanup();
    }



    /** Returns the global object to which all Session have access. */
    public GlobalObject getGlobalObject() {
        return SessionManager.globalObject;
    }



    /** Sets the global object. */
    public void setGlobalObject(GlobalObject globalObject) {
        SessionManager.globalObject = globalObject;
    }



    //--------------------------------------------------------------------------
    // implementation of the interface Runnable
    //--------------------------------------------------------------------------

    /** Cleans broken, closed as well as timed out Connections. */
    public void run() {
        while (cleanupActive) {
            try {
                cleanup();

                try {
                    Thread.sleep(config.getServerCleanupInterval());
                }
                catch (InterruptedException ex) {
                    log.error(ex);
                }
            }
            catch (Exception ex) {
                log.error(ex);
            }
        }
    }

}
