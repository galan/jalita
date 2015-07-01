/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * Author: Daniel Galán y Martins
 * Creation date: 16.05.2003
 * Revision: $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:58 $
 * $Log: ListSelectionListener.java,v $
 * Revision 1.2  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.1 2004/07/26 21:40:27 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.ui.widgets;

/**
 * Listener for selection changes
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.2 $
 */
public interface ListSelectionListener {

	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	/** Called when selection in a list has changed */
	public void valueChanged(ListSelectionEvent e);

}
