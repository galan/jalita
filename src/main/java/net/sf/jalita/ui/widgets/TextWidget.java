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
 * $Log: TextWidget.java,v $
 * Revision 1.2  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.1 2004/07/26 21:40:27 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.ui.forms.BasicForm;



/**
 * Abstract class for Widgets that contain textmanupulation operations
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.2 $
 */
public abstract class TextWidget extends BasicWidget {

	//--------------------------------------------------------------------------
	// instance variables
	//--------------------------------------------------------------------------

	/** The textbuffer */
	private final StringBuffer buffer = new StringBuffer();


	//--------------------------------------------------------------------------
	// constructors
	//--------------------------------------------------------------------------

	/** Creates a new TextWidget-Object */
	public TextWidget(BasicForm owner, String text, boolean focusable, int posLine, int posColumn) {
		this(owner, text, focusable, posLine, posColumn, text.length());
	}


	/** Creates a new TextWidget-Object */
	public TextWidget(BasicForm owner, String text, boolean focusable, int posLine, int posColumn, int width) {
		super(owner, focusable);
		setWidth(width);
		setPositionLine(posLine);
		setPositionColumn(posColumn);

		setText(text);
	}


	//--------------------------------------------------------------------------
	// private & protected methods
	//--------------------------------------------------------------------------

	/** Fits the text to the width of the widget */
	protected void cutBufferToFit() {
		if (buffer.length() > getWidth()) {
			buffer.setLength(getWidth());
		}
	}


	/** Returns the cursorposition of the text */
	protected int getTextCursorPosition() {
		return getCursorColumn() - getPositionColumn() + 1;
	}


	/** Sets the cursorpositon */
	protected void setTextCursorPosition(int pos) {
		if (pos < 1) {
			pos = 1;
		}
		else if (pos > buffer.length()) {
			pos = buffer.length() + 1;
		}

		if (pos >= getWidth()) {
			pos = getWidth();
		}

		setCursor(getPositionLine(), getPositionColumn() + pos - 1);
	}


	/** Sets the cursoposition to the end */
	protected void setTextCursorPositionToEnd() {
		setTextCursorPosition(buffer.length() + 1);
	}


	/** Sets the cursorposition to the start */
	protected void setTextCursorPositionToStart() {
		setTextCursorPosition(1);
	}


	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	/** Sets the text */
	public void setText(String text) {
		buffer.setLength(0);
		buffer.append(text);
		cutBufferToFit();
		setTextCursorPositionToEnd();
		setDirty(true);
	}


	/** Inserts text */
	public void insertText(String text) {
		if (getTextCursorPosition() > buffer.length() + 1) {
			return;
		}

		int freeSpace = getWidth() - buffer.length();

		if (text.length() <= freeSpace) {
			buffer.insert(getTextCursorPosition() - 1, text);
			setTextCursorPosition(getTextCursorPosition() + text.length());
		}
		else {
			buffer.insert(getTextCursorPosition() - 1, text.substring(0, freeSpace));
			setTextCursorPosition(getTextCursorPosition() + freeSpace);
		}
		setDirty(true);
	}


	/** Deletes charsToDelete Charaters in the text */
	public void delText(int charsToDelete) {
		if (buffer.length() == 0) {
			return;
		}
		// same as backspace
		if (charsToDelete < 0) {
			if (getTextCursorPosition() > 1) {
				// don't delete more chars then there are
				if (-charsToDelete > (getTextCursorPosition() - 1)) {
					charsToDelete = -getTextCursorPosition() + 1;
				}

				buffer.delete(getTextCursorPosition() + charsToDelete - 1, getTextCursorPosition() - 1); //+charsToDelete, da charsToDelete negativ ist
				setTextCursorPosition(getTextCursorPosition() + charsToDelete);
			}
		}

		// same as del
		else {
			if (getTextCursorPosition() <= buffer.length()) {
				// don't delete more chars then there are
				if (charsToDelete > (buffer.length() - getTextCursorPosition() + 1)) {
					charsToDelete = buffer.length() - getTextCursorPosition() + 1;
				}
				buffer.delete(getTextCursorPosition() - 1, getTextCursorPosition() + charsToDelete - 1);
			}
		}

		setDirty(true);
	}


	/** Returns the text */
	public String getText() {
		return buffer.toString();
	}


	/** Returns the length of the text */
	public int getTextLength() {
		return buffer.length();
	}


	/** Deletes the text */
	public void clearText() {
		buffer.setLength(0);
		setCursor(getPositionLine(), getPositionColumn());
		setDirty(true);
	}


	/** Called when widget recives barcode */
	@Override
	public void focusEntered() {
		// do nothing
	}


	/** Called when widget recives a key */
	@Override
	public void focusLeft() {
		// do nothing
	}


	//--------------------------------------------------------------------------
	// override abstract methods of BasicWidget
	//--------------------------------------------------------------------------

	/** Process barcode */
	@Override
	public void processBarcodeReceived(TerminalEvent e) {
		// do nothing here
	}


	/** Process Key event */
	@Override
	public void processKeyPressed(TerminalEvent e) {
		if (e.getKey() == TerminalEvent.KEY_ENTER) {
			owner.focusNextPossibleWidget();
			return;
		}
		else if (e.getKey() == TerminalEvent.KEY_HOME) {
			setTextCursorPositionToStart();
			return;
		}
		else if (e.getKey() == TerminalEvent.KEY_END) {
			setTextCursorPositionToEnd();
			return;
		}
		else if (e.getKey() == TerminalEvent.KEY_BACKSPACE) {
			delText(-1);
		}
		else if (e.getKey() == TerminalEvent.KEY_DEL) {
			delText(1);
		}
		else if (e.getKey() == TerminalEvent.KEY_LEFT) {
			setTextCursorPosition(getTextCursorPosition() - 1);
		}
		else if (e.getKey() == TerminalEvent.KEY_RIGHT) {
			setTextCursorPosition(getTextCursorPosition() + 1);
		}
		else if (e.getKey() == TerminalEvent.KEY_UP) {
			owner.focusPreviousPossibleWidget();
		}
		else if (e.getKey() == TerminalEvent.KEY_DOWN) {
			owner.focusNextPossibleWidget();
		}
		else if (e.isPrintable()) {
			insertText(e.getKeyAsString());
		}
	}

}
