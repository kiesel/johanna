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
public class HannahEchoRequest extends HannahRequest {

    public HannahEchoRequest(String command) {
        super(command);
    }
    
    @Override
    public HannahResponse execute(SessionStore store) {
        return new HannahResponse(true, "You said: >" + this.getCommand() + "<");
    }

}
