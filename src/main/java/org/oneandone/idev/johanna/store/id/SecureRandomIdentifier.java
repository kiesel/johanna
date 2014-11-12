package org.oneandone.idev.johanna.store.id;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 * Creates identifiers using {@link SecureRandom}.
 * @author Stephan Fuhrmann
 */
public class SecureRandomIdentifier extends Identifier {
    private static SecureRandom random = new SecureRandom();
    
    private final static int BYTESIZE = 16;
    
    static {
        random = new SecureRandom();
    }

    private byte[] id;

    public SecureRandomIdentifier(String prefix) {
        super(prefix);
        try {
            this.createUniqid();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SecureRandomIdentifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    SecureRandomIdentifier() {
        this("");
    }
    
    public static Identifier forId(String id) {
        Objects.requireNonNull(id);
        
        SecureRandomIdentifier self= new SecureRandomIdentifier(prefixFrom(id));
        self.id= DatatypeConverter.parseHexBinary(suffixFrom(id));
        
        return self;
    }

    private void createUniqid() throws NoSuchAlgorithmException {
        byte[] b32 = new byte[BYTESIZE];
        random.nextBytes(b32);
        this.id= b32;
    }

    @Override
    protected String uniqid() {
        StringBuilder builder= new StringBuilder(BYTESIZE * 2);
        
        for (byte b : this.id) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
