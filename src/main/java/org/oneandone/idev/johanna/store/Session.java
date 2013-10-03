/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author kiesel
 */
public class Session {
    private UUID id;
    private Date created;
    private int ttl;
    
    private Map<String, String> values;

    public Session(UUID id, Date created, int ttl) {
        this.id = id;
        this.created = created;
        this.ttl = ttl;
        this.values= new ConcurrentHashMap<String, String>();
    }

    public Session() {
        this(UUID.randomUUID(), new Date(), 86400);
    }

    String getId() {
        return this.id.toString();
    }
    
    public void putValue(String k, String v) {
        this.values.put(k, v);
    }
    
    public String getValue(String k) {
        return this.values.get(k);
    }
    
    public boolean removeValue(String k) {
        return (null != this.values.remove(k));
    }
    
    public boolean hasValue(String k) {
        return this.values.containsKey(k);
    }
    
    public int setTimeout(int ttl) {
        int old= this.ttl;
        this.ttl= ttl;
        return old;
    }
    
    public void terminate() {
        // NOOP
    }
}
