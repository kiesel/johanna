/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import java.util.Iterator;
import java.util.logging.Logger;
import org.oneandone.idev.johanna.protocol.Response;
import org.oneandone.idev.johanna.store.AbstractSession;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class SessionKeysRequest extends SessionBasedRequest {
    private static final Logger LOG = Logger.getLogger(SessionKeysRequest.class.getName());

    public SessionKeysRequest(String command) {
        super(command);
    }

    protected Response processSession(SessionStore store, AbstractSession s) {
        String stor= this.paramAt(2);
        
        if (!this.validStorageArea(stor)) {
            return Response.BADSTOR;
        }
        Iterator<String> i= s.keys().iterator();
        StringBuilder buf= new StringBuilder();
        
        while (i.hasNext()) {
            buf.append(i.next());
            if (i.hasNext()) buf.append(" ");
        }
        
        return new Response(true, buf.toString());
    }
}
