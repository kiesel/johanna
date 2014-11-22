/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import org.oneandone.idev.johanna.store.id.SecureRandomIdentifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @see SecureRandomIdentifier
 * @author Stephan Fuhrmann
 */
public class SecureRandomIdentifierTest {
    
    public SecureRandomIdentifierTest() {
    }
    
    /**
     * Test of uniqid method, of class SecureRandomIdentifier.
     */
    @Test
    public void uniqid_wo_prefix_is_32_chars() {
        assertEquals(32, new SecureRandomIdentifier("").toString().length());
    }
    
    /**
     * 
     */
    @Test
    public void check_prefix() {
        assertEquals("hallo", new SecureRandomIdentifier("hallo").toString().substring(0, 5));
    }
}