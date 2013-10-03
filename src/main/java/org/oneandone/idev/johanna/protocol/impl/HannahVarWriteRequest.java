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
public class HannahVarWriteRequest extends SessionKeyBasedRequest {
    private static final Logger LOG = Logger.getLogger(HannahVarWriteRequest.class.getName());

    public HannahVarWriteRequest(String command) {
        super(command);
    }
    
    protected HannahResponse executeOnSessionKey(SessionStore store, Session s, String name) {
        String value= this.paramAt(4);
        
        s.putValue(name, value);
        return HannahResponse.OK;
    }
    
}
