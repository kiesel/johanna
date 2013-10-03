/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol;

import org.oneandone.idev.johanna.protocol.impl.HannahSessionCreateRequest;
import org.oneandone.idev.johanna.protocol.impl.HannahEchoRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.oneandone.idev.johanna.protocol.impl.HannahSessionIsValidRequest;
import org.oneandone.idev.johanna.protocol.impl.HannahSessionKeysRequest;
import org.oneandone.idev.johanna.protocol.impl.HannahSessionSetTimeoutRequest;
import org.oneandone.idev.johanna.protocol.impl.HannahSessionTerminateRequest;
import org.oneandone.idev.johanna.protocol.impl.HannahVarDeleteRequest;
import org.oneandone.idev.johanna.protocol.impl.HannahVarReadRequest;
import org.oneandone.idev.johanna.protocol.impl.HannahVarWriteRequest;

/**
 *
 * @author kiesel
 */
public class HannahRequestFactory {
    private Map<String, Class> cmds;

    public HannahRequestFactory() {
        this.cmds= new HashMap<String, Class>();
        this.cmds.put("echo", HannahEchoRequest.class);
        this.cmds.put("session_create", HannahSessionCreateRequest.class);
        this.cmds.put("session_terminate", HannahSessionTerminateRequest.class);
        this.cmds.put("session_isvalid", HannahSessionIsValidRequest.class);
        this.cmds.put("session_settimeout", HannahSessionSetTimeoutRequest.class);
        this.cmds.put("session_keys", HannahSessionKeysRequest.class);
        
        this.cmds.put("var_write", HannahVarWriteRequest.class);
        this.cmds.put("var_read", HannahVarReadRequest.class);
        this.cmds.put("var_delete", HannahVarDeleteRequest.class);
    }

    public HannahRequest createRequest(String i) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        String[] parts= i.split(" ");
        
        if (!this.cmds.containsKey(parts[0])) {
            throw new IllegalArgumentException("Invalid session command.");
        }
                
        return (HannahRequest)this.cmds.get(parts[0]).getConstructor(String.class).newInstance(i);
    }
}
