/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

/**
 *
 * @author kiesel
 */
public interface SessionStore {

    void cleanupSessions();

    AbstractSession createSession(int ttl);

    AbstractSession createSession(String prefix, int ttl);

    AbstractSession createSession(Identifier id, int ttl);

    void dumpStats();

    AbstractSession getSession(String id);

    boolean hasSession(String id);

    int size();

    void startAutomaticGarbageCollection();

    void stopAutomaticGarbageCollection() throws InterruptedException;

    boolean terminateSession(String id);
    
}
