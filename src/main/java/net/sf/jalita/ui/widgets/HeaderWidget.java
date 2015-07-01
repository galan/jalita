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
 * $Log: HeaderWidget.java,v $
 * Revision 1.2  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.1 2004/07/26 21:40:27 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import java.io.IOException;

import net.sf.jalita.ui.forms.BasicForm;



/**
 * Headerline
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.2 $
 */
public class HeaderWidget extends LabelWidget {

	//--------------------------------------------------------------------------
	// constructors
	//--------------------------------------------------------------------------

	/** Creates a new HeaderWidget-Object */
	public HeaderWidget(BasicForm owner, String text) {
		super(owner, text, 1, 1);

		/** @todo 20 durch "getIO().getWidth()" ersetzten */
		setWidth(20);
	}


	/** Creates a new HeaderWidget-Object */
	public HeaderWidget(BasicForm owner, String text, int textAlignment) {
		this(owner, text);

		setTextAlignment(textAlignment);
	}


	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	/** Draws the header at the topin the display */
	@Override
	public void paint() throws IOException {
		//getIO().drawLine(getPositionLine(), getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
		getIO().setUnderlined(true);
		getIO().cursorMoveAbsolut(1, 1);

		/** @todo 20 durch "getIO().getWidth()" ersetzten */
		StringBuffer headerBuffer = new StringBuffer(20);
		for (int i = 0; i < 20; i++) {
			headerBuffer.append(" ");
		}
		getIO().writeText(headerBuffer.toString());

		getIO().writeText(getText(), getPositionLine(), getPositionColumn());
		getIO().setUnderlined(false);
		//getIO().writeInverseText(getText(), getPositionLine(), getPositionColumn()+1);
	}


	/** Sets the text in the header */
	@Override
	public void setText(String text) {
		super.setText(text);
		/** @todo 20 durch "getIO().getWidth()" ersetzten */
		setWidth(20);
	}

}
