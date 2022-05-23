package org.oneandone.idev.johanna.protocol;

/**
 *
 * @author kiesel
 */
public class Response {
    public static final Response OK= new Response(true);
    public static final Response BADSESS= new Response(false, "BADSESS");
    public static final Response NOKEY= new Response(false, "NOKEY");
    public static final Response BADSTOR= new Response(false, "BADSTORAGE Supported: [tmp]");
    
    private boolean success;
    private boolean close;
    private String data;

    public Response(boolean success) {
        this(success, null);
    }
    
    public Response(boolean success, String data) {
        this.success= success;
        this.data = data;
        this.close= false;
        
        if (!this.success && this.data == null) {
            throw new IllegalStateException("Message required for failure message.");
        }
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
        
        if (this.data != null) buf.append(this.data);
        buf.append("\n");
        
        return buf.toString();
    }
}
