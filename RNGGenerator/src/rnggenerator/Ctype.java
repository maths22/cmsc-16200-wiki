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
public abstract class Ctype {
    private boolean plural;
    private String name;
    
    public Ctype(){
        plural = false;
    }
    
    abstract public String getDeclaration();
    abstract public String getParseCode(String parent);
    abstract public String getType();
    
    public void setPlural(boolean state) {
        plural = state;
    }
    
    public boolean isPlural() {
        return plural;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
}
