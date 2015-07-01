/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * Author: Daniel Galán y Martins
 * Creation date: 02.05.2003
 * Revision: $Revision: 1.4 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:59 $
 * $Log: BasicTerminalIO.java,v $
 * Revision 1.4  2009/12/02 21:53:59  danielgalan
 * naming
 *
 * Revision 1.3 2009/02/23 13:42:06 ilgian
 * Added getPort() method
 * Revision 1.2 2005/05/23 18:10:19 danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 * Revision 1.1 2004/07/26 21:40:27 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;

import net.sf.jalita.util.Configuration;

import org.apache.log4j.Logger;



/**
 * Describes the basic functionality, which must be supported by each terminal
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.4 $
 */
public abstract class BasicTerminalIO implements TerminalIOInterface {

	//--------------------------------------------------------------------------
	// class variables
	//--------------------------------------------------------------------------

	/** log4j reference */
	public final static Logger log = Logger.getLogger(Configuration.class);

	//--------------------------------------------------------------------------
	// instance variables
	//--------------------------------------------------------------------------

	/** Session Socket */
	protected Socket socket;


	//--------------------------------------------------------------------------
	// private & protected methods
	//--------------------------------------------------------------------------

	/** Writer, which writes on the socket */
	protected abstract Writer getWriter();


	/** Reader, which reads from the socket */
	protected abstract Reader getReader();


	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	/**
	 * Returns the host address including port number in the notation:
	 * xxx.xxx.xxx.xxx:port
	 */
	@Override
	public String toString() {
		return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
	}


	/** Closes the writer, reader and the socket */
	public void close() throws IOException {
		log.debug("Closing TerminalIO for '" + toString() + "'");

		if (!socket.isClosed()) {
			getWriter().close();
			getReader().close();
			socket.close();
		}
	}


	/** Returns the internet-adress associated with the socket */
	public InetAddress getInetAdress() {
		return socket.getInetAddress();
	}


	/** Returns the port associated with the socket */
	public int getPort() {
		return socket.getPort();
	}

}
