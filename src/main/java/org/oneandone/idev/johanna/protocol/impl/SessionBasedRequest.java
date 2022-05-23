package org.oneandone.idev.johanna.protocol.impl;

import java.util.logging.Logger;
import org.oneandone.idev.johanna.protocol.Request;
import org.oneandone.idev.johanna.protocol.Response;
import org.oneandone.idev.johanna.store.AbstractSession;
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
        AbstractSession s = store.getSession(this.paramAt(1));
        if (s == null) {
            return Response.BADSESS;
        }
        
        // Otherwise, perform request
        return processSession(store, s);
    }

    protected abstract Response processSession(SessionStore store, AbstractSession s);

    protected boolean validStorageArea(String stor) {
        return "tmp".equals(stor);
    }
}
