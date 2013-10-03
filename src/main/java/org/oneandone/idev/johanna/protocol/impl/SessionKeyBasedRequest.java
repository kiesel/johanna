/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import org.oneandone.idev.johanna.protocol.Request;
import org.oneandone.idev.johanna.protocol.Response;
import org.oneandone.idev.johanna.store.Session;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public abstract class SessionKeyBasedRequest extends SessionBasedRequest {

    public SessionKeyBasedRequest(String command) {
        super(command);
    }

    @Override
    protected Response executeOnSession(SessionStore store, Session s) {
        String stor = this.paramAt(2);
        String name = this.paramAt(3);

        if (!"tmp".equals(stor)) {
            return new Response(false, "BADSTOR Only tmp supported.");
        }
        
        return executeOnSessionKey(store, s, name);
    }

    abstract protected Response executeOnSessionKey(SessionStore store, Session s, String name);
}
