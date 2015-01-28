/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maths22.cs162.enigma;

/**
 *
 * @author maths22
 */
public class Enigma {
    private final Rotor rot1;
    private final Rotor rot2;
    private final Rotor rot3;
    private final Plugboard pb;
    private final Reflector refl;
    
    public Enigma(String plugboard, String[] rotors, int[] ring, String reflector){
        rot1 = Rotor.generateRotor(rotors[0], ring[0]);
        rot2 = Rotor.generateRotor(rotors[1], ring[1]);
        rot3 = Rotor.generateRotor(rotors[2], ring[2]);
        pb = new Plugboard(plugboard);
        refl = Reflector.generateReflector(reflector);
        pb.setNextTransfomer(rot3);
        rot3.setNextTransfomer(rot2);
        rot2.setNextTransfomer(rot1);
        rot1.setNextTransfomer(refl);   
    }
    
    public void setMsgKey(String key){
        rot1.setOffset(key.charAt(0)-'A');
        rot2.setOffset(key.charAt(1)-'A');
        rot3.setOffset(key.charAt(2)-'A');
    }
    
    public String encryptStr(String enc){
        StringBuilder ret = new StringBuilder();
        enc = enc.replaceAll("[^a-zA-Z]", "");
        enc = enc.toUpperCase();
        for (char ch: enc.toCharArray()) {
            char newch = (char) (pb.transform(ch-'A') + 'A');
            ret.append(newch);
            System.out.println();
        }
        return ret.toString().replaceAll("(.{5})(?!$)", "$1 ");
    }
    
    public static void main(String[] argv){
        Enigma eng = new Enigma(
                "AM FI NV PS TU WZ",
                new String[] {"II","I","III"},
                new int[] {24,13,22},
                "A");
        eng.setMsgKey("ABL");
        System.out.println(eng.encryptStr("GCDSE AHUGW TQGRK VLFGX UCALX VYMIG MMNMF DXTGN VHVRM MEVOU YFZSL RHDRR XFJWC FHUHM UNZEF RDISI KBGPM YVXUZ "));
    }
    
}
