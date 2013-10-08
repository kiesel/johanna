/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kiesel
 */
public class SessionStore {
    private static final Logger LOG = Logger.getLogger(SessionStore.class.getName());
    private Map<String,Session> store;

    private int intervalGC= 60000;
    private Timer gc;

    public SessionStore() {
        this.store = new ConcurrentHashMap<String, Session>();
    }
    
    public int size() {
        return this.store.size();
    }
    
    public Session createSession(int ttl) {
        return this.createSession("", ttl);
    }
    
    public Session createSession(String prefix, int ttl) {
        return this.createSession(this.newIdentifier(prefix), ttl);
    }
    
    public Session createSession(Identifier id, int ttl) {
        Session s= new Session(id, ttl);
        this.store.put(s.getId(), s);
        return s;
    }
    
    protected Identifier newIdentifier(String prefix) {
        return new MD5Identifier(prefix);
    }
    
    protected Session session(String id) {
        Session s= this.store.get(id);
        
        // Protect against delivery of expired sessions
        if (s != null) {
            if (s.hasExpired()) return null;
        }
        
        return s;
    }
    
    public boolean hasSession(String id) {
        return this.session(id) != null;
    }
    
    public boolean terminateSession(String id) {
        Session s= this.session(id);
        if (null == s) {
            return false;
        }
        
        return this.terminateSession(s);
    }
    
    private boolean terminateSession(Session s) {
        s.expire();
        return true;
    }

    public Session getSession(String id) {
        return this.session(id);
    }
    
    public void cleanupSessions() {
        this.dumpStats();
        
        LOG.info("Starting session garbage collection run...");
        Iterator<String> i= this.store.keySet().iterator();
        
        int checked= 0, cleaned= 0;
        while (i.hasNext()) {
            Session s= this.store.get(i.next());
            checked++;
            
            if (s.terminateIfExpired()) {
                i.remove();
                cleaned++;
            }
        }
        
        LOG.info("Session garbage collection completed.");
        LOG.log(Level.INFO, "Checked [{0}] / cleaned [{1}]", new Object[]{checked, cleaned});
    }
    
    public void dumpStats() {
        long memory= 0;
        long count= 0;
        long terminated= 0;
        
        Iterator<String> i= this.store.keySet().iterator();
        while (i.hasNext()) {
            Session s= this.store.get(i.next());
            count++;
            memory+= s.payloadBytesUsed();
            
            if (s.hasExpired()) terminated++;
        }
        
        LOG.log(Level.INFO, "Stats: [{0}] sessions [{1}] expired, [{2}] bytes used", new Object[]{count, terminated, memory});
    }
    
    public void startAutomaticGarbageCollectionThread() {
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
    
    public void stopAutomaticGarbageCollection() throws InterruptedException {
        if (this.gc == null) return;

        LOG.info("---> Unscheduling garbage collection run.");
        this.gc.cancel();
    }
}
