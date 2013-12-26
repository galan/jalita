package net.sf.jalita.test.misc;

/*
 * Copyright (c) 2002
 * IL Internet Logistics AG
 * Hufnerstrasse 51
 * 22305 Hamburg, Germany
 *
 -------------------------------------------------------------------------------
                              Revision History
 -------------------------------------------------------------------------------
 Rev    Date        Who  What
 -------------------------------------------------------------------------------
 0.1    03.05.2003  DG   Start der Kodierung
 -------------------------------------------------------------------------------
 */

import java.util.Vector;
import net.sf.jalita.ui.widgets.*;
import net.sf.jalita.ui.automation.FormAutomationSet;
import net.sf.jalita.io.TerminalEvent;

import net.sf.jalita.ui.forms.*;



/**
 * Form zum Testen
 *
 * @author  Daniel Galán y Martins [DG]
 * @version 0.1
 */

public class TestForm1 extends BasicForm {

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
    // constructors
    //--------------------------------------------------------------------------

    public TestForm1(FormAutomationSet owner) {
        super(owner, " TestForm1");
    }



    //--------------------------------------------------------------------------
    // private & protected methods
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    public void initWidgets() {
        text01 = new TextFieldWidget(this, "Feld 1", 3, 10, 8);
        pass01 = new PasswordFieldWidget(this, "secret", 4, 10, 8);

        label01 = new LabelWidget(this, "Label:", 3, 2);
        label02 = new LabelWidget(this, "Boing:", 4, 2);

        button01 = new ButtonWidget(this, "Btn1", 6, 2, 8);
        button02 = new ButtonWidget(this, "Btn2", 6, 11, 8);

        button01.addButtonListener(new ButtonListener() {
            public void actionPerformed(TerminalEvent e) {
                button01ActionPerfomed(e);
            }
        });

        Vector v = new Vector(10);

        v.add("äöüÄÖÜß");
        v.add("Hello");
        v.add("Hell");
        v.add("Otaku");
        v.add("FooBar");
        v.add("Chaitee");
        v.add("BernieUndErt");
        v.add("GabelstapelfahrerKlaus");
        v.add("BOFH");
        v.add("WTF");
        v.add("RTFM");

        list01 = new ListWidget(this, 10, 2, 18, 5, v);

        key01 = new KeyLabelWidget(this, "vielleicht?", TerminalEvent.KEY_F02, 8, 2, 8);
        key02 = new KeyLabelWidget(this, "jo", TerminalEvent.KEY_F09, 8, 11, 8);

        format01 = new FormatFieldWidget(this, 12.3, 16, 2, 5, 2);
        //format02 = new FormatFieldWidget(this, 17, 2, 7, 0);

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
        owner.doAction(TestAutomation.ACTION_FINISHED);
        e.consume();
    }



    public void key01ActionPerfomed(TerminalEvent e) {
        System.out.println("key01ActionPerfomed");
        try {
            format01.setNumber(123456789.67890d);
        }
        catch (FormatFieldRangeException ex) {
            System.err.println("Value " + ex.getValue() + " to big");
        }
        e.consume();
    }



    public void key02ActionPerfomed(TerminalEvent e) {
        System.out.println("key02ActionPerfomed");
        System.out.println("format01: [" + format01.getNumber() + "]");
        //format01.setNumber(123456789.0d);
        e.consume();
    }



    //--------------------------------------------------------------------------
    // implementation of the interface xxx
    //--------------------------------------------------------------------------


    public void processBarcodeReceived(TerminalEvent e) {
        System.out.println("Barcode received in TestForm1 -> Barcode [" + e.getBarcode() + "]");
    }



    public void processKeyPressed(TerminalEvent e) {
        if (e.getKey() == TerminalEvent.KEY_F01) {
            button01ActionPerfomed(e);
        }

        System.out.println("Keypress received in TestForm1");
    }



    public void formLeft() {
        // do nothing
    }



    public void formEntered() {
        // do nothing
    }

}
