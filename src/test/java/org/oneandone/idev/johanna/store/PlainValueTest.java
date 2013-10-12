package org.oneandone.idev.johanna.store;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alex Kiesel <alex.kiesel@1und1.de>
 */
public class PlainValueTest {
    
    /**
     * Test of asEncoded method, of class PlainValue.
     */
    @Test
    public void testAsEncoded() {
        assertEquals("hällo", new PlainValue("hällo").asEncoded());
    }
    
    @Test
    public void fromIntern() {
        assertEquals("Hällo", new PlainValue(new byte[] {
            (byte)0x48, 
            (byte)0xc3, 
            (byte)0xa4, 
            (byte)0x6c, 
            (byte)0x6c, 
            (byte)0x6f
        }).asEncoded());
    }

    /**
     * Test of asIntern method, of class PlainValue.
     */
    @Test
    public void testAsIntern() {
        assertArrayEquals(new byte[] { (byte)0xc3, (byte)0xa4 }, new PlainValue("ä").asIntern());
    }
}