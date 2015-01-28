package com.maths22.cs162.enigma;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class Rotor implements Transformer {
    private Transformer nextT;
    private final BidiMap<Integer,Integer> charMap;
    private int offset;
    private final int ringSet;
    private final List<Integer> turnover;

    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    public static Rotor generateRotor(String name, int offset){
        switch (name) {
            case "I":
                return new Rotor("EKMFLGDQVZNTOWYHXUSPAIBRCJ","Q",offset);
            case "II":
                return new Rotor("AJDKSIRUXBLHWTMCQGZNPYFVOE","E",offset);
            case "III":
                return new Rotor("BDFHJLCPRTXVZNYEIWGAKMUSQO","V",offset);
            case "IV":
                return new Rotor("ESOVPZJAYQUIRHXLNFTGKDCMWB","J",offset);
            case "V":
                return new Rotor("VZBRGITYUPSDNHLXAWMJQOFECK","Z",offset);
            case "VI":
                return new Rotor("JPGVOUMFYQBENHZRDKASXLICTW","ZM",offset);
            case "VII":
                return new Rotor("NZJHGRCXMYSWBOUFAIVLPEKQDT","ZM",offset);
            case "VIII":
                return new Rotor("FKQHTLXOCBJSPDZRAMEWNIUYGV","ZM",offset);
            default:
                return null;
        }
    }

    private Rotor(String mapping, String flip, int start) {
        charMap = new DualHashBidiMap<>();
        turnover = new ArrayList<>();
        
        char[] mapArr = mapping.toCharArray();
        for (int i = 0; i < Rotor.CHAR_COUNT; i++) {
            charMap.put(i, mapArr[i]-'A');
        }
        
        for (char ch: flip.toCharArray()) {
            turnover.add(ch-'A');
        }
        ringSet = start-1;
        offset = 0;
    }

    @Override
    public void setNextTransfomer(Transformer t) {
        this.nextT = t;
    }
    
    private int pmod(int n, int m){
        return (n < 0) ? (m - (abs(n) % m) ) %m : (n % m);
    }

    @Override
    public int transform(int c) {
        int ret = charMap.get(pmod(c+offset-ringSet,Transformer.CHAR_COUNT));
        ret = nextT.transform(pmod(ret-offset+ringSet,Transformer.CHAR_COUNT));
        ret = charMap.inverseBidiMap().get(pmod(ret+offset-ringSet,Transformer.CHAR_COUNT));
        return pmod(ret-offset+ringSet,Transformer.CHAR_COUNT);
    }
    
    @Override
    public void advance() {
        if(turnover.contains((this.offset)%Transformer.CHAR_COUNT)){
            nextT.advance();
        }
        this.offset = (1+offset)%Transformer.CHAR_COUNT;
    }
    
}
