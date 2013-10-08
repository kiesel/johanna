/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author kiesel
 */
public abstract class AbstractSession {
    public static final int DEFAULT_TTL = 3600;
    protected static final Logger LOG = Logger.getLogger(AbstractSession.class.getName());
    protected Date expiryDate;
    protected Identifier id;
    protected int ttl;

    public AbstractSession(Identifier id) {
        this(id, DEFAULT_TTL);
    }

    public AbstractSession(Identifier id, int ttl) {
        this.id = Objects.requireNonNull(id);
        this.setTTL(ttl);
        this.touch();
    }

    public void expire() {
        this.expiryDate = new Date(new Date().getTime() - 1);
    }

    public Date expiryDate() {
        return this.expiryDate;
    }

    public String getId() {
        return this.id.toString();
    }

    public final int getTTL() {
        return this.ttl;
    }

    public boolean hasExpired() {
        return this.hasExpired(new Date());
    }

    public boolean hasExpired(Date ref) {
        return this.expiryDate().before(ref);
    }

    public final void setTTL(int ttl) {
        this.ttl = ttl;
    }

    public int setTimeout(final int ttl) {
        this.touch();
        int old = this.getTTL();
        this.setTTL(ttl);
        return old;
    }

    public boolean terminateIfExpired() {
        if (!this.hasExpired()) {
            return false;
        }
        // Cleanup
        this.terminate();
        return true;
    }

    protected final void touch() {
        this.expiryDate = new Date(new Date().getTime() + (this.getTTL() * 1000));
    }

    public abstract void putValue(String k, String v);
    
    public abstract String getValue(String k);
    
    public abstract boolean removeValue(String k);
    
    public abstract boolean hasValue(String k);

    public abstract Set<String> keys();

    protected abstract void terminate();
}
