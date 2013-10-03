/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import org.oneandone.idev.johanna.protocol.HannahRequest;
import org.oneandone.idev.johanna.protocol.HannahResponse;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class HannahSessionTerminateRequest extends HannahRequest {

    public HannahSessionTerminateRequest(String command) {
        super(command);
    }

    @Override
    public HannahResponse execute(SessionStore store) {
        boolean success= store.terminateSession(this.paramAt(1));
        return (success
                ? HannahResponse.OK
                : HannahResponse.BADSESS
        );
    }
    
}
