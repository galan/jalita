/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * Author: Daniel Galán y Martins
 * Creation date: 13.05.2003
 * Revision: $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:58 $
 * $Log: SessionObject.java,v $
 * Revision 1.2  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.1 2004/07/26 21:40:28 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.server;

/**
 * A session object is accessible only for the associated session.
 * Yout could use this application specific in Forms, to keep data
 * for the respective user.
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.2 $
 */
public interface SessionObject {

	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	/**
	 * This method is called when the Session will be closed and
	 * should be implemted if some finalisation should be done.
	 * The TerminalIOInterface is allready closed at this moment.
	 */
	public void finish();

}
