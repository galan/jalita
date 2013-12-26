package net.sf.jalita.test.misc;

import java.net.*;
import java.io.*;

public class LocalPortScanner {

  public static void main(String[] args) {

    for (int port = 1; port <= 7000; port++) {

      try {
        // the next line will fail and drop into the catch block if
        // there is already a server running on the port
        new ServerSocket(port);
      }
      catch (IOException e) {
        System.out.println("There is a server on port " + port + ".");
      } // end try

    } // end for

  }

}
