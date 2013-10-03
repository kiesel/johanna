/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import org.oneandone.idev.johanna.protocol.HannahRequest;
import org.oneandone.idev.johanna.protocol.HannahResponse;
import org.oneandone.idev.johanna.store.Session;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public abstract class SessionBasedRequest extends HannahRequest {

    public SessionBasedRequest(String command) {
        super(command);
    }

    @Override
    public HannahResponse execute(SessionStore store) {
        Session s = store.getSession(this.paramAt(1));
        if (s == null) {
            return HannahResponse.BADSESS;
        }
        return executeOnSession(store, s);
    }

    protected abstract HannahResponse executeOnSession(SessionStore store, Session s);
}
