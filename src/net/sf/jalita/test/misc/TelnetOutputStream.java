// FrontEnd Plus GUI for JAD
// DeCompiled : TelnetOutputStream.class

package net.sf.jalita.test.misc;

import java.io.*;



public class TelnetOutputStream extends BufferedOutputStream {

    boolean stickyCRLF;
    boolean seenCR;
    public boolean binaryMode;

    public TelnetOutputStream(OutputStream outputstream, boolean flag) {
        super(outputstream);
        stickyCRLF = false;
        seenCR = false;
        binaryMode = false;
        binaryMode = flag;
    }



    public void setStickyCRLF(boolean flag) {
        stickyCRLF = flag;
    }



    public void write(int i) throws IOException {
        if (binaryMode) {
            super.write(i);
            return;
        }
        if (seenCR) {
            if (i != 10) {
                super.write(0);
            }
            super.write(i);
            if (i != 13) {
                seenCR = false;
            }
        }
        else {
            if (i == 13) {
                if (stickyCRLF) {
                    seenCR = true;
                }
                else {
                    super.write(13);
                    i = 0;
                }
            }
            super.write(i);
        }
    }



    public void write(byte abyte0[], int i, int j) throws IOException {
        if (binaryMode) {
            super.write(abyte0, i, j);
            return;
        } while (--j >= 0) {
            write(abyte0[i++]);
        }
    }
}
