/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * Author: Daniel Galán y Martins
 * Creation date: 25.04.2003
 * Revision: $Revision: 1.5 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:59 $
 * $Log: SocketDispatcherThread.java,v $
 * Revision 1.5  2009/12/02 21:53:59  danielgalan
 * naming
 *
 * Revision 1.4 2009/03/30 09:51:53 ilgian
 * Added Thread name (useful for remote debugging purposes)
 * Revision 1.3 2009/02/26 16:52:51 ilgian
 * Set thread name to socket address
 * Revision 1.2 2005/05/23 18:10:20 danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 * Revision 1.1 2004/07/26 21:40:28 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.server;

import java.net.Socket;

import net.sf.jalita.util.Configuration;

import org.apache.log4j.Logger;



/**
 * Resolves incoming connections
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.5 $
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
	private final SessionManager manager = SessionManager.getSessionManager();


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
		super(acceptedSocket.getInetAddress().getHostAddress() + ":" + acceptedSocket.getPort() + ".SocketDispatcher");
		log.debug("Creating instance of SocketDispatcherThread");
		socket = acceptedSocket;
	}


	//--------------------------------------------------------------------------
	// implementation of the interface Runnable
	//--------------------------------------------------------------------------

	@Override
	public void run() {
		log.debug("Connection received from " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
		/*
		 * Better by lastActivity
		 * try {
		 * socket.setSoTimeout(config.getSessionTimeOut());
		 * }
		 * catch (SocketException ex) {
		 * }
		 */

		// TODO Filter at this point IP-adresses?
		manager.addSession(socket);
		setName(socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

		log.debug("Dispatcher for " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " has finished");
	}

}
