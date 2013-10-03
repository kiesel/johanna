/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol;

/**
 *
 * @author kiesel
 */
public class HannahResponse {
    public static final HannahResponse OK= new HannahResponse(true, "OK");
    public static final HannahResponse BADSESS= new HannahResponse(false, "BADSESS");
    public static final HannahResponse NOKEY= new HannahResponse(false, "NOKEY");
    
    private boolean success;
    private String data;

    public HannahResponse(boolean success, String data) {
        this.success = success;
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuffer buf= new StringBuffer(1024);
        
        if (this.success) {
            buf.append("+OK ");
        } else {
            buf.append("-");
        }
        
        buf.append(data);
        buf.append("\n");
        
        return buf.toString();
    }
}
