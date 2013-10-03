/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import java.util.logging.Logger;
import org.oneandone.idev.johanna.protocol.HannahRequest;
import org.oneandone.idev.johanna.protocol.HannahResponse;
import org.oneandone.idev.johanna.store.Session;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class HannahSessionSetTimeoutRequest extends HannahRequest {
    private static final Logger LOG = Logger.getLogger(HannahSessionSetTimeoutRequest.class.getName());

    public HannahSessionSetTimeoutRequest(String command) {
        super(command);
    }

    @Override
    public HannahResponse execute(SessionStore store) {
        Session s= store.getSession(this.paramAt(1));
        if (s == null) return HannahResponse.BADSESS;
        
        s.setTTL(Integer.parseInt(this.paramAt(2)));
        return HannahResponse.OK;
    }
    
}
