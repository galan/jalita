/***********************************************************************
 * 
 * This software is published under the terms of the LGPL
 * License version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * 
 *********************************************************************** 
 *
 * Author:   	  Daniel "tentacle" Galán y Martins
 * Creation date: 05.08.2004 - 00:59:57
 *  
 * Revision:      $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2004/09/24 22:35:41 $
 * 
 * $Log: LoginAutomation.java,v $
 * Revision 1.2  2004/09/24 22:35:41  danielgalan
 * extented examples a liite bit
 *
 * Revision 1.1  2004/08/06 00:55:33  danielgalan
 * prepare release
 *
 **********************************************************************/
package net.sf.jalita.examples.login;

import net.sf.jalita.ui.automation.FormAutomationSet;



/**
 * Example: Simple Automation, which simulates a simple login ..
 * 
 * @author  Daniel "tentacle" Galán y Martins
 * @version $Revision: 1.2 $
 */
public class LoginAutomation extends FormAutomationSet {

    //--------------------------------------------------------------------------
    // constants
    //--------------------------------------------------------------------------

    public final static int STATE_LOGIN = 1;

    public final static int ACTION_FINISHED = 1;
    public final static int ACTION_LOGIN = 2;
    public final static int ACTION_RESET = 3;



    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // class methods
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // local classes
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    private LoginForm loginForm;



    //--------------------------------------------------------------------------
    // constructors
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // private & protected methods
    //--------------------------------------------------------------------------

    protected void initAutomationSet() {
        loginForm = new LoginForm(this);
        addForm(STATE_LOGIN, loginForm);
        setInitState(STATE_LOGIN);
    }
    


    private void doActionLogin() {
        showWaitScreen(" processing login..");
        try {
            // simulate request time to eg database
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
        }
        
        
	    setState(STATE_LOGIN);
    }



    private void doActionReset() {
        loginForm.resetFields();
	    setState(STATE_LOGIN);
    }



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    public void doAction(int action) {
        switch (action) {
    	case ACTION_FINISHED:
    	    setState(STATE_FINISHED);
    	    break;
    	case ACTION_LOGIN:
    	    doActionLogin();
    	    break;
		case ACTION_RESET:
		    doActionReset();
		    break;
    	}
    }



    public String toString() {
        return "LoginAutomation";
    }



    public void finish() {
    }

}
