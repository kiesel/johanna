package org.oneandone.idev.johanna.store;

import java.util.Set;

/**
 *
 * @author kiesel
 */
public interface ISession {

    void expire();

    String getId();

    int getTTL();

    String getValue(String k);

    boolean hasExpired();

    boolean hasValue(String k);

    Set<String> keys();

    long payloadBytesUsed();

    void putValue(String k, String v);

    boolean removeValue(String k);

    void setTTL(int ttl);

    int setTimeout(final int ttl);

    boolean terminateIfExpired();
}
