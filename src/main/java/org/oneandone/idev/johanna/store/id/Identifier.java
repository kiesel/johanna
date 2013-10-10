/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store.id;

import java.util.Objects;

/**
 *
 * @author kiesel
 */
public abstract class Identifier {
    private final String prefix;
    
    public Identifier(String prefix) {
        this.prefix= Objects.requireNonNull(prefix);
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
