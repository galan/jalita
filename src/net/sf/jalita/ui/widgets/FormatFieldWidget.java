/***********************************************************************
 * 
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2004 Daniel Galán y Martins
 * 
 *********************************************************************** 
 *
 * Author:   	  Daniel "tentacle" Galán y Martins
 * Creation date: 23.07.2003
 *  
 * Revision:      $Revision: 1.1 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2004/07/26 21:40:27 $
 * 
 * $Log: FormatFieldWidget.java,v $
 * Revision 1.1  2004/07/26 21:40:27  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.log4j.Logger;

import net.sf.jalita.application.Configuration;
import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.ui.forms.BasicForm;



/**
 * A formatted textfield for numbers
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.1 $
 */

public class FormatFieldWidget extends BasicWidget {

    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    /** log4j reference */
    public final static Logger log = Logger.getLogger(Configuration.class);

    /** leading minus sign */
    private static final String MINUS = "-";


    
    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** Positions before the decimal point */
    private int positionsBeforeDecimalPoint = 4;

    /** Positions after the decimal point */
    private int positionsAfterDecimalPoint = 2;

    /** True if decimal point should be shown in the number */
    private boolean showDecimalPoint = true;

    /** Position of the decimal point */
    private int positionDecimalPoint;

    /** is sign allowed */
    private boolean signAllowed = false;

    /** Are Cursor-Keys permitted to add or substract values */
    private boolean cursorKeysEnabled = false;

    /** sign */
    private boolean minusSign = false;

    /** The text of this widget, which keeps the number without decimal point */
    protected StringBuffer buffer = new StringBuffer();



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a new FormatFieldWidget-Object - leading sign is denied and field is empty */
    public FormatFieldWidget(BasicForm owner, int posLine, int posColumn, int postionsBeforeDecimalPoint,
                             int positionsAfterDecimalPoint) {
        this(owner, 0.0d, posLine, posColumn, postionsBeforeDecimalPoint, positionsAfterDecimalPoint, false);
        clearField();
    }



    /** Creates a new FormatFieldWidget-Object - field is empty */
    public FormatFieldWidget(BasicForm owner, int posLine, int posColumn, int postionsBeforeDecimalPoint,
                             int positionsAfterDecimalPoint, boolean signAllowed) {
        this(owner, 0.0d, posLine, posColumn, postionsBeforeDecimalPoint, positionsAfterDecimalPoint, signAllowed);
        clearField();
    }



    /** Creates a new FormatFieldWidget-Object - field is empty */
    public FormatFieldWidget(BasicForm owner, int posLine, int posColumn, int postionsBeforeDecimalPoint,
                             int positionsAfterDecimalPoint, boolean signAllowed, boolean cursorKeysEnabled) {
        this(owner, 0.0d, posLine, posColumn, postionsBeforeDecimalPoint, positionsAfterDecimalPoint, signAllowed, cursorKeysEnabled);
        clearField();
    }



    /** Creates a new FormatFieldWidget-Object - leading sign is denied */
    public FormatFieldWidget(BasicForm owner, double number, int posLine, int posColumn,
                             int postionsBeforeDecimalPoint, int positionsAfterDecimalPoint) {
        this(owner, number, posLine, posColumn, postionsBeforeDecimalPoint, positionsAfterDecimalPoint, false);
    }



    /** Creates a new FormatFieldWidget-Object */
    public FormatFieldWidget(BasicForm owner, double number, int posLine, int posColumn,
                             int postionsBeforeDecimalPoint, int positionsAfterDecimalPoint,
                             boolean newSignAllowed) {
        this(owner, number, posLine, posColumn, postionsBeforeDecimalPoint, positionsAfterDecimalPoint, newSignAllowed, false);
    }



    /** Creates a new FormatFieldWidget-Object */
    public FormatFieldWidget(BasicForm owner, double number, int posLine, int posColumn,
                             int postionsBeforeDecimalPoint, int positionsAfterDecimalPoint,
                             boolean newSignAllowed, boolean newCursorKeysEnabled) {
        super(owner, true);

        signAllowed = newSignAllowed;
        cursorKeysEnabled = newCursorKeysEnabled;

        setPositionLine(posLine);
        setPositionColumn(posColumn);

        setSize(postionsBeforeDecimalPoint, positionsAfterDecimalPoint, number);
    }



    //--------------------------------------------------------------------------
    // private & protected methods
    //--------------------------------------------------------------------------

    /** Inserts text */
    private void insertText(String text) {
        int difference = (showDecimalPoint ? 1 : 0) + (signAllowed ? 1 : 0);
        if (buffer.length() >= getWidth() - difference) {
            return;
        }

        int freeSpace = getWidth() - buffer.length() - difference;

        if (text.length() <= freeSpace) {
            buffer.append(text);
        }
        else {
            buffer.append(text.substring(0, freeSpace));
        }
        setDirty(true);
    }



    /** Clears charsToDelete charactes in the text */
    private void delLastChar() {
        if (buffer.length() == 0) {
            return;
        }

        if (buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }

        setDirty(true);
    }



    /** Adds or substracts 1 to current number, when UP/DOWN Keys are used */
    private void cursorKeyPressed(boolean up) {
        try {
            double number = getNumber();
            double key = 1.0D;
            if (!up) {
                key = -1.0;
            }
            number = number + key;
            if (number == 0.0D) {
                number = number + key;
            }
            try {
                setNumber(number);
                setDirty(true);
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

    /** Sets the leading sign */
    public void changeSign() {
        if (signAllowed) {
            minusSign = !minusSign;
            setDirty(true);
        }
    }



    /** Set position before and after the decimal point */
    public void setSize(int positionsBeforeDecimalPoint, int positionsAfterDecimalPoint, double number) {

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



    /** draws the widget */
    public void paint() throws IOException {
        getIO().setUnderlined(true);

        // Draw underline (Won't be drawn anyway, but better than spaces to identify in terminalemulation.)
        getIO().cursorMoveAbsolut(getPositionLine(), getPositionColumn());
        for (int i = 0; i < getWidth(); i++) {
            getIO().writeText("_");
        }

        // draw sign
        if (signAllowed && minusSign) {
            getIO().setUnderlined(false);
            getIO().writeText("-", getPositionLine(), getPositionColumn());
            getIO().setUnderlined(true);
        }

        // draw decimal point
        if (showDecimalPoint) {
            getIO().setUnderlined(false);
            getIO().writeText(".", getPositionLine(), getPositionColumn() + getWidth() - positionDecimalPoint);
            getIO().setUnderlined(true);
        }

        // positions before decimal point
        if (buffer.length() > positionsAfterDecimalPoint) {
            String sub = buffer.substring(0, buffer.length() - positionsAfterDecimalPoint);
            getIO().writeText(sub, getPositionLine(), getPositionColumn() + getWidth() - positionDecimalPoint - sub.length());
        }

        // positions after decimal point
        if (buffer.length() > 0) {
            String sub = null;
            // fit length of text
            if (buffer.length() > positionsAfterDecimalPoint) {
                sub = buffer.substring(buffer.length() - positionsAfterDecimalPoint);
            }
            else {
                sub = buffer.toString();
            }
            getIO().writeText(sub, getPositionLine(), getPositionColumn() + getWidth() - sub.length());
        }

        setCursor(getPositionLine(), getPositionColumn() + getWidth() - 1);
        getIO().setUnderlined(false);
    }



    /** Returns the shown number */
    public double getNumber() {
        StringBuffer result = new StringBuffer();

        if (signAllowed && minusSign) {
            result.append(MINUS);
        }

        if (showDecimalPoint && (buffer.length() > positionsAfterDecimalPoint)) {
            result.append(buffer.substring(0, buffer.length() - positionsAfterDecimalPoint));
        }
        else {
            result.append(buffer.substring(0, buffer.length()));
        }

        result.append(".");

        // parse position after decimal point
        StringBuffer nachKomma = new StringBuffer();

        if ((buffer.length() > 0) && (buffer.length() <= positionsAfterDecimalPoint)) {
            nachKomma.append(buffer.toString());
            // fill with zeros
            for (int i = nachKomma.length(); i < positionsAfterDecimalPoint; i++) {
                nachKomma.insert(0, "0");
            }
        }
        else if (buffer.length() > positionsAfterDecimalPoint) {
            nachKomma.append(buffer.substring(buffer.length() - positionsAfterDecimalPoint));
        }
        // Has no position after decimal point (field is empty)
        else if (buffer.length() == 0) {
            nachKomma.append("0");
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

        /** @todo  oder besser Exception werfen? */
        buffer.setLength(0);
        buffer.append(prePoint);

        // Validate if numbers after decmal point are to long -> cut them off
        if (showDecimalPoint && (postPoint.length() > positionsAfterDecimalPoint)) {
            postPoint = postPoint.substring(0, positionsAfterDecimalPoint);
        }

        if (showDecimalPoint) {
            buffer.append(postPoint);
        }

        setDirty(true);
    }



    /** Clears the text in the field */
    public void clearField() {
        buffer.setLength(0);
        setDirty(true);
    }



    //--------------------------------------------------------------------------
    // override abstract methods of BasicWidget
    //--------------------------------------------------------------------------

    /** Process barcode */
    public void processBarcodeReceived(TerminalEvent e) {
        // do nothing here
    }



    /** Process key event */
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
    }



    /** Process focus recived event */
    public void focusEntered() {
        // do nothing
    }



    /** Process focus lost event */
    public void focusLeft() {
        // do nothing
    }

}
