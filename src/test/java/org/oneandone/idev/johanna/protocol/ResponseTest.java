package org.oneandone.idev.johanna.protocol;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kiesel
 */
public class ResponseTest {
    
    public ResponseTest() {
    }
    
    @Test
    public void emptyOk() {
        assertEquals(
                "+OK\n",
                new Response(true).toString()
        );
    }

    @Test
    public void failure() {
        assertEquals(
                "-ERROR\n",
                new Response(false, "ERROR").toString()
        );
    }
    
    @Test
    public void failure_must_have_message() {
        try {
            Response r= new Response(false);
            fail("Expected exception not caught.");
        } catch (IllegalStateException e) {
        }
    }
}