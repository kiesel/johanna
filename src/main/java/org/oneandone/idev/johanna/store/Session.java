/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kiesel
 */
public class Session {
    private static final Logger LOG = Logger.getLogger(Session.class.getName());
    private static final int DEFAULT_TTL= 3600;

    private String prefix;
    private UUID id;
    private int ttl;
    
    private Map<String, String> values;
    private Date expiryDate;

    public Session(String prefix, UUID id, int ttl) {
        this.prefix= prefix;
        this.id = id;
        this.setTTL(ttl);
        this.touch();
        this.values= new ConcurrentHashMap<String, String>();
    }
    
    public Session(String prefix) {
        this(prefix, UUID.randomUUID(), DEFAULT_TTL);
    }

    public Session() {
        this("", UUID.randomUUID(), DEFAULT_TTL);
    }

    public final void setTTL(int ttl) {
        this.ttl= ttl;
    }
    public final int getTTL() {
        return this.ttl;
    }

    public String getId() {
        return this.prefix + this.id.toString();
    }
    
    public void putValue(String k, String v) {
        this.values.put(k, v);
        this.touch();
    }
    
    public String getValue(String k) {
        return this.values.get(k);
    }
    
    public boolean removeValue(String k) {
        this.touch();
        return (null != this.values.remove(k));
    }
    
    public boolean hasValue(String k) {
        return this.values.containsKey(k);
    }
    
    public int setTimeout(final int ttl) {
        this.touch();
        int old= this.getTTL();
        this.setTTL(ttl);
        return old;
    }
    
    public void terminate() {
        LOG.log(Level.INFO, "Termination session {0}", this.getId());
        this.values= null;
    }
    
    public Set<String> keys() {
        return this.values.keySet();
    }
    
    public boolean hasExpired() {
        return this.hasExpired(new Date());
    }
    
    public boolean hasExpired(Date ref) {
        return this.expiryDate().before(ref);
    }
    
    public Date expiryDate() {
        return this.expiryDate;
    }

    public boolean terminateIfExpired() {
        LOG.log(Level.INFO, "Expiry date: {0} - expired? {1}", new Object[]{this.expiryDate.toString(), this.hasExpired()});
        if (!this.hasExpired()) return false;
        
        // Cleanup
        LOG.log(Level.INFO, "Session {0} has expired.", this.getId());
        this.terminate();
        return true;
    }

    private void touch() {
        this.expiryDate= new Date(new Date().getTime() + this.getTTL());
    }

    public void expire() {
        this.expiryDate= new Date(new Date().getTime() - 1);
    }
}
