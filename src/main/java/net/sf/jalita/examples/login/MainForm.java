/***********************************************************************
 * This software is published under the terms of the LGPL
 * License version 2.1, a copy of which has been included with this
 * distribution in the 'lgpl.txt' file.
 * Author: Daniel Gal·n y Martins
 * Creation date: 05.12.2004 - 18:06:56
 * Revision: $Revision: 1.2 $
 * Checked in by: $Author: danielgalan $
 * Last modified: $Date: 2009/12/02 21:53:58 $
 * $Log: MainForm.java,v $
 * Revision 1.2  2009/12/02 21:53:58  danielgalan
 * naming
 *
 * Revision 1.1 2004/12/05 17:51:36 danielgalan
 * extended the examples
 **********************************************************************/
package net.sf.jalita.examples.login;

import java.util.Vector;

import net.sf.jalita.io.TerminalEvent;
import net.sf.jalita.ui.automation.FormAutomationSet;
import net.sf.jalita.ui.forms.BasicForm;
import net.sf.jalita.ui.widgets.ButtonListener;
import net.sf.jalita.ui.widgets.ButtonWidget;
import net.sf.jalita.ui.widgets.FormatFieldRangeException;
import net.sf.jalita.ui.widgets.FormatFieldWidget;
import net.sf.jalita.ui.widgets.KeyLabelWidget;
import net.sf.jalita.ui.widgets.LabelWidget;
import net.sf.jalita.ui.widgets.ListWidget;
import net.sf.jalita.ui.widgets.PasswordFieldWidget;
import net.sf.jalita.ui.widgets.TextFieldWidget;



/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author Daniel Gal·n y Martins
 * @version $Revision: 1.2 $
 */
public class MainForm extends BasicForm {

	//--------------------------------------------------------------------------
	// constants
	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// class variables
	//--------------------------------------------------------------------------

	private TextFieldWidget text01;
	private PasswordFieldWidget pass01;

	private ButtonWidget button01;
	private ButtonWidget button02;

	private KeyLabelWidget key01;
	private KeyLabelWidget key02;

	private LabelWidget label01;
	private LabelWidget label02;

	private ListWidget list01;

	private FormatFieldWidget format01;
	private FormatFieldWidget format02;


	//--------------------------------------------------------------------------
	// class methods
	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// local classes
	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// instance variables
	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// constructors
	//--------------------------------------------------------------------------

	public MainForm(FormAutomationSet owner) {
		super(owner, "Main (for testing)");
	}


	//--------------------------------------------------------------------------
	// private & protected methods
	//--------------------------------------------------------------------------

	//--------------------------------------------------------------------------
	// public methods
	//--------------------------------------------------------------------------

	@Override
	public void initWidgets() {
		text01 = new TextFieldWidget(this, "Feld 1", 3, 10, 8);
		pass01 = new PasswordFieldWidget(this, "secret", 4, 10, 8);

		label01 = new LabelWidget(this, "Label1:", 3, 2);
		label02 = new LabelWidget(this, "Label2:", 4, 2);

		button01 = new ButtonWidget(this, "logout", 6, 2, 8);
		button02 = new ButtonWidget(this, "Btn2", 6, 11, 8);

		button01.addButtonListener(new ButtonListener() {

			public void actionPerformed(TerminalEvent e) {
				button01ActionPerfomed(e);
			}
		});

		Vector v = new Vector(10);
		v.add("‰ˆ¸ƒ÷‹ﬂ");
		v.add("Hello");
		v.add("Hell");
		v.add("Otaku");
		v.add("FooBar");
		v.add("Chai");
		v.add("Shiva");
		v.add("ThisIsAVeryLongTextWhichIsTruncated");
		v.add("BOFH");
		v.add("WTF");
		v.add("RTFM");

		list01 = new ListWidget(this, 10, 2, 18, 5, v);

		key01 = new KeyLabelWidget(this, "press", TerminalEvent.KEY_F02, 8, 2, 8);
		key02 = new KeyLabelWidget(this, "me", TerminalEvent.KEY_F09, 8, 11, 8);

		format01 = new FormatFieldWidget(this, 12.3, 16, 2, 5, 2);
		format02 = new FormatFieldWidget(this, 17, 2, 7, 0);

		key01.addButtonListener(new ButtonListener() {

			public void actionPerformed(TerminalEvent e) {
				key01ActionPerfomed(e);
			}
		});

		key02.addButtonListener(new ButtonListener() {

			public void actionPerformed(TerminalEvent e) {
				key02ActionPerfomed(e);
			}
		});

		addWidget(text01);
		addWidget(pass01);
		addWidget(label01);
		addWidget(label02);
		addWidget(button01);
		addWidget(button02);
		addWidget(list01);
		addWidget(key01);
		addWidget(key02);
		addWidget(format01);
		addWidget(format02);
	}


	public void button01ActionPerfomed(TerminalEvent e) {
		System.out.println("button01ActionPerfomed");
		owner.doAction(LoginAutomation.ACTION_LOGOUT);
		e.consume();
	}


	public void key01ActionPerfomed(TerminalEvent e) {
		log.info("key01ActionPerfomed");
		try {
			format01.setNumber(123456789.67890d);
		}
		catch (FormatFieldRangeException ex) {
			log.error("Value " + ex.getValue() + " to big");
		}
		e.consume();
	}


	public void key02ActionPerfomed(TerminalEvent e) {
		System.out.println("key02ActionPerfomed");
		System.out.println("format01: [" + format01.getNumber() + "]");
		//format01.setNumber(123456789.0d);
		e.consume();
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
