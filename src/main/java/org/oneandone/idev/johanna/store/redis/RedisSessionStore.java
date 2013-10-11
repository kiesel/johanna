/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store.redis;

import java.util.Iterator;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.oneandone.idev.johanna.store.AbstractSession;
import org.oneandone.idev.johanna.store.id.Identifier;
import org.oneandone.idev.johanna.store.SessionStore;
import org.oneandone.idev.johanna.store.id.IdentifierFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author kiesel
 */
public class RedisSessionStore implements SessionStore {
    private static final Logger LOG = Logger.getLogger(RedisSessionStore.class.getName());
    private JedisPool pool;
    private IdentifierFactory idFactory;

    private int intervalGC= 60000;
    private Timer gc;

    public RedisSessionStore(IdentifierFactory factory, JedisPool pool) {
        this.setIdentifierFactory(factory);
        this.pool= Objects.requireNonNull(pool);
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
        RedisBackedSession s= new RedisBackedSession(id, ttl, this.pool);
        
        s.register();
        return s;
    }

    @Override
    public void dumpStats() {
        long memory= 0;
        long count= 0;
        long terminated= 0;
        
        Jedis j= this.pool.getResource();
        try {
            Iterator<String> i= j.keys(RedisBackedSession.REDIS_PREFIX).iterator();
            while (i.hasNext()) {
                RedisBackedSession s= this.session(i.next());
                count++;
                memory+= s.payloadBytesUsed();

                if (s.hasExpired()) terminated++;
            }
        } finally {
            this.pool.returnResource(j);
        }
        
        LOG.log(Level.INFO, "Stats: [{0}] sessions [{1}] expired, [{2}] bytes used", new Object[]{count, terminated, memory});
    }

    @Override
    public AbstractSession getSession(String id) {
        return this.session(id);
    }

    @Override
    public boolean hasSession(String id) {
        return this.session(id) != null;
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cleanupSessions() {
        // NOOP
    }

    @Override
    public void startAutomaticGarbageCollection() {
        if (null != this.gc) return;

        LOG.info("---> Scheduled garbage collection run.");
        this.gc= new Timer("SessionStoreGC", true);
        this.gc.schedule(new TimerTask() {

            @Override
            public void run() {
                dumpStats();
                cleanupSessions();
            }
        }, this.intervalGC, this.intervalGC);
    }
    
    @Override
    public void stopAutomaticGarbageCollection() throws InterruptedException {
        if (this.gc == null) return;

        LOG.info("---> Unscheduling garbage collection run.");
        this.gc.cancel();
    }

    @Override
    public boolean terminateSession(String id) {
        RedisBackedSession s= (RedisBackedSession) this.getSession(id);
        if (s == null) return false;
        
        s.terminate();
        return true;
    }

    private RedisBackedSession session(String id) {
        RedisBackedSession s= new RedisBackedSession(this.idFactory.fromString(id), this.pool);
        if (s.hasExpired()) return null;
        
        return s;
    }

    @Override
    public final void setIdentifierFactory(IdentifierFactory f) {
        this.idFactory= Objects.requireNonNull(f);
    }
}
