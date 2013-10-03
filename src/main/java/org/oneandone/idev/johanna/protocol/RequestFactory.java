/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol;

import org.oneandone.idev.johanna.protocol.impl.SessionCreateRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.oneandone.idev.johanna.protocol.impl.SessionIsValidRequest;
import org.oneandone.idev.johanna.protocol.impl.SessionKeysRequest;
import org.oneandone.idev.johanna.protocol.impl.SessionSetTimeoutRequest;
import org.oneandone.idev.johanna.protocol.impl.SessionTerminateRequest;
import org.oneandone.idev.johanna.protocol.impl.VarDeleteRequest;
import org.oneandone.idev.johanna.protocol.impl.VarReadRequest;
import org.oneandone.idev.johanna.protocol.impl.VarWriteRequest;

/**
 *
 * @author kiesel
 */
public class RequestFactory {
    private Map<String, Class> cmds;

    public RequestFactory() {
        this.cmds= new HashMap<String, Class>();
        this.cmds.put("session_create", SessionCreateRequest.class);
        this.cmds.put("session_terminate", SessionTerminateRequest.class);
        this.cmds.put("session_isvalid", SessionIsValidRequest.class);
        this.cmds.put("session_settimeout", SessionSetTimeoutRequest.class);
        this.cmds.put("session_keys", SessionKeysRequest.class);
        
        this.cmds.put("var_write", VarWriteRequest.class);
        this.cmds.put("var_read", VarReadRequest.class);
        this.cmds.put("var_delete", VarDeleteRequest.class);
    }

    public Request createRequest(String i) throws InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        String[] parts= i.split(" ");
        
        if (!this.cmds.containsKey(parts[0])) {
            throw new IllegalArgumentException("Invalid session command.");
        }
                
        return (Request)this.cmds.get(parts[0]).getConstructor(String.class).newInstance(i);
    }
}
