/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store.redis;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import org.oneandone.idev.johanna.store.AbstractSession;
import org.oneandone.idev.johanna.store.Identifier;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class RedisBackedSession extends AbstractSession {
    public final static String REDIS_PREFIX= "sess:";
    
    private JedisPool pool;
    
    protected String key() {
        return REDIS_PREFIX + this.getId();
    }
    
    @Override
    public void putValue(String k, String v) {
        this.pool.getResource().hset(this.key(), k, v);
    }

    @Override
    public String getValue(String k) {
        return this.pool.getResource().hget(this.key(), k);
    }

    @Override
    public boolean removeValue(String k) {
        return (1 == this.pool.getResource().hdel(this.key(), k));
    }

    @Override
    public boolean hasValue(String k) {
        return this.redis().hexists(this.key(), k);
    }

    @Override
    public Set<String> keys() {
        return this.redis().hkeys(this.key());
    }

    @Override
    protected void terminate() {
        LOG.log(Level.INFO, "Terminating session {0}", this.getId());
        
        this.redis().del(this.key());
    }

    public RedisBackedSession(Identifier id, JedisPool pool) {
        this(id, AbstractSession.DEFAULT_TTL, pool);
    }

    public RedisBackedSession(Identifier id, int ttl, JedisPool pool) {
        super(id, ttl);
        this.pool= pool;
    }

    @Override
    public long payloadBytesUsed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Jedis redis() {
        return this.pool.getResource();
    }

    public void register() {
        Jedis j= this.pool.getResource();
        j.hset(this.key(), "meta:session", "0");
        this.touch();
    }

    protected final void touch() {
        this.pool.getResource().expire(this.key(), this.getTTL());
    }

    @Override
    public void expire() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date expiryDate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasExpired() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
