/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import org.oneandone.idev.johanna.protocol.Response;
import org.oneandone.idev.johanna.store.AbstractSession;
import org.oneandone.idev.johanna.store.PlainValue;
import org.oneandone.idev.johanna.store.SessionStore;
import org.oneandone.idev.johanna.store.Value;

/**
 *
 * @author kiesel
 */
public abstract class SessionKeyBasedRequest extends SessionBasedRequest {

    public SessionKeyBasedRequest(String command) {
        super(command);
    }

    @Override
    protected Response processSession(SessionStore store, AbstractSession s) {
        String stor = this.paramAt(2);
        String name = this.paramAt(3);

        if (!this.validStorageArea(stor)) {
            return Response.BADSTOR;
        }
        
        return processSessionKey(store, s, name);
    }
    
    abstract protected Response processSessionKey(SessionStore store, AbstractSession s, String name);
}
