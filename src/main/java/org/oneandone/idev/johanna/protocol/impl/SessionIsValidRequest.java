package org.oneandone.idev.johanna.protocol.impl;

import java.util.logging.Logger;
import org.oneandone.idev.johanna.protocol.Response;
import org.oneandone.idev.johanna.store.AbstractSession;
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
    protected Response processSession(SessionStore store, AbstractSession s) {
        return Response.OK;
    }
}
