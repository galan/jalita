/***********************************************************************
 * 
 * This software is published under the terms of the LGPL
 * License version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * 
 *********************************************************************** 
 *
 * Author:   	  Daniel "tentacle" Gal�n y Martins
 * Creation date: 05.08.2004 - 01:05:05
 *  
 * Revision:      $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2004/09/24 22:35:41 $
 * 
 * $Log: LoginForm.java,v $
 * Revision 1.2  2004/09/24 22:35:41  danielgalan
 * extented examples a liite bit
 *
 * Revision 1.1  2004/08/06 00:55:33  danielgalan
 * prepare release
 *
 **********************************************************************/
package net.sf.jalita.examples.login;

import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.ui.automation.FormAutomationSet;
import net.sf.jalita.ui.forms.BasicForm;
import net.sf.jalita.ui.widgets.ButtonListener;
import net.sf.jalita.ui.widgets.ButtonWidget;
import net.sf.jalita.ui.widgets.LabelWidget;
import net.sf.jalita.ui.widgets.PasswordFieldWidget;
import net.sf.jalita.ui.widgets.TextFieldWidget;



/**
 * Example: FormLayout and events for Login
 * 
 * @author  Daniel "tentacle" Gal�n y Martins
 * @version $Revision: 1.2 $
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
    
    protected void resetFields() {
        textName.setText("");
        textPass.setText("");
    }



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

        buttonReset.addButtonListener(new ButtonListener() {
            public void actionPerformed(TerminalEvent e) {
                buttonResetActionPerfomed(e);
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



    public void buttonResetActionPerfomed(TerminalEvent e) {
        owner.doAction(LoginAutomation.ACTION_RESET);

        // resetFields();
        // -> you could of course call such methods directly in the form,
        // these is just to demonstrate the state's and action's in the automation.
        // you should seperate logic and ui later that way, you decide
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