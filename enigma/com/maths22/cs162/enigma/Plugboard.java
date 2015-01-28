package com.maths22.cs162.enigma;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class Plugboard implements Transformer {
    private Transformer nextT;
    private final BidiMap<Integer,Integer> charMap;
    
    protected Plugboard(String in) {
        charMap = new DualHashBidiMap<>();
        
        String[] splitStr = in.split(" ");
        
        for (String pair: splitStr) {
            if(pair.length()>1){
                charMap.put(pair.charAt(0)-'A', pair.charAt(1)-'A');
                charMap.put(pair.charAt(1)-'A', pair.charAt(0)-'A');
            }
        }
    }

    @Override
    public void setNextTransfomer(Transformer t) {
        this.nextT = t;
    }

    @Override
    public int transform(int c) {
        nextT.advance();
        int ret = charMap.getOrDefault(c,c);
        ret = nextT.transform(ret);
        ret = charMap.inverseBidiMap().getOrDefault(ret,ret);
        
        return ret;
    }
    
    @Override
    public void advance() {
    }
    
}
