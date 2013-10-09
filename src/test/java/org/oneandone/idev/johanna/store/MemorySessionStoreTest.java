/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import org.oneandone.idev.johanna.store.memory.Session;
import org.oneandone.idev.johanna.store.memory.MemorySessionStore;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kiesel
 */
public class MemorySessionStoreTest {
    private MemorySessionStore cut;
    
    public MemorySessionStoreTest() {
    }
    
    @Before
    public void setUp() {
        this.cut= new MemorySessionStore();
    }

    /**
     * Test of size method, of class MemorySessionStore.
     */
    @Test
    public void initialSize() {
        assertEquals(0, this.cut.size());
    }

    /**
     * Test of createSession method, of class MemorySessionStore.
     */
    @Test
    public void testCreateSession() {
        Session s= this.cut.createSession(10);
        assertTrue(this.cut.hasSession(s.getId()));
    }

    /**
     * Test of terminateSession method, of class MemorySessionStore.
     */
    @Test
    public void testTerminateSession() {
        Session s= this.cut.createSession(10);
        this.cut.terminateSession(s.getId());
        assertFalse(this.cut.hasSession(s.getId()));
    }
}