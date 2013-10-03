/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author kiesel
 */
public class SessionStore {
    private Map<String,Session> store;

    public SessionStore() {
        this.store = new ConcurrentHashMap<String, Session>();
    }
    
    public int size() {
        return this.store.size();
    }
    
    public Session createSession() {
        Session s= new Session();
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

        s.terminate();
        this.store.remove(id);
        return true;
    }
}
