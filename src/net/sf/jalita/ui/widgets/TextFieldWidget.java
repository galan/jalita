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
 * $Log: TextFieldWidget.java,v $
 * Revision 1.1  2004/07/26 21:40:27  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.ui.widgets;

import java.io.IOException;
import net.sf.jalita.ui.forms.BasicForm;



/**
 * A simple Textfield
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.1 $
 */
public class TextFieldWidget extends TextWidget {

    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a new TextFieldWidget-Object */
    public TextFieldWidget(BasicForm owner, String text, int posLine, int posColumn, int width) {
        super(owner, text, true, posLine, posColumn, width);
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** Draws the widget */
    public void paint() throws IOException {
        getIO().setUnderlined(true);

        getIO().cursorMoveAbsolut(getPositionLine(), getPositionColumn());
        getIO().writeText(getText());

        for (int i = getTextLength(); i < getWidth(); i++) {
            getIO().writeText(" ");
        }

        getIO().setUnderlined(false);
    }

}
