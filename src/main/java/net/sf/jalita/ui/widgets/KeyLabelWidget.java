/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * Author: Daniel Galán y Martins
 * Creation date: 14.05.2003
 * Revision: $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:58 $
 * $Log: KeyLabelWidget.java,v $
 * Revision 1.2  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.1 2004/07/26 21:40:27 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import java.io.IOException;
import java.util.Vector;

import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.io.TerminalIOInterface;
import net.sf.jalita.ui.forms.BasicForm;



/**
 * Inverted button, Action for this button should be processed global in the
 * form
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.2 $
 */

public class KeyLabelWidget extends LabelWidget {

	//--------------------------------------------------------------------------
	// instance variables
	//--------------------------------------------------------------------------

	/** Key this button responds to and triggers the event */
	private int key = TerminalEvent.KEY_UNDEFINED;

	/** registered listeners */
	private final Vector listener = new Vector(1, 1);


	//--------------------------------------------------------------------------
	// constructors
	//--------------------------------------------------------------------------

	/** Creates a new KeyLabelWidget */
	public KeyLabelWidget(BasicForm owner, String text, int key, int posLine, int posColumn, int width) {
		super(owner, text, posLine, posColumn);

		setWidth(width);

		// set key, currently only functionkeys are considered
		if ((key == TerminalEvent.KEY_F01) || (key == TerminalEvent.KEY_F02) || (key == TerminalEvent.KEY_F03) || (key == TerminalEvent.KEY_F04) || (key == TerminalEvent.KEY_F05) || (key == TerminalEvent.KEY_F06) || (key == TerminalEvent.KEY_F07) || (key == TerminalEvent.KEY_F08) || (key == TerminalEvent.KEY_F09) || (key == TerminalEvent.KEY_F10) || (key == TerminalEvent.KEY_F11) || (key == TerminalEvent.KEY_F12) || (key == TerminalEvent.KEY_ENTER)) {

			this.key = key;
		}
	}


	/** Creates a new KeyLabelWidget */
	public KeyLabelWidget(BasicForm owner, String text, int key, int posLine, int posColumn, int width, int textAlignment) {
		this(owner, text, key, posLine, posColumn, width);

		setTextAlignment(textAlignment);
	}


	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	/** Draws the KeyLabel */
	@Override
	public void paint() throws IOException {
		getIO().drawLine(getPositionLine(), getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());

		StringBuffer textToDraw = new StringBuffer(getWidth());
		if (key != TerminalEvent.KEY_UNDEFINED) {
			switch (key) {
				case TerminalEvent.KEY_F01:
					textToDraw.append("F1");
					break;
				case TerminalEvent.KEY_F02:
					textToDraw.append("F2");
					break;
				case TerminalEvent.KEY_F03:
					textToDraw.append("F3");
					break;
				case TerminalEvent.KEY_F04:
					textToDraw.append("F4");
					break;
				case TerminalEvent.KEY_F05:
					textToDraw.append("F5");
					break;
				case TerminalEvent.KEY_F06:
					textToDraw.append("F6");
					break;
				case TerminalEvent.KEY_F07:
					textToDraw.append("F7");
					break;
				case TerminalEvent.KEY_F08:
					textToDraw.append("F8");
					break;
				case TerminalEvent.KEY_F09:
					textToDraw.append("F9");
					break;
				case TerminalEvent.KEY_F10:
					textToDraw.append("F10");
					break;
				case TerminalEvent.KEY_F11:
					textToDraw.append("F11");
					break;
				case TerminalEvent.KEY_F12:
					textToDraw.append("F12");
					break;
				case TerminalEvent.KEY_ENTER:
					textToDraw.append("ENTER");
					break;
			}

			textToDraw.append("=");
		}

		textToDraw.append(getText());

		if (textToDraw.length() > getWidth()) {
			textToDraw.setLength(getWidth());
		}

		//        int startPos = (getWidth() - textToDraw.length()) / 2;
		//        getIO().writeInverseText(textToDraw.toString(), getPositionLine(), getPositionColumn() + startPos);
		getIO().writeInverseText(format(textToDraw, getWidth()).toString(), getPositionLine(), getPositionColumn());
	}


	/** Sets the Text */
	@Override
	public void setText(String text) {
		int widthNow = getWidth();
		super.setText(text);
		setWidth(widthNow);
	}


	/** Returns the code of the key, which this button response to */
	public int getKey() {
		return key;
	}


	/** Informs the listeners (specific key was pressed) */
	public void fireActionPerformed(TerminalEvent e) {
		for (int i = 0; i < listener.size(); i++) {
			((ButtonListener)listener.elementAt(i)).actionPerformed(e);
		}
	}


	/** Registeres listener */
	public void addButtonListener(ButtonListener l) {
		listener.add(l);
	}


	/** Removes listener */
	public void removeButtonListener(ButtonListener l) {
		listener.remove(l);
	}

}
