package com.maths22.cs162.enigma;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class Reflector implements Transformer {

    private final BidiMap<Integer, Integer> charMap;
    private final int offset;

    private Reflector(String mapping) {
        charMap = new DualHashBidiMap<>();

        char[] mapArr = mapping.toCharArray();
        for (int i = 0; i < Rotor.CHAR_COUNT; i++) {
            charMap.put(i, mapArr[i] - 'A');
        }
        offset = 0;
    }

    public static Reflector generateReflector(String name) {
        switch (name) {
            case "A":
                return new Reflector("EJMZALYXVBWFCRQUONTSPIKHGD");
            case "B":
                return new Reflector("YRUHQSLDPXNGOKMIEBFZCWVJAT");
            case "C":
                return new Reflector("FVPJIAOYEDRZXWGCTKUQSBNMHL");
            default:
                return null;
        }
    }

    @Override
    public void setNextTransfomer(Transformer t) {
    }

    @Override
    public int transform(int c) {
        int ret = charMap.get((c + offset) % Transformer.CHAR_COUNT);
        return ret;
    }

    @Override
    public void advance() {
    }

}
