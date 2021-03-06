package org.oneandone.idev.johanna.store.id;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * JUnit test for {@link UUIDIdentifier}.
 * @author Stephan Fuhrmann <stephan@tynne.de>
 */
public class UUIDIdentifierTest {
    
    private IdentifierFactory identifierFactory;
    
    @Before
    public void init() {
        identifierFactory = new IdentifierFactory('x', IdentifierFlavor.UUID);
    }
    
    @Test
    public void testInitWithNoArgs() {
        UUIDIdentifier identifier = new UUIDIdentifier(identifierFactory);
        String uuidid = identifier.uniqid();
        UUID uuid = UUID.fromString(uuidid);
        assertNotNull(uuid);
    }
    
    @Test
    public void testInitWithPrefix() {
        UUIDIdentifier identifier = new UUIDIdentifier("prEfIx",identifierFactory);
        String uuidid = identifier.uniqid();
        UUID uuid = UUID.fromString(uuidid);
        assertNotNull(uuid);
    }
    
    @Test(expected = NullPointerException.class)
    public void testInitWithNullPrefix() {
        UUIDIdentifier identifier = new UUIDIdentifier(null,identifierFactory);
    }
    
    @Test(expected = NullPointerException.class)
    public void testInitWithNullPrefixAndNullId() {
        UUIDIdentifier identifier = new UUIDIdentifier(null, null, identifierFactory);
    }
    
    @Test(expected = NullPointerException.class)
    public void testInitWithNullPrefixAndId() {
        UUIDIdentifier identifier = new UUIDIdentifier(null, UUID.randomUUID(), identifierFactory);
    }
    
    @Test(expected = NullPointerException.class)
    public void testInitWithPrefixAndNullId() {
        UUIDIdentifier identifier = new UUIDIdentifier("prEfIx", null, identifierFactory);
    }
    
    @Test
    public void testPrefix() {
        UUIDIdentifier identifier = new UUIDIdentifier("prEfIx", identifierFactory);
        assertEquals("prEfIx", identifier.prefix());
        assertEquals(0, identifier.toString().indexOf("prEfIx"));
    }
    
    @Test
    public void testUniqid() {
        UUID expect = UUID.randomUUID();
        UUIDIdentifier identifier = new UUIDIdentifier("prEfIx", expect, identifierFactory);
        String uuidid = identifier.uniqid();
        UUID actual = UUID.fromString(uuidid);
        assertEquals(expect, actual);
    }
    
    @Test
    public void testForId() {
        UUID expect = UUID.randomUUID();
        
        
        String prefix = "pReFiX";
        String id = expect.toString();
        
        UUIDIdentifier identifier = UUIDIdentifier.forId(
                prefix + identifierFactory.getIdentifierSeparator() + id, identifierFactory);
        
        assertEquals(prefix, identifier.prefix());
        assertEquals(id, identifier.uniqid());
    }
    
    @Test
    public void testForIdWithIpv4() throws UnknownHostException {
        UUID expect = UUID.randomUUID();
        
        
        String prefix = Hex.encodeHexString(InetAddress.getByName("127.0.0.1").getAddress());
        String id = expect.toString();
        
        UUIDIdentifier identifier = UUIDIdentifier.forId(
                prefix + identifierFactory.getIdentifierSeparator() + id, identifierFactory);
        
        assertEquals(prefix, identifier.prefix());
        assertEquals(id, identifier.uniqid());
    }

    @Test
    public void testForIdWithIpv6() throws UnknownHostException {
        UUID expect = UUID.randomUUID();
        
        
        String prefix = Hex.encodeHexString(InetAddress.getByName("::1").getAddress());
        String id = expect.toString();
        
        UUIDIdentifier identifier = UUIDIdentifier.forId(
                prefix + identifierFactory.getIdentifierSeparator() + id, identifierFactory);
        
        assertEquals(prefix, identifier.prefix());
        assertEquals(id, identifier.uniqid());
    }
}
