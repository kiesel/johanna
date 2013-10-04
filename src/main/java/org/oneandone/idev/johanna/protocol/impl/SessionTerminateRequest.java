/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import org.oneandone.idev.johanna.protocol.Request;
import org.oneandone.idev.johanna.protocol.Response;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class SessionTerminateRequest extends Request {

    public SessionTerminateRequest(String command) {
        super(command);
    }

    @Override
    public Response process(SessionStore store) {
        boolean success= store.terminateSession(this.paramAt(1));
        return (success
                ? Response.OK
                : Response.BADSESS
        );
    }
    
}
