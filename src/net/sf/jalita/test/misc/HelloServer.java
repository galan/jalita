package net.sf.jalita.test.misc;

import java.net.*;
import java.io.*;

public class HelloServer {

    public HelloServer() {
        try {
            ServerSocket ss = new ServerSocket(604);

            while (true) {
                Socket connection = ss.accept();
                System.out.println("Connection created");
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                //InputStreamReader in = new InputStreamReader(connection.getInputStream());

                out.write("Hello Hell\r\n");
                out.flush();
                connection.close();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public static void main(String[] args) {
        new HelloServer();
    }

}