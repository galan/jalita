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
 * Creation date: 25.04.2003
 *  
 * Revision:      $Revision: 1.1 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2004/07/26 21:40:28 $
 * 
 * $Log: SocketDispatcherThread.java,v $
 * Revision 1.1  2004/07/26 21:40:28  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.server;

import java.net.*;
import org.apache.log4j.Logger;
import net.sf.jalita.application.Configuration;



/**
 * Resolves incoming connections
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.1 $
 */

public class SocketDispatcherThread extends Thread {

    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    /** log4j reference */
    public final static Logger log = Logger.getLogger(Configuration.class);



    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** Socket which represents a connection, which will create a new Session */
    private Socket socket = null;

    /** The SessionManager who will recive new connections */
    private SessionManager manager = SessionManager.getSessionManager();




    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a SocketDispatcherThread object */
    private SocketDispatcherThread() {
        this(null);
        log.debug("Creating instance of SocketDispatcherThread");
    }



    /** Creates a SocketDispatcherThread object */
    public SocketDispatcherThread(Socket acceptedSocket) {
        super();
        log.debug("Creating instance of SocketDispatcherThread");
        socket = acceptedSocket;
    }



    //--------------------------------------------------------------------------
    // implementation of the interface Runnable
    //--------------------------------------------------------------------------

    public void run() {
        log.debug("Connection received from " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        /* Better by lastActivity
        try {
            socket.setSoTimeout(config.getSessionTimeOut());
        }
        catch (SocketException ex) {
        }
        */

        // TODO Filter at this point IP-adresses?
        manager.addSession(socket);

        log.debug("Dispatcher for " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " has finished");
    }

}
