/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store.id;

/**
 *
 * @author fury
 */
public class IdentifierFactory {
    private final char identifierSeparator;
    private final IdentifierFlavor identifierFlavor;
    
    public IdentifierFactory(char identifierSeparator, IdentifierFlavor identifierFlavor) {
        this.identifierSeparator = identifierSeparator;
        this.identifierFlavor = identifierFlavor;
    }
    
    public Identifier newIdentifier(String prefix) {
        return identifierFlavor.newIdentifier(prefix, this);
    }

    public Identifier fromString(String id) {
        return identifierFlavor.fromString(id, this);
    }

    IdentifierFlavor getIdentifierFlavor() {
        return identifierFlavor;
    }

    char getIdentifierSeparator() {
        return identifierSeparator;
    }
}
