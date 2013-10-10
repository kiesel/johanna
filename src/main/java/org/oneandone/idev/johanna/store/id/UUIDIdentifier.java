package org.oneandone.idev.johanna.store.id;

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
    
    public UUIDIdentifier(String prefix, UUID id) {
        super(prefix);
        this.id= id;
    }

    public UUIDIdentifier() {
        this("");
    }
    
    public static UUIDIdentifier forId(String id) {
        return new UUIDIdentifier(
                id.substring(0, 8),
                UUID.fromString(id.substring(8))
        );
    }
    
    @Override
    protected String uniqid() {
        return this.id.toString();
    }
}
