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
    
    /** This is a special char for separating the (optional) ip prefix from the
     * unique generated id.
     */
    final static char PREFIX_SEPARATOR=';';
    
    public Identifier(String prefix) {
        this.prefix= Objects.requireNonNull(prefix);
    }
    
    protected String prefix() {
        return this.prefix;
    }
    
    @Override
    public String toString() {
        return this.prefix + PREFIX_SEPARATOR + this.uniqid();
    }

    /** Extracts the unique generated uniqid part of a identifier string. 
     * @see #uniqid() 
     */
    protected final static String uniquePartOf(String inIdentifier) {
        int index = inIdentifier.indexOf(PREFIX_SEPARATOR);

        if (index == -1) {
            return inIdentifier; // no separator found
        } else {
            return inIdentifier.substring(index + 1);
        }
    }
    
    /** Extracts the prefix part of a identifier string. 
     * @see #prefix() 
     */
    protected final static String prefixPartOf(String inIdentifier) {
        int index = inIdentifier.indexOf(PREFIX_SEPARATOR);

        if (index == -1) {
            return ""; // no separator found
        } else {
            return inIdentifier.substring(0, index);
        }
    }
    
    protected abstract String uniqid();
}
