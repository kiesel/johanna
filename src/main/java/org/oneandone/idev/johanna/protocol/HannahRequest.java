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
public abstract class HannahRequest {
    private String command;

    public HannahRequest(String command) {
        this.command = command;
    }
    
    public abstract HannahResponse execute(SessionStore store);

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
        final HannahRequest other = (HannahRequest) obj;
        if ((this.command == null) ? (other.command != null) : !this.command.equals(other.command)) {
            return false;
        }
        return true;
    }

    protected String getCommand() {
        return this.command;
    }
}
