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
 * $Log: SocketConnectionListener.java,v $
 * Revision 1.1  2004/07/26 21:40:28  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.server;


import java.io.*;
import java.net.*;
import org.apache.log4j.Logger;
import net.sf.jalita.application.Configuration;



/**
 * Listens to requests for Connections to delegate them in a separete Thread
 * to the SessionManager.
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.1 $
 */

public class SocketConnectionListener {

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

    /** ServerSocket which waits for incoming Connections */
    private ServerSocket server = null;



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a SocketConnectionListener object */
    public SocketConnectionListener() {
        log.debug("Creating instance of FunkterminalServer");
    }



    //--------------------------------------------------------------------------
    // private & protected methods
    //--------------------------------------------------------------------------

    /** Should Listener respond to new requests? */
    private boolean waitForConnections = true;



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /**
     * Starts actually the Server(Socket), which accepts Connections, to delegate them as
     * Socket to a separate Thread (to respond faster to new connection requests).
     */
    public void startListening() {
        try {
            server = new ServerSocket(config.getServerPort());
        }
        catch (IOException ex) {
            if( waitForConnections ) {
                log.error("Could not initiate ServerSocket, presumably Port " +
                          config.getServerPort() + " allready in use or insufficient rights!", ex);
            }
            return;
        }


        Socket socket = null;
        while (!server.isClosed() && waitForConnections) {
            try {
                log.debug("Waiting for new connections ..");
                socket = server.accept();
                log.debug("New connection recived ..");

                // during waiting (server.accept()) Jalita could be closed, this will throw usually an IOException
                if (waitForConnections) {
                    Thread t = new SocketDispatcherThread(socket);
                    t.start();
                }
            }
            catch (IOException ex) {
                if ((socket != null) && waitForConnections) {
                    log.error("Error accepting connetion from host: '" + socket.getInetAddress() + ":" + socket.getPort() + "'", ex);
                }
                if (!waitForConnections) {
                    break;
                }
            }

            // try to respawn ServerSocket
            if (server.isClosed() && waitForConnections) {
                try {
                    server = new ServerSocket(config.getServerPort());
                }
                catch(IOException ex) {
                    if( waitForConnections ) {
                        log.error(ex);
                    }
                }
            }
        }

        stopListening();
    }



    /** Closes the Server, no new connection requests could be served */
    public void stopListening() {
        waitForConnections = false;

        if ((server != null) && !server.isClosed()) {
            try {
                server.close();
                Thread.sleep(1000);
            }
            catch (Exception ex) {
                log.error("Error during closing Jalita", ex);
            }
        }
        server = null;
    }

}
