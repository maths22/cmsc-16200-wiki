package com.maths22.cs162.enigma;

public interface Transformer {
    public final int CHAR_COUNT = 26;
    
    public void setNextTransfomer(Transformer t);
    public int transform(int c);
    public void advance();
}
