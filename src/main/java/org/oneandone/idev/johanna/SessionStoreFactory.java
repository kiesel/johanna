package org.oneandone.idev.johanna;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.oneandone.idev.johanna.store.SessionStore;
import org.oneandone.idev.johanna.store.id.IdentifierFactory;
import org.oneandone.idev.johanna.store.memory.MemorySessionStore;
import org.oneandone.idev.johanna.store.redis.RedisSessionStore;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * List of possible session stores and creation of session stores.
 * @author fury
 */
enum SessionStoreFactory {
    /** The sessions are kept in memory. */
    MEMORY(new CreationDelegate() {
        @Override
        public SessionStore create(IdentifierFactory identifierFactory, String host) {
            log.info("Using \"memory\" backend.");
            return new MemorySessionStore(identifierFactory);
        }
    }),
    /** The sessions are kept in a remote REDIS session store. 
     */
    REDIS(new CreationDelegate() {
        @Override
        public SessionStore create(IdentifierFactory identifierFactory, String host) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxActive(JohannahServer.MAX_THREADS);
            JedisPool pool = new JedisPool(config, host);

            log.log(Level.INFO, "Using \"redis\" backend: {0} @ {1}", new Object[]{pool, host});

            return new RedisSessionStore(identifierFactory, pool);
        }
    });

    private final CreationDelegate creationDelegate;

    private SessionStoreFactory(CreationDelegate creationDelegate) {
        this.creationDelegate = Objects.requireNonNull(creationDelegate);
    }

    /** Delegates the creation of a {@link SessionStore} to a class. */
    private static abstract class CreationDelegate {
        protected final Logger log = Logger.getLogger(SessionStoreFactory.class.getName());
        public abstract SessionStore create(IdentifierFactory identifierFactory, String host);
    }

    /** Creates a new session store with the given parameters.
     * The session store created corresponds to the current enum type.
     * @param identifierFactory the factory for creating session identifiers.
     * @param host the remote host the session store runs on (if required, for example for REDIS).
     * @see #MEMORY
     * @see #REDIS
     */
    public SessionStore create(IdentifierFactory identifierFactory, String host) {
        return creationDelegate.create(identifierFactory, host);
    }
}
