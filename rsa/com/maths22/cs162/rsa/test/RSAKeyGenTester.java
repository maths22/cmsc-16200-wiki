package com.maths22.cs162.rsa.test;

import com.maths22.cs162.rsa.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class RSAKeyGenTester{
	public static void main(String[] argv) throws IOException, FileNotFoundException {
		RSAKeyGen key = new RSAKeyGen();
		ObjectOutputStream pub = new ObjectOutputStream(new FileOutputStream("pubKey.key"));
		ObjectOutputStream priv = new ObjectOutputStream(new FileOutputStream("privKey.key"));
		pub.writeObject(key.getPublicKey());
		priv.writeObject(key.getPrivateKey());
	}
}
