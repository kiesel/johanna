/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import java.util.logging.Logger;
import org.oneandone.idev.johanna.protocol.HannahRequest;
import org.oneandone.idev.johanna.protocol.HannahResponse;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class HannahSessionIsValidRequest extends HannahRequest {
    private static final Logger LOG = Logger.getLogger(HannahSessionIsValidRequest.class.getName());

    public HannahSessionIsValidRequest(String command) {
        super(command);
    }

    @Override
    public HannahResponse execute(SessionStore store) {
        boolean valid= store.hasSession(this.paramAt(1));
        return (valid ? HannahResponse.OK : HannahResponse.BADSESS);
    }
}
