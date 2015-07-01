/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * Author: Daniel Galán y Martins
 * Creation date: 06.05.2003
 * Revision: $Revision: 1.3 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:58 $
 * $Log: BasicWidget.java,v $
 * Revision 1.3  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.2 2005/05/23 18:10:19 danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 * Revision 1.1 2004/07/26 21:40:27 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import java.awt.Dimension;
import java.io.IOException;

import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.io.TerminalEventListener;
import net.sf.jalita.io.TerminalIOInterface;
import net.sf.jalita.ui.forms.BasicForm;
import net.sf.jalita.util.Configuration;

import org.apache.log4j.Logger;



/**
 * Superclass for all visible components on Forms
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.3 $
 */
public abstract class BasicWidget implements TerminalEventListener {

	//--------------------------------------------------------------------------
	// class variables
	//--------------------------------------------------------------------------

	/** log4j reference */
	public final static Logger log = Logger.getLogger(Configuration.class);

	/** possible values for the text alignment */
	public final static int TEXT_ALIGNMENT_CENTER = 0;
	public final static int TEXT_ALIGNMENT_LEFT = 1;
	public final static int TEXT_ALIGNMENT_RIGHT = 2;

	//--------------------------------------------------------------------------
	// instance variables
	//--------------------------------------------------------------------------

	/** Could this widget recive a focus */
	private boolean focusable = false;

	/** Where should the widget be drawn on the screen */
	private WidgetPosition widgetPos = new WidgetPosition();

	/** If the widget could get a focus, where should it be drawn */
	private WidgetPosition cursorPos = new WidgetPosition();

	/** The dimension of the widget */
	private Dimension dim = new Dimension(1, 1);

	/** Has the widget been changed and should it repainted */
	private boolean dirty = true;

	/** How should the textalignment be */
	private int textAlignment = TEXT_ALIGNMENT_CENTER;

	/** The Form, this widgets belongs to */
	protected BasicForm owner;

	/** Should the widget be inverted */
	protected boolean inverse = false;

	/** Should the widget be underlined */
	protected boolean underlined = false;


	//--------------------------------------------------------------------------
	// constructors
	//--------------------------------------------------------------------------

	/** Creates a BasicWidget object */
	public BasicWidget(BasicForm owner, boolean focusable) {
		this.focusable = focusable;
		this.owner = owner;
	}


	//--------------------------------------------------------------------------
	// private & protected methods
	//--------------------------------------------------------------------------

	/** Returns the Terminal-Interface */
	protected TerminalIOInterface getIO() {
		return owner.getIO();
	}


	/** Sets the Cursor to the WidgetPosition */
	protected void setCursor(WidgetPosition pos) {
		this.cursorPos = pos;
	}


	/** Sets the Cursor to the Coordinaten */
	protected void setCursor(int line, int column) {
		cursorPos.setLine(line);
		cursorPos.setColumn(column);
	}


	/** Sets the Cursor on the line */
	protected void setCursorLine(int line) {
		this.cursorPos.setLine(line);
	}


	/** Sets the Cursor on the column */
	protected void setCursorColumn(int column) {
		this.cursorPos.setColumn(column);
	}


	/** Drwas the widget */
	protected abstract void paint() throws IOException;


	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	/** Should the widget recive the focus */
	public boolean isFocusable() {
		return focusable;
	}


	/** Sets if the widget could recive the focus */
	public void setFocusable(boolean focusable) {
		this.focusable = focusable;
	}


	/** Returns the position of the widget */
	public WidgetPosition getPosition() {
		return widgetPos;
	}


	/** Sets the position of the widget */
	public void setPositon(WidgetPosition pos) {
		this.widgetPos = pos;
	}


	/** Returns the line of the widget */
	public int getPositionLine() {
		return widgetPos.getLine();
	}


	/** Sets the line of the widget */
	public void setPositionLine(int line) {
		this.widgetPos.setLine(line);
	}


	/** Returns the column of the widget */
	public int getPositionColumn() {
		return widgetPos.getColumn();
	}


	/** Sets the column of the widget */
	public void setPositionColumn(int column) {
		this.widgetPos.setColumn(column);
	}


	/** returns the position of the cursor */
	public WidgetPosition getCursor() {
		return cursorPos;
	}


	/** Returns the line of the cursor */
	public int getCursorLine() {
		return cursorPos.getLine();
	}


	/** Returns the column of the cursor */
	public int getCursorColumn() {
		return cursorPos.getColumn();
	}


	/** Returns the size of the widget */
	public Dimension getSize() {
		return dim;
	}


	/** Sets the size of teh widget */
	public void setSize(Dimension newSize) {
		dim = newSize;
		if (dim.getHeight() < 1)
			dim.setSize(dim.getWidth(), 1);
		if (dim.getWidth() < 1)
			dim.setSize(1, dim.getHeight());
	}


	/** Returns the height */
	public int getHeight() {
		return (int)dim.getHeight();
	}


	/** Returns the width */
	public int getWidth() {
		return (int)dim.getWidth();
	}


	/** Sets the hight */
	public void setHeight(int height) {
		if (height < 1)
			height = 1;
		dim.setSize(dim.getWidth(), height);
	}


	/** Sets the width */
	public void setWidth(int width) {
		if (width < 1)
			width = 1;
		dim.setSize(width, dim.getHeight());
	}


	/** Sets the size of the widget */
	public void setSize(int width, int height) {
		if (height < 1)
			height = 1;
		if (width < 1)
			width = 1;
		dim.setSize(width, height);
	}


	/** Tells us if the widget should be repainted on next paint() */
	public boolean isDirty() {
		return dirty;
	}


	/** Sets the widget dirty, so it is repainted next time */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}


	/** Returns which alignment the text has */
	public int getTextAlignment() {
		return textAlignment;
	}


	/** Sets the textalignment */
	public void setTextAlignment(int alignment) {
		switch (alignment) {
			case TEXT_ALIGNMENT_LEFT:
			case TEXT_ALIGNMENT_CENTER:
			case TEXT_ALIGNMENT_RIGHT:
				textAlignment = alignment;
				break;
			default:
				throw new IllegalArgumentException("invalid justification arg.");
		}
	}


	protected final void pad(StringBuffer to, int howMany) {
		for (int i = 0; i < howMany; i++)
			to.append(' ');
	}


	/** Format a String */
	public StringBuffer format(StringBuffer text, int maxChars) {
		String s = text.toString();
		StringBuffer where = new StringBuffer();
		String wanted = s.substring(0, Math.min(s.length(), maxChars));

		// If no space left for justification, return maxChars' worth */
		if (wanted.length() > maxChars) {
			where.append(wanted);
		}
		// Else get the spaces in the right place.
		else
			switch (textAlignment) {
				case TEXT_ALIGNMENT_RIGHT:
					pad(where, maxChars - wanted.length());
					where.append(wanted);
					break;
				case TEXT_ALIGNMENT_CENTER:
					int startPos = where.length();
					pad(where, (maxChars - wanted.length()) / 2);
					where.append(wanted);
					pad(where, (maxChars - wanted.length()) / 2);
					// Adjust for "rounding error"
					pad(where, maxChars - (where.length() - startPos));
					break;
				case TEXT_ALIGNMENT_LEFT:
					where.append(wanted);
					pad(where, maxChars - wanted.length());
					break;
			}
		return where;
	}


	/** executes a manual repaint */
	public void redraw() throws IOException {
		dirty = false;
		paint();
	}


	/** Tells us if the widget has the focus */
	public boolean isFocused() {
		return owner.hasWidgetFocus(this);
	}


	/** Sets the widget inverse mode. Not supported by all widgets. */
	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}


	/** Tells if the widget is painted inverse. */
	public boolean isInverse() {
		return inverse;
	}


	/** Sets the underline of the widget */
	public void setUnderlined(boolean underlined) {
		this.underlined = underlined;
	}


	/** Tells us if the widget is underlined, not supported by all widgets */
	public boolean isUnderlined() {
		return underlined;
	}


	/** Called when widget recives the focus */
	public abstract void focusEntered();


	/** Called when widget lost its focus */
	public abstract void focusLeft();


	/** Called when widget recives barcode */
	public abstract void processBarcodeReceived(TerminalEvent e);


	/** Called when widget recives a key */
	public abstract void processKeyPressed(TerminalEvent e);


	//--------------------------------------------------------------------------
	// implementation of the interface TerminalKeyListener
	//--------------------------------------------------------------------------

	/**
	 * Accepts barcodess, instead of this method processBarcodeReceived() should
	 * be used in subclasses
	 */
	public final void barcodeReceived(TerminalEvent e) {
		log.debug("Barcode received in BasicWidget -> Barcode [" + e.getBarcode() + "]");

		processBarcodeReceived(e);
	}


	/**
	 * Accepts Keys, instead of this method processKeyPressed() should be used
	 * in subclasses
	 */
	public final void keyPressed(TerminalEvent e) {
		if (e.isPrintable()) {
			log.debug("Keypress received in BasicWidget -> Key (printable): [" + e.getKeyAsString() + "]");
		}
		else if (e.isFunctionKey()) {
			log.debug("Keypress received in BasicWidget -> FunktionKey: [" + java.lang.Math.abs(e.getKey()) + "]");
		}
		else {
			log.debug("Keypress received in BasicWidget -> Key (non-printable): [" + e.getKey() + "]");
		}

		processKeyPressed(e);
	}

}
