package org.oneandone.idev.johanna.store;

/**
 *
 * @author kiesel
 */
public interface Value {
    String asEncoded();
    byte[] asIntern();
}
