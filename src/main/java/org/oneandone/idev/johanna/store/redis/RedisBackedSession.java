/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store.redis;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.oneandone.idev.johanna.store.AbstractSession;
import org.oneandone.idev.johanna.store.PlainValue;
import org.oneandone.idev.johanna.store.Value;
import org.oneandone.idev.johanna.store.id.Identifier;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class RedisBackedSession extends AbstractSession {
    public final static String REDIS_PREFIX= "s:";
    private final static String REDIS_KEY_PREFIX= "v:";
    private final static String REDIS_META_KEY= "ttl";
    
    private JedisPool pool;
    
    protected String key() {
        return REDIS_PREFIX + this.getId();
    }
    
    private Jedis jedis() {
        return this.pool.getResource();
    }
    
    @Override
    public void putValue(String k, Value v) {
        Jedis j= this.jedis();
        try {
            j.hset(this.key().getBytes(), this.marshal(k).getBytes(), v.asIntern());
        } finally {
            this.pool.returnResource(j);
        }
    }

    @Override
    public Value getValue(String k) {
        Jedis j= this.jedis();
        try {
            return this.valueFromIntern(j.hget(this.key().getBytes(), this.marshal(k).getBytes()));
        } finally {
            this.pool.returnResource(j);
        }
    }

    @Override
    public boolean removeValue(String k) {
        Jedis j= this.jedis();
        try {
            return (1 == j.hdel(this.key(), this.marshal(k)));
        } finally {
            this.pool.returnResource(j);
        }
    }

    @Override
    public boolean hasValue(String k) {
        Jedis j= this.jedis();
        try {
            return j.hexists(this.key(), this.marshal(k));
        } finally {
            this.pool.returnResource(j);
        }
    }

    @Override
    public Set<String> keys() {
        Set<String> out, set;
        Jedis j= this.jedis();
        try {
            set= j.hkeys(this.key());
        } finally {
            this.pool.returnResource(j);
        }
        out= new HashSet(set.size());
        for (String s : set) {
            if (REDIS_META_KEY.equals(s)) continue;
            out.add(this.unmarshal(s));
        }
        
        return out;
    }

    @Override
    protected void terminate() {
        LOG.log(Level.INFO, "Terminating session {0}", this.getId());
        Jedis j= this.jedis();
        try {
            j.del(this.key());
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

    public void register() {
        this.touch();
    }
    
    protected final void touch(Jedis j) {
        j.hset(this.key(), REDIS_META_KEY, Integer.toString(this.getTTL()));
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
            return !j.hexists(this.key(), REDIS_META_KEY);
        } finally {
            this.pool.returnResource(j);
        }
    }

    private String marshal(String k) {
        return REDIS_KEY_PREFIX + k;
    }

    private String unmarshal(String s) {
        if (!s.startsWith(REDIS_KEY_PREFIX)) {
            LOG.log(Level.WARNING, "Invalid key name: \"{0}\"", s);
            throw new IllegalArgumentException("Invalid key name, expected start with prefix.");
        }
        return s.substring(2);
    }

    public Value valueFromIntern(byte[] bytes) {
        return new PlainValue(bytes);
    }
}
