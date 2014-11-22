/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store.id;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kiesel
 */
public class MD5IdentifierTest {

    public MD5IdentifierTest() {
    }

    /**
     * Test of uniqid method, of class MD5Identifier.
     */
    @Test
    public void uniqid_wo_prefix_is_33_chars() {
        assertEquals(33, new MD5Identifier("").toString().length());
    }
}
