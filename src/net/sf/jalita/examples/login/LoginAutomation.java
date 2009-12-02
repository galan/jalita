/***********************************************************************
 * This software is published under the terms of the LGPL
 * License version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Author: Daniel Galán y Martins
 * Creation date: 05.08.2004 - 00:59:57
 * Revision: $Revision: 1.4 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:58 $
 * $Log: LoginAutomation.java,v $
 * Revision 1.4  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.3 2004/12/05 17:51:36 danielgalan
 * extended the examples
 * Revision 1.2 2004/09/24 22:35:41 danielgalan
 * extented examples a liite bit
 * Revision 1.1 2004/08/06 00:55:33 danielgalan
 * prepare release
 **********************************************************************/
package net.sf.jalita.examples.login;

import net.sf.jalita.ui.automation.FormAutomationSet;



/**
 * Example: Simple Automation, which simulates a simple login ..
 * 
 * @author Daniel Galán y Martins
 * @version $Revision: 1.4 $
 */
public class LoginAutomation extends FormAutomationSet {

	//--------------------------------------------------------------------------
	// constants
	//--------------------------------------------------------------------------

	public final static int STATE_LOGIN = 1;
	public final static int STATE_MAIN = 2;

	public final static int ACTION_FINISHED = 1;
	public final static int ACTION_LOGIN = 2;
	public final static int ACTION_RESET = 3;
	public final static int ACTION_LOGOUT = 4;

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
	private MainForm mainForm;


	//--------------------------------------------------------------------------
	// constructors
	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// private & protected methods
	//--------------------------------------------------------------------------

	@Override
	protected void initAutomationSet() {
		loginForm = new LoginForm(this);
		mainForm = new MainForm(this);
		addForm(STATE_LOGIN, loginForm);
		addForm(STATE_MAIN, mainForm);
		setInitState(STATE_LOGIN);
	}


	private void doActionLogin() {
		showWaitScreen(" processing login..");
		try {
			// simulate request time to eg database
			Thread.sleep(2000);
			if (loginForm.getUsername().equalsIgnoreCase("test") && loginForm.getPassword().equals("test")) {
				loginForm.resetFields();
				loginForm.setLoginWrong(false);
				setState(STATE_MAIN);
				return;
			}
		}
		catch (InterruptedException e) {
		}
		loginForm.setLoginWrong(true);
		setState(STATE_LOGIN);
	}


	private void doActionReset() {
		loginForm.resetFields();
		setState(STATE_LOGIN);
	}


	private void doActionLogout() {
		setState(STATE_LOGIN);
	}


	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	@Override
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
			case ACTION_LOGOUT:
				doActionLogout();
				break;
		}
	}


	@Override
	public String toString() {
		return "LoginAutomation";
	}


	@Override
	public void finish() {
	}

}
