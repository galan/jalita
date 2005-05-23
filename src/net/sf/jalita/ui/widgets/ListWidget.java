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
 * Creation date: 13.05.2003
 *  
 * Revision:      $Revision: 1.3 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2005/05/23 18:10:19 $
 * 
 * $Log: ListWidget.java,v $
 * Revision 1.3  2005/05/23 18:10:19  danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 *
 * Revision 1.2  2004/08/06 00:55:34  danielgalan
 * prepare release
 *
 * Revision 1.1  2004/07/26 21:40:27  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import net.sf.jalita.ui.forms.BasicForm;
import net.sf.jalita.io.TerminalEvent;
import javax.swing.ListModel;
import javax.swing.AbstractListModel;
import java.io.IOException;
import java.util.Vector;
import net.sf.jalita.io.TerminalIOInterface;



/**
 * Abstract class for widgets that represent a list
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.3 $
 */
public class ListWidget extends BasicWidget {

    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** The model with the data */
    private ListModel model;

    /** the inverted line */
    private int selectedRow = 1;

    /** the selected line in the model */
    private int selectedIndex = 0;

    /** ListSelectionEvents */
    private Vector listener = new Vector(1, 1);

    /** Should the positioncursor to the left and right be shown */
    private boolean positionCursor = true;




    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a new ListWidget-Object */
    public ListWidget(BasicForm owner, int posLine, int posColumn, int width, int rows, final Vector listData) {
        this(owner, posLine, posColumn, width, rows, listData, true);
    }



    /** Creates a new ListWidget-Object */
    public ListWidget(BasicForm owner, int posLine, int posColumn, int width, int rows, final Vector listData, boolean withPositionCursor) {
        this(owner, posLine, posColumn, width, rows,
            new AbstractListModel() {
                public int getSize() {return listData.size(); }
                public Object getElementAt(int i) { return listData.elementAt(i); }
            }
        , withPositionCursor);
    }



    /** Creates a new ListWidget-Object */
    public ListWidget(BasicForm owner, int posLine, int posColumn, int width, int rows, ListModel model) {
        this(owner, posLine, posColumn, width, rows, model, true);
    }



    /** Creates a new ListWidget-Object */
    public ListWidget(BasicForm owner, int posLine, int posColumn, int width, int rows, ListModel model, boolean withPositionCursor) {
        super(owner, true);

        setWidth(width);
        setHeight(rows);

        setPositionLine(posLine);
        setPositionColumn(posColumn);
        setPositionCursorVisible(withPositionCursor);

        setListModel(model);
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** Sets the ListModel, which supplies the data for the list */
    public void setListModel(ListModel model) {
        if (model == null) {
            this.model = new AbstractListModel() {
                public int getSize() { return 0; }
                public Object getElementAt(int i) { return "Keine Daten"; }
            };

        }
        else {
            this.model = model;
        }

        selectedRow = (int)Math.ceil(getHeight() / 2f);
        selectedIndex = 0;
        setCursor(getPositionLine() + selectedRow - 1, getPositionColumn());
        setDirty(true);
    }



    /** Sets the ListModel, which supplies the data for the list */
    public void setListModel(final Vector listData) {
        setListModel(
            new AbstractListModel() {
                public int getSize() {return listData.size(); }
                public Object getElementAt(int i) { return listData.elementAt(i); }
            }
        );
    }



    /** Returns the currently selected object */
    public Object getSelected() {
        if (model.getSize() == 0) {
            return null;
        }

        return model.getElementAt(selectedIndex);
    }



    /** Returns the index of the selected entry */
    public int getSelectedIndex() {
        return selectedIndex;
    }



    /** Sets the selected entry */
    public void setSelectedIndex(int selected) {
        if (selected < 0) {
            selected = 0;
        }
        else if (selected > model.getSize()) {
            selected = model.getSize() - 1;
        }

        this.selectedIndex = selected;
        fireValueChanged(new ListSelectionEvent(this));

        setDirty(true);
    }



    /** Called when widget recives the focus */
    public void focusEntered() {
        setDirty(true);
    }



    /** Called when widget lost its focus */
    public void focusLeft() {
        setDirty(true);
    }



    /** Sets if to the left and to the right the positioncursor should be shown */
    public void setPositionCursorVisible(boolean cursorVisible) {
        positionCursor = cursorVisible;
    }



    /** Tells us if to the left and to the right the positioncursor should be shown */
    public boolean isPositionCursorVisible() {
        return positionCursor;
    }



    /** Draws the widget */
    public void paint() throws IOException {
        if (isFocused()) {
            if (positionCursor) {
                getIO().writeInverseText(">", getPositionLine() + selectedRow - 1, getPositionColumn());
                getIO().drawLine(getPositionLine() + selectedRow - 1, getPositionColumn() + 1, TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth() - 2);
                getIO().writeInverseText("<", getPositionLine() + selectedRow - 1, getPositionColumn() + getWidth() - 1);
            }
            else {
                getIO().drawLine(getPositionLine() + selectedRow - 1, getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
            }
        }
        else {
            if (positionCursor) {
                getIO().writeText(">", getPositionLine() + selectedRow - 1, getPositionColumn());
                getIO().clearLine(getPositionLine() + selectedRow - 1, getPositionColumn() + 1, TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth() - 2);
                getIO().writeText("<", getPositionLine() + selectedRow - 1, getPositionColumn() + getWidth() - 1);
            }
            else {
                getIO().clearLine(getPositionLine() + selectedRow - 1, getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
            }
        }

        int topIndex = selectedIndex - selectedRow;

        for (int i = 1; i <= getHeight(); i++) {
            if (((i + topIndex) >= 0) && (i + topIndex < (model.getSize()))) {
                Object obj = model.getElementAt(i + topIndex);

                if (obj != null) {

                    // text beschneiden, falls zu lang für Liste
                    String listText = null;
                    int maxWidth = positionCursor ? getWidth() - 2 : getWidth();

                    if (obj.toString().length() > maxWidth) {
                        listText = obj.toString().substring(0, maxWidth);
                    }
                    else {
                        listText = obj.toString();
                    }


                    if ((i == selectedRow) && isFocused()) {
                        if (positionCursor) {
                            getIO().writeInverseText(listText, getPositionLine() + i - 1, getPositionColumn() + 1);
                        }
                        else {
                            getIO().writeInverseText(listText, getPositionLine() + i - 1, getPositionColumn());
                        }
                    }

                    else {
                        if (positionCursor) {
                            getIO().clearLine(getPositionLine() + i - 1, getPositionColumn() + 1, TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth() - 2);
                            getIO().writeText(listText, getPositionLine() + i - 1, getPositionColumn() + 1);
                        }
                        else {
                            getIO().clearLine(getPositionLine() + i - 1, getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
                            getIO().writeText(listText, getPositionLine() + i - 1, getPositionColumn());
                        }
                    }
                }
            }

            else {
                if (positionCursor) {
                    getIO().clearLine(getPositionLine() + i - 1, getPositionColumn() + 1, TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth() - 2);
                }
                else {
                    getIO().clearLine(getPositionLine() + i - 1, getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
                }
            }
        }

    }



    /** Informw the listerns about selectionchanges */
    public void fireValueChanged(ListSelectionEvent e) {
        for (int i = 0; i < listener.size(); i++) {
            ((ListSelectionListener)listener.elementAt(i)).valueChanged(e);
        }
    }



    /** Registers listeners */
    public void addListSelectionListener(ListSelectionListener l) {
        listener.add(l);
    }



    /** removes listeners */
    public void removeListSelectionListener(ListSelectionListener l) {
        listener.remove(l);
    }



    //--------------------------------------------------------------------------
    // override abstract methods of BasicWidget
    //--------------------------------------------------------------------------

    /** Process barcodes */
    public void processBarcodeReceived(TerminalEvent e) {
    }



    /** Tastendruck verarbeiten */
    public void processKeyPressed(TerminalEvent e) {
        if (e.getKey() == TerminalEvent.KEY_ENTER) {
            owner.focusNextPossibleWidget();
            return;
        }
        else if (e.getKey() == TerminalEvent.KEY_HOME) {
            setSelectedIndex(0);
            return;
        }
        else if (e.getKey() == TerminalEvent.KEY_END) {
            setSelectedIndex(model.getSize()-1);
            return;
        }
        else if (e.getKey() == TerminalEvent.KEY_DEL) {
            // ?
        }
        else if (e.getKey() == TerminalEvent.KEY_UP) {
            if (selectedIndex > 0) {
                setSelectedIndex(getSelectedIndex() - 1);
            }
        }
        else if (e.getKey() == TerminalEvent.KEY_DOWN) {
            if (selectedIndex < (model.getSize() - 1)) {
                setSelectedIndex(getSelectedIndex() + 1);
            }
        }
        else if (e.isPrintable()) {
            // nada
        }
    }

}
