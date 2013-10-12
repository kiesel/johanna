package org.oneandone.idev.johanna.store;

import java.nio.charset.Charset;

/**
 *
 * @author Alex Kiesel <alex.kiesel@1und1.de>
 */
public class PlainValue implements Value {
    String value;
    
    public PlainValue(String value) {
        this.value= value;
    }
    
    public PlainValue(byte[] bytes) {
        this(new String(bytes, Charset.forName("utf-8")));
    }
    
    @Override
    public String asEncoded() {
        return this.value;
    }

    @Override
    public byte[] asIntern() {
        return this.value.getBytes(Charset.forName("utf-8"));
    }
}
