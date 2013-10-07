/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kiesel
 */
public class Session {
    private static final Logger LOG = Logger.getLogger(Session.class.getName());
    public static final int DEFAULT_TTL= 3600;

    private Identifier id;
    
    private Map<String, String> values;
    private int ttl;
    private Date expiryDate;

    public Session(Identifier id, int ttl) {
        this.id = id;
        this.setTTL(ttl);
        this.touch();
        this.values= new ConcurrentHashMap<String, String>();
    }
    
    public Session(Identifier id) {
        this(id, DEFAULT_TTL);
    }

    public final void setTTL(int ttl) {
        this.ttl= ttl;
    }
    public final int getTTL() {
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
        LOG.log(Level.INFO, "Terminating session {0}, expired at {1}", new Object[] {
            this.getId(), 
            this.expiryDate()
        });
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
        if (!this.hasExpired()) return false;
        
        // Cleanup
        this.terminate();
        return true;
    }

    private void touch() {
        this.expiryDate= new Date(new Date().getTime() + (this.getTTL() * 1000));
    }

    public void expire() {
        this.expiryDate= new Date(new Date().getTime() - 1);
    }
    
    public long payloadBytesUsed() {
        long bytes= 0;

        Iterator<Entry<String, String>> i= this.values.entrySet().iterator();
        while (i.hasNext()) {
            Entry<String, String> entry= i.next();
            
            bytes += entry.getKey().length();
            bytes += entry.getValue().length();
        }
        
        return bytes;
    }
}
