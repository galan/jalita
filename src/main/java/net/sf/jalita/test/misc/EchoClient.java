package net.sf.jalita.test.misc;

import java.net.*;
import java.io.*;



public class EchoClient {

    public static void main(String[] args) {

        String hostname = "localhost";

        if (args.length > 0) {
            hostname = args[0];
        }

        PrintWriter out = null;
        BufferedReader networkIn = null;
        BufferedReader userIn = null;

        try {
            Socket theSocket = new Socket(hostname, 7);
            networkIn = new BufferedReader(new InputStreamReader(theSocket.getInputStream()));
            userIn = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(theSocket.getOutputStream());
            System.out.println("Connected to echo server");
            System.out.println(networkIn.readLine());

            while (true) {
                String theLine = userIn.readLine();
                if (theLine.equals(".")) {
                    break;
                }
                out.println(theLine);
                out.flush();
                System.out.println(networkIn.readLine());
            }

        } // end try
        catch (IOException e) {
            System.err.println(e);
        }
        finally {
            try {
                if (networkIn != null) {
                    networkIn.close();
                }
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {}
        }

    } // end main

}
