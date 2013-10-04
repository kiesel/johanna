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

    private UUID id;
    private Date created;
    private int ttl;
    
    private Map<String, String> values;
    private Date lastmodified;

    public Session(UUID id, Date created, int ttl) {
        this.id = id;
        this.created = created;
        this.lastmodified= new Date();
        this.setTTL(ttl);
        this.values= new ConcurrentHashMap<String, String>();
    }

    public Session() {
        this(UUID.randomUUID(), new Date(), 86400);
    }

    public final void setTTL(int ttl) {
        this.ttl= ttl;
    }
    public int getTTL() {
        return this.ttl;
    }

    public String getId() {
        return this.id.toString();
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
        return this.expiryDate().after(new Date());
    }
    
    private Date expiryDate() {
        return new Date(this.getLastModified().getTime() + this.getTTL());
    }

    public boolean terminateIfExpired() {
        if (!this.hasExpired()) return false;
        
        // Cleanup
        LOG.log(Level.INFO, "Session {0} has expired.", this.getId());
        this.terminate();
        return true;
    }

    private Date getLastModified() {
        return this.lastmodified;
    }

    private void touch() {
        this.lastmodified= new Date();
    }

}
