/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol.impl;

import org.oneandone.idev.johanna.protocol.Request;
import org.oneandone.idev.johanna.protocol.Response;
import org.oneandone.idev.johanna.store.SessionStore;

/**
 *
 * @author kiesel
 */
public class HangupRequest extends Request {

    public HangupRequest(String command) {
        super(command);
    }

    @Override
    public Response process(SessionStore store) {
        Response r= new Response(true, "BYE");
        r.setClose(true);
        return r;
    }
    
}
