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
public class HannahVarDeleteRequest extends SessionKeyBasedRequest {
    private static final Logger LOG = Logger.getLogger(HannahVarDeleteRequest.class.getName());

    public HannahVarDeleteRequest(String command) {
        super(command);
    }

    @Override
    public HannahResponse executeOnSessionKey(SessionStore store, Session s, String name) {
        boolean removeValue = s.removeValue(name);
        if (!removeValue) return HannahResponse.NOKEY;
        return HannahResponse.OK;
    }
}
