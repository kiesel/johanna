package org.oneandone.idev.johanna.store.id;

/**
 *
 * @author kiesel
 */
public enum IdentifierFlavor {
    MD5 {

        @Override
        public Identifier newIdentifier(String prefix, IdentifierFactory identifierFactory) {
            return new MD5Identifier(prefix, identifierFactory);
        }

        @Override
        public Identifier fromString(String id, IdentifierFactory identifierFactory) {
            return MD5Identifier.forId(id, identifierFactory);
        }
    },
    UUID {

        @Override
        public Identifier newIdentifier(String prefix, IdentifierFactory identifierFactory) {
            return new UUIDIdentifier(prefix, identifierFactory);
        }

        @Override
        public Identifier fromString(String id, IdentifierFactory identifierFactory) {
            return UUIDIdentifier.forId(id, identifierFactory);
        }
    };
        
    abstract Identifier newIdentifier(String prefix, IdentifierFactory identifierFactory);
    abstract Identifier fromString(String id, IdentifierFactory identifierFactory);
}
