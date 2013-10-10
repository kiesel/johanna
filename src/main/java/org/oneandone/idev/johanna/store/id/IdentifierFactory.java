package org.oneandone.idev.johanna.store.id;

/**
 *
 * @author kiesel
 */
public enum IdentifierFactory {
    MD5 {

        @Override
        public Identifier newIdentifier(String prefix) {
            return new MD5Identifier(prefix);
        }

        @Override
        public Identifier fromString(String id) {
            return MD5Identifier.forId(id);
        }
    },
    UUID {

        @Override
        public Identifier newIdentifier(String prefix) {
            return new UUIDIdentifier(prefix);
        }

        @Override
        public Identifier fromString(String id) {
            return UUIDIdentifier.forId(id);
        }
    };
        
    public abstract Identifier newIdentifier(String prefix);
    public abstract Identifier fromString(String id);
}
