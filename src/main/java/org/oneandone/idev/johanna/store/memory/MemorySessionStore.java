/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store.memory;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.oneandone.idev.johanna.store.AbstractSession;
import org.oneandone.idev.johanna.store.id.Identifier;
import org.oneandone.idev.johanna.store.SessionStore;
import org.oneandone.idev.johanna.store.id.IdentifierFactory;

/**
 *
 * @author kiesel
 */
public class MemorySessionStore implements SessionStore {
    private static final Logger LOG = Logger.getLogger(MemorySessionStore.class.getName());
    private Map<String, AbstractSession> store;
    private IdentifierFactory idFactory;

    private int intervalGC= 60000;
    private Timer gc;

    public MemorySessionStore(IdentifierFactory f) {
        this.setIdentifierFactory(f);
        this.store = new ConcurrentHashMap<>();
    }
    
    @Override
    public int size() {
        return this.store.size();
    }
    
    @Override
    public AbstractSession createSession(int ttl) {
        return this.createSession("", ttl);
    }
    
    @Override
    public AbstractSession createSession(String prefix, int ttl) {
        return this.createSession(this.idFactory.newIdentifier(prefix), ttl);
    }
    
    @Override
    public AbstractSession createSession(Identifier id, int ttl) {
        AbstractSession s= new Session(id, ttl);
        this.store.put(s.getId(), s);
        return s;
    }
    
    protected AbstractSession session(String id) {
        AbstractSession s= this.store.get(id);
        
        // Protect against delivery of expired sessions
        if (s != null) {
            if (s.hasExpired()) return null;
        }
        
        return s;
    }
    
    @Override
    public boolean hasSession(String id) {
        return this.session(id) != null;
    }
    
    @Override
    public boolean terminateSession(String id) {
        AbstractSession s= this.session(id);
        if (null == s) {
            return false;
        }
        
        return this.terminateSession(s);
    }
    
    private boolean terminateSession(AbstractSession s) {
        s.expire();
        return true;
    }

    @Override
    public AbstractSession getSession(String id) {
        return this.session(id);
    }
    
    @Override
    public void cleanupSessions() {
        this.dumpStats();
        
        LOG.info("Starting session garbage collection run...");
        Iterator<String> i= this.store.keySet().iterator();
        
        int checked= 0, cleaned= 0;
        while (i.hasNext()) {
            AbstractSession s= this.store.get(i.next());
            checked++;
            
            if (s.terminateIfExpired()) {
                i.remove();
                cleaned++;
            }
        }
        
        LOG.info("Session garbage collection completed.");
        LOG.log(Level.INFO, "Checked [{0}] / cleaned [{1}]", new Object[]{checked, cleaned});
    }
    
    @Override
    public void dumpStats() {
        long memory= 0;
        long count= 0;
        long terminated= 0;
        
        Iterator<String> i= this.store.keySet().iterator();
        while (i.hasNext()) {
            AbstractSession s= this.store.get(i.next());
            count++;
            memory+= s.payloadBytesUsed();
            
            if (s.hasExpired()) terminated++;
        }
        
        LOG.log(Level.INFO, "Stats: [{0}] sessions [{1}] expired, [{2}] bytes used", new Object[]{count, terminated, memory});
    }
    
    @Override
    public void startAutomaticGarbageCollection() {
        if (null != this.gc) return;

        LOG.info("---> Scheduled garbage collection run.");
        this.gc= new Timer("SessionStoreGC", true);
        this.gc.schedule(new TimerTask() {

            @Override
            public void run() {
                cleanupSessions();
            }
        }, intervalGC, intervalGC);
    }
    
    @Override
    public void stopAutomaticGarbageCollection() throws InterruptedException {
        if (this.gc == null) return;

        LOG.info("---> Unscheduling garbage collection run.");
        this.gc.cancel();
    }

    @Override
    public final void setIdentifierFactory(IdentifierFactory f) {
        this.idFactory= Objects.requireNonNull(f);
    }
}
