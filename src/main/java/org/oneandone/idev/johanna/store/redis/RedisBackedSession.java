/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store.redis;

import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import org.oneandone.idev.johanna.store.AbstractSession;
import org.oneandone.idev.johanna.store.Identifier;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class RedisBackedSession extends AbstractSession {
    public final static String REDIS_PREFIX= "sess:";
    private String REDIS_META_KEY= "meta";
    
    private JedisPool pool;
    
    protected String key() {
        return REDIS_PREFIX + this.getId();
    }
    
    private Jedis jedis() {
        Jedis j= this.pool.getResource();
        if (!j.isConnected()) j.connect();
        return j;
    }
    
    @Override
    public void putValue(String k, String v) {
        Jedis j= this.jedis();
        try {
            j.hset(this.key(), k, v);
        } finally {
            this.pool.returnResource(j);
        }
    }

    @Override
    public String getValue(String k) {
        Jedis j= this.jedis();
        try {
            return this.pool.getResource().hget(this.key(), k);
        } finally {
            this.pool.returnResource(j);
        }
    }

    @Override
    public boolean removeValue(String k) {
        Jedis j= this.jedis();
        try {
            return (1 == this.pool.getResource().hdel(this.key(), k));
        } finally {
            this.pool.returnResource(j);
        }
    }

    @Override
    public boolean hasValue(String k) {
        Jedis j= this.jedis();
        try {
            return this.redis().hexists(this.key(), k);
        } finally {
            this.pool.returnResource(j);
        }
    }

    @Override
    public Set<String> keys() {
        Jedis j= this.jedis();
        try {
            return this.redis().hkeys(this.key());
        } finally {
            this.pool.returnResource(j);
        }
    }

    @Override
    protected void terminate() {
        LOG.log(Level.INFO, "Terminating session {0}", this.getId());
        Jedis j= this.jedis();
        try {
            this.redis().del(this.key());
        } finally {
            this.pool.returnResource(j);
        }
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
        return 0;
    }

    private Jedis redis() {
        return this.pool.getResource();
    }

    public void register() {
        Jedis j= this.jedis();
        try {
            j.hset(this.key(), "meta:session", "0");
        } finally {
            this.pool.returnResource(j);
        }

        this.touch();
    }
    
    protected final void touch(Jedis j) {
        j.hset(this.key(), REDIS_META_KEY, "0");
        j.expire(this.key(), this.getTTL());
    }

    @Override
    protected final void touch() {
        Jedis j= this.jedis();
        try {
            this.touch(j);
        } finally {
            this.pool.returnResource(j);
        }
    }

    @Override
    public void expire() {
        this.terminate();
    }

    @Override
    public boolean hasExpired() {
        Jedis j= this.jedis();
        try {
            return j.hexists(this.key(), REDIS_META_KEY);
        } finally {
            this.pool.returnResource(j);
        }
    }
}
