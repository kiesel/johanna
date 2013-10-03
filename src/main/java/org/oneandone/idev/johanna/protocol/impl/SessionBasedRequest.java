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
public abstract class SessionBasedRequest extends Request {

    public SessionBasedRequest(String command) {
        super(command);
    }

    @Override
    public Response execute(SessionStore store) {
        Session s = store.getSession(this.paramAt(1));
        if (s == null) {
            return Response.BADSESS;
        }
        return executeOnSession(store, s);
    }

    protected abstract Response executeOnSession(SessionStore store, Session s);
}
