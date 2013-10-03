/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kiesel
 */
public class SessionStoreTest {
    private SessionStore cut;
    
    public SessionStoreTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.cut= new SessionStore();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of size method, of class SessionStore.
     */
    @Test
    public void initialSize() {
        assertEquals(0, this.cut.size());
    }

    /**
     * Test of createSession method, of class SessionStore.
     */
    @Test
    public void testCreateSession() {
        Session s= this.cut.createSession(10);
        assertTrue(this.cut.hasSession(s.getId()));
    }

    /**
     * Test of terminateSession method, of class SessionStore.
     */
    @Test
    public void testTerminateSession() {
        Session s= this.cut.createSession(10);
        this.cut.terminateSession(s.getId());
        assertFalse(this.cut.hasSession(s.getId()));
    }
}