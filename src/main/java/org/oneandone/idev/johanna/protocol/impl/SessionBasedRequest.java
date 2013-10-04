/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.oneandone.idev.johanna.protocol.Request;
import org.oneandone.idev.johanna.protocol.Response;
import org.oneandone.idev.johanna.store.Session;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public abstract class SessionBasedRequest extends Request {
    private static final Logger LOG = Logger.getLogger(SessionBasedRequest.class.getName());

    public SessionBasedRequest(String command) {
        super(command);
    }

    @Override
    public Response process(SessionStore store) {
        Session s = store.getSession(this.paramAt(1));
        if (s == null) {
            return Response.BADSESS;
        }
        
        LOG.log(Level.INFO, "Session expired @ {0}", s.expiryDate());
        
        // Check for session expiry, first
        if (s.hasExpired()) {
            return Response.BADSESS;
        }
        
        // Otherwise, perform request
        return processSession(store, s);
    }

    protected abstract Response processSession(SessionStore store, Session s);
}
