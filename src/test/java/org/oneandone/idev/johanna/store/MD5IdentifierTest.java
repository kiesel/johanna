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
public class MD5IdentifierTest {
    
    public MD5IdentifierTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of uniqid method, of class MD5Identifier.
     */
    @Test
    public void uniqid_wo_prefix_is_32_chars() {
        assertEquals(32, new MD5Identifier("").toString().length());
    }
}