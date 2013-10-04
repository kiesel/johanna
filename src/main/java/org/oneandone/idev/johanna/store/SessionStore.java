/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import java.util.Iterator;
import java.util.Map;
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

    public SessionStore() {
        this.store = new ConcurrentHashMap<String, Session>();
    }
    
    public int size() {
        return this.store.size();
    }
    
    public Session createSession(int ttl) {
        Session s= new Session();
        s.setTTL(ttl);
        this.store.put(s.getId(), s);
        return s;
    }
    
    public boolean hasSession(String id) {
        return this.store.containsKey(id);
    }
    
    public boolean terminateSession(String id) {
        Session s= this.store.get(id);
        if (null == s) {
            return false;
        }
        
        return this.terminateSession(s);
    }
    
    private boolean terminateSession(Session s) {
        s.terminate();
        this.store.remove(s.getId());
        return true;
    }

    public Session getSession(String id) {
        return this.store.get(id);
    }
    
    public void cleanupSessions() {
        LOG.info("Session garbage collection start ...");
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
}
