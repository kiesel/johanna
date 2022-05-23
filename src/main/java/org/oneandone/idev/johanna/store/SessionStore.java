package org.oneandone.idev.johanna.store;

import org.oneandone.idev.johanna.store.id.Identifier;
import org.oneandone.idev.johanna.store.id.IdentifierFactory;

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

    void scheduleMaintenanceTask();

    void cancelMaintenanceTask() throws InterruptedException;

    boolean terminateSession(String id);

    void setIdentifierFactory(IdentifierFactory f);
}
