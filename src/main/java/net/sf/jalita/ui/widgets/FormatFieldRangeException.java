/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * Author: Daniel Galán y Martins
 * Creation date: 29.07.2003
 * Revision: $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:58 $
 * $Log: FormatFieldRangeException.java,v $
 * Revision 1.2  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.1 2004/07/26 21:40:27 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.ui.widgets;

/**
 * Diese Exception wird geworfen wenn beim setzten der Zahl in einem
 * FormatFieldWidget
 * die L�nge des Feldes �berschritten wird.
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.2 $
 */

public class FormatFieldRangeException extends Exception {

	//--------------------------------------------------------------------------
	// instance variables
	//--------------------------------------------------------------------------

	/** value, which was tried to be set */
	private double value;


	//--------------------------------------------------------------------------
	// constructors
	//--------------------------------------------------------------------------

	public FormatFieldRangeException(double wrongValue) {
		super();
		value = wrongValue;
	}


	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	/** Returns the value, which was tried to set */
	public double getValue() {
		return value;
	}


	/** Sets the value, which was tried to set */
	public void setValue(double value) {
		this.value = value;
	}

}
