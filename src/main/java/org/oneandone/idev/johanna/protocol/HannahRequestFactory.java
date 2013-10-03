/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kiesel
 */
public class HannahRequestFactory {
    private Map<String, String> cmds;

    public HannahRequestFactory() {
        this.cmds= new HashMap<String,String>();
        this.cmds.put("echo", "HannahEchoRequest");
        this.cmds.put("session_create", "HannahSessionCreateRequest");
        this.cmds.put("session_terminate", "HannahSessionTerminateRequest");
    }

    public HannahRequest createRequest(String i) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        String[] parts= i.split(" ");
        
        if (!this.cmds.containsKey(parts[0])) {
            throw new IllegalArgumentException("Invalid session command.");
        }
        
        Class<HannahRequest> c= (Class<HannahRequest>) this.getClass().getClassLoader().loadClass(
                this.getClass().getPackage().getName() + "." +
                this.cmds.get(parts[0])
        );
        
        return c.getConstructor(String.class).newInstance(i);
    }
}
