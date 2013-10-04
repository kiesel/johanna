/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import java.util.logging.Logger;
import org.oneandone.idev.johanna.protocol.Request;
import org.oneandone.idev.johanna.protocol.Response;
import org.oneandone.idev.johanna.store.Session;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class SessionIsValidRequest extends SessionBasedRequest {
    private static final Logger LOG = Logger.getLogger(SessionIsValidRequest.class.getName());

    public SessionIsValidRequest(String command) {
        super(command);
    }

    @Override
    public Response processSession(SessionStore store, Session s) {
        return Response.OK;
    }
}
