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
 * Creation date: 03.05.2003
 *  
 * Revision:      $Revision: 1.4 $
 * Checked in by: $Author: ilgian $
 * Last modified: $Date: 2008/12/02 13:13:32 $
 * 
 * $Log: BasicForm.java,v $
 * Revision 1.4  2008/12/02 13:13:32  ilgian
 * Added getHeader method
 *
 * Revision 1.3  2008/10/09 12:30:20  ilgian
 * Added facilities to handle form's title
 *
 * Revision 1.2  2005/05/23 18:10:19  danielgalan
 * some cleaning and removing some cycles (not all removed yet)
 *
 * Revision 1.1  2004/07/26 21:40:28  danielgalan
 * Jalita initial cvs commit :)
 *
 **********************************************************************/
package net.sf.jalita.ui.forms;

import java.util.Vector;
import net.sf.jalita.io.TerminalEventListener;
import net.sf.jalita.ui.widgets.BasicWidget;
import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.io.TerminalIOInterface;
import net.sf.jalita.ui.automation.FormAutomationSet;
import java.util.Enumeration;
import java.io.IOException;
import net.sf.jalita.ui.widgets.HeaderWidget;
import net.sf.jalita.server.SessionObject;
import net.sf.jalita.ui.widgets.KeyLabelWidget;
import net.sf.jalita.util.Configuration;

import java.util.Hashtable;
import org.apache.log4j.Logger;



/**
 * Skeletal structure for Form's
 *
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.4 $
 */

public abstract class BasicForm implements TerminalEventListener {

    //--------------------------------------------------------------------------
    // constants
    //--------------------------------------------------------------------------

    /** Carriage Return / LineFeed */
    protected final static String CRLF = "\r\n";

    

    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    /** log4j reference */
    public final static Logger log = Logger.getLogger(Configuration.class);


    
    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    /** FormAutomationSet, this Forms belongs to */
    protected FormAutomationSet owner;

    /** All widgets of this Form. Order is simultaneously tab-order. */
    private Vector widgets = new Vector(5, 2);

    /** Widget with current focus, if there is no focusable widget null. */
    private BasicWidget focusedWidget;

    /** Header row */
    private HeaderWidget header;

    /** Before the next drawing should the screen be cleared? */
    private boolean clearScreen = true;

    /** All KeylabelListener. Key: TerminalEvent-Constant from the HotKeys / Object: KeyLabelWidget */
    private Hashtable keyLabels = new Hashtable();




    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    /** Creates a new BasicForm-Object */
    public BasicForm(FormAutomationSet owner, String headerText) {
        log.debug("Creating instance of BasicForm");
        this.owner = owner;

        initWidgets();

        setTitle(headerText);

        focusFirstPossibleWidget();
    }



    /** Creates a new BasicForm-Object */
    public BasicForm(FormAutomationSet owner) {
        log.debug("Creating instance of BasicForm");
        this.owner = owner;

        initWidgets();

        focusFirstPossibleWidget();
    }



    //--------------------------------------------------------------------------
    // private & protected methods
    //--------------------------------------------------------------------------

    /** Sets all widgets in the Form to dirty, so that all widgets will be repainted on next call of paint */
    private void setWidgetsDirty() {
        Enumeration enumWidgets = widgets.elements();
        while (enumWidgets.hasMoreElements()) {
            BasicWidget bw = (BasicWidget)enumWidgets.nextElement();
            bw.setDirty(true);
        }
    }



    /** Returns the Session object */
    protected SessionObject getSessionObject() {
        return owner.getOwner().getSessionObject();
    }



    /** Sets the Session object */
    protected void setSessionObject(SessionObject obj) {
        owner.getOwner().setSessionObject(obj);
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    /** Returns the owner of this Form */
    public FormAutomationSet getOwner() {
        return owner;
    }


    /** Returns the TerminalInterface */
    public TerminalIOInterface getIO() {
        return owner.getIO();
    }



    /** Provokes the Form to a whole repaint on next call of paint */
    public void markFormDirty() {
        clearScreen = true;
        setWidgetsDirty();
    }



    /** Paints the form */
    public void paint(boolean fullRedraw) throws IOException {
        if (fullRedraw) {
            markFormDirty();
        }
        paint();
    }



    /** Returns the current focused widget, on no focused widget null. */
    public BasicWidget getFocusedWidget() {
        return focusedWidget;
    }



    /** Focuses the first possible widget and returns it */
    public BasicWidget focusFirstPossibleWidget() {
        Enumeration enumWidgets = widgets.elements();

        if (focusedWidget != null) {
            focusedWidget.focusLeft();
        }

        focusedWidget = null;
        while (enumWidgets.hasMoreElements()) {
            BasicWidget bw = (BasicWidget)enumWidgets.nextElement();
            if (bw.isFocusable()) {
                focusedWidget = bw;
                focusedWidget.focusEntered();
                break;
            }
        }

        return focusedWidget;
    }



    /** Focuses the last possible widget an returns it */
    public BasicWidget focusLastPossibleWidget() {
        if (focusedWidget != null) {
            focusedWidget.focusLeft();
        }

        focusedWidget = null;
        for (int i = widgets.size(); i > 0; i--) {
            BasicWidget bw = (BasicWidget)widgets.elementAt(i-1);
            if (bw.isFocusable()) {
                focusedWidget = bw;
                focusedWidget.focusEntered();
                break;
            }
        }

        return focusedWidget;
    }



    /** Focuses the next possible widget and returns it */
    public BasicWidget focusNextPossibleWidget() {
        if (focusedWidget == null) {
            return focusFirstPossibleWidget();
        }
        else {
            focusedWidget.focusLeft();
        }

        int indexFocused = widgets.indexOf(focusedWidget);

        for (int i = indexFocused + 1; i < widgets.size(); i++) {
            BasicWidget bw = (BasicWidget)widgets.elementAt(i);
            if (bw.isFocusable()) {
                focusedWidget = bw;
                focusedWidget.focusEntered();
                return focusedWidget;
            }
        }

        // no one found, take firstpossible (or null)
        return focusFirstPossibleWidget();
    }



    /** Focuses the previous widget and returns it */
    public BasicWidget focusPreviousPossibleWidget() {
        if (focusedWidget == null) {
            return focusFirstPossibleWidget();
        }
        else {
            focusedWidget.focusLeft();
        }

        int indexFocused = widgets.indexOf(focusedWidget);

        for (int i = indexFocused - 1; i >= 0; i--) {
            BasicWidget bw = (BasicWidget)widgets.elementAt(i);
            if (bw.isFocusable()) {
                focusedWidget = bw;
                focusedWidget.focusEntered();
                return focusedWidget;
            }
        }

        // noe one found, take first possible (or null)
        return focusLastPossibleWidget();
    }



    /** Paint-routine to draw the dirty widgets */
    public void paint() throws IOException {
        // bildschirm falls benötigt löschen
        if (clearScreen) {
            clearScreen = false;
            getIO().clearScreen();
        }


        // paint widgets
        Enumeration enumWidgets = widgets.elements();
        while (enumWidgets.hasMoreElements()) {
            BasicWidget bw = (BasicWidget)enumWidgets.nextElement();
            if (bw.isDirty()) {
                bw.redraw();
            }
        }

        // set cursor
        BasicWidget bw = getFocusedWidget();
        if (bw != null) {
            getIO().cursorMoveAbsolut(bw.getCursor().getLine(), bw.getCursor().getColumn());
        }

        getIO().flush();
    }

    /** Adds and registers a widget to the form */
    public void addWidget(BasicWidget widget) {
        if (widget != null) {
            if (widgets.contains(widget)) {
                widgets.remove(widget);
            }

            widgets.add(widget);


            // Globale KeyListener for the KeyLabelWidgets (aka "return of the ToolbarButtons" ;)
            if ((widget instanceof KeyLabelWidget) && (((KeyLabelWidget)widget).getKey() != TerminalEvent.KEY_UNDEFINED)) {
                if (keyLabels.contains(widget)) {
                    keyLabels.remove(widget);
                }

                keyLabels.put(new Integer(((KeyLabelWidget)widget).getKey()), widget);
            }
        }
    }


    

    /** Queries the widget if it has the focus */
    public boolean hasWidgetFocus(BasicWidget widget) {
        return focusedWidget.equals(widget);
    }



    /** In this method all widgets should be created and registered */
    public abstract void initWidgets();



    /** Catches the scanned barcodes in the form for further evaluation */
    public abstract void processBarcodeReceived(TerminalEvent e);



    /** Catches the pressed key in the form for further evaluation */
    public abstract void processKeyPressed(TerminalEvent e);



    /** Catches the leaving from a Form for further evaluation */
    public abstract void formLeft();



    /** Catches the entering in a Form for further evaluation */
    public abstract void formEntered();



    //--------------------------------------------------------------------------
    // implementation of the interface TerminalKeyListener
    //--------------------------------------------------------------------------

    /** Catches a recived barcode */
    public final void barcodeReceived(TerminalEvent e) {
        log.info("Barcode received in BasicForm -> Barcode [" + e.getBarcode() + "]");

        processBarcodeReceived(e);
    }



    /** Catches a recived keypress */
    public final void keyPressed(TerminalEvent e) {
        if (e.isPrintable()) {
            log.info("Keypress received in BasicForm -> Key (printable): [" + e.getKeyAsString() + "]");
        }
        else if (e.isFunctionKey()) {
            log.info("Keypress received in BasicForm -> FunktionKey: [" + java.lang.Math.abs(e.getKey()) + "]");
        }
        else {
            log.info("Keypress received in BasicForm -> Key (non-printable): [" + e.getKey() + "]");
        }

        if (e.getKey() == TerminalEvent.KEY_TAB) {
            focusNextPossibleWidget();
            return;
        }
        else if (e.getKey() == TerminalEvent.KEY_TAB_BACK) {
            focusPreviousPossibleWidget();
            return;
        }
        
        processKeyPressed(e);

        if (!e.isConsumed()) {
            Integer intKeyLabel = new Integer(e.getKey());

            if (keyLabels.containsKey(intKeyLabel)) {
                KeyLabelWidget klw = (KeyLabelWidget)keyLabels.get(intKeyLabel);
                klw.fireActionPerformed(e);
            }

            else if (focusedWidget != null) {
                focusedWidget.keyPressed(e);
            }
        }
    }

    public String getTitle(){
    	if(header != null)
    		return header.getText();
    	else 
    		return null;
    }
    
    public void setTitle(String title){
    	if(header != null){
    		header.setText(title);
    	} else {
    		header = new HeaderWidget(this, title);
    		addWidget(header);
    	}
    }
    
    public Enumeration<BasicWidget> widgets(){
    	return widgets.elements();
    }



	public HeaderWidget getHeader() {
		return header;
	}
}
