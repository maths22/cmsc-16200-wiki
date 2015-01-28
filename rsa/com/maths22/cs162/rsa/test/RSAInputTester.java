package com.maths22.cs162.rsa.test;


import com.maths22.cs162.rsa.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class RSAInputTester {

    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
        ObjectInputStream key = new ObjectInputStream(new FileInputStream(args[0]));

        try (RSAInputStream secureIn = new RSAInputStream(System.in, (RSAKey) key.readObject())) {
            int b;
            while (-1 != (b = secureIn.read())) {
                System.out.write(b);
            }

            System.out.flush();
            System.out.close();
            secureIn.close();
        }
    }
}
