/***********************************************************************
 * 
 * This software is published under the terms of the LGPL
 * version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Copyright (C) 2008 Gianluca Sartori
 * 
 *********************************************************************** 
 *
 * Author:   	  Gianluca Sartori
 * Creation date: 12.03.2008
 *  
 * Revision:      $Revision: 1.1 $
 * Checked in by: $Author: ilgian $
 * Last modified: $Date: 2008/03/21 14:01:57 $
 * 
 * $Log: MenuWidget.java,v $
 * Revision 1.1  2008/03/21 14:01:57  ilgian
 * New widget
 *
 *
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import java.io.IOException;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.io.TerminalIOInterface;
import net.sf.jalita.ui.forms.BasicForm;



/**
 * Abstract class for widgets that represent a list
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.1 $
 */
public class MenuWidget extends ListWidget {

	private int lastKeypressed = TerminalEvent.KEY_UNDEFINED;
	private int topIndex = 0;


	/** Creates a new ListWidget-Object */
    public MenuWidget(BasicForm owner, int posLine, int posColumn, int width, int rows, final Vector listData) {
        this(owner, posLine, posColumn, width, rows, listData, false);
    }



    /** Creates a new ListWidget-Object */
    public MenuWidget(BasicForm owner, int posLine, int posColumn, int width, int rows, final Vector listData, boolean withPositionCursor) {
        this(owner, posLine, posColumn, width, rows,
            new AbstractListModel() {
                public int getSize() {return listData.size(); }
                public Object getElementAt(int i) { return listData.elementAt(i); }
            }
        , withPositionCursor);
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
    public void processKeyPressed(TerminalEvent e) {
    	lastKeypressed = e.getKey();
       	super.processKeyPressed(e);
    }
    
    public void fireValueChanged(ListSelectionEvent e) {
    	if(lastKeypressed == TerminalEvent.KEY_ENTER){
    		super.fireValueChanged(e);
    	} else {
    		if(getSelectedIndex() >= topIndex + getHeight()){
    			topIndex = getSelectedIndex() - getHeight() + 1;
    		} 
    		else if(getSelectedIndex() < topIndex){
    			topIndex = getSelectedIndex();
    		} 

    	}
    	
    }



    /** Draws the widget */
	public void paint() throws IOException {
		if (isFocused()) {
			getIO().drawLine(getPositionLine() + getSelectedRow() - 1, getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
		} else {
			getIO().clearLine(getPositionLine(), getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
		}


		for (int i = topIndex, j = 0; j < getHeight(); i++, j++) {
			if ((i >= 0) && (i < getListModel().getSize())) {
				Object obj = getListModel().getElementAt(i);

				if (obj != null) {
					String listText = null;
					int maxWidth = isPositionCursorVisible() ? getWidth() - 2 : getWidth();

					if (obj.toString().length() > maxWidth) {
						listText = obj.toString().substring(0, maxWidth);
					} else {
						listText = obj.toString();
					}

					if ((i == getSelectedIndex()) && isFocused()) {
						getIO().writeInverseText(padToWidth(listText), getPositionLine() + j, getPositionColumn());
						setCursor(getPositionLine() + j, getPositionColumn());
					}
					else {
						getIO().clearLine(getPositionLine() + j, getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
						getIO().writeText(listText, getPositionLine() + j, getPositionColumn());
					}
				}
			} else {
				getIO().clearLine(getPositionLine() + j, getPositionColumn(), TerminalIOInterface.ORIENTATION_HORIZONTAL, getWidth());
			}
		}
		
	}



	private String padToWidth(String s) {
		String result = s;
		while(result.length()<getWidth())
			result += ' ';
		return result;
	}    
    
    public boolean isPositionCursorVisible() {
		return false;
	}



	public void setPositionCursorVisible(boolean cursorVisible) {
		if(cursorVisible) 
			throw new IllegalArgumentException("Position cursor cannot be visible in MenuWidget.");
		super.setPositionCursorVisible(cursorVisible);
	}
}
