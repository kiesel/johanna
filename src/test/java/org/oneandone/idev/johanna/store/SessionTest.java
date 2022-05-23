package org.oneandone.idev.johanna.store;

import org.oneandone.idev.johanna.store.id.MD5Identifier;
import org.oneandone.idev.johanna.store.memory.Session;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.oneandone.idev.johanna.store.id.IdentifierFactory;
import org.oneandone.idev.johanna.store.id.IdentifierFlavor;

/**
 *
 * @author kiesel
 */
public class SessionTest {
    private Session cut;
    private IdentifierFactory factory;
    
    public SessionTest() {
    }
    
    @Before
    public void setUp() {
        this.factory = new IdentifierFactory('x', IdentifierFlavor.MD5);
        this.cut= new Session(factory.newIdentifier(""));
        this.cut.putValue("k", "v");
    }
    
    /**
     * Test of getId method, of class Session.
     */
    @Test
    public void testGetId() {
        Session other= new Session(factory.newIdentifier(""));
        assertNotEquals(this.cut.getId(), other.getId());
    }

    /**
     * Test of putValue method, of class Session.
     */
    @Test
    public void testPutValueDoesOverwrite() {
        this.cut.putValue("k", "hello");
        assertEquals("hello", this.cut.getValue("k"));
    }

    /**
     * Test of getValue method, of class Session.
     */
    @Test
    public void testGetValueReturnsNullForNoValue() {
        assertEquals(null, this.cut.getValue("dne"));
    }
    
    /**
     * Test of getValue method, of class Session.
     */
    @Test
    public void testGetValue() {
        assertEquals("v", this.cut.getValue("k"));
    }
    

    /**
     * Test of removeValue method, of class Session.
     */
    @Test
    public void testRemoveValueWhenItDoesNotExist() {
        assertFalse(this.cut.removeValue("v"));
    }

    /**
     * Test of removeValue method, of class Session.
     */
    @Test
    public void testRemoveValue() {
        assertTrue(this.cut.removeValue("k"));
    }

    /**
     * Test of hasValue method, of class Session.
     */
    @Test
    public void testHasValue() {
        assertFalse(this.cut.hasValue("dne"));
    }

    /**
     * Test of setTimeout method, of class Session.
     */
    @Test
    public void testSetTimeout() {
        assertEquals(Session.DEFAULT_TTL, this.cut.setTimeout(60));
    }
    
    @Test
    public void notExpired() {
        this.cut.setTTL(1);
        assertFalse(this.cut.hasExpired());
    }
    
    @Test
    public void hasExpired() throws Exception {
        this.cut.setTTL(1);
        this.cut.putValue("foo", "bar");
        
        assertTrue(this.cut.hasExpired(new Date(new Date().getTime() + 2000)));
    }
    
    @Test
    public void expires_next_day() throws ParseException {
        Session s= new Session(factory.newIdentifier(""), 86400);
        s.putValue("foo", "bar");

        Calendar today= new GregorianCalendar();
        Calendar actual= new GregorianCalendar();
        actual.setTime(s.expiryDate());

        assertNotEquals(today.get(Calendar.DAY_OF_YEAR), actual.get(Calendar.DAY_OF_YEAR));
    }
    
    @Test
    public void expiry_reset_by_writing_operation() throws Exception {
        this.cut.putValue("foo", "bar");
        Date origExp= this.cut.expiryDate();
        Thread.sleep(1000);
        
        this.cut.putValue("bar", "baz");
        Date newExp= this.cut.expiryDate();
        
        assertNotEquals(origExp, newExp);
    }

    @Test
    public void expiry_untouched_by_writing_operation() throws Exception {
        this.cut.putValue("foo", "bar");
        Date origExp= this.cut.expiryDate();
        Thread.sleep(1000);
        
        this.cut.getValue("bar");
        Date newExp= this.cut.expiryDate();
        
        assertEquals(origExp, newExp);
    }
}