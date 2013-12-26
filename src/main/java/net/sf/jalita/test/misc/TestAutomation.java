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
 0.1    06.05.2003  DG   Start der Kodierung
 -------------------------------------------------------------------------------
 */

import net.sf.jalita.ui.forms.BasicForm;

import net.sf.jalita.ui.automation.*;



/**
 * Eine implemtentierung FormAutomatenSets zum testen
 *
 * @author  Daniel Galán y Martins [DG]
 * @version 0.1
 */

public class TestAutomation extends FormAutomationSet {

    //--------------------------------------------------------------------------
    // constants
    //--------------------------------------------------------------------------

    public final static int STATE_TEST = 1;

    public final static int ACTION_FINISHED = 1;



    //--------------------------------------------------------------------------
    // class variables
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // class methods
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // instance variables
    //--------------------------------------------------------------------------

    BasicForm form1 ;



    //--------------------------------------------------------------------------
    // public methods
    //--------------------------------------------------------------------------

    public BasicForm getCurrentForm() {
        return form1;
    }



    //--------------------------------------------------------------------------
    // implementation of the abstract methods of interface FormAutomationSet
    //--------------------------------------------------------------------------

    protected void initAutomationSet() {
        form1 = new TestForm1(this);

        addForm(STATE_TEST, form1);

        setInitState(STATE_TEST);
    }




    public void doAction(int state) {
        switch (state) {
            case ACTION_FINISHED:
                // -- aktion was auch immer ausführen --
                setState(STATE_FINISHED);
                break;
        }

    }



    public String toString() {
        return "TestAutomation";
    }



    public void finish() {
        //
    }



} // End of class TestAutomation
