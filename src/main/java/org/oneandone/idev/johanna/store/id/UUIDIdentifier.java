package org.oneandone.idev.johanna.store.id;

import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author kiesel
 */
public class UUIDIdentifier extends Identifier {
    private final UUID id;

    public UUIDIdentifier(String prefix, IdentifierFactory identifierFactory) {
        super(prefix, identifierFactory);
        this.id= UUID.randomUUID();
    }
    
    public UUIDIdentifier(String prefix, UUID id, IdentifierFactory identifierFactory) {
        super(prefix, identifierFactory);
        this.id= Objects.requireNonNull(id);
    }

    public UUIDIdentifier(IdentifierFactory identifierFactory) {
        this("", identifierFactory);
    }
    
    public static UUIDIdentifier forId(String id, IdentifierFactory identifierFactory) {
        return new UUIDIdentifier(
                prefixPartOf(id, identifierFactory),
                UUID.fromString(uniquePartOf(id, identifierFactory)),
                identifierFactory
        );
    }
    
    @Override
    protected String uniqid() {
        return this.id.toString();
    }
}
