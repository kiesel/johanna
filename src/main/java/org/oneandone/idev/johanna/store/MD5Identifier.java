/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oneandone.idev.johanna.store;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kiesel
 */
public class MD5Identifier extends Identifier {
    private static SecureRandom random;
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
    
    private void createUniqid() throws NoSuchAlgorithmException {
        MessageDigest digest= MessageDigest.getInstance("md5");
        this.id= digest.digest(new Integer(random.nextInt()).toString().getBytes());
    }

    @Override
    protected String uniqid() {
        StringBuilder builder= new StringBuilder(32);
        
        for (byte b : this.id) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    private void initRandom() throws NoSuchAlgorithmException {
        if (null != random) return;
        
        // Length operation
        random= SecureRandom.getInstance("SHA1PRNG");
    }
}
