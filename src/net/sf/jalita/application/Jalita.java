/***********************************************************************
 * 
 * This software is published under the terms of the LGPL
 * License version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * 
 *********************************************************************** 
 *
 * Author:   	  Daniel "tentacle" Galán y Martins
 * Creation date: 01.07.2003
 *  
 * Revision:      $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2004/08/06 00:55:34 $
 * 
 * $Log: Jalita.java,v $
 * Revision 1.2  2004/08/06 00:55:34  danielgalan
 * prepare release
 *
 * Revision 1.1  2004/07/26 21:40:28  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.application;

import java.net.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import net.sf.jalita.server.SocketConnectionListener;
import net.sf.jalita.server.SessionManager;



/**
 * Jalita main class - this is where all starts
 *
 * @author Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.2 $
 */
public class Jalita {

    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    /** log4j reference */
    public final static Logger log = Logger.getLogger(Configuration.class);

    /** Singleton-Object of Jalita */
    private static Jalita jalita = null;



    //--------------------------------------------------------------------------
    // class methods
    //--------------------------------------------------------------------------

    /** Returns the singleton */
    public static Jalita getJalita() {
        if (jalita == null ) {
            jalita = new Jalita();
        }
        return jalita;
    }


    /** This method is called if Jalita runs as Windows Service and should be stopped */
    public static void stopApplication(boolean doExit) {
        /** TODO stopListening before shutdown? */

    	// inform SessionManager to close sessions and save the data of them
        SessionManager sessionManger = SessionManager.getSessionManager();
        sessionManger.shutdown();

        // close SocketConnectionListener -> no new Connections are accepted
        Jalita jalita = getJalita();
        jalita.cl.stopListening();

        // terminate application
        log.debug("Jalita will be closed.");
        if (doExit) {
            jalita.terminateJalita();
        }
    }


    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** Socket-Listener, which is waiting for incoming connections */
    private SocketConnectionListener cl;

    /** Listener thread */
    private Thread listenerThread;

    
    
    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates Jalita, only by the singleton accesor */
    private Jalita() {
        cl = new SocketConnectionListener();

        // this would actually start the server, which waits for incoming connections, until
        // stopListener() is called (when running as windows service)
        listenerThread = new Thread(new Runnable() {
            public void run() {
                cl.startListening();
                log.debug("Socket Listener stopped successfully.");
            }
        });
        listenerThread.start();
    }


    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** Terminates the application */
    public void terminateJalita() {
        System.exit(0);
    }

    

    //--------------------------------------------------------------------------
    // main method
    //--------------------------------------------------------------------------

    /** This starts Jalita */
    public static void main(String[] args) {
    	// init logging (log4j)
    	try {
    		BasicConfigurator.resetConfiguration();
    		PropertyConfigurator.configure("log4j.properties");
    		log.debug("Log4j initialised");
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    		System.exit(-1);
    	}

        // Load programm settings
        Configuration config = Configuration.getConfiguration();

        try {
            config.checkConfiguration();

            try {
            	log.info("Starting " + config.getApplicationName() +
                         " (Version " + config.getAppliationVersion() + ")" +
                         " - Host: '" + InetAddress.getLocalHost().getHostAddress() +
                         ":" + config.getServerPort() + "");
                log.debug("Java-Version: " +
                          System.getProperty("java.runtime.name") + " " +
                          System.getProperty("java.vm.vendor") + " - " +
                          System.getProperty("java.vm.version")+"");

                // Start application (should create singleton)
                getJalita();
            }
            catch (UnknownHostException ex) {
                log.error(ex);
            }
            catch (Exception ex) {
                log.error(ex);
            }
        }
        catch (ConfigurationException ex) {
            log.error("Configuration is incorrect!", ex);
        }
    }

}
