/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * Author: Daniel Galán y Martins
 * Creation date: 06.05.2003
 * Revision: $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:58 $
 * $Log: WidgetPosition.java,v $
 * Revision 1.2  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.1 2004/07/26 21:40:27 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.ui.widgets;

/**
 * Describes the position of a widget
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.2 $
 */
public class WidgetPosition {

	//--------------------------------------------------------------------------
	// instance variables
	//--------------------------------------------------------------------------

	/** Row */
	private int line = 1;

	/** Column */
	private int column = 1;


	//--------------------------------------------------------------------------
	// constructors
	//--------------------------------------------------------------------------

	/** Creates a WidgetPosition object */
	public WidgetPosition() {
	}


	/** Creates a WidgetPosition object */
	public WidgetPosition(int line, int column) {
		this.line = line;
		this.column = column;
	}


	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	/** Returns the row (line) */
	public int getLine() {
		return line;
	}


	/** Sets the row (line) */
	public void setLine(int line) {
		this.line = line;
	}


	/** Returns the column */
	public int getColumn() {
		return column;
	}


	/** Sets the column */
	public void setColumn(int column) {
		this.column = column;
	}

}
