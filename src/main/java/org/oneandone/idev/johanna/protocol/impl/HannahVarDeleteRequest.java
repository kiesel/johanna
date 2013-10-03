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
public class HannahVarDeleteRequest extends HannahRequest {
    private static final Logger LOG = Logger.getLogger(HannahVarDeleteRequest.class.getName());

    public HannahVarDeleteRequest(String command) {
        super(command);
    }

    @Override
    public HannahResponse execute(SessionStore store) {
        String id= this.paramAt(1);
        String stor= this.paramAt(2);
        String name= this.paramAt(3);
        
        Session s= store.getSession(id);
        if (s == null) return HannahResponse.BADSESS;
        
        if (!"tmp".equals(stor)) {
            return new HannahResponse(false, "BADSTOR Only tmp supported.");
        }
        
        boolean removeValue = s.removeValue(name);
        
        return new HannahResponse(true, s.getValue(name));
    }
}
