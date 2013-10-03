/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.protocol;

import org.oneandone.idev.johanna.protocol.impl.HannahEchoRequest;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author kiesel
 */
public class HannahRequestFactoryTest extends TestCase {
    
    public HannahRequestFactoryTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of createRequest method, of class HannahRequestFactory.
     */
    public void testcreateEchoRequest() throws Exception {
        HannahRequestFactory instance = new HannahRequestFactory();
        assertEquals(
                new HannahEchoRequest("echo Hello World"),
                instance.createRequest("echo Hello World")
        );
    }
    
    @Test
    public void testCreateInvalidRequest() throws Exception {
        HannahRequestFactory instance= new HannahRequestFactory();
        try {
            instance.createRequest("foobar");
            fail("Expected exception not caught.");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }
}
