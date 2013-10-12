/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol;

import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public abstract class Request {
    private String command;
    protected String prefix;

    public Request(String command) {
        this.command = command;
        this.prefix= null;
    }
    
    public void setPrefix(String s) {
        this.prefix= s;
    }
    
    protected String paramAt(int i) {
        String[] parts= this.command.split(" ");
        if (i >= parts.length) {
            return null;
        }
        
        return parts[i];
    }
    
    public abstract Response process(SessionStore store);

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.command != null ? this.command.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Request other = (Request) obj;
        if ((this.command == null) ? (other.command != null) : !this.command.equals(other.command)) {
            return false;
        }
        return true;
    }

    protected String getCommand() {
        return this.command;
    }
}
