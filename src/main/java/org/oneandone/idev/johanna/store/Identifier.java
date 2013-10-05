/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

/**
 *
 * @author kiesel
 */
public abstract class Identifier {
    private String prefix;
    
    public Identifier(String prefix) {
        this.prefix= prefix;
    }
    
    protected String prefix() {
        return this.prefix;
    }
    
    @Override
    public String toString() {
        return this.prefix + this.uniqid();
    }
    
    protected abstract String uniqid();
}
