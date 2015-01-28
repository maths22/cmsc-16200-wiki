package com.maths22.cs162.rsa.test;

import com.maths22.cs162.rsa.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class RSAOutputTester {

    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
        ObjectInputStream key = new ObjectInputStream(new FileInputStream(args[0]));

        try (RSAOutputStream secureOut = new RSAOutputStream(System.out, (RSAKey) key.readObject())) {
            int b=0;
            while (-1 != (b = System.in.read())) {
                secureOut.write(b);
            }
            secureOut.flush();
        }
    }
}
