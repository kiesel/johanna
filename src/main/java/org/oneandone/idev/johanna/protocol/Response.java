/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol;

/**
 *
 * @author kiesel
 */
public class Response {
    public static final Response OK= new Response(true);
    public static final Response BADSESS= new Response(false, "BADSESS");
    public static final Response NOKEY= new Response(false, "NOKEY");
    
    private boolean success;
    private boolean close;
    private String data;

    public Response(boolean success) {
        this.success= success;
        this.close= false;
        this.data= null;
    }
    
    public Response(boolean success, String data) {
        this(success);
        this.data = data;
    }

    public boolean getClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }
    
    @Override
    public String toString() {
        StringBuilder buf= new StringBuilder(1024);
        
        if (this.success) {
            buf.append("+OK");
            if (null != this.data) buf.append(" ");
        } else {
            buf.append("-");
        }
        
        buf.append(this.data);
        buf.append("\n");
        
        return buf.toString();
    }
}
