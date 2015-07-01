/***********************************************************************
 * This software is published under the terms of the LGPL
 * License version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Author: Daniel Galán y Martins
 * Creation date: 05.08.2004 - 01:05:05
 * Revision: $Revision: 1.4 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:58 $
 * $Log: LoginForm.java,v $
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
 * @author Daniel Galán y Martins
 * @version $Revision: 1.4 $
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
	private LabelWidget labelWrongLogin;
	private LabelWidget labelHint;

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

	@Override
	public void initWidgets() {
		textName = new TextFieldWidget(this, "", 3, 10, 8);
		textPass = new PasswordFieldWidget(this, "", 4, 10, 8);

		labelName = new LabelWidget(this, "Name:", 3, 2);
		labelPass = new LabelWidget(this, "Pass:", 4, 2);
		labelWrongLogin = new LabelWidget(this, "", 10, 2);
		labelHint = new LabelWidget(this, "Try test/test", 12, 2);

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
		addWidget(labelWrongLogin);
		addWidget(labelHint);
	}


	public void buttonOkActionPerfomed(TerminalEvent e) {
		owner.doAction(LoginAutomation.ACTION_LOGIN);
	}


	public void buttonResetActionPerfomed(TerminalEvent e) {
		owner.doAction(LoginAutomation.ACTION_RESET);

		// resetFields();
		// -> you could of course call such methods directly in the form,
		// this is just to demonstrate the state's and action's in the automation.
		// you should seperate logic and ui later that way
	}


	public void setLoginWrong(boolean wrongInput) {
		if (wrongInput) {
			labelWrongLogin.setText("Wrong Name/Passw.!");
		}
		else {
			labelWrongLogin.setText("");
		}
	}


	public String getUsername() {
		return textName.getText();
	}


	public String getPassword() {
		return textPass.getText();
	}


	@Override
	public void processBarcodeReceived(TerminalEvent e) {
	}


	@Override
	public void processKeyPressed(TerminalEvent e) {
	}


	@Override
	public void formLeft() {
	}


	@Override
	public void formEntered() {
	}

}
