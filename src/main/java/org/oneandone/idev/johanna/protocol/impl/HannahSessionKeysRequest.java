/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import org.oneandone.idev.johanna.protocol.HannahRequest;
import org.oneandone.idev.johanna.protocol.HannahResponse;
import org.oneandone.idev.johanna.store.Session;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class HannahSessionKeysRequest extends SessionBasedRequest {
    private static final Logger LOG = Logger.getLogger(HannahSessionKeysRequest.class.getName());

    public HannahSessionKeysRequest(String command) {
        super(command);
    }

    protected HannahResponse executeOnSession(SessionStore store, Session s) {
        Iterator<String> keyIterator= s.keys().iterator();
        StringBuffer buf= new StringBuffer();
        
        while (keyIterator.hasNext()) {
            buf.append(keyIterator.next());
            buf.append(" ");
        }
        
        return new HannahResponse(true, buf.toString());
    }
}
