/***********************************************************************
 * 
 * This software is published under the terms of the LGPL
 * License version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * 
 *********************************************************************** 
 *
 * Author:   	  Daniel "tentacle" Galán y Martins
 * Creation date: 05.08.2004 - 01:05:05
 *  
 * Revision:      $Revision: 1.1 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2004/08/06 00:55:33 $
 * 
 * $Log: LoginForm.java,v $
 * Revision 1.1  2004/08/06 00:55:33  danielgalan
 * prepare release
 *
 **********************************************************************/
package net.sf.jalita.examples.login;

import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.test.misc.TestAutomation;
import net.sf.jalita.ui.automation.FormAutomationSet;
import net.sf.jalita.ui.forms.BasicForm;
import net.sf.jalita.ui.widgets.ButtonListener;
import net.sf.jalita.ui.widgets.ButtonWidget;
import net.sf.jalita.ui.widgets.LabelWidget;
import net.sf.jalita.ui.widgets.PasswordFieldWidget;
import net.sf.jalita.ui.widgets.TextFieldWidget;



/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.1 $
 */
public class LoginForm extends BasicForm {

    //--------------------------------------------------------------------------
    // constants
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // class methods
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    private TextFieldWidget textName;
    private PasswordFieldWidget textPass;

    private LabelWidget labelName;
    private LabelWidget labelPass;

    private ButtonWidget buttonOk;
    private ButtonWidget buttonReset;



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    public LoginForm(FormAutomationSet owner) {
        super(owner, "Login");
    }



    //--------------------------------------------------------------------------
    // private & protected methods
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    public void initWidgets() {
        textName = new TextFieldWidget(this, "", 3, 10, 8);
        textPass = new PasswordFieldWidget(this, "", 4, 10, 8);

        labelName = new LabelWidget(this, "Name:", 3, 2);
        labelPass = new LabelWidget(this, "Pass:", 4, 2);

        buttonOk = new ButtonWidget(this, "OK", 6, 2, 8);
        buttonReset = new ButtonWidget(this, "Reset", 6, 11, 8);
        
        buttonOk.addButtonListener(new ButtonListener() {
            public void actionPerformed(TerminalEvent e) {
                buttonOkActionPerfomed(e);
            }
        });

        addWidget(textName);
        addWidget(textPass);
        addWidget(labelName);
        addWidget(labelPass);
        addWidget(buttonOk);
        addWidget(buttonReset);
    }



    public void buttonOkActionPerfomed(TerminalEvent e) {
        owner.doAction(LoginAutomation.ACTION_LOGIN);
    }



    public void processBarcodeReceived(TerminalEvent e) {
    }



    public void processKeyPressed(TerminalEvent e) {
    }



    public void formLeft() {
    }



    public void formEntered() {
    }

}