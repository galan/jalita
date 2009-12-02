/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * Author: Daniel Galán y Martins
 * Creation date: 15.05.2003
 * Revision: $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:58 $
 * $Log: LineWidget.java,v $
 * Revision 1.2  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.1 2004/07/26 21:40:27 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import java.io.IOException;

import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.io.TerminalIOInterface;
import net.sf.jalita.ui.forms.BasicForm;



/**
 * A simple line
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.2 $
 */
public class LineWidget extends BasicWidget {

	//--------------------------------------------------------------------------
	// instance variables
	//--------------------------------------------------------------------------

	/** Horizontal or vertical */
	private int orientation;


	//--------------------------------------------------------------------------
	// constructors
	//--------------------------------------------------------------------------

	/** Creates a new Line */
	public LineWidget(BasicForm owner, int posLine, int posColumn, int orientation, int length) {
		super(owner, false);

		setWidth(length);
		setPositionLine(posLine);
		setPositionColumn(posColumn);

		if ((orientation == TerminalIOInterface.ORIENTATION_HORIZONTAL) || (orientation == TerminalIOInterface.ORIENTATION_VERTIKAL)) {
			this.orientation = orientation;
		}
		else {
			this.orientation = TerminalIOInterface.ORIENTATION_HORIZONTAL;
		}
	}


	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	/** Draws the line on the Screen */
	@Override
	public void paint() throws IOException {
		getIO().drawLine(getPositionLine(), getPositionColumn(), orientation, getWidth());
	}


	/** Called when widget recives the focus */
	@Override
	public void focusEntered() {
		// do nothing
	}


	/** Called when widget lost its focus */
	@Override
	public void focusLeft() {
		// do nothing
	}


	//--------------------------------------------------------------------------
	// override abstract methods of BasicWidget
	//--------------------------------------------------------------------------

	/** Process barcodes */
	@Override
	public void processBarcodeReceived(TerminalEvent e) {
		// do nothing
	}


	/** Process Key events */
	@Override
	public void processKeyPressed(TerminalEvent e) {
		// do nothing
	}

}
