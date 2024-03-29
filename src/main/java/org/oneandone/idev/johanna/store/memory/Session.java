package org.oneandone.idev.johanna.store.memory;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.oneandone.idev.johanna.store.AbstractSession;
import org.oneandone.idev.johanna.store.Value;
import org.oneandone.idev.johanna.store.id.Identifier;

/**
 *
 * @author kiesel
 */
public class Session extends AbstractSession {
    protected Date expiryDate;
    private Map<String, Value> values;

    public Session(Identifier id, int ttl) {
        super(id, ttl);
        this.touch();
        this.values= new ConcurrentHashMap<>();
    }
    
    public Session(Identifier id) {
        this(id, DEFAULT_TTL);
    }
    
    @Override
    public void putValue(String k, String v) {
        this.values.put(k, this.fromEncoded(v));
        this.touch();
    }
    
    @Override
    public String getValue(String k) {
        Value v= this.values.get(k);
        if (v == null) return null;
        
        return v.asEncoded();
    }
    
    @Override
    public boolean removeValue(String k) {
        this.touch();
        return (null != this.values.remove(k));
    }
    
    @Override
    public boolean hasValue(String k) {
        return this.values.containsKey(k);
    }
    
    @Override
    public void terminate() {
        LOG.log(Level.INFO, "Terminating session {0}, expired at {1}", new Object[] {
            this.getId(), 
            this.expiryDate()
        });
        this.values= null;
    }
    
    @Override
    public Set<String> keys() {
        return this.values.keySet();
    }
    
    @Override
    public long payloadBytesUsed() {
        long bytes= 0;

        Iterator<Entry<String, Value>> i= this.values.entrySet().iterator();
        while (i.hasNext()) {
            Entry<String, Value> entry= i.next();
            
            bytes += entry.getKey().length();
            bytes += entry.getValue().asIntern().length;
        }
        
        return bytes;
    }

    @Override
    protected final void touch() {
        this.expiryDate = new Date(new Date().getTime() + (this.getTTL() * 1000L));
    }
    
    public Date expiryDate() {
        return (Date)this.expiryDate.clone();
    }

    @Override
    public void expire() {
        this.expiryDate = new Date(new Date().getTime() - 1);
    }

    @Override
    public boolean hasExpired() {
        return this.hasExpired(new Date());
    }

    public boolean hasExpired(Date ref) {
        return this.expiryDate().before(ref);
    }
}
