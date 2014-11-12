package org.oneandone.idev.johanna.store.id;

import java.util.UUID;

/**
 *
 * @deprecated there is pseudo generated randomness in UUID.randomUUID
 * @author kiesel
 */
@Deprecated
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
                prefixFrom(id),
                UUID.fromString(suffixFrom(id))
        );
    }
    
    @Override
    protected String uniqid() {
        return this.id.toString();
    }
}
