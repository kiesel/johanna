package org.oneandone.idev.johanna.store;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;

/**
 *
 * @author Alex Kiesel <alex.kiesel@1und1.de>
 */
public class URLDecodedValue implements Value {
    private static URLCodec codec= new URLCodec();
    private byte[] intern;
    
    public URLDecodedValue(String orig) throws DecoderException {
        this.intern= codec.decode(orig.getBytes());
    }
    
    @Override
    public String asEncoded() {
        return new String(codec.encode(this.intern));
    }

    @Override
    public byte[] asIntern() {
        return this.intern;
    }
}
