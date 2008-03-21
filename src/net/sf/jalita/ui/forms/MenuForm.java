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
 * Creation date: 21.03.2008
 *  
 * Revision:      $Revision: 1.1 $
 * Checked in by: $Author: ilgian $
 * Last modified: $Date: 2008/03/21 14:02:27 $
 * 
 * $Log: MenuForm.java,v $
 * Revision 1.1  2008/03/21 14:02:27  ilgian
 * New Form
 *
 *
 **********************************************************************/
package net.sf.jalita.ui.forms;

import java.util.Vector;

import net.sf.jalita.ui.widgets.MenuWidget;

import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.ui.automation.FormAutomationSet;

/**
 * Basic Menu Form
 *
 * @author  Gianluca Sartori
 * @version $Revision: 1.1 $
 */
public abstract class MenuForm extends BasicForm {

	protected MenuWidget menu;

	
	public MenuForm(FormAutomationSet owner, String headerText) {
        super(owner, headerText);
    }

    public MenuForm(FormAutomationSet owner) {
        super(owner);
    }

   public void initWidgets() {
	   Vector v = getMenuList();
	   //TODO: don't like fixed width and height...
	   menu = new MenuWidget(this, 2, 1, 20, 7, v, false);
       addWidget(menu);
    }

    public void processBarcodeReceived(TerminalEvent e) {
    }

    public void processKeyPressed(TerminalEvent e) {
    	if(e.getKey() == TerminalEvent.KEY_ENTER){
    		owner.doAction(menu.getSelectedIndex()+1);
    	}
    }

    public void formLeft() {
    }

    public void formEntered() {
    }

    public abstract Vector getMenuList();



}
