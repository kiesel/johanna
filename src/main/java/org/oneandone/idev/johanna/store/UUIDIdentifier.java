package org.oneandone.idev.johanna.store;

import java.util.UUID;

/**
 *
 * @author kiesel
 */
public class UUIDIdentifier extends Identifier {
    private final UUID id;

    public UUIDIdentifier(String prefix) {
        super(prefix);
        this.id= UUID.randomUUID();
    }

    public UUIDIdentifier() {
        this("");
    }
    
    @Override
    protected String uniqid() {
        return this.id.toString();
    }
}
