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
 * Creation date: 06.05.2003
 *  
 * Revision:      $Revision: 1.1 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2004/07/26 21:40:27 $
 * 
 * $Log: ListSelectionEvent.java,v $
 * Revision 1.1  2004/07/26 21:40:27  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.ui.widgets;



/**
 * This event descibes which value the new selection has
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.1 $
 */
public class ListSelectionEvent {

    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** The selected object */
    private Object selectedObject;

    /** The index of the current selected entry */
    private int selectedIndex;

    /** The triggerer of this event */
    private ListWidget source;



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a ListSelectionEvent object */
    public ListSelectionEvent(ListWidget source) {
        this.source = source;
        selectedObject = source.getSelected();
        selectedIndex = source.getSelectedIndex();

    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** Returns the triggerer of this event */
    public ListWidget getSource() {
        return source;
    }



    /** Returns the selected object */
    public Object getSelectedObject() {
        return selectedObject;
    }



    /** Returns the index of the selected object */
    public int getSelectedIndex() {
        return selectedIndex;
    }

}
