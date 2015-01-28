package com.maths22.cs162.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSAKeyGen {
	private final BigInteger p;
	private final BigInteger q;
	private final BigInteger n;
	private final BigInteger phi;
	private BigInteger e;
	private final BigInteger d;
	private final SecureRandom rand;
	
	public RSAKeyGen(){
		this.rand = new SecureRandom();
		this.p = BigInteger.probablePrime(1024,rand);	
		this.q = BigInteger.probablePrime(1024,rand);
		this.n = p.multiply(q);
		this.phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		this.e = phi;
		while(!e.gcd(phi).equals(BigInteger.ONE)){
			this.e = BigInteger.probablePrime(32,rand);
		}
		this.d = e.modInverse(phi);
	}
	
	public RSAKey getPrivateKey(){
		return new RSAKey(this.n, this.d);
	}
	
	public RSAKey getPublicKey(){
		return new RSAKey(this.n, this.e);
	}
}
