package net.sf.jalita.test.misc;

import java.io.*;
import java.net.*;


public class HelloClient {

    public HelloClient() {
        try {
            Socket socket = new Socket("127.0.0.1", 604);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(br.readLine());
            socket.close();
        }
        catch (IOException ex) {
        }
    }



    public static void main(String[] args) {
        new HelloClient();
    }

}