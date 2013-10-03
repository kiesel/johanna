/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol;

import org.oneandone.idev.johanna.store.Session;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class HannahSessionCreateRequest extends HannahRequest {

    public HannahSessionCreateRequest(String command) {
        super(command);
    }

    @Override
    public HannahResponse execute(SessionStore store) {
        Session s= store.createSession();
        return new HannahResponse(true, s.getId());
    }
}
