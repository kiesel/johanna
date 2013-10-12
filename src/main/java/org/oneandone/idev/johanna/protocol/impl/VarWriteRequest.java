/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import java.util.logging.Logger;
import org.oneandone.idev.johanna.protocol.Response;
import org.oneandone.idev.johanna.store.AbstractSession;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class VarWriteRequest extends SessionKeyBasedRequest {
    private static final Logger LOG = Logger.getLogger(VarWriteRequest.class.getName());

    public VarWriteRequest(String command) {
        super(command);
    }
    
    @Override
    protected Response processSessionKey(SessionStore store, AbstractSession s, String name) {
        String value= this.paramAt(4);
        
        s.putValue(name, this.toValue(value));
        return Response.OK;
    }
    
}
