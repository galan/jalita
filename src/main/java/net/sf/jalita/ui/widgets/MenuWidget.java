/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2008 Gianluca Sartori
 * Author: Gianluca Sartori
 * Creation date: 12.03.2008
 * Revision: $Revision: 1.6 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:58 $
 * $Log: MenuWidget.java,v $
 * Revision 1.6  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.5 2009/03/05 11:43:16 ilgian
 * Bug fix for selected item exceeding 2 digits
 * Revision 1.4 2009/02/02 14:54:56 ilgian
 * Fixed some bugs in numeric keyboard shortcuts
 * Revision 1.3 2009/02/02 14:26:46 ilgian
 * Enhanced support for keybord shortcuts
 * Revision 1.2 2008/10/09 15:24:15 ilgian
 * Added support for numbered menu lists
 * Revision 1.1 2008/03/21 14:01:57 ilgian
 * New widget
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.io.TerminalIOInterface;
import net.sf.jalita.ui.forms.BasicForm;



/**
 * Abstract class for widgets that represent a list
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.6 $
 */
public class MenuWidget extends ListWidget {

	private final static String NUMBERS = "0123456789";

	private int lastKeypressed = TerminalEvent.KEY_UNDEFINED;
	private int topIndex = 0;
	private boolean numbered = true;
	private final int lastIndex = 0;
	private long lastKeyPressed;
	private int lastNumberPressed;


	/** Creates a new ListWidget-Object */
	public MenuWidget(BasicForm owner, int posLine, int posColumn, int width, int rows, final Vector listData) {
		this(owner, posLine, posColumn, width, rows, listData, false);
	}


	/** Creates a new ListWidget-Object */
	public MenuWidget(BasicForm owner, int posLine, int posColumn, int width, int rows, final Vector listData, boolean withPositionCursor) {
		this(owner, posLine, posColumn, width, rows, new AbstractListModel() {

			public int getSize() {
				return listData.size();
			}


			public Object getElementAt(int i) {
				return listData.elementAt(i);
			}
		}, withPositionCursor);
	}


	/** Creates a new ListWidget-Object */
	public MenuWidget(BasicForm owner, int posLine, int posColumn, int width, int rows, ListModel model) {
		this(owner, posLine, posColumn, width, rows, model, false);
	}


	/** Creates a new ListWidget-Object */
	public MenuWidget(BasicForm owner, int posLine, int posColumn, int width, int rows, ListModel model, boolean withPositionCursor) {
		super(owner, posLine, posColumn, width, rows, model, withPositionCursor);
	}


	/** Tastendruck verarbeiten */
	@Override
	public void processKeyPressed(TerminalEvent e) {
		lastKeypressed = e.getKey();
		int index = getSelectedIndex();
		int current = getSelectedIndex() + 1;
		if (numbered) {
			if (NUMBERS.indexOf(e.getKeyAsChar()) >= 0) {
				long currentSecond = GregorianCalendar.getInstance().getTimeInMillis() / 1000;//(float)1000;
				int idx = Integer.valueOf(e.getKeyAsString()).intValue();
				if (getListModel().getSize() < 10) {
					if (idx > getListModel().getSize())
						idx = getListModel().getSize();
					setSelectedIndex(idx - 1);
				}
				else {
					long secondsElapsed = currentSecond - lastKeyPressed;
					if ((secondsElapsed == 1 || secondsElapsed == 0) && lastNumberPressed != 0) {
						if (current < 10) {
							idx = (current * 10) + idx;
						}
					}
					if (idx > getListModel().getSize())
						idx = getListModel().getSize();
					setSelectedIndex(idx - 1);
				}
				lastKeyPressed = currentSecond;
				lastNumberPressed = NUMBERS.indexOf(e.getKeyAsChar());
			}
			else if (e.getKey() == TerminalEvent.KEY_BACKSPACE) {
				//delete last
				if (current < 10) {
					setSelectedIndex(0);
				}
				else {
					setSelectedIndex((current / 10) - 1);
				}
				lastNumberPressed = 0;
			}
			else if (e.getKey() == TerminalEvent.KEY_F04 || e.getKey() == TerminalEvent.KEY_DEL) {
				setSelectedIndex(0);
				lastNumberPressed = 0;
			}
			else {
				lastNumberPressed = 0;
			}
		}
		super.processKeyPressed(e);
	}


	@Override
	public void fireValueChanged(ListSelectionEvent e) {
		if (lastKeypressed == TerminalEvent.KEY_ENTER) {
			super.fireValueChanged(e);
		}
		else {
			int lastVisible = getHeight();
			if (numbered)
				lastVisible--;
			if (getSelectedIndex() >= topIndex + lastVisible) {
				topIndex = getSelectedIndex() - lastVisible + 1;
			}
			else if (getSelectedIndex() < topIndex) {
				topIndex = getSelectedIndex();
			}

		}

	}


	/** Draws the widget */
	@Override
	public void paint() throws IOException {
		if (isFocused()) {
			getIO().drawLine(getPositionLine() + getSelectedRow() - 1, getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
		}
		else {
			getIO().clearLine(getPositionLine(), getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
		}

		boolean footerDone = false;
		int lastVisible = getHeight();
		if (numbered)
			lastVisible--;

		for (int i = topIndex, j = 0; j < lastVisible; i++, j++) {
			if ((i >= 0) && (i < getListModel().getSize())) {
				Object obj = getListModel().getElementAt(i);

				if (obj != null) {
					String listText = null;
					int maxWidth = isPositionCursorVisible() ? getWidth() - 2 : getWidth();

					listText = obj.toString();
					if (numbered)
						listText = (i + 1) + ". " + listText;
					if (listText.length() > maxWidth) {
						listText = listText.substring(0, maxWidth);
					}

					if ((j == (lastVisible - 1)) && numbered) {
						getIO().setUnderlined(true);
						footerDone = true;
					}
					if ((i == getSelectedIndex()) && isFocused()) {
						getIO().writeInverseText(padToWidth(listText), getPositionLine() + j, getPositionColumn());
						setCursor(getPositionLine() + j, getPositionColumn());
					}
					else {
						getIO().clearLine(getPositionLine() + j, getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
						getIO().writeText(padToWidth(listText), getPositionLine() + j, getPositionColumn());
					}
					getIO().setUnderlined(false);
				}
			}
			else {
				getIO().clearLine(getPositionLine() + j, getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
			}
		}

		if (numbered) {
			if (!footerDone) {
				getIO().setUnderlined(true);
				getIO().writeText(padToWidth(""), getPositionLine() + getHeight() - 2, getPositionColumn());
				getIO().setUnderlined(false);
			}
			getIO().writeText("F4:Canc F2:Esc", getPositionLine() + getHeight() - 1, 0);
			getIO().writeText(">", getPositionLine() + getHeight() - 1, getPositionColumn() + getWidth() - 4);
			int selectedIdx = getSelectedIndex() + 1;
			getIO().writeText(new Integer(selectedIdx).toString(), getPositionLine() + getHeight() - 1, getPositionColumn() + getWidth() - (selectedIdx < 100 ? 2 : 3));
		}

	}


	private String padToWidth(String s) {
		String result = s;
		while (result.length() < getWidth())
			result += ' ';
		return result;
	}


	@Override
	public boolean isPositionCursorVisible() {
		return false;
	}


	@Override
	public void setPositionCursorVisible(boolean cursorVisible) {
		if (cursorVisible)
			throw new IllegalArgumentException("Position cursor cannot be visible in MenuWidget.");
		super.setPositionCursorVisible(cursorVisible);
	}


	public boolean isNumbered() {
		return numbered;
	}


	public void setNumbered(boolean numbered) {
		this.numbered = numbered;
	}
}
