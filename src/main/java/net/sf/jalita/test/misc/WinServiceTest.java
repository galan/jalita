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

import java.io.*;



/**
 * Klasse zum Testen der Anbieter die Java-Anwendungen als Windows-Dienst
 * laufen lassen.
 *
 * @author  Daniel Galán y Martins [DG]
 * @version 0.1
 */

public class WinServiceTest {

    public static final int STOP = 1;
    public static final int START = 2;

    public static boolean runFlag = true;

    /** Creates a WinServiceTest object */
    public WinServiceTest(int signal) {
        File f = new File("D:\\temp\\_winservicetest\\signal.txt");

        if (signal == STOP) {
            f.delete();
            try {
                FileWriter fw = new FileWriter(f);
                fw.write("stop");
                fw.flush();
            }
            catch (IOException ex) {
            }
            runFlag = false;
        }

        else if (signal == START) {
            f.delete();
            try {
                FileWriter fw = new FileWriter(f);

                while (runFlag) {
                    fw.write("start\r\n");
                    fw.flush();
                    try {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException ex) {
                    }
                }
            }
            catch (IOException ex) {
            }

        }
    }



    public static void main(String args[]) {
        new WinServiceTest(START);
        System.out.println("start servcie");
    }



    public static void stopApplication() {
        new WinServiceTest(STOP);
        System.out.println("stop service");
    }

} // End of class WinServiceTest
