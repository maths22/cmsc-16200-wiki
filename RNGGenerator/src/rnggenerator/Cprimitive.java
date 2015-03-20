/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rnggenerator;

/**
 *
 * @author maths22
 */
public class Cprimitive extends Ctype {
    private String type;

    Cprimitive(String type) {
        super();
        type = type;
    }

    @Override
    public String getDeclaration() {
        if(!isPlural()){
            return type + " " + getName() + ";";
        } else {
            return type + "* " + getName() + ";";
        }
    }

    @Override
    public String getParseCode(String parent) {
        if(!isPlural()){
            return "";
            //return type + " " + getName() + ";";
        } else {
            return "";
            //return type + "* " + getName() + ";";
        }
    }
    
    @Override
    public String getType() {
        if(!isPlural()){
            return type;
        } else {
            return type + "*";
        }
    }
    
    
}
