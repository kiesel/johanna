/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store.id;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author kiesel
 */
public class MD5Identifier extends Identifier {
    private static Random random;

    private byte[] id;

    public MD5Identifier(String prefix) {
        super(prefix);
        try {
            initRandom();
            this.createUniqid();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MD5Identifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    MD5Identifier() {
        this("");
    }
    
    public static Identifier forId(String id) {
        Objects.requireNonNull(id);
        
        MD5Identifier self= new MD5Identifier(prefixPartOf(id));
        self.id= DatatypeConverter.parseHexBinary(uniquePartOf(id));
        
        return self;
    }

    private void createUniqid() throws NoSuchAlgorithmException {
        byte[] b32 = new byte[32];
        random.nextBytes(b32);
        
        MessageDigest digest= MessageDigest.getInstance("md5");
        this.id= digest.digest(b32);
    }

    @Override
    protected String uniqid() {
        return Hex.encodeHexString(id);
    }

    private void initRandom() throws NoSuchAlgorithmException {
        if (null != random) return;
        
        // Length operation
        random= new Random(0x80);
    }
}
