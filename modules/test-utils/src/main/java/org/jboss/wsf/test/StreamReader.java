package org.jboss.wsf.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public final class StreamReader implements Runnable {
    InputStream is;
    OutputStream out;

    StreamReader(InputStream is, OutputStream os) {
        this.is = is;
        this.out = os;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            String line = null;
            while ((line = br.readLine()) != null) {
                writer.write(line);

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}