/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 *
 * @author kiesel
 */
public class Session extends AbstractSession {
    private Map<String, String> values;

    public Session(Identifier id, int ttl) {
        super(id, ttl);
        this.values= new ConcurrentHashMap<String, String>();
    }
    
    public Session(Identifier id) {
        this(id, DEFAULT_TTL);
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
