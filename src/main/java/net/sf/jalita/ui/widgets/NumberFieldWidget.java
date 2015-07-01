/***********************************************************************
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * Author: Daniel Galán y Martins
 * Creation date: 17.10.2003
 * Revision: $Revision: 1.3 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:58 $
 * $Log: NumberFieldWidget.java,v $
 * Revision 1.3  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.2 2005/05/23 18:10:19 danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 * Revision 1.1 2004/07/26 21:40:27 danielgalan
 * Jalita initial cvs commit :)
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.ui.forms.BasicForm;



/**
 * A formatted Textfield for numbers with different inputmodes
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.3 $
 */

public class NumberFieldWidget extends BasicWidget {

	//--------------------------------------------------------------------------
	// class variables
	//--------------------------------------------------------------------------

	/** Minus-sign */
	private static final String MINUS = "-";

	/** inputmode for the textfield */
	public static int CALCULATOR = 1;
	public static int CASH_BOX = 2;

	//--------------------------------------------------------------------------
	// instance variables
	//--------------------------------------------------------------------------

	/** Positions before the DecimalPoint */
	private int positionsBeforeDecimalPoint = 4;

	/** Positions after the decimal point */
	private int positionsAfterDecimalPoint = 2;

	/** True if decimal point should be shown in the number */
	private boolean showDecimalPoint = true;

	/** Position of the decimal point */
	private int positionDecimalPoint;

	/** is sign allowed */
	private boolean signAllowed = false;

	/** Is zero permitted */
	private boolean zeroAllowed = false;

	/** Are Cursor-Keys permitted to add or substract values */
	private boolean cursorKeysEnabled = false;

	/** sign */
	private boolean minusSign = false;

	/** Should numbers be cleared on direct input (setNumber or Cursortasten) */
	private boolean clearBufferWhileNewInput = false;

	/** Buffer could be deleted */
	private boolean bufferReadyToClear = false;

	/** The text of this widget, which keeps the number without decimal point */
	protected StringBuffer buffer = new StringBuffer();

	/** Current inputmode for the textfield */
	private int inputMethod = CALCULATOR;


	//--------------------------------------------------------------------------
	// constructors
	//--------------------------------------------------------------------------

	/** Creates an ew NumberFieldWidget */
	public NumberFieldWidget(BasicForm owner, int posLine, int posColumn, int postionsBeforeDecimalPoint, int positionsAfterDecimalPoint, boolean signAllowed, boolean cursorKeysEnabled, int inputMethod, boolean clearBufferWhileNewInput) {
		this(owner, 0.0d, posLine, posColumn, postionsBeforeDecimalPoint, positionsAfterDecimalPoint, signAllowed, cursorKeysEnabled, inputMethod, clearBufferWhileNewInput);
		clearField();
	}


	/** Creates an ew NumberFieldWidget */
	public NumberFieldWidget(BasicForm owner, double number, int posLine, int posColumn, int postionsBeforeDecimalPoint, int positionsAfterDecimalPoint, boolean signAllowed, boolean cursorKeysEnabled, int inputMethod, boolean clearBufferWhileNewInput) {
		super(owner, true);

		this.signAllowed = signAllowed;
		this.cursorKeysEnabled = cursorKeysEnabled;
		this.inputMethod = inputMethod;
		this.clearBufferWhileNewInput = clearBufferWhileNewInput;

		setPositionLine(posLine);
		setPositionColumn(posColumn);

		setNumber(postionsBeforeDecimalPoint, positionsAfterDecimalPoint, number);
	}


	//--------------------------------------------------------------------------
	// private & protected methods
	//--------------------------------------------------------------------------

	/** Inserts a point, specialmethod for CALCULATOR-Inputmode */
	private void insertPoint() {
		// Insert point only if no point is in the buffer allready

		if ((inputMethod == CALCULATOR) && (showDecimalPoint) && (buffer.indexOf(".") == -1)) {
			buffer.append(".");
			setDirty(true);
		}
	}


	/** Inserts text */
	private void insertText(String text) {

		// Clear buffer, because defaultvalue
		if (bufferReadyToClear && clearBufferWhileNewInput) {
			buffer = new StringBuffer();
			bufferReadyToClear = false;
		}

		int difference = 0;

		// Calculate difference for different Inputmodes
		if (inputMethod == CASH_BOX) {
			difference = (showDecimalPoint ? 1 : 0) + (signAllowed ? 1 : 0);
		}
		else {
			difference = (signAllowed ? 1 : 0);
		}

		if (inputMethod == CASH_BOX) {
			// Is the buffer big enough
			if (buffer.length() >= getWidth() - difference) {
				return;
			}

			// calc free bufferspace
			int freeSpace = getWidth() - buffer.length() - difference;

			// If there is enough space save all, elswise truncate
			if (text.length() <= freeSpace) {
				buffer.append(text);
			}
			else {
				buffer.append(text.substring(0, freeSpace));
			}
		}
		else if (inputMethod == CALCULATOR) {
			int pointPos = buffer.indexOf(".");

			if (pointPos < 0) {
				// numbers before decimal point will be inserted .. because no decimal point exists

				// Check all numbers before decimal point
				int freeSpace = positionsBeforeDecimalPoint - buffer.length();
				if (text.length() <= freeSpace) {
					buffer.append(text);
				}
				else {
					buffer.append(text.substring(0, freeSpace));
				}
			}
			else {
				// numbers after decimal point will be inserted .. because decimal point exists

				// Check all numbers after decimal point
				int realLength = buffer.length() - (pointPos + 1);
				int freeSpace = positionsAfterDecimalPoint - realLength;

				if (text.length() <= freeSpace) {
					buffer.append(text);
				}
				else {
					buffer.append(text.substring(0, freeSpace));
				}
			}
		}

		// redraw
		setDirty(true);
	}


	/** Deletes a character */
	private void delLastChar() {
		if (buffer.length() == 0) {
			return;
		}

		if (buffer.length() > 0) {
			if (clearBufferWhileNewInput && bufferReadyToClear) {
				buffer = new StringBuffer();
				bufferReadyToClear = false;
			}
			else {
				buffer.deleteCharAt(buffer.length() - 1);
			}
		}

		setDirty(true);
		bufferReadyToClear = false;
	}


	/** Adds or substracts 1 to current number when UP/DOWN Keys are used */
	private void cursorKeyPressed(boolean up) {
		try {
			double number = getNumber();
			double key = 1.0D;
			if (!up) {
				key = -1.0;
			}
			number = number + key;
			if (!zeroAllowed && (number == 0.0D)) {
				number = number + key;
			}
			if (zeroAllowed && !signAllowed && (number < 0.0)) {
				number = 0.0;
			}
			try {
				setNumber(number);
				setDirty(true);
				bufferReadyToClear = true;
			}
			catch (FormatFieldRangeException ffre) {
				log.error(ffre);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	/** Sets the sign */
	public void changeSign() {
		if (signAllowed) {
			minusSign = !minusSign;
			setDirty(true);
		}
	}


	/** Draws the widget */
	@Override
	public void paint() throws IOException {
		getIO().setUnderlined(true);

		// Draw underline
		getIO().cursorMoveAbsolut(getPositionLine(), getPositionColumn());
		for (int i = 0; i < getWidth(); i++) {
			getIO().writeText("_");
		}

		// Set sign
		if (signAllowed && minusSign) {
			getIO().setUnderlined(false);
			getIO().writeText("-", getPositionLine(), getPositionColumn());
			getIO().setUnderlined(true);
		}

		if (inputMethod == CASH_BOX) {
			// draw decimal point
			if (showDecimalPoint) {
				getIO().setUnderlined(false);
				getIO().writeText(".", getPositionLine(), getPositionColumn() + getWidth() - positionDecimalPoint);
				getIO().setUnderlined(true);
			}

			// Places before decimal point
			if (buffer.length() > positionsAfterDecimalPoint) {
				String sub = buffer.substring(0, buffer.length() - positionsAfterDecimalPoint);
				getIO().writeText(sub, getPositionLine(), getPositionColumn() + getWidth() - positionDecimalPoint - sub.length());
			}

			// Places after decimal point
			if (buffer.length() > 0) {
				String sub = null;
				// fit textlength
				if (buffer.length() > positionsAfterDecimalPoint) {
					sub = buffer.substring(buffer.length() - positionsAfterDecimalPoint);
				}
				else {
					sub = buffer.toString();
				}

				getIO().writeText(sub, getPositionLine(), getPositionColumn() + getWidth() - sub.length());
			}
		}
		else if (inputMethod == CALCULATOR) {

			// draw all
			if (buffer.length() > 0) {
				String sub = buffer.toString();

				getIO().writeText(sub, getPositionLine(), getPositionColumn() + getWidth() - sub.length());
			}

		}

		setCursor(getPositionLine(), getPositionColumn() + getWidth() - 1);

		getIO().setUnderlined(false);
	}


	/** Returns the shown number */
	public double getNumber() {
		StringBuffer result = new StringBuffer();

		// evaluate sign
		if (signAllowed && minusSign) {
			result.append(MINUS);
		}

		StringBuffer vorKomma = new StringBuffer();
		// evaluate number before decimal point
		if (inputMethod == CASH_BOX) {
			if (showDecimalPoint && (buffer.length() > positionsAfterDecimalPoint)) {
				vorKomma.append(buffer.substring(0, buffer.length() - positionsAfterDecimalPoint));
			}
			else if (!showDecimalPoint) {
				vorKomma.append(buffer.substring(0, buffer.length()));
			}
		}
		else if (inputMethod == CALCULATOR) {
			int pointPos = buffer.indexOf(".");
			if (pointPos < 0) {
				vorKomma.append(buffer.substring(0, buffer.length()));
			}
			else {
				vorKomma.append(buffer.substring(0, pointPos));
			}
		}

		result.append(vorKomma);
		result.append(".");

		StringBuffer nachKomma = new StringBuffer();
		// evaluate number after decimal point
		if (inputMethod == CASH_BOX) {

			// Positions after decimal point is smaller then posiitons till the point
			if ((buffer.length() > 0) && (buffer.length() <= positionsAfterDecimalPoint)) {
				nachKomma.append(buffer.toString());
				// Fill with zeros
				for (int i = nachKomma.length(); i < positionsAfterDecimalPoint; i++) {
					nachKomma.insert(0, "0");
				}
			}
			// Positions after decimal point is bigger then positions till point
			else if (buffer.length() > positionsAfterDecimalPoint) {
				nachKomma.append(buffer.substring(buffer.length() - positionsAfterDecimalPoint));
			}
			// Has no positions after decimal point (field is empty)
			else if (buffer.length() == 0) {
				nachKomma.append("0");
			}
		}
		else if (inputMethod == CALCULATOR) {
			int pointPos = buffer.indexOf(".");
			// Has no positions after decimal point
			if (pointPos < 0) {
				nachKomma.append("0");
			}
			// Has positions after decimal point
			else {
				nachKomma.append(buffer.substring(pointPos + 1, buffer.length()));
			}
		}

		result.append(nachKomma);
		return Double.parseDouble(result.toString());
	}


	/** Returns the number as String */
	public String getNumberString() {
		return Double.toString(getNumber());
	}


	/** Returns the whole raw buffer, sign inclusive */
	public String getRawString() {
		StringBuffer result = new StringBuffer();
		if (signAllowed && minusSign) {
			result.append(MINUS);
		}
		result.append(buffer);
		return result.toString();
	}


	/** Sets the saved number */
	public void setNumber(double number) throws FormatFieldRangeException {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY);
		symbols.setDecimalSeparator('.');

		// validate leading sign and set it if necessary
		if (number < 0 && signAllowed) {
			minusSign = true;
		}
		else {
			minusSign = false;
		}

		StringBuffer zeroPre = new StringBuffer();
		for (int i = 1; i < positionsBeforeDecimalPoint; i++) {
			zeroPre.append("#");
		}
		StringBuffer zeroPost = new StringBuffer();
		for (int i = 0; i < positionsAfterDecimalPoint; i++) {
			zeroPost.append("0");
		}
		// It has to be worked with DecimalFormat, because the textual representation can contain "E"
		DecimalFormat df = new DecimalFormat(zeroPre.toString() + "0." + zeroPost.toString(), symbols);

		String stringNumber = df.format(Math.abs(number));
		String prePoint = stringNumber.substring(0, stringNumber.indexOf('.'));
		String postPoint = stringNumber.substring(stringNumber.indexOf('.') + 1);

		// Validate if numbers before decimal point exceeds the lenth of the field -> set all to 9
		if ((positionsBeforeDecimalPoint - prePoint.length()) < 0) {
			throw new FormatFieldRangeException(number);
		}

		/** @todo oder besser Exception werfen? */
		buffer.setLength(0);
		buffer.append(prePoint);

		// Validate if numbers after decmal point are to long -> cut them off
		if (showDecimalPoint && (postPoint.length() > positionsAfterDecimalPoint)) {
			postPoint = postPoint.substring(0, positionsAfterDecimalPoint);
		}

		// Add Point if CALCULATOR-Mode is used
		if (inputMethod == CALCULATOR && showDecimalPoint) {
			buffer.append(".");
		}

		if (showDecimalPoint) {
			buffer.append(postPoint);
		}

		setDirty(true);
		bufferReadyToClear = true;
	}


	/** Set positions before and after the decimal point */
	public void setNumber(int positionsBeforeDecimalPoint, int positionsAfterDecimalPoint, double number) {

		// Pr�fen ob Punkt und Nachkommastelle
		if (positionsAfterDecimalPoint <= 0) {
			this.positionsAfterDecimalPoint = 0;
			positionDecimalPoint = 0;
			showDecimalPoint = false;
		}
		else {
			this.positionsAfterDecimalPoint = positionsAfterDecimalPoint;
			positionDecimalPoint = positionsAfterDecimalPoint + 1;
			showDecimalPoint = true;
		}

		// Pr�fen Vorkommastelle
		if (positionsBeforeDecimalPoint <= 0) {
			this.positionsBeforeDecimalPoint = 1;
		}
		else {
			this.positionsBeforeDecimalPoint = positionsBeforeDecimalPoint;
		}

		setWidth(positionsBeforeDecimalPoint + positionsAfterDecimalPoint + (showDecimalPoint ? 1 : 0) + (signAllowed ? 1 : 0));

		try {
			setNumber(number);
		}
		catch (FormatFieldRangeException ex) {
			buffer.setLength(0);
		}

		setDirty(true);
	}


	/** Clears the text in the field */
	public void clearField() {
		buffer.setLength(0);
		setDirty(true);
	}


	/** The Buffer can be cleared ob inout when set to true */
	public void setClearBufferWhileNewInput() {
		bufferReadyToClear = true;
	}


	/** Returns true if sign is allowed */
	public boolean isSignAllowed() {
		return signAllowed;
	}


	/** Defines if sign is allowed */
	public void setSignAllowed(boolean signAllowed) {
		this.signAllowed = signAllowed;
	}


	/** Returns true if zero with cursor keys is permitted */
	public boolean isZeroAllowed() {
		return zeroAllowed;
	}


	/** Sets if zero with cursor keys is permitted */
	public void setZeroAllowed(boolean zeroAllowed) {
		this.zeroAllowed = zeroAllowed;
	}


	//--------------------------------------------------------------------------
	// override abstract methods of BasicWidget
	//--------------------------------------------------------------------------

	/** Process barcode */
	@Override
	public void processBarcodeReceived(TerminalEvent e) {
		// do nothing here
	}


	/** Process key event */
	@Override
	public void processKeyPressed(TerminalEvent e) {
		if (e.getKey() == TerminalEvent.KEY_ENTER) {
			owner.focusNextPossibleWidget();
			return;
		}
		else if (e.getKey() == TerminalEvent.KEY_BACKSPACE) {
			delLastChar();
		}
		else if (e.getKey() == TerminalEvent.KEY_DEL) {
			delLastChar();
		}
		else if (e.getKey() == TerminalEvent.KEY_UP) {
			if (cursorKeysEnabled) {
				cursorKeyPressed(true);
			}
			else {
				owner.focusPreviousPossibleWidget();
			}
		}
		else if (e.getKey() == TerminalEvent.KEY_DOWN) {
			if (cursorKeysEnabled) {
				cursorKeyPressed(false);
			}
			else {
				owner.focusNextPossibleWidget();
			}
		}
		else if (Character.isDigit(e.getKeyAsChar())) {
			insertText(e.getKeyAsString());
		}
		else if (e.getKeyAsString().equals(MINUS)) {
			changeSign();
		}
		else if (e.getKeyAsString().equals(".")) {
			if (inputMethod == CALCULATOR) {
				insertPoint();
			}
		}
	}


	/** Process focus recived event */
	@Override
	public void focusEntered() {
		// do nothing
	}


	/** Process focus lost event */
	@Override
	public void focusLeft() {
		// do nothing
	}

}
