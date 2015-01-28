package com.maths22.cs162.rsa;

import java.math.BigInteger;
import java.io.Serializable;

public class RSAKey implements Serializable{
	private final BigInteger n;
	private final BigInteger exp;
	
	public RSAKey(BigInteger n, BigInteger exp){
		this.n = n;
		this.exp = exp;
	}
	
	public BigInteger getN(){
		return this.n;
	}
	
	public BigInteger getExp(){
		return this.exp;
	}
	
}
