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
    
    private final static int PREFIX_CHARS = 8;
    
    /** The prefix is defined to be always the first 8 chars.
     * Keep this magic here if we want to change it.
     * @return the prefix from the identifier.
     */
    protected static String prefixFrom(String identifier) {
        // TODO IPv6
        return identifier.substring(0, PREFIX_CHARS);
    }
    
    /** The prefix is defined to be always the first 8 chars.
     * Keep this magic here if we want to change it.
     * @return the suffix from the identifier.
     */
    protected static String suffixFrom(String identifier) {
        return identifier.substring(PREFIX_CHARS);
    }    
    
    @Override
    public String toString() {
        return this.prefix + this.uniqid();
    }
    
    protected abstract String uniqid();
}
