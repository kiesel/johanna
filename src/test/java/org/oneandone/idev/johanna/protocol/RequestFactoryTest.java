package org.oneandone.idev.johanna.protocol;

import junit.framework.TestCase;
import org.junit.Test;
import org.oneandone.idev.johanna.protocol.impl.SessionCreateRequest;

/**
 *
 * @author kiesel
 */
public class RequestFactoryTest extends TestCase {
    
    public RequestFactoryTest(String testName) {
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
        RequestFactory instance = new RequestFactory();
        assertEquals(
                new SessionCreateRequest("session_create 86400"),
                instance.createRequest("session_create 86400")
        );
    }
    
    @Test
    public void testCreateInvalidRequest() throws Exception {
        RequestFactory instance= new RequestFactory();
        try {
            instance.createRequest("foobar");
            fail("Expected exception not caught.");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }
}
