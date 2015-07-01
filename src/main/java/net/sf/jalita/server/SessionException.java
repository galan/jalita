/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * Author: Daniel Galán y Martins
 * Creation date: 03.05.2003
 * Revision: $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:59 $
 * $Log: SessionException.java,v $
 * Revision 1.2  2009/12/02 21:53:59  danielgalan
 * naming
 *
 * Revision 1.1 2004/07/26 21:40:28 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.server;

/**
 * Configuration, read from the propertyfile, and global Constants
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.2 $
 */
public class SessionException extends Exception {

	//--------------------------------------------------------------------------
	// constructors
	//--------------------------------------------------------------------------

	public SessionException(String message) {
		super(message);
	}

}
