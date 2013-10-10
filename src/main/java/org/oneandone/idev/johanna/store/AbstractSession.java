/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import org.oneandone.idev.johanna.store.id.Identifier;
import java.util.Objects;
import java.util.logging.Logger;
import org.oneandone.idev.johanna.store.id.IdentifierFactory;

/**
 *
 * @author kiesel
 */
public abstract class AbstractSession implements ISession {
    protected static final Logger LOG = Logger.getLogger(AbstractSession.class.getName());
    public static final int DEFAULT_TTL = 3600;

    protected Identifier id;
    protected int ttl;

    public AbstractSession(Identifier id) {
        this(id, DEFAULT_TTL);
    }

    public AbstractSession(Identifier id, int ttl) {
        this.id = Objects.requireNonNull(id);
        this.setTTL(ttl);
    }

    @Override
    public String getId() {
        return this.id.toString();
    }

    @Override
    public final int getTTL() {
        return this.ttl;
    }

    @Override
    public final void setTTL(int ttl) {
        this.ttl = ttl;
    }

    @Override
    public int setTimeout(final int ttl) {
        this.touch();
        int old = this.getTTL();
        this.setTTL(ttl);
        return old;
    }
    
    protected abstract void touch();

    @Override
    public boolean terminateIfExpired() {
        if (!this.hasExpired()) {
            return false;
        }
        // Cleanup
        this.terminate();
        return true;
    }

    protected abstract void terminate();
}
